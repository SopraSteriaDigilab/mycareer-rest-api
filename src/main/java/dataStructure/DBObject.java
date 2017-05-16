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
 * 
 * TODO: Describe this TYPE.
 *
 */
public abstract class DBObject implements Serializable
{
  /* long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 1L;

  private static final String DOCUMENT_CONVERSION_EXCEPTION = "Error converting DBObject to Document";

  /* int Property - Represents Unique ID for the object. */
  private int id;

  /* String Property - Represents the time the objective was last modified. */
  private Date lastModified;

  /**
   * Default Constructor - Responsible for initialising this object.
   *
   * @param id
   * @param lastModified
   */
  public DBObject()
  {
    this.setLastModified();
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

  /** @return the timeStamp. */
  public String getLastModified()
  {
    return dateToLocalDateTime(lastModified).toString();
  }

  /** @param lastModified */
  public void setLastModified()
  {
    this.lastModified = localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE));
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public Date getLastModifiedAsDate()
  {
    return lastModified;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   * @throws DocumentConversionException
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
