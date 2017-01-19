package services;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import services.validate.Validate;

public class Helper {

	
	public static String generateID(long id){
		LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(UK_TIMEZONE));
		String stringDate = localDateTime.toString().replace("-", "").replace("T", "").replace(":", "").replace(".", "");
		String generatedId = id + "_" + stringDate;
		return generatedId;
	}
	
	public static Set<String> stringEmailsToHashSet(String emailsString) throws InvalidAttributeValueException{
		Set<String> emailSet = new HashSet<>();
		String[] emailArray = emailsString.split(",");
		
		for(String email : emailArray){
			if(email.isEmpty() || !Validate.isValidEmailSyntax(email.trim())){
				throw new InvalidAttributeValueException("One or more of the emails are invalid");
			}
			emailSet.add(email.trim());
		}	
		return emailSet;
	}
	
}
