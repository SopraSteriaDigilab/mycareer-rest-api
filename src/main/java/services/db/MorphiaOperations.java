package services.db;

import static dataStructure.EmailAddresses.*;
import static dataStructure.EmployeeProfile.*;
import static dataStructure.Employee.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import dataStructure.DBObject;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

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
  private static final String EMPLOYEE_NOT_FOUND = "Employee not found based on the criteria: ";

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
   * Retrieves an employee object from the database based on the given criteria.
   *
   * @param field The field to search
   * @param value The value of the field to search on
   * @return the {@code Employee} object
   * @throw EmployeeNotFoundException if the employee could not be found
   */
  public <T> Employee getEmployeeOrThrow(final String field, final T value) throws EmployeeNotFoundException
  {
    Employee employee = datastore.find(Employee.class, field, value).get();

    if (employee == null)
    {
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND + field + ": " + value);
    }

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
    Query<Employee> query = datastore.find(Employee.class);
    Criteria mail = query.criteria(MAIL).equal(emailAddress);
    Criteria targetAddress = query.criteria(TARGET_ADDRESS).equal(emailAddress);
    Criteria userAddress = query.criteria(USER_ADDRESS).equal(emailAddress);
    query.or(mail, targetAddress, userAddress);

    return query.get();
  }

  /**
   * Retrieves an employee object from the database by matching the given email address
   *
   * @param emailAddress the email address to search for
   * @return the {@code Employee} object or throws exception if null
   * @throws EmployeeNotFoundException
   */
  public Employee getEmployeeFromEmailAddressOrThrow(String emailAddress) throws EmployeeNotFoundException
  {
    Employee employee = getEmployeeFromEmailAddress(emailAddress);

    if (employee == null)
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND + emailAddress);

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

  // This currently is private, untested and unused, but may be used in future to update multiple fields in a single db
  // call
  private <T extends DBObject> void updateEmployee(final long employeeID, final Map<String, T> updates)
  {
    UpdateOperations<Employee> ops = datastore.createUpdateOperations(Employee.class);
    updates.forEach((k, v) -> ops.set(k, v));
    datastore.update(getEmployeeQuery(EMPLOYEE_ID, employeeID), ops);
  }

  /**
   * Updates an aspect of an employee's data using the given criteria. These include Objectives, Notes, and Development
   * Needs.
   *
   * @param employeeID the employee ID of the employee to update
   * @param updateField the field/aspect to update
   * @param updateObject the updated field/aspect
   */
  public <T> void updateEmployee(final long employeeID, final String updateField, final T updateObject)
  {
    UpdateOperations<Employee> ops = datastore.createUpdateOperations(Employee.class).set(updateField, updateObject);
    datastore.update(getEmployeeQuery(EMPLOYEE_ID, employeeID), ops);
  }

  public void saveEmployee(final Employee employee)
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
