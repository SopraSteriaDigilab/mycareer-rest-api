package services.validate;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;

public class Validate {

	
	public static boolean areStringsEmptyorNull(String... strings) throws InvalidAttributeValueException{
		if(strings.length < 1)
			throw new InvalidAttributeValueException("No values have been given, please try again with values.");
		
		for(String string : strings){
			if (string == null || string.isEmpty()){
				throw new InvalidAttributeValueException("One or more of the values are empty. Please fill in all the required fields.");
			}
		}
		
		return false;
	}
	
	public static Set<String> stringEmailsToHashSet(String emailsString) throws InvalidAttributeValueException{
		Set<String> emailSet = new HashSet<>();
		String[] emailArray = emailsString.split(",");
		
		for(String email : emailArray){
			if(email.isEmpty() || !isValidEmailSyntax(email.trim())){
				throw new InvalidAttributeValueException("One or more of the emails are invalid");
			}
			emailSet.add(email.trim());
		}	
		
		return emailSet;
	}
	
	private static boolean isValidEmailSyntax(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
		return pattern.matcher(email).matches();
	}


}
