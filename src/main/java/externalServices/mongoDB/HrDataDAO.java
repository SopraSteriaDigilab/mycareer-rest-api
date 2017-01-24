package externalServices.mongoDB;


import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.DUPLICATE_FEEDBACK;
import static dataStructure.Constants.ERROR_LINKING_FBTOUSER;
import static dataStructure.Constants.FEEDBACK_NOTADDED_ERROR;
import static dataStructure.Constants.GROUPFBREQ_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_COMPETENCY_CONTEXT;
import static dataStructure.Constants.INVALID_COMPETENCY_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_CONTEXT_USERID;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_FBREQ_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_FEEDBACK;
import static dataStructure.Constants.INVALID_FEEDBACKREQ_CONTEXT;
import static dataStructure.Constants.INVALID_IDNOTFOND;
import static dataStructure.Constants.INVALID_NOTE;
import static dataStructure.Constants.INVALID_NOTEID;
import static dataStructure.Constants.INVALID_NOTE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_OBJECTIVE;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.INVALID_OBJECTIVE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_USEREMAIL;
import static dataStructure.Constants.INVALID_USERGUID_NOTFOUND;
import static dataStructure.Constants.NOTDELETED_FBREQ;
import static dataStructure.Constants.NOTE_NOTADDED_ERROR;
import static dataStructure.Constants.NULL_OBJECTIVE;
import static dataStructure.Constants.NULL_USER_DATA;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoException;

import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.GroupFeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import externalServices.ad.ADProfileDAO;

/**
 * 
 * @author Christopher Kai
 * @version 1.0
 * @since 23rd January 2017
 * 
 * This class contains the definition of the EmployeeDAO object
 *
 */

public class HrDataDAO {
	
		
		public HrDataDAO(Datastore dbConnection) {
			EmployeeDAO.dbConnection = dbConnection;
		}//Constructor
		
		//Set of methods to query the database providing HR data
		
		public static long getTotalNumberOfUsers(){
			
			long totalAccounts = EmployeeDAO.dbConnection.find(Employee.class).countAll();
			return totalAccounts;
			
		}//getNumberOfUsers
		
		public static long getTotalUsersWithObjectives() {
			
			long totalUsersWithObjectives= EmployeeDAO.dbConnection.find(Employee.class).field("objectives").exists().countAll();
			return totalUsersWithObjectives;
			
		}//getTotalUsersWithObjectives
		
		public static long getTotalUsersWithDevelopmentNeeds() {
			
			long totalUsersWithDevelopmentNeeds= EmployeeDAO.dbConnection.find(Employee.class).field("developmentNeeds").exists().countAll();
			return totalUsersWithDevelopmentNeeds;
			
		}//getTotalUsersWithDevelopmentNeeds
		
		
		

}
