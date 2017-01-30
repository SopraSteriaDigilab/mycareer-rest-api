package domain;

import java.util.List;

/**
 * 
 * @author Steven Hamilton
 * @version 1.0
 * @since 30th January 2017
 * 
 * This class contains the definition of the HRData object, containing all details for the HR Dashboard.
 *
 */
public class HRData {
	
	private long totalAccounts;
	private long totalUsersWithObjectives;
	private long totalUsersWithDevelopmentNeeds;
	private long totalUsersWithNotes;
	private long totalUsersWithCompetencies;
	private long totalUsersWithSubmittedFeedback;
	private long totalUsersWithFeedback;
	private List<HRObjectiveData> hrObjectiveData;
	private List<HRDevNeedsData> hrDevNeedsData;

	public HRData(HRTotals hrTotals,List<HRObjectiveData> hrObjectiveData, List<HRDevNeedsData> hrDevNeedsData) {
		this.totalAccounts=hrTotals.getTotalAccounts();
		this.totalUsersWithObjectives=hrTotals.getTotalUsersWithObjectives();
		this.totalUsersWithDevelopmentNeeds=hrTotals.getTotalUsersWithDevelopmentNeeds();
		this.totalUsersWithNotes=hrTotals.getTotalUsersWithNotes();
		this.totalUsersWithCompetencies=hrTotals.getTotalUsersWithCompetencies();
		this.totalUsersWithSubmittedFeedback=hrTotals.getTotalUsersWithSubmittedFeedback();
		this.totalUsersWithFeedback=hrTotals.getTotalUsersWithFeedback();	
		this.hrObjectiveData=hrObjectiveData;
		this.hrDevNeedsData=hrDevNeedsData;
	}

	public long getTotalAccounts() {
		return totalAccounts;
	}

	public void setTotalAccounts(long totalAccounts) {
		this.totalAccounts = totalAccounts;
	}

	public long getTotalUsersWithObjectives() {
		return totalUsersWithObjectives;
	}

	public void setTotalUsersWithObjectives(long totalUsersWithObjectives) {
		this.totalUsersWithObjectives = totalUsersWithObjectives;
	}

	public long getTotalUsersWithDevelopmentNeeds() {
		return totalUsersWithDevelopmentNeeds;
	}

	public void setTotalUsersWithDevelopmentNeeds(long totalUsersWithDevelopmentNeeds) {
		this.totalUsersWithDevelopmentNeeds = totalUsersWithDevelopmentNeeds;
	}

	public long getTotalUsersWithNotes() {
		return totalUsersWithNotes;
	}

	public void setTotalUsersWithNotes(long totalUsersWithNotes) {
		this.totalUsersWithNotes = totalUsersWithNotes;
	}

	public long getTotalUsersWithCompetencies() {
		return totalUsersWithCompetencies;
	}

	public void setTotalUsersWithCompetencies(long totalUsersWithCompetencies) {
		this.totalUsersWithCompetencies = totalUsersWithCompetencies;
	}

	public long getTotalUsersWithSubmittedFeedback() {
		return totalUsersWithSubmittedFeedback;
	}

	public void setTotalUsersWithSubmittedFeedback(long totalUsersWithSubmittedFeedback) {
		this.totalUsersWithSubmittedFeedback = totalUsersWithSubmittedFeedback;
	}

	public long getTotalUsersWithFeedback() {
		return totalUsersWithFeedback;
	}

	public void setTotalUsersWithFeedback(long totalUsersWithFeedback) {
		this.totalUsersWithFeedback = totalUsersWithFeedback;
	}

	public List<HRObjectiveData> getHrObjectiveData() {
		return hrObjectiveData;
	}

	public void setHrObjectiveData(List<HRObjectiveData> hrObjectiveData) {
		this.hrObjectiveData = hrObjectiveData;
	}

	public List<HRDevNeedsData> getHrDevNeedsData() {
		return hrDevNeedsData;
	}

	public void setHrDevNeedsData(List<HRDevNeedsData> hrDevNeedsData) {
		this.hrDevNeedsData = hrDevNeedsData;
	}
	
	
	
	
	
	

}
