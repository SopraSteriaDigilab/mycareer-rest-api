package dataStructure;

import javax.management.InvalidAttributeValueException;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class Competency extends DBObject implements Comparable<Competency>
{
  /** Represents competency title of any Competencyobject. */
  public enum CompetencyTitle
  {
    ACCOUNTABILITY("Accountability"), EFFECTIVE_COMMUNICATION("Effective Communication"), LEADERSHIP(
        "Leadership"), SERVICE_EXCELLENCE("Service Excellence"), BUSINESS_AWARENESS(
            "Business Awareness"), FUTURE_ORIENTATION(
                "Future Orientation"), INNOVATION_AND_CHANGE("Innovation and Change"), TEAMWORK("Teamwork");

    private String competencyTitleStr;

    private CompetencyTitle(String competencyTitleStr)
    {
      this.competencyTitleStr = competencyTitleStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @return
     */
    public String getCompetencyTitleStr()
    {
      return this.competencyTitleStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param competencyTitleStr
     * @return
     * @throws InvalidAttributeValueException
     */
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
  private String title;

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
    return title;
  }

  /** @param title The value to set. */
  public void setTitle(CompetencyTitle title)
  {
    this.title = title.getCompetencyTitleStr();
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

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   *
   * @param competency
   * @return
   */
  @Override
  public int compareTo(Competency competency)
  {
    return Boolean.compare(!this.isSelected(), !competency.isSelected());
  }

}
