package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OpenMyCareer extends automationFramework.TestConfig{
	
	@Test
	public void openTest() {

		// navigate to MyCareer UAT site
		driver.get(baseURL);
		
		// wait until loading spinner is gone
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loading-spinner"))));
	
		// check title
		// declare expected title
		String expectedTitle = "MyCareer";
		// get actual title
		String actualTitle = driver.getTitle();
		
		// compare expected and actual titles
        Assert.assertEquals(expectedTitle,actualTitle);

	}
}
