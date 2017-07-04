package dataStructure;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

/**
 * An employee's development need. Used by Morphia as a template for mapping from a MongoDB document.
 */
public class DevelopmentNeed extends Objective
{
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String ID = "developmentNeeds.id";

  /** TODO describe */
  public static final String LAST_MODIFIED = "developmentNeeds.lastModified";

  /** TODO describe */
  public static final String CREATED_ON = "developmentNeeds.createdOn";

  /** TODO describe */
  public static final String TITLE = "developmentNeeds.title";

  /** TODO describe */
  public static final String DESCRIPTION = "developmentNeeds.description";

  /** TODO describe */
  public static final String DUE_DATE = "developmentNeeds.dueDate";

  /** TODO describe */
  public static final String PROPOSED_BY = "developmentNeeds.proposedBy";

  /** TODO describe */
  public static final String PROGRESS = "developmentNeeds.progress";

  /** TODO describe */
  public static final String IS_ARCHIVED = "developmentNeeds.isArchived";

  /** TODO describe */
  public static final String CATEGORY = "developmentNeeds.category";

  private static final String DEVELOPMENT_NEED = "development need";

  /* int Property - Represents the category of the objective */
  private String category;

  /**
   * DevelopmentNeed Constructor - No-args constructor provided for use by Morphia. Should not be used in application
   * code.
   */
  public DevelopmentNeed()
  {
  }

  /**
   * Development Need Constructor - Responsible for initialising this object.
   */
  public DevelopmentNeed(String title, String description, LocalDate dueDate, Category category)
  {
    super(title, description, dueDate);
    this.setCategory(category);
  }

  /**
   * Development Need Constructor - Responsible for initialising this object.
   */
  public DevelopmentNeed(int id, String title, String description, LocalDate dueDate, Category category)
  {
    this(title, description, dueDate, category);
    this.setId(id);
  }

  /** @return the full description of the category of this development need */
  public String getCategory()
  {
    return this.category;
  }

  /** @param the Category to set. */
  public void setCategory(Category category)
  {
    this.category = category.getCategoryStr();
    this.setLastModified();
  }

  /**
   * Override of differences method.
   *
   * @see dataStructure.Objective#differences(dataStructure.Objective)
   *
   * @param objective The objective to compare with this.
   * @return a document containing the differences (only title, description, dueDate & category) of the development
   *         need.
   */
  @Override
  public Document differences(Objective objective)
  {
    DevelopmentNeed developmentNeed = (DevelopmentNeed) objective;
    Document differences = super.differences(developmentNeed);
    if (!this.getCategory().equals(developmentNeed.getCategory()))
    {
      differences.append("category", developmentNeed.getCategory());
    }
    return differences;
  }

  /**
   * Override of createActivity method.
   *
   * Creates an item of activity describing the given action performed on this development need by the given employee.
   *
   * @see dataStructure.Objective#createActivity(dataStructure.Action, dataStructure.EmployeeProfile)
   *
   * @param activityType The action performed.
   * @param profile The profile of the employee performing the action.
   * @return The generated activity.
   */
  @Override
  public Activity createActivity(final Action activityType, final EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(activityType.getVerb())
        .append(" ").append(DEVELOPMENT_NEED).append(" #").append(getId()).append(": ").append(getTitle()).toString();

    return new Activity(activityString, getLastModified());
  }

  /**
   * A category of development need.
   */
  public enum Category
  {
    JOB_TRAINING("On Job Training"), CLASSROOM_TRAINING("Classroom Training"), ONLINE(
        "Online or E-learning"), SELF_STUDY("Self Study"), OTHER("Other");

    private String categoryStr;

    private Category(String categoryStr)
    {
      this.categoryStr = categoryStr;
    }

    /** @return The full string description of this category. */
    public String getCategoryStr()
    {
      return this.categoryStr;
    }

    /**
     * @param categoryString The full string description of a category
     * @return The {@code Category} instance whose full description is equal to the provided {@code categoryString}.
     * @throws IllegalArgumentException if the provided {@code categoryString} did not match a category description.
     */
    public static Category getCategoryFromString(String categoryString)
    {
      for (final Category category : values())
      {
        if (category.categoryStr.equals(categoryString))
        {
          return category;
        }
      }

      throw new IllegalArgumentException("This enum string does not exist");
    }
  }
}
