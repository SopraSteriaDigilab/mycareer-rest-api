package dataStructure;

import java.util.List;

public class HRData {
	
	private long totalAccounts;
	private long totalUsersWithObjectives;
	private long totalUsersWithDevelopmentNeeds;
	private long totalUsersWithNotes;
	private long totalUsersWithCompetencies;
	private long totalUsersWithSubmittedFeedback;
	private long totalUsersWithFeedback;
	private List<HRObjectiveData> hrObjectiveData;

	public HRData(long totalAccounts, long totalUsersWithObjectives, long totalUsersWithDevelopmentNeeds,
			long totalUsersWithNotes, long totalUsersWithCompetencies, long totalUsersWithSubmittedFeedback,
			long totalUsersWithFeedback,List<HRObjectiveData> hrObjectiveData) {
		this.totalAccounts=totalAccounts;
		this.totalUsersWithObjectives=totalUsersWithObjectives;
		this.totalUsersWithDevelopmentNeeds=totalUsersWithDevelopmentNeeds;
		this.totalUsersWithNotes=totalUsersWithNotes;
		this.totalUsersWithCompetencies=totalUsersWithCompetencies;
		this.totalUsersWithSubmittedFeedback=totalUsersWithSubmittedFeedback;
		this.totalUsersWithFeedback=totalUsersWithFeedback;	
		this.hrObjectiveData=hrObjectiveData;
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
	
	
	
	

}
