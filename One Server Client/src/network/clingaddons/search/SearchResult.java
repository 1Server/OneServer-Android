package network.clingaddons.search;

import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

public class SearchResult {
	
	protected String result;
	protected UnsignedIntegerFourBytes count;
	protected UnsignedIntegerFourBytes totalMatches;
	protected UnsignedIntegerFourBytes containerUpdateID;
	
	public SearchResult(String result, UnsignedIntegerFourBytes count,
						UnsignedIntegerFourBytes totalMatches,
						UnsignedIntegerFourBytes containerUpdateID) {
		
		this.result = result;
		this.count = count;
		this.totalMatches = totalMatches;
		this.containerUpdateID = containerUpdateID;
		
	}
	
	public SearchResult(String result, long count, long totalMatches) {
		this(result, count, totalMatches, 0);
	}
	
	public SearchResult(String result, long count, long totalMatches, long updatedID) {
		this(
				result,
				new UnsignedIntegerFourBytes(count),
				new UnsignedIntegerFourBytes(totalMatches),
				new UnsignedIntegerFourBytes(updatedID));
	}
	
	public String getResult() {
		return result;
	}
	
	public UnsignedIntegerFourBytes getCount() {
		return count;
	}
	
	public long getCountLong() {
		return count.getValue();
	}
	
	public UnsignedIntegerFourBytes getTotalMatches() {
		return totalMatches;
	}
	
	public long getTotalMatchesLong() {
		return totalMatches.getValue();
	}
	
	public UnsignedIntegerFourBytes getContainerUpdateID() {
		return containerUpdateID;
	}
	
	public long getContainerUpdateIDLong() {
		return containerUpdateID.getValue();
	}

}
