//package externalServices.mongoDB;
//
//
//import org.mongodb.morphia.Datastore;
//
//import dataStructure.Employee;
//
///**
// * 
// * @author Christopher Kai
// * @version 1.0
// * @since 23rd January 2017
// * 
// * This class contains the definition of the EmployeeDAO object
// *
// */
//
//public class HrDataDAO {
//	
//		
//		public HrDataDAO(Datastore dbConnection) {
//			EmployeeDAO.dbConnection = dbConnection;
//		}//Constructor
//		
//		//Set of methods to query the database providing HR data
//		
//		public static long getTotalNumberOfUsers(){
//			
//			long totalAccounts = EmployeeDAO.dbConnection.find(Employee.class).countAll();
//			return totalAccounts;
//			
//		}//getNumberOfUsers
//		
//		public static long getTotalUsersWithObjectives() {
//			
//			long totalUsersWithObjectives= EmployeeDAO.dbConnection.find(Employee.class).field("objectives").exists().countAll();
//			return totalUsersWithObjectives;
//			
//		}//getTotalUsersWithObjectives
//		
//		public static long getTotalUsersWithDevelopmentNeeds() {
//			
//			long totalUsersWithDevelopmentNeeds= EmployeeDAO.dbConnection.find(Employee.class).field("developmentNeeds").exists().countAll();
//			return totalUsersWithDevelopmentNeeds;
//			
//		}//getTotalUsersWithDevelopmentNeeds
//		
//		
//		
//
//}
