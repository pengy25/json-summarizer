package batchReader;

import java.util.Iterator;
import targetInfoReader.TargetInfo;
import targetInfoReader.TargetInfoReader;

/*
 * Reads input info based on the given batch size
 * The usage of this class should be as an Iterator over BatchSummary
 */
public class BatchReader implements Iterator<BatchSummary> {
	private int batchSize;
	private TargetInfoReader reader;
	
	/*
	 * The constructor that requires input path and batch size
	 */
	public BatchReader(String filePath, int batchSize) {
		this.reader = new TargetInfoReader(filePath);
		this.batchSize = batchSize;
	}
	
	/*
	 * Required by Iterator interface.
	 * Indicates if the input has next batch to read.
	 */
	public boolean hasNext() {
		return this.reader.hasNext();
	}
	
	/*
	 * Required by Iterator interface.
	 * Return a summary of a batch of user information
	 */
	public BatchSummary next() {
		BatchSummary summary = new BatchSummary();
		int count = 0; // Count number of user information processed
		
		// Go through the input and fill out the summary information within a batch
		while (this.reader.hasNext() && count < this.batchSize) {
			TargetInfo info = this.reader.next();
			summary.userAmount += 1;
			if (!summary.yearToUserAmount.containsKey(info.year)) {
				summary.yearToUserAmount.put(info.year, 0);
			}
			summary.yearToUserAmount.put(info.year, summary.yearToUserAmount.get(info.year) + 1);
			summary.friendNumLst.add(info.friend_num);
			summary.ageLst.add(info.age);
			summary.balanceSum += info.balance;
			summary.unreadSum += info.unread_num;
			count++;
		}
		return summary;
	}
}
