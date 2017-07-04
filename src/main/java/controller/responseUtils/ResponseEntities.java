package controller.responseUtils;

import static org.springframework.http.ResponseEntity.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * Helper class used to construct {@code ResponseEntity} objects for REST controllers.
 */
public class ResponseEntities
{
  private static final String SUCCESS = "success";
  
  private ResponseEntities()
  {
  }

  public static ResponseEntity<Map<String, Object>> success(final String message, final String responseDataName,
      final Object responseData)
  {
    final Map<String, Object> responseMap = new HashMap<>();
    
    putSuccessMessage(responseMap, message);
    putResponseData(responseMap, responseDataName, responseData);
    
    return success(responseMap);
  }
  
  public static <T> ResponseEntity<T> success(T response)
  {
    return ok(response);
  }
  
  public static void putSuccessMessage(final Map<String, ? super String> successMap, final String successMessage)
  {
    successMap.put(SUCCESS, successMessage);
  }
  
  public static <T> void putResponseData(final Map<String, ? super T> responseMap, final String responseDataName, final T responseData)
  {
    responseMap.put(responseDataName, responseData);    
  }
}
