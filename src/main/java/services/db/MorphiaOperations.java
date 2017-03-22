package services.db;

import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Employee;
import dataStructure.EmployeeProfile;

/**
 * Encapsulates all database operations performed using Morphia.
 * 
 * This class should be used for find/update/delete queries that affect entire data structures.
 *
 * @see dataStructure
 * @see MongoOperation
 */
public class MorphiaOperations
{
  private static final String PROFILE = "profile";
  private static final String EMPLOYEE_ID = "profile.employeeID";
  private static final String EMAIL_ADDRESSES = "profile.emailAddresses";

  private final Datastore datastore;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param datastore
   */
  public MorphiaOperations(final Datastore datastore)
  {
    this.datastore = datastore;
  }

  /**
   * Retrieves an employee object from the database based on the given criteria.
   *
   * @param field The field to search
   * @param value The value of the field to search on
   * @return the {@code Employee} object or null if it could not be found
   */
  public <T> Employee getEmployee(final String field, final T value)
  {
    Employee employee = datastore.find(Employee.class, field, value).get();

    return employee;
  }
  
  /**
   * Retrieves an employee object from the database by matching the given email address
   *
   * @param emailAddress the email address to search for
   * @return the {@code Employee} object or null if it could not be found
   */
  public Employee getEmployeeFromEmailAddress(final String emailAddress)
  {
    Set<String> emailAddresses = new HashSet<>();
    emailAddresses.add(emailAddress);

    return getEmployeeFromEmailAddress(emailAddresses);
  }
  
  /**
   * 
   * Retrieves an employee object from the database by matching the given email addresses
   *
   * @param emailAddresses the email addresses to search for
   * @return the {@code Employee} object or null if it could not be found
   */
  public Employee getEmployeeFromEmailAddress(final Set<String> emailAddresses)
  {
    Employee employee = datastore.find(Employee.class).filter(in(EMAIL_ADDRESSES), emailAddresses).get();
    
    return employee;
  }

  /**
   * Retrieves an employee profile object from the database based on the given criteria.
   *
   * @param field The field to search
   * @param value The value of the field to search on
   * @throws NullPointerException if the employee count not be found.
   */
  public <T> EmployeeProfile getEmployeeProfile(final String field, final T value) throws NullPointerException
  {
    EmployeeProfile profile = datastore.find(Employee.class, field, value).retrievedFields(true, PROFILE).get()
        .getProfile();

    return profile;
  }
  
  /**
   * Updates an aspect of an employee's data using the given criteria.  These include Objectives, Notes, and Development Needs.
   *
   * @param employeeID the employee ID of the employee to update
   * @param updateField the field/aspect to update
   * @param updateObject the updated field/aspect
   */
  public <T> void updateEmployee(final long employeeID, final String updateField, final T updateObject)
  {
    UpdateOperations<Employee> ops = datastore.createUpdateOperations(Employee.class).set(updateField,
        updateObject);
    datastore.update(getEmployeeQuery(EMPLOYEE_ID, employeeID), ops);
  }

  public void updateEmployee(Employee employee)
  {
    datastore.save(employee);
  }

  private <T> Query<Employee> getEmployeeQuery(final String field, final T value)
  {
    return datastore.find(Employee.class, field, value);
  }

  private String in(String field)
  {
    return field.concat(" in");
  }
}
