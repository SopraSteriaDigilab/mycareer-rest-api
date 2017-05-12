package services.mappers;

import static utils.Conversions.*;
import static services.ad.query.LDAPQueries.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmailAddresses;
import dataStructure.EmployeeProfile;

//TODO Update map methods to user map string/chain eachother
//TODO remove sopra
public class EmployeeProfileMapper
{
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProfileMapper.class);
  
  private static final String REPORTEES_NOT_FOUND = "Exception while fetching reportees: {}";
  private static final String NOT_AN_EMPLOYEE = "Cannot create an EmployeeProfile: employeeType is not \"EMP\"";
  private static final String EMPLOYEE_NOT_FOUND = "Cannot create an EmployeeProfile: Exception thrown while fetching employeeID: ";
  private static final String HR_PERMISSION_NOT_FOUND = "Exception while fetching HR Dashboard Permission: {}";
  private static final String ATTRIBUTE_NOT_FOUND = "Exception while fetching attribute, {}: {}";

  private EmployeeProfile profile = new EmployeeProfile();

  public EmployeeProfile map(final SearchResult steriaEmployeeProfile)
  {
    setSteriaDetails(steriaEmployeeProfile);

    return profile;
  }

  private void setSteriaDetails(SearchResult steriaEmployeeProfile) throws InvalidEmployeeProfileException
  {
    final Attributes attributes = steriaEmployeeProfile.getAttributes();
    final EmailAddresses emailAddresses = new EmailAddresses.Builder().mail(mapString(MAIL, attributes))
        .targetAddress(mapString(TARGET_ADDRESS, attributes)).build();

    if (!isEmployee(attributes))
    {
      throw new InvalidEmployeeProfileException(NOT_AN_EMPLOYEE);
    }

    profile = new EmployeeProfile.Builder().employeeID(mapEmployeeID(attributes, EXTENSION_ATTRIBUTE_2))
        .employeeType(mapString(EMPLOYEE_TYPE, attributes)).forename(mapString(GIVEN_NAME, attributes))
        .surname(mapString(SN, attributes)).username(mapString(SAM_ACCOUNT_NAME, attributes))
        .emailAddresses(emailAddresses).company(mapString(COMPANY, attributes))
        .superSector(mapString(OU, attributes)).sector(mapSector(attributes))
        .steriaDepartment(mapString(DEPARTMENT, attributes)).manager(mapIsManager(attributes))
        .reporteeCNs(mapReporteeCNs(attributes)).accountExpires(mapAccountExpires(attributes)).build();
  }

  private boolean isEmployee(final Attributes attributes)
  {
    final String employeeType = mapString(EMPLOYEE_TYPE, attributes);
    final boolean isEmployee = EMPLOYEE.equals(employeeType);

    return isEmployee;
  }

  private boolean mapIsManager(final Attributes attributes)
  {
    final Attribute directReports = attributes.get(DIRECT_REPORTS);

    return directReports != null;
  }

  @SuppressWarnings("unchecked")
  private List<String> mapReporteeCNs(final Attributes attributes)
  {
    if (!mapIsManager(attributes))
    {
      return null;
    }

    NamingEnumeration<String> reportees = null;
    final List<String> reporteeCNs = new ArrayList<>();

    try
    {
      reportees = (NamingEnumeration<String>) attributes.get(DIRECT_REPORTS).getAll();

      while (reportees.hasMoreElements())
      {
        final String s = reportees.next().toString();
        final String[] t = s.split(",");

        // We need to extract only the 1st element of the array, removing the first 3 chars (cn=)
        reporteeCNs.add(t[0].substring(3));
      }
    }
    catch (NamingException e)
    {
      LOGGER.warn(REPORTEES_NOT_FOUND, e.getMessage());
    }

    Collections.sort(reporteeCNs);

    return reporteeCNs;
  }

  private Long mapEmployeeID(final Attributes attributes, final String employeeIDAttribute)
      throws InvalidEmployeeProfileException
  {
    Long employeeID = null;

    try
    {
      String employeeIDString = (String) attributes.get(employeeIDAttribute).get();
      employeeID = Long.parseLong(employeeIDString.substring(1));
    }
    catch (NamingException | NoSuchElementException | NullPointerException e)
    {
      throw new InvalidEmployeeProfileException(EMPLOYEE_NOT_FOUND, e);
    }
    catch (ClassCastException e)
    {
      throw new InvalidEmployeeProfileException(EMPLOYEE_NOT_FOUND, e);
    }

    return employeeID;
  }

  @SuppressWarnings("unchecked")
  public static boolean mapHRPermission(final Attributes attributes)
  {
    NamingEnumeration<String> groups = null;
    boolean hasHRDash = false;
    try
    {
      groups = (NamingEnumeration<String>) attributes.get(MEMBER_OF).getAll();
      String group = null;

      while (groups.hasMoreElements())
      {
        group = groups.next().toUpperCase();
        if (group.contains(AD_SOPRA_HR_DASH.toUpperCase()))
        {
          hasHRDash = true;
          break;
        }
      }
    }
    catch (NamingException | ClassCastException e)
    {
      LOGGER.warn(HR_PERMISSION_NOT_FOUND, e.getMessage());
    }

    return hasHRDash;
  }

  private String mapSector(final Attributes attributes)
  {
    return mapString(STERIA_SECTOR_UNIT, attributes).substring(3);
  }

  private Date mapAccountExpires(final Attributes attributes)
  {
    final String result = mapString(ACCOUNT_EXPIRES, attributes);
    if (result.length() < 18)
    {
      return null;
    }

    return ldapTimestampToDate(result);
  }

  private String mapString(final String field, final Attributes attributes)
  {
    String mappedString = null;

    try
    {
      mappedString = (String) attributes.get(field).get();
    }
    catch (NamingException | ClassCastException | NullPointerException e)
    {
      LOGGER.warn(ATTRIBUTE_NOT_FOUND, field, e.getMessage());
    }

    return mappedString;
  }
}
