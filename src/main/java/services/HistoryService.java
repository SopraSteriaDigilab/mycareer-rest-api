package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import dataStructure.DevelopmentNeed;
import dataStructure.Feedback;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;
import services.db.MongoOperations;
import services.db.MorphiaOperations;

/**
 * This class contains the definition of the EmployeeDAO object
 *
 */
@Component
@PropertySource("${ENVIRONMENT}.properties")
public class HistoryService
{
  @Autowired
  private MorphiaOperations morphiaOperations;
  
  public HistoryService(final MorphiaOperations morphiaOperations)
  {
    this.morphiaOperations = morphiaOperations;
  }

  public List<Objective> getObjectives(long employeeId) throws EmployeeNotFoundException
  {
    return null;
  }

  public List<DevelopmentNeed> getDevelopmentNeeds(long employeeId) throws EmployeeNotFoundException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Note> getNotes(long employeeID) throws EmployeeNotFoundException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Feedback> getFeedback(long employeeID) throws EmployeeNotFoundException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Rating> getRatings(long employeeId) throws EmployeeNotFoundException
  {
    // TODO Auto-generated method stub
    return null;
  }
}
