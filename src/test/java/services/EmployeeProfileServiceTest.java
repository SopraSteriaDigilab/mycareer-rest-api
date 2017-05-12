
//  TODO Commented code to be reviewed
  
//package services;
//
//
//import java.util.Hashtable;
//import java.util.List;
//
//import javax.naming.directory.Attributes;
//import javax.naming.directory.SearchControls;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import dataStructure.EmployeeProfile;
//import services.ad.ADConnection;
//import services.ad.ADConnectionException;
//
///**
// * Units tests for the EmployeeProfileService class.
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(ADConnection.class)
//public class EmployeeProfileServiceTest
//{
//  private static final String[] AD_SOPRA_ATTRIBUTES = { "sn", "givenName", "company", "sAMAccountName",
//      "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf" };
//  private static final String AD_SOPRA_TREE = "ou=uk,ou=users,ou=sopragroup,ou=usersemea,DC=emea,DC=msad,DC=sopra";
//  private static final String AD_STERIA_SEARCH_TREE = "DC=one,DC=steria,DC=dom";
//  private static final String[] AD_STERIA_ATTRIBUTES = { "directReports", "sn", "givenName", "mail", "targetAddress",
//      "company", "sAMAccountName", "department", "ou", "SteriaSectorUnit" };
//  
//  private static final String SEARCH_FILTER = "cn=";
//  
//  
//  @Mock
//  private Hashtable<String, String> mockSopraADSettings;
//  
//  @Mock
//  private Hashtable<String, String> mockSteriaADSettings;
//  
//  @Mock
//  private List<EmployeeProfile> allEmployeeProfiles;
//  
//  @Mock
//  private ADConnection adConnection;
//  
//  @Mock
//  private SearchControls mockSearchControls;
//  
//  @Mock
//  private Attributes mockAttributes;
//  
//  @InjectMocks
//  private EmployeeProfileService unitUnderTest;
//  
//  @Before
//  public void setup() throws Exception
//  {
//    unitUnderTest = new EmployeeProfileService(mockSopraADSettings, mockSteriaADSettings);
//    PowerMockito.whenNew(ADConnection.class).withArguments(mockSopraADSettings).thenReturn(adConnection);
//    Mockito.when(adConnection.searchAD(mockSearchControls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, SEARCH_FILTER)).thenReturn(mockAttributes);
////    Mockito.when(mockAttributes.getAll()).thenReturn(mockResults);
//  }
//  
//  @Test
//  public void fetchAllEmployeeProfilesSuccess() throws ADConnectionException
//  {
//    unitUnderTest.fetchAllEmployeeProfiles();
//    
//    
//  }
//}
