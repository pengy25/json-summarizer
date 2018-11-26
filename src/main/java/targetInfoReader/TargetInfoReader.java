package targetInfoReader;

import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import com.google.gson.stream.JsonReader;

/*
 * Help reading user information from the input.
 * The usage of this class should be as an Iterator over TargetInfo
 */
public class TargetInfoReader implements Iterator<TargetInfo> {
	private JsonReader inputData;
	private boolean isClosed;
	
	/*
	 * The constructor that requires input path
	 */
	public TargetInfoReader(String fileInputPath) {
		FileReader fileReader = null;
		this.inputData = null;
		isClosed = false;
		
		try {
			fileReader = new FileReader(new File(fileInputPath));
			this.inputData = new JsonReader(fileReader);
			// Assume input as a Json Array
			this.inputData.beginArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Required by Iterator interface.
	 * Indicates if the input has next user information to read.
	 */
	public boolean hasNext() {
		boolean result = false;
		
		// If the source is already close, return false
		if (isClosed) {
			return result;
		}
		
		// Otherwise, return the result of hasNext() from this.inputData
		try {
			result = this.inputData.hasNext();
			
			// If the inputData indicates no more information to read, close the input.
			if (!result) {
				this.inputData.endArray();
				this.inputData.close();
				isClosed = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * Required by Iterator interface.
	 * Return an user information object
	 */
	public TargetInfo next() {
		TargetInfo result = new TargetInfo();
		
		try {
			// Start reading the input as a Json object in a fashion similar to Gson documentation:
			// http://www.javadoc.io/doc/com.google.code.gson/gson/2.8.5
			this.inputData.beginObject();
			while (this.inputData.hasNext()) {
				String objName = this.inputData.nextName();
				
				if (objName.equals("guid")) {
					// Read user id
					result.uid = this.inputData.nextString();
				} else if (objName.equals("age")) {
					// Read user age
					result.age = this.inputData.nextInt();
				} else if (objName.equals("friends")) {
					// Don't store friend details Only count number of friends
					this.inputData.beginArray();
					while (this.inputData.hasNext()) {
						this.inputData.beginObject();
						this.inputData.nextName();
						this.inputData.skipValue();
						result.friend_num++;
						this.inputData.endObject();
					}
					this.inputData.endArray();
				} else if (objName.equals("balance")) {
					// Read the balance through string manipulation
					String balanceStr = this.inputData.nextString().replace("$", "").replace(",", "");
					result.balance = Double.parseDouble(balanceStr);
				} else if (objName.equals("isActive")) {
					// Read isActive flag
					this.inputData.nextBoolean();
				} else if (objName.equals("registered")) {
					// Read date of registration
					String dateStringForProcessing = this.inputData.nextString().replace("T", " ").replace(":", "");
					DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss Z");
					ZonedDateTime date = ZonedDateTime.parse(dateStringForProcessing, targetFormat);
					result.year = date.getYear();
				} else if (objName.equals("greeting")) {
					// Read the last integer in greeting to determine the number
					// of unread messages
					int lastInt = 0;
					String greetingStr = this.inputData.nextString();
					Scanner strScan = new Scanner(greetingStr.split("!")[1]);
					while (strScan.hasNext()) {
						if (strScan.hasNextInt()) {
							lastInt = strScan.nextInt();
						} else {
							strScan.next();
						}
					}
					strScan.close();
					result.unread_num = lastInt;
				} else {
					// Skip other entries
					this.inputData.skipValue();
				}
				
				
			}
			this.inputData.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
