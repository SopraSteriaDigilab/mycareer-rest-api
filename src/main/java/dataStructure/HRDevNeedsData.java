package dataStructure;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Steven Hamilton
 * @version 1.0
 * @since 30th January 2017
 * 
 * This class contains the definition of the HRDevNeedsData object, providing totals and progress status of Development Needs.
 *
 */
@JsonIgnoreProperties({"devNeeds"})
public class HRDevNeedsData implements Serializable {
	
	private static final long serialVersionUID = 4805745506626193436L;
	
	private long employeeID;
	private String fullName;
	private long totalDevNeeds;
	private long proposedCount;
	private long setCount;
	private long completeCount;
	private List<DevelopmentNeed> devNeeds;
	
	public HRDevNeedsData(long employeeID, String fullName, List<DevelopmentNeed> latestVersionDevNeeds) {

		this.employeeID = employeeID;
		this.fullName = fullName;
		this.devNeeds = latestVersionDevNeeds;
		this.totalDevNeeds= devNeeds.size();
		this.proposedCount = countProgress(devNeeds, 0);
		this.setCount = countProgress(devNeeds, 1);
		this.completeCount = countProgress(devNeeds, 2);
	}
	
	private long countProgress(List<DevelopmentNeed> devNeeds, int status) {
		int count = 0;
		for (DevelopmentNeed devNeed : devNeeds) {
			if (devNeed.getProgress() == status)
				count++;
		}
		return count;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getTotalDevNeeds() {
		return totalDevNeeds;
	}

	public void setTotalDevNeeds(long totalDevNeeds) {
		this.totalDevNeeds = totalDevNeeds;
	}

	public long getProposedCount() {
		return proposedCount;
	}

	public void setProposedCount(long proposedCount) {
		this.proposedCount = proposedCount;
	}

	public long getSetCount() {
		return setCount;
	}

	public void setSetCount(long setCount) {
		this.setCount = setCount;
	}

	public long getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(long completeCount) {
		this.completeCount = completeCount;
	}

	public List<DevelopmentNeed> getDevNeeds() {
		return devNeeds;
	}

	public void setDevNeeds(List<DevelopmentNeed> devNeeds) {
		this.devNeeds = devNeeds;
	}
	
	

}
