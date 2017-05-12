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
  
  public HistoryService(final MorphiaOperations morphiaOperations)
  {
    this.morphiaOperations = morphiaOperations;
  }
  
  public List<Objective> getObjectives(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getObjectives();
  }

  public List<DevelopmentNeed> getDevelopmentNeeds(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getDevelopmentNeeds();
  }

  public List<Note> getNotes(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getNotes();
  }

  public List<Feedback> getFeedback(long employeeId) throws EmployeeNotFoundException
  {
    List<Feedback> feedbackList = morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getFeedback();
    Collections.reverse(feedbackList);

    return feedbackList;
  }

  public List<Rating> getRatings(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId).getRatings();
  }
}
