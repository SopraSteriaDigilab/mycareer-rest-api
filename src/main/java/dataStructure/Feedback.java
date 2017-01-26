package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.management.InvalidAttributeValueException;

import services.validate.Validate;

/**
 * Feedback object for MyCareer
 *
 * @author Ridhwan Nacef
 * @version 1.0
 * @since January 2016
 * 
 */
public class Feedback implements Serializable {
	
	private static final long serialVersionUID = -1220037164373122395L;

	/** Unique identifier */
	private int id;
	
	/** Email address of feedback provider */
	private String providerEmail;
	
	/** Name of feedback provider */
	private String providerName;
	
	/** The feedback */
	private String feedbackDescription;
	
	/** Time stamp of feedback */
	private String timeStamp;
	
	/** Empty Constructor */
	public Feedback(){}

	/** Constructor with params */
	public Feedback(int id, String providerEmail, String providerName, String feedback, String timeStamp) {
		super();
		this.id = id;
		this.providerEmail = providerEmail;
		this.providerName = providerName;
		this.feedbackDescription = feedbackDescription;
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the feedback id
	 * @return returns the id of the feedback
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the feedback id 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * Gets the feedback providers email
	 * @return the email of the feedback provider
	 */
	public String getProviderEmail() {
		return providerEmail;
	}

	/**
	 * Sets the feedback providers email
	 * @param providerEmail
	 * @throws InvalidAttributeValueException 
	 */
	public void setProviderEmail(String providerEmail) throws InvalidAttributeValueException {
		if(Validate.isValidEmailSyntax(providerEmail))
			this.providerEmail = providerEmail;
		throw new InvalidAttributeValueException("This email address is not valid syntax."); 
	}

	/**
	 * Gets the feedback providers name
	 * @return the name of the feedback provider
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * Sets the providers name
	 * @param providerName
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * Gets the feedback description
	 * @return Returns the description of the feedback
	 */
	public String getFeedbackDescription() {
		return feedbackDescription;
	}

	/**
	 * Sets the feedback description
	 * @param feedbackDescription
	 */
	public void setFeedbackDescription(String feedbackDescription) {
		this.feedbackDescription = feedbackDescription;
	}

	/**
	 * Gets the feedback time stamp
	 * @return the time stamp of the feedback
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Sets the feedback times tamp
	 * @param timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = LocalDateTime.now(ZoneId.of(UK_TIMEZONE)).toString();
	}

}
