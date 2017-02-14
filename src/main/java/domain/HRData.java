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
	
	private List<HRObjectiveData> hrObjectiveData;
	private List<HRDevNeedsData> hrDevNeedsData;

	public HRData(List<HRObjectiveData> hrObjectiveData, List<HRDevNeedsData> hrDevNeedsData) {
		this.hrObjectiveData=hrObjectiveData;
		this.hrDevNeedsData=hrDevNeedsData;
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
