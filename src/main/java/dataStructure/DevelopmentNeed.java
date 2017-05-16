package dataStructure;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class DevelopmentNeed extends Objective
{
  /* long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 1L;

  /** Represent Category of a development need. */
  public enum Category
  {
    JobTraining("On Job Training"), ClassroomTraining("Classroom Training"), Online("Online or E-learning"), SelfStudy(
        "Self Study"), Other("Other");

    private String categoryStr;

    private Category(String categoryStr)
    {
      this.categoryStr = categoryStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @return
     */
    public String getCategoryStr()
    {
      return this.categoryStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param categoryString
     * @return
     * @throws InvalidAttributeValueException
     */
    public static Category getCategoryFromString(String categoryString) throws InvalidAttributeValueException
    {
      switch (categoryString)
      {
        case "On Job Training":
          return Category.JobTraining;
        case "Classroom Training":
          return Category.ClassroomTraining;
        case "Online or E-learning":
          return Category.Online;
        case "Self Study":
          return Category.SelfStudy;
        case "Other":
          return Category.Other;
      }
      throw new InvalidAttributeValueException("This enum string does not exist");
    }

  }

  // TODO Why is this an Object and not a String??
  private static final Object DEVELOPMENT_NEED = "development need";

  /* int Property - Represents the category of the objective */
  private String category;

  /**
   * No-args Constructor - Responsible for initialising this object.
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

  /** @return the category */
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
   * Returns a document containing the differences (only title, description, dueDate & category) of the development
   * need.
   *
   * @see dataStructure.Objective#differences(dataStructure.Objective)
   *
   * @param objective
   * @return
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
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see dataStructure.Objective#createActivity(dataStructure.CRUD, dataStructure.EmployeeProfile)
   *
   * @param activityType
   * @param profile
   * @return
   */
  public Activity createActivity(final CRUD activityType, final EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(activityType.getVerb())
        .append(" ").append(DEVELOPMENT_NEED).append(" #").append(getId()).append(": ").append(getTitle()).toString();

    return new Activity(activityString, getLastModified());
  }
}
