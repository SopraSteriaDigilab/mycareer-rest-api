package services;

import static org.mockito.MockitoAnnotations.*;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import static model.TestModels.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import config.ADConfig;
import dataStructure.EmployeeProfile;
import services.ad.ADConnection;
import services.ad.ADConnectionException;
import services.ad.ADOperations;
import services.ad.ADSearchSettings;
import services.ad.ADSearchSettingsImpl;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.mappers.EmployeeProfileMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EmployeeProfileService.class, ADConnection.class, ADOperations.class, EmployeeProfileMapper.class })
public class EmployeeProfileServiceTest
{
  private ADSearchSettings sopraAdSearchSettings = new ADConfig().sopraADSearchSettings();

  @Mock
  private MorphiaOperations mockMorphiaOperations;

  @Mock
  private MongoOperations mockMongoOperations;

  @Mock
  private SearchResult mockSearchResult;
  
  @Mock
  private Attributes mockAttributes;

  @InjectMocks
  private EmployeeProfileService serviceUnderTest;

  @Before
  public void setup() throws Exception
  {
    initMocks(this);
    serviceUnderTest = new EmployeeProfileService(mockMorphiaOperations, mockMongoOperations, sopraAdSearchSettings);
    PowerMockito.mockStatic(ADOperations.class);
    PowerMockito.mockStatic(EmployeeProfileMapper.class);
  }

  @Test
  public void fetchEmployeeProfileHasHRDashSuccessTest() throws EmployeeNotFoundException, ADConnectionException, NamingException
  {
    // arrange
    final EmployeeProfile expected = newEmployeeProfile();
    when(mockMorphiaOperations.getEmployeeProfile(EmployeeProfile.EMPLOYEE_ID, EMPLOYEE_ID))
        .thenReturn(expected);
    PowerMockito.when(ADOperations.searchADSingleResult(sopraAdSearchSettings, "ou=usersemea,DC=emea,DC=msad,DC=sopra",
        anyString(), ADSearchSettingsImpl.LdapPort.LOCAL)).thenReturn(mockSearchResult);
    when(mockSearchResult.getAttributes()).thenReturn(mockAttributes);
    PowerMockito.when(EmployeeProfileMapper.mapHRPermission(mockAttributes)).thenReturn(true);

    // act
    final EmployeeProfile actual = serviceUnderTest.fetchEmployeeProfile(EMPLOYEE_ID);

    // assert
    assertTrue(actual == expected);
    assertTrue(expected.getHasHRDash());
    assertTrue(actual.getHasHRDash());
    assertNotEquals(expected, newEmployeeProfile());
  }
}
