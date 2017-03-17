package services.db;

import org.mongodb.morphia.Datastore;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(MorphiaOperations.class);

  private static final String EMPLOYEE_NOT_FOUND = "No employee matches the given criteria";

  private static final String PROFILE = "profile";

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
   * @return the {@code Employee} object
   * @throws DBOperationException if the query returns no results
   */
  public <T> Employee getEmployee(final String field, final T value) throws DBOperationException
  {
    Employee employee = datastore
        .find(Employee.class, field, value)
        .get();
    
    if (employee == null)
    {
      throw new DBOperationException(EMPLOYEE_NOT_FOUND);
    }
    
    return employee;
  }

  /**
   * Retrieves an employee profile object from the database based on the given criteria.
   *
   * @param field The field to search
   * @param value The value of the field to search on
   * @throws DBOperationException if the query returns no results
   */
  public <T> EmployeeProfile getEmployeeProfile(final String field, final T value) throws DBOperationException
  {
    EmployeeProfile profile = null;

    try
    {
      profile = datastore
          .find(Employee.class, field, value)
          .retrievedFields(true, PROFILE)
          .get()
          .getProfile();
    }
    catch (NullPointerException npe)
    {
      LOGGER.error(EMPLOYEE_NOT_FOUND);
      throw new DBOperationException(EMPLOYEE_NOT_FOUND, npe);
    }

    return profile;
  }
}
