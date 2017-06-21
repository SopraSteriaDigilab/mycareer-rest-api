package dataStructure;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;

import org.powermock.api.mockito.PowerMockito;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DBObject.class, Gson.class, ObjectMapper.class })
public class DBObjectTest
{
  private final HashMap<String, Object> map = initMap();
  private final Document document = new Document("key", "value");
  private final Gson mockGson = PowerMockito.mock(Gson.class);
  private final ObjectMapper mockObjectMapper = PowerMockito.mock(ObjectMapper.class);
  
  private DBObject unitUnderTest;
  
  private HashMap<String, Object> initMap()
  {
    final HashMap<String, Object> map = new HashMap<>();
    
    map.put("key", "value");
    
    return map;
  }
  
  @Before
  public void setup() throws Exception
  {
    PowerMockito.whenNew(Gson.class).withNoArguments().thenReturn(mockGson);
    PowerMockito.whenNew(ObjectMapper.class).withNoArguments().thenReturn(mockObjectMapper);
    PowerMockito.when(mockGson.toJson(unitUnderTest)).thenReturn(anyString());
  }
  
  @Test
  public void toDocumentSuccessTest() throws Exception
  {
    // arrange
    PowerMockito.when(mockObjectMapper.readValue(anyString(), HashMap.class)).thenReturn(map);
    
    unitUnderTest = spy(DBObject.class);

    // act
    final Document actual = unitUnderTest.toDocument();

    // assert
    assertEquals(actual, document);
  }
  
  @Test(expected = DocumentConversionException.class)
  public void toDocumentExceptionTest() throws Exception
  {
    // arrange
    final IOException e = new IOException();
    PowerMockito.when(mockObjectMapper.readValue(anyString(), HashMap.class)).thenThrow(e);
    
    unitUnderTest = spy(DBObject.class);

    // act
    unitUnderTest.toDocument();
  }
}
