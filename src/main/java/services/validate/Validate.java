package services.validate;

import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;

public class Validate {

	
	public static boolean areStringsEmptyorNull(String... strings) throws InvalidAttributeValueException{
		if(strings.length < 1)
			throw new InvalidAttributeValueException("No values have been given, please try again with values.");
		
		for(String string : strings){
			if (string == null || string.isEmpty())
				throw new InvalidAttributeValueException("One or more of the values are empty. Please try again.");
		}
		
		return false;
	}
	
	public static boolean isNull(Object... objects) throws InvalidAttributeValueException{
		if(objects.length < 1)
			throw new InvalidAttributeValueException("No values have been given, please try again with values.");
		
		for(Object object : objects){
			if(object == null)
				throw new InvalidAttributeValueException("One or more of the values are null. Please try again.");
		}
		
		return false;
	}
	
	
	public static boolean isValidEmailSyntax(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
		return pattern.matcher(email).matches();
	}


}
