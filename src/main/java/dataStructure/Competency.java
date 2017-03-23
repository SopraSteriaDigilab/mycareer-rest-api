package dataStructure;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

/**
 * This class contains the definition of the Competency object.
 *
 */
public class Competency extends DBObject
{

  /** Represents competency title of any Competencyobject. */
  public enum CompetencyTitle
  {
    ACCOUNTABILITY("Accountability"), EFFECTIVE_COMMUNICATION("Effective Communication"), LEADERSHIP(
        "Leadership"), SERVICE_EXCELLENCE("Service Excellence"), BUSINESS_AWARENESS(
            "Business Awareness"), FUTURE_ORIENTATION(
                "Future Orientation"), INNOVATION_AND_CHANGE("Innovation and Change"), TEAMWORK("Teamwork");

    private String competencyTitleStr; 

    CompetencyTitle(String competencyTitleStr)
    {
      this.competencyTitleStr = competencyTitleStr;
    }

    public String getCompetencyTitleStr()
    {
      return this.competencyTitleStr;
    }

    public static CompetencyTitle getCompetencyTitleFromString(String competencyTitleStr)
        throws InvalidAttributeValueException
    {
      switch (competencyTitleStr)
      {
        case "Accountability":
          return CompetencyTitle.ACCOUNTABILITY;
        case "Effective Communication":
          return CompetencyTitle.EFFECTIVE_COMMUNICATION;
        case "Leadership":
          return CompetencyTitle.LEADERSHIP;
        case "Service Excellence":
          return CompetencyTitle.SERVICE_EXCELLENCE;
        case "Business Awareness":
          return CompetencyTitle.BUSINESS_AWARENESS;
        case "Future Orientation":
          return CompetencyTitle.FUTURE_ORIENTATION;
        case "Innovation and Change":
          return CompetencyTitle.INNOVATION_AND_CHANGE;
        case "Teamwork":
          return CompetencyTitle.TEAMWORK;
      }
      throw new InvalidAttributeValueException("This enum string does not exist");
    }
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 1327626647922503101L;

  /** String Property - Represents the title of the competency. */
  private CompetencyTitle title;

  /** boolean Property - Indicates if the competenct is selected. */
  private boolean isSelected;

  /**
   * Default Constructor - Responsible for initialising this object.
   */
  public Competency()
  {
  }

  /**
   * Competeny_NEW Constructor - Responsible for initialising this object.
   *
   * @param title
   * @param isSelected
   */
  public Competency(int id, CompetencyTitle title)
  {
    this.setId(id);
    this.setTitle(title);
    this.setSelected(false);
  }

  /** @return the title */
  public String getTitle()
  {
    return title.getCompetencyTitleStr();
  }

  /** @param title The value to set. */
  public void setTitle(CompetencyTitle title)
  {
    this.title = title;
    this.setLastModified();
  }

  /** @return the isSelected */
  public boolean isSelected()
  {
    return isSelected;
  }

  /** @param isSelected The value to set. */
  public void setSelected(boolean isSelected)
  {
    this.isSelected = isSelected;
    this.setLastModified();
  }

  @Override
  public Document toDocument()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
