package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Class to handle text template manipulation.
 *
 */
@Component
@PropertySource("classpath:${ENVIRONMENT}.properties")
public class Template
{

  /**
   * Takes in a template path and populates according to varargs after. Uses the
   * {@linkplain java.text.MessageFormat.format MessageFormat.format} method for formating.
   *
   * @param pathname
   * @param args
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static String populateTemplate(String pathname, String... args) throws FileNotFoundException, IOException
  {
    String templateString = Utils.readFile(new File(pathname));

    return MessageFormat.format(templateString, (Object[]) args);
  }

}
