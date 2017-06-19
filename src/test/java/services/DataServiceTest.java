package services;

import static dataStructure.EmailAddresses.*;
import static dataStructure.EmployeeProfile.*;
import static org.mockito.MockitoAnnotations.*;
import static model.EmailSets.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.mongodb.client.FindIterable;

import services.db.MongoOperations;

public class DataServiceTest
{
  @Mock
  private MongoOperations mockEmployeeOperations;
  
  @InjectMocks
  private DataService unitUnderTest;
  
  @Before
  public void setup()
  {
    initMocks(this);
    unitUnderTest = new DataService(mockEmployeeOperations);
    
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccess()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) emailsOnly());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), emailsOnly());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccessWithNull()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) emailsAndNull());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), emailsOnly());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccessWithEmpty()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) emailsAndEmpty());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), emailsOnly());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccessWithNullAndEmpty()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) emailsNullAndEmpty());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), emailsOnly());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccessNullOnly()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) nullOnly());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), new HashSet<>());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllEmailAddressesSuccessEmptyOnly()
  {
    when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS)).thenReturn((Set<Object>) emptyOnly());
    
    assertEquals(unitUnderTest.getAllEmailAddresses(), new HashSet<>());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void getAllNamesAndIdsSuccess()
  {
    final FindIterable<Document> retVal= mock(FindIterable.class);
    
    when(mockEmployeeOperations.getFields(new Document(), FORENAME, SURNAME, EMPLOYEE_ID)).thenReturn(retVal);
    
    assertEquals(unitUnderTest.getAllNamesAndIds(), retVal);
  }
}
