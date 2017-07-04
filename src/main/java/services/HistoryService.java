package services;

import static dataStructure.EmployeeProfile.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import dataStructure.DevelopmentNeed;
import dataStructure.Feedback;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;
import services.db.MorphiaOperations;

/**
 * Service class which provides access to an employee's entire history on MyCareer.
 */
@Component
public class HistoryService
{
  private MorphiaOperations morphiaOperations;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param morphiaOperations
   */
  public HistoryService(final MorphiaOperations morphiaOperations)
  {
    this.morphiaOperations = morphiaOperations;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Objective> getObjectives(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getObjectives();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<DevelopmentNeed> getDevelopmentNeeds(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getDevelopmentNeeds();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Note> getNotes(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getNotes();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Feedback> getFeedback(long employeeId) throws EmployeeNotFoundException
  {
    List<Feedback> feedbackList = morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getFeedback();
    Collections.reverse(feedbackList);

    return feedbackList;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Rating> getRatings(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getRatings();
  }
}
