package dataStructure;


import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class contains the definition of the Development Need object.
 */
public class DevelopmentNeed_NEW extends Objective_NEW
{

  /** Represent Category of a development need. */
  public enum Category
  {
    JobTraining("On Job Training"), ClassroomTraining("Classroom Training"), Online("Online or E-learning"), SelfStudy("Self Study"), Other("Other");
    
    private String categoryStr;
    
    Category(String categoryStr){
      this.categoryStr = categoryStr; 
     }
     
     public String getCategoryStr() {
       return this.categoryStr;
     }
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 7267058498926094125L;

  /** int Property - Represents the category of the objective */
  @NotBlank(message = ERROR_EMPTY)
  @Pattern(regexp = "^(OnJobTraining)|(ClassroomTraining)|(OnlineorELearning)|(SelfStudy)|(Other)$")
  private String category;

  /**
   * Default Constructor - Responsible for initialising this object.
   */
  public DevelopmentNeed_NEW()
  {
  }

  /**
   * TYPE Constructor - Responsible for initialising this object.
   */
  public DevelopmentNeed_NEW(DevelopmentNeed_NEW developmentNeed)
  {
    super(developmentNeed);
    this.setCategory(Category.valueOf(developmentNeed.getCategory()));
  }

  /** @return the category */
  public String getCategory()
  {
    return Category.valueOf(this.category).getCategoryStr();
  }

  /**
   * Mutator for the named property. Must be one of the following: OnJobTraining, ClassroomTraining, OnlineorELearning,
   * SelfStudy, Other.
   */
  public String setCategory(Category category)
  {
    return this.category = category.getCategoryStr();
  }

}
