package jsonSummarizer;

import batchReader.BatchReader;
import batchReader.BatchSummary;

/*
 * The main class to run the summarizer for demo purpose
 */
public class Main {
	
	/*
	 * The program uses the first argument as the input path, and uses the second argument
	 * as the batch size.
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("The program requires two arguments: inputPath and batchSize");
			return;
		}
		
		String inputPath = args[0];
		int batchSize = Integer.parseInt(args[1]);
		
		BatchReader reader = new BatchReader(inputPath, batchSize);
		
		while (reader.hasNext()) {
			BatchSummary summary = reader.next();
			
			System.out.println("Yearly user registrations:");
			for (Integer year : summary.yearToUserAmount.keySet()) {
				System.out.print(year + ", " + summary.yearToUserAmount.get(year) + " users; ");
			}
			System.out.println();
			System.out.println("Median friend number: " + summary.getFriendMedian());
			System.out.printf("Mean balance: %.2f\n", summary.getBalanceMean());
			System.out.println("Median age: " + summary.getAgeMedian());
			System.out.println("Mean unread messages: " + summary.getUnreadMean());
			System.out.println();
		}
	}

}
