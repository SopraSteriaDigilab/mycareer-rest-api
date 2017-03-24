package dataStructure;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

/**
 * This class contains the definition of the Development Need object.
 */
public class DevelopmentNeed extends Objective
{
  /** Represent Category of a development need. */
  public enum Category
  {
    JobTraining("On Job Training"), ClassroomTraining("Classroom Training"), Online("Online or E-learning"), SelfStudy(
        "Self Study"), Other("Other");

    private String categoryStr;

    Category(String categoryStr)
    {
      this.categoryStr = categoryStr;
    }

    public String getCategoryStr()
    {
      return this.categoryStr;
    }

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

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 1L;

  /** int Property - Represents the category of the objective */
  private String category;

  /**
   * Default Constructor - Responsible for initialising this object.
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
   * Returns a document containing the differences (only title, description, dueDate & category) of the development need.
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
    if(!this.getCategory().equals(developmentNeed.getCategory()))
    {
      differences.append("category", developmentNeed.getCategory());
    }
    return differences;
  }
  
  

}
