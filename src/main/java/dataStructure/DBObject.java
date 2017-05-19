package dataStructure;

import static utils.Conversions.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * A Java object used by Morphia as a template for mapping from a MongoDB document. Contains basic members used by most
 * MyCareer data structures.
 */
public abstract class DBObject implements Serializable
{
  private static final long serialVersionUID = 1L;

  private static final String DOCUMENT_CONVERSION_EXCEPTION = "Error converting DBObject to Document";

  private int id;
  private Date lastModified;

  /**
   * DBObject Constructor - No-args constructor provided for use by Morphia. Should not be used in application code.
   */
  public DBObject()
  {
    setLastModified();
  }

  /** @return the id. */
  public int getId()
  {
    return id;
  }

  /** @param id The value to set the named property to. */
  public void setId(int id)
  {
    this.id = id;
  }

  /** @return A string representation of the date/time this DB object was last modified. */
  public String getLastModified()
  {
    return dateToLocalDateTime(lastModified).toString();
  }

  /** Sets the last modified date/time of this DB object to the current moment. */
  public void setLastModified()
  {
    lastModified = localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE));
  }

  /** @return The last modified date/time */
  public Date getLastModifiedAsDate()
  {
    return lastModified;
  }

  /**
   * @return A {@code Document} representation of this {@code DBObject}.
   * @throws DocumentConversionException if there was exception while attempting to convert
   */
  public Document toDocument() throws DocumentConversionException
  {
    try
    {
      Gson gson = new Gson();
      String json = gson.toJson(this);
      @SuppressWarnings("unchecked")
      HashMap<String, Object> result = new ObjectMapper().readValue(json, HashMap.class);
      return new Document(result);
    }
    catch (IOException e)
    {
      throw new DocumentConversionException(DOCUMENT_CONVERSION_EXCEPTION, e);
    }
  }

}
