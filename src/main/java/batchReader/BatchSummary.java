package batchReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * Holds summary of a batch of user information
 */
public class BatchSummary {
	public int userAmount; // Number of user in the batch
	public Map<Integer, Integer> yearToUserAmount; // Categorize user into year of registration
	public List<Integer> friendNumLst; // List of friend number
	public List<Integer> ageLst; // List of age
	public double balanceSum; // Sum of user balance in the batch
	public int unreadSum; // Sum of user unread messages in the batch
	
	public BatchSummary() {
		yearToUserAmount = new TreeMap<Integer, Integer>();
		friendNumLst = new ArrayList<Integer>();
		ageLst = new ArrayList<Integer>();
		balanceSum = 0;
		unreadSum = 0;
	}
	
	/*
	 * Calculate average balance in the batch
	 */
	public double getBalanceMean() {
		return (double)this.balanceSum / this.userAmount;
	}
	
	/*
	 * Calculate average number of unread messages in the batch
	 */
	public double getUnreadMean() {
		return ((double)this.unreadSum) / this.userAmount;
	}
	
	/*
	 * Calculate median number of friends in the batch
	 */
	public int getFriendMedian() {
		Collections.sort(this.friendNumLst);
		return this.friendNumLst.get((int)(this.friendNumLst.size() / 2));
	}
	
	/*
	 * Calculate median user age in the batch
	 */
	public int getAgeMedian() {
		Collections.sort(this.ageLst);
		return this.ageLst.get((int)(this.ageLst.size() / 2));
	}
}
