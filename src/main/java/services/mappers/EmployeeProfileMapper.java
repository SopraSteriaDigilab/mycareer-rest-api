package services.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;

public class EmployeeProfileMapper implements BiMapper<Optional<SearchResult>, EmployeeProfile>
{
  private static final Logger LOGGER =  LoggerFactory.getLogger(EmployeeProfileMapper.class);

  private static final String INVALID_VALUE = "Exception while attempting to set a value: {}";
  private static final String REPORTEES_NOT_FOUND = "Exception while fetching reportees: {}";
  private static final String EMPLOYEE_NOT_FOUND = "Cannot create an EmployeeProfile: Exception thrown while fetching employeeID: {}";
  private static final String GUID_NOT_FOUND = "Exception while fetching GUID: {}";
  private static final String HR_PERMISSION_NOT_FOUND = "Exception while fetching HR Dashboard Permission: {}";
  private static final String SECTOR_NOT_FOUND = "Exception while fetching sector: {}";
  private static final String ATTRIBUTE_NOT_FOUND = "Exception while fetching a String attribute: {}";
  
  private static final String SN = "sn";
  private static final String GIVEN_NAME = "givenName";
  private static final String MAIL = "mail";
  private static final String TARGET_ADDRESS = "targetAddress";
  private static final String SAM_ACCOUNT_NAME = "sAMAccountName";
  private static final String COMPANY = "company";
  private static final String DEPARTMENT = "department";
  private static final String MEMBER_OF = "memberOf";
  private static final String EXTENSION_ATTRIBUTE_7 = "extensionAttribute7";
  private static final String EXTENSION_ATTRIBUTE_2 = "extensionAttribute2";
  private static final String OBJECT_GUID = "objectGUID";
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";
  private static final String DIRECT_REPORTS = "directReports";
  private static final String OU = "ou";
  private static final String STERIA_SECTOR_UNIT = "SteriaSectorUnit";
  
  private final EmployeeProfile profile = new EmployeeProfile();
  
  @Override
  public EmployeeProfile apply(Optional<SearchResult> sopraEmployeeProfile, Optional<SearchResult> steriaEmployeeProfile) throws InvalidEmployeeProfileException
  {
    sopraEmployeeProfile.ifPresent(this::setSopraDetails);
    steriaEmployeeProfile.ifPresent(this::setSteriaDetails);
    
    return profile;
  }

  private void setSopraDetails(SearchResult sopraEmployeeProfile) throws InvalidEmployeeProfileException
  {
    final Attributes attributes = sopraEmployeeProfile.getAttributes();
    
    try
    {
      profile.setCompany(mapString(COMPANY, attributes));
      profile.setEmailAddress(mapString(MAIL, attributes));
      profile.setEmployeeID(mapEmployeeID(attributes, EXTENSION_ATTRIBUTE_7));
      profile.setForename(mapString(GIVEN_NAME, attributes));
      profile.setGUID(mapGUID(attributes));
      profile.setHasHRDash(mapHRPermission(attributes));
      profile.setSopraDepartment(mapString(DEPARTMENT, attributes));
      profile.setSurname(mapString(SN, attributes));
      profile.setUsername(mapString(SAM_ACCOUNT_NAME, attributes));
    }
    catch (InvalidAttributeValueException e)
    {
      LOGGER.warn(INVALID_VALUE, e.getMessage());
    }
  }
  
  private void setSteriaDetails(SearchResult steriaEmployeeProfile)
  {
    final Attributes attributes = steriaEmployeeProfile.getAttributes();
    
    try
    { 
      profile.setEmployeeID(mapEmployeeID(attributes, EXTENSION_ATTRIBUTE_2));
      profile.setForename(mapString(GIVEN_NAME, attributes));
      profile.setSurname(mapString(SN, attributes));
      profile.setUsername(mapString(SAM_ACCOUNT_NAME, attributes));
      profile.setEmailAddress(mapString(MAIL, attributes));
      profile.setCompany(mapString(COMPANY, attributes));
      profile.setSuperSector(mapString(OU, attributes));
      profile.setSector(mapSector(attributes));
      profile.setSteriaDepartment(mapString(DEPARTMENT, attributes));
      profile.setIsManager(mapIsManager(attributes));
      if (mapIsManager(attributes))
      {
        profile.setReporteeCNs(mapReporteeCNs(attributes));
      }
    }
    catch (InvalidAttributeValueException e)
    {
      LOGGER.warn(INVALID_VALUE, e.getMessage());
    }
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

  private Long mapEmployeeID(final Attributes attributes, final String employeeIDAttribute) throws InvalidEmployeeProfileException
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
  
  private String mapGUID(final Attributes attributes)
  {
    String guid = null;
    
    try
    {
      UUID uuid = UUID.nameUUIDFromBytes((byte[]) attributes.get(OBJECT_GUID).get());
      guid = uuid.toString();
    }
    catch (NamingException | ClassCastException e)
    {
      LOGGER.warn(GUID_NOT_FOUND, e.getMessage());
    }
    
    return guid;
  }

  @SuppressWarnings("unchecked")
  private boolean mapHRPermission(final Attributes attributes)
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
    String mappedString = null;
    
    try
    {
      mappedString = ((String) attributes.get(STERIA_SECTOR_UNIT).get()).substring(3);
    }
    catch (NamingException | ClassCastException e)
    {
      LOGGER.warn(SECTOR_NOT_FOUND, e.getMessage());
    }
    
    return mappedString;
  }
  
  private String mapString(final String field, final Attributes attributes)
  {
    String mappedString = null;
    
    try
    {
      mappedString = (String) attributes.get(field).get();
    }
    catch (NamingException | ClassCastException e)
    {
      LOGGER.warn(ATTRIBUTE_NOT_FOUND, e.getMessage());
    }
    
    return mappedString;
  }
}
