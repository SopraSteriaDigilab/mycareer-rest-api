package dataStructure;

import static dataStructure.Constants.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;

import com.google.gson.Gson;

/**
 * This class contains the definition of the ADProfile_Advanced object
 *
 */
public class ADProfile_Advanced extends ADProfile_Basic implements Serializable
{

  // Global Constant
  private static final long serialVersionUID = -5982675570485578676L;
  // Global Variables
  private String GUID;
  private String team;
  private String sopraDepartment;
  private String steriaDepartment;
  private String sector;
  private String superSector;
  private String company;
  private List<String> reporteeCNs;

  // Empty Constructor
  public ADProfile_Advanced()
  {
    company = INVALID_STRING;
    superSector = INVALID_STRING;
    sector = INVALID_STRING;
    steriaDepartment = INVALID_STRING;
    sopraDepartment = INVALID_STRING;
    team = INVALID_STRING;
    GUID = INVALID_STRING;
    reporteeCNs = new ArrayList<String>();
  }

  // Constructor with parameters
  public ADProfile_Advanced(long employeeID, String guid, String name, String surname, String emailAddress,
      String username, String company, String superSector, String sector, String steriaDepartment,
      String sopraDepartment, String team, boolean isManager, boolean hasHRDash, List<String> reps)
      throws InvalidAttributeValueException
  {
    super(employeeID, surname, name, isManager, username, emailAddress, hasHRDash);
    setGUID(guid);
    setCompany(company);
    setSuperSector(superSector);
    setSector(sector);
    setSteriaDepartment(steriaDepartment);
    setSopraDepartment(sopraDepartment);
    setTeam(team);
    setReporteeCNs(reps);
  }

  public String getSopraDepartment()
  {
    return sopraDepartment;
  }

  public String getSteriaDepartment()
  {
    return steriaDepartment;
  }

  public String getSector()
  {
    return sector;
  }

  public void setSteriaDepartment(String steriaDepartment) throws InvalidAttributeValueException
  {
    if (steriaDepartment != null && steriaDepartment.length() > 0 && steriaDepartment.length() < 150)
    {
      this.steriaDepartment = steriaDepartment;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public void setSector(String sector) throws InvalidAttributeValueException
  {
    if (sector != null && sector.length() > 0 && sector.length() < 15)
    {
      this.sector = sector;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public void setSopraDepartment(String sopraDepartment) throws InvalidAttributeValueException
  {
    if (sopraDepartment != null && sopraDepartment.length() > 0 && sopraDepartment.length() < 150)
    {
      this.sopraDepartment = sopraDepartment;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public void setSuperSector(String superSector) throws InvalidAttributeValueException
  {
    if (superSector != null && superSector.length() > 0 && superSector.length() < 150)
    {
      this.superSector = superSector;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  /**
   * 
   * @param guid this is a unique value created for each employee of the company
   * @throws InvalidAttributeValueException
   */
  public void setGUID(String guid) throws InvalidAttributeValueException
  {
    if (guid != null && guid.length() > 0)
    {
      this.GUID = guid;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_GUID);
    }
  }

  public String getGUID()
  {
    return this.GUID;
  }

  /**
   * 
   * @param com the company name which length must be less than 150 characters
   * @throws InvalidAttributeValueException
   */
  public void setCompany(String com) throws InvalidAttributeValueException
  {
    if (com != null && com.length() > 0 && com.length() < 150)
    {
      this.company = com;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public String getCompany()
  {
    return this.company;
  }

  /**
   * 
   * @param team the team name which length must be less than 150 characters
   * @throws InvalidAttributeValueException
   */
  public void setTeam(String team) throws InvalidAttributeValueException
  {
    if (team != null && team.length() > 0 && team.length() < 150) this.team = team;
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_TEAM);
  }

  public String getTeam()
  {
    return this.team;
  }

  /**
   * This method assigns reportees to a manager
   * 
   * @param repostees
   * @throws InvalidAttributeValueException
   */
  public void setReporteeCNs(List<String> reportees) throws InvalidAttributeValueException
  {
    if (reportees == null) throw new InvalidAttributeValueException(Constants.INVALID_NULLREPORTEESLIST);

    this.reporteeCNs = reportees;
  }

  public List<String> getReporteeCNs()
  {
    return this.reporteeCNs;
  }

  /**
   * This method adds a reportee's CN to the list of reportees
   * 
   * @param cn
   * @return true of false indicating whether the operation was successful or not
   * @throws InvalidAttributeValueException
   */
  public boolean addReportee(String cn) throws InvalidAttributeValueException
  {
    if (this.reporteeCNs == null) this.reporteeCNs = new ArrayList<>();
    if (cn != null && cn.length() > 1) return reporteeCNs.add(cn);
    else throw new InvalidAttributeValueException(INVALID_NULLREPORTEE);
  }

  public String toGson()
  {
    Gson gsonData = new Gson();
    return gsonData.toJson(this);
  }

  @Override
  public String toString()
  {
    String s = "";
    s += super.toString();
    // Add the generic information
    s += "GUID: " + this.getGUID() + "\n";
    s += "EmailAddress " + this.getEmailAddress() + "\n";
    s += "Company: " + this.getCompany() + "\n";
    s += "Team: " + this.getTeam() + "\n";
    if (getIsManager())
    {
      s += "List of Reportees: \n";
      s += this.getReporteeCNs().toString();
    }
    s += "\n";
    return s;
  }

  public String getSuperSector()
  {
    return superSector;
  }
}
