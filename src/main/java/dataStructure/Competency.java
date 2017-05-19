package dataStructure;

/**
 * An employee's competency from the Sopra Steria <a
 * href=http://portal.corp.sopra/hr/HR_IN_ST/MyPath/Competency_Framework/Pages/default.aspx>competency framework</a>.
 * All employee's have all competencies, but they may choose which ones to focus upon.
 * 
 * @see Employee
 * @see DBObject
 */
public class Competency extends DBObject implements Comparable<Competency>
{
  /**
   * The names and titles of the competencies in the Sopra Steria <a
   * href=http://portal.corp.sopra/hr/HR_IN_ST/MyPath/Competency_Framework/Pages/default.aspx>competency framework</a>
   */
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

    /** @return The title string of this {@code CompetencyTitle} */
    public String getCompetencyTitleStr()
    {
      return competencyTitleStr;
    }

    /**
     * @param competencyTitleStr The title of a competency
     * @return The {@code CompetencyTitle} instance whose title is equal to the provided {@code competencyTitleStr}.
     * @throws IllegalArgumentException if the provided {@code competencyTitleStr} did not match a competency title.
     */
    public static CompetencyTitle getCompetencyTitleFromString(final String competencyTitleStr)
        throws IllegalArgumentException
    {
      for (final CompetencyTitle competencyTitle : values())
      {
        if (competencyTitle.competencyTitleStr.equals(competencyTitleStr))
        {
          return competencyTitle;
        }
      }

      throw new IllegalArgumentException("This enum string does not exist");
    }
  }

  private static final long serialVersionUID = 1L;

  private String title;
  private boolean isSelected;

  /**
   * Competency Constructor - No-args constructor provided for use by Morphia. Should not be used in application code.
   */
  public Competency()
  {
  }

  /**
   * Competency Constructor - Responsible for initialising this object.
   *
   * @param id The id number of this competency.
   * @param title The title of this competency.
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

  /** @return isSelected - {@code true} if this competency is being focussed upon. */
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
   * Override of compareTo method.
   * 
   * Compares the negated focus of this competency with the negated focus of the other competency. Ignores all other
   * aspects of the two competencies.
   *
   * @see isSelected()
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   * @see Boolean#compare
   *
   * @return {@code Boolean.compare(!this.isSelected(), !competency.isSelected());}
   */
  @Override
  public int compareTo(Competency competency)
  {
    return Boolean.compare(!this.isSelected(), !competency.isSelected());
  }

}
