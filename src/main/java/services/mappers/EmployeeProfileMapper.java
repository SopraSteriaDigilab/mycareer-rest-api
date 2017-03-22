package services.mappers;

import static utils.Conversions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;

//TODO Update map methods to user map string/chain eachother
//TODO remove sopra
public class EmployeeProfileMapper implements Mapper<Optional<SearchResult>, EmployeeProfile>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProfileMapper.class);
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  private static final String REPORTEES_NOT_FOUND = "Exception while fetching reportees: {}";
  private static final String EMPLOYEE_NOT_FOUND = "Cannot create an EmployeeProfile: Exception thrown while fetching employeeID: ";
  private static final String HR_PERMISSION_NOT_FOUND = "Exception while fetching HR Dashboard Permission: {}";
  private static final String ATTRIBUTE_NOT_FOUND = "Exception while fetching attribute, {}: {}";

  private static final String SN = "sn";
  private static final String GIVEN_NAME = "givenName";
  private static final String MAIL = "mail";
  private static final String TARGET_ADDRESS = "targetAddress";
  private static final String SAM_ACCOUNT_NAME = "sAMAccountName";
  private static final String COMPANY = "company";
  private static final String DEPARTMENT = "department";
  private static final String MEMBER_OF = "memberOf";
  private static final String EXTENSION_ATTRIBUTE_2 = "extensionAttribute2";
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";
  private static final String DIRECT_REPORTS = "directReports";
  private static final String OU = "ou";
  private static final String STERIA_SECTOR_UNIT = "SteriaSectorUnit";
  private static final String ACCOUNT_EXPIRES = "accountExpires";

  private EmployeeProfile profile = new EmployeeProfile();

  public EmployeeProfile map(Optional<SearchResult> steriaEmployeeProfile, Optional<SearchResult> sopraEmployeeProfile)
  {
    // sopraEmployeeProfile.ifPresent(this::setSopraDetails);
    steriaEmployeeProfile.ifPresent(this::setSteriaDetails);

    return profile;
  }

  // private void setSopraDetails(SearchResult sopraEmployeeProfile) throws InvalidEmployeeProfileException
  // {
  // final Attributes attributes = sopraEmployeeProfile.getAttributes();
  //
  // profile = new EmployeeProfile.Builder().employeeID(mapEmployeeID(attributes, EXTENSION_ATTRIBUTE_7))
  // .forename(mapString(GIVEN_NAME, attributes)).surname(mapString(SN, attributes))
  // .username(mapString(SAM_ACCOUNT_NAME, attributes)).emailAddress(mapString(MAIL, attributes))
  // .company(mapString(COMPANY, attributes)).sopraDepartment(mapString(DEPARTMENT, attributes))
  // .guid(mapGUID(attributes)).hasHRDash(mapHRPermission(attributes)).build();
  // }

  private void setSteriaDetails(SearchResult steriaEmployeeProfile) throws InvalidEmployeeProfileException
  {
    final Attributes attributes = steriaEmployeeProfile.getAttributes();

    profile = new EmployeeProfile.Builder().employeeID(mapEmployeeID(attributes, EXTENSION_ATTRIBUTE_2))
        .forename(mapString(GIVEN_NAME, attributes)).surname(mapString(SN, attributes))
        .username(mapString(SAM_ACCOUNT_NAME, attributes))
        .emailAddress(
            Stream.of(mapString(MAIL, attributes), mapString(TARGET_ADDRESS, attributes)).collect(Collectors.toSet()))
        .company(mapString(COMPANY, attributes)).superSector(mapString(OU, attributes)).sector(mapSector(attributes))
        .steriaDepartment(mapString(DEPARTMENT, attributes)).manager(mapIsManager(attributes))
        .reporteeCNs(mapReporteeCNs(attributes)).accountExpires(mapAccountExpires(attributes)).build();
  }

  private boolean mapIsManager(Attributes attributes)
  {
    final Attribute directReports = attributes.get(DIRECT_REPORTS);

    return directReports != null;
  }

  @SuppressWarnings("unchecked")
  private List<String> mapReporteeCNs(Attributes attributes)
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
      LOGGER.error(EMPLOYEE_NOT_FOUND, e);
      throw new InvalidEmployeeProfileException(e);
    }
    catch (ClassCastException e)
    {
      LOGGER.error(EMPLOYEE_NOT_FOUND, e);
      throw new InvalidEmployeeProfileException(e);
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
