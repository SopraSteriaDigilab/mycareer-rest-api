package services.ews;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Employee;
import dataStructure.FeedbackRequest;
import services.EmployeeDAO;
import services.Helper;

public class Outgoing {
	
	private static final Logger logger = LoggerFactory.getLogger(Outgoing.class);
	
	private static final String FEEDBACK_REQUEST_SUBJECT = "Feedback Request";
	
	public static void processFeedbackRequest(long employeeID, String emailsString, String notes) throws Exception {
		Employee requester = EmployeeDAO.getEmployee(employeeID);
		Set<String> recipientList = Helper.stringEmailsToHashSet(emailsString);
		List<String> errorRecipientList = new ArrayList<String>();
		
		for(String recipient : recipientList){
			String tempID = Helper.generateID(employeeID);
			String body = notes + " \n\n Feedback_Request_" + tempID;
			try {
				EmailService.sendEmail(requester.getEmailAddress(), recipient, FEEDBACK_REQUEST_SUBJECT, body);
			} catch (Exception e) {
				logger.error(e.getMessage());
				errorRecipientList.add(recipient);
				continue;
			}
			EmployeeDAO.addFeedbackRequest(requester, new FeedbackRequest(tempID, recipient));
		}
		
		if(!errorRecipientList.isEmpty()){
			throw new Exception("There were issues sending requests to the following addresses: \n" + errorRecipientList.toString());
		}
	}

}
