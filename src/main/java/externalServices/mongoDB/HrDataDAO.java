
package externalServices.mongoDB;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;

import dataStructure.Employee;
import dataStructure.HRData;
import dataStructure.HRObjectiveData;

/**
 * 
 * @author Christopher Kai
 * @version 1.0
 * @since 23rd January 2017
 * 
 *        This class contains the definition of the EmployeeDAO object
 *
 */

public class HrDataDAO {

	public HrDataDAO(Datastore dbConnection) {
		EmployeeDAO.dbConnection = dbConnection;
	}// Constructor

	// Set of methods to query the database providing HR data

	public static long getTotalNumberOfUsers() {

		long totalAccounts = EmployeeDAO.dbConnection.find(Employee.class).countAll();
		return totalAccounts;

	}// getNumberOfUsers

	public static long getTotalUsersWithObjectives() {

		long totalUsersWithObjectives = EmployeeDAO.dbConnection.find(Employee.class).field("objectives").exists()
				.countAll();
		return totalUsersWithObjectives;

	}// getTotalUsersWithObjectives

	public static long getTotalUsersWithDevelopmentNeeds() {

		long totalUsersWithDevelopmentNeeds = EmployeeDAO.dbConnection.find(Employee.class).field("developmentNeeds")
				.exists().countAll();
		return totalUsersWithDevelopmentNeeds;

	}// getTotalUsersWithDevelopmentNeeds

	public static long getTotalUsersWithNotes() {

		long totalUsersWithNotes = EmployeeDAO.dbConnection.find(Employee.class).field("notes").exists().countAll();
		return totalUsersWithNotes;

	}// getTotalUsersWithNotes

	public static long getTotalUsersWithCompetencies() {

		long totalUsersWithCompetencies = EmployeeDAO.dbConnection.find(Employee.class).field("competencies").exists()
				.countAll();
		return totalUsersWithCompetencies;

	}// getTotalUsersWithCompetencys

	public static long getTotalUsersWithSubmittedFeedback() {

		long totalUsersWithSubmittedFeedback = EmployeeDAO.dbConnection.find(Employee.class)
				.field("groupFeedbackRequests").exists().countAll();
		return totalUsersWithSubmittedFeedback;

	}// getTotalUsersWithSubmittedFeedback

	public static long getTotalUsersWithFeedback() {

		long totalUsersWithFeedback = EmployeeDAO.dbConnection.find(Employee.class).field("feedback").exists()
				.countAll();
		return totalUsersWithFeedback;

	}// getTotalUsersWithFeedback()

	public static List<HRObjectiveData> getHRObjectiveData() {
		
		List<HRObjectiveData> hrDataList = new ArrayList<>();

		List<Employee> query = EmployeeDAO.dbConnection.createQuery(Employee.class).field("objectives").exists().retrievedFields(true, "forename","surname","employeeID","objectives")
				.asList();

		if (!query.isEmpty()) {
			for (Employee employee : query) {
				hrDataList.add(new HRObjectiveData(employee.getEmployeeID(), employee.getFullName(),
						employee.getLatestVersionObjectives()));
			}
		}

		return hrDataList;

	}
	
	public static HRData getHRData(){
		long totalAccounts = EmployeeDAO.dbConnection.find(Employee.class).countAll();
		long totalUsersWithObjectives = EmployeeDAO.dbConnection.find(Employee.class).field("objectives").exists()
				.countAll();
		long totalUsersWithDevelopmentNeeds = EmployeeDAO.dbConnection.find(Employee.class).field("developmentNeeds")
				.exists().countAll();
		long totalUsersWithNotes = EmployeeDAO.dbConnection.find(Employee.class).field("notes").exists().countAll();
		long totalUsersWithCompetencies = EmployeeDAO.dbConnection.find(Employee.class).field("competencies").exists()
				.countAll();
		long totalUsersWithSubmittedFeedback = EmployeeDAO.dbConnection.find(Employee.class).field("feedback").exists()
				.countAll();
		long totalUsersWithFeedback = EmployeeDAO.dbConnection.find(Employee.class).field("feedback").exists()
				.countAll();
		
		List<HRObjectiveData> hrObjectiveData = new ArrayList<>();

		List<Employee> query = EmployeeDAO.dbConnection.createQuery(Employee.class).field("objectives").exists().retrievedFields(true, "forename","surname","employeeID","objectives")
				.asList();

		if (!query.isEmpty()) {
			for (Employee employee : query) {
				hrObjectiveData.add(new HRObjectiveData(employee.getEmployeeID(), employee.getFullName(),
						employee.getLatestVersionObjectives()));
			}
		}
		
		HRData hrData= new HRData(totalAccounts,totalUsersWithObjectives,totalUsersWithDevelopmentNeeds,totalUsersWithNotes,totalUsersWithCompetencies,totalUsersWithSubmittedFeedback,totalUsersWithFeedback,hrObjectiveData);

		return hrData;
		
	}

}// HrDataDAO
