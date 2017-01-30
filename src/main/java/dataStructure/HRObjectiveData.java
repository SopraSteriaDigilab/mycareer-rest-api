package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Steven Hamilton
 * @version 1.0
 * @since 30th January 2017
 * 
 * This class contains the definition of the HRObjectiveData object, providing totals and progress status of un-archived Objectives.
 *
 */
@JsonIgnoreProperties({"objectives"})
public class HRObjectiveData implements Serializable {

	private static final long serialVersionUID = -4920780991827924267L;

	private long employeeID;
	private String fullName;
	private long totalObjectives;
	private long proposedCount;
	private long setCount;
	private long completeCount;
	private List<Objective> objectives;

	public HRObjectiveData(long employeeID, String fullName, List<Objective> latestVersionObjectives) {

		this.employeeID = employeeID;
		this.fullName = fullName;
		this.objectives = removeArchived(latestVersionObjectives);
		this.totalObjectives= objectives.size();
		this.proposedCount = countProgress(objectives, 0);
		this.setCount = countProgress(objectives, 1);
		this.completeCount = countProgress(objectives, 2);
	}

	private List<Objective> removeArchived(List<Objective> latestVersionObjectives) {
		List<Objective> unarchivedObjectives=new ArrayList<Objective>();
		for (Objective latestVersionObj :latestVersionObjectives)
			if (latestVersionObj.getIsArchived()==false)
				unarchivedObjectives.add(latestVersionObj);			
		return unarchivedObjectives;
	}

	private long countProgress(List<Objective> objectives, int status) {
		int count = 0;
		for (Objective objective : objectives) {
			if (objective.getProgress() == status)
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

	public List<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Objective> objectives) {
		this.objectives = objectives;
	}

	public long getTotalObjectives() {
		return totalObjectives;
	}

	public void setTotalObjectives(long totalObjectives) {
		this.totalObjectives = totalObjectives;
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

}
