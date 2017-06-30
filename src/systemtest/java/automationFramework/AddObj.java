package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.Assert;

public class AddObj extends TestConfig{

	@Parameters({"objOrDN"})
	@Test
	public void testObj(String objOrDN) throws InterruptedException {
		
		// wait until add objective button loaded
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("add-obj"))));
		Thread.sleep(5);
		
		// click to add objective
		WebElement objBtn = driver.findElement(By.id("add-obj"));
		objBtn.click();
		
		// wait until form opens
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.visibilityOfElementLocated((By.id("objective-title"))));
		
		//enter title
		WebElement objTitle = driver.findElement(By.id("objective-title"));
		objTitle.sendKeys("Automated objective title");
		Thread.sleep(5);
		
		// enter text
		WebElement objText = driver.findElement(By.id("objective-text"));
		objText.sendKeys("Automated objective text");
		Thread.sleep(5);
		
		WebElement objDate = driver.findElement(By.id("objective-date"));
		Thread.sleep(5);
		
		// submit obj
		WebElement objSubmit = driver.findElement(By.id("submit-obj"));
		objSubmit.click();
		
		// get toaster text
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Objective added";
		
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);
		
		Thread.sleep(5);
		
	}
	
	@BeforeTest
	public void navToObjTab() throws InterruptedException{
		// get current url
		String currentUrl = driver.getCurrentUrl();
		String expectedUrl = "http://mycareer-uat.duns.uk.sopra/myobjectives";

		if(!currentUrl.equals(expectedUrl)){
			// go to objectives tab
			WebElement objTab = driver.findElement(By.id("myobjectives"));
			objTab.click();
			Thread.sleep(5);

			// wait until loading spinner is gone
			WebDriverWait wait1 = new WebDriverWait(driver, 4000);
			wait1.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loading-spinner"))));
		}
	}
	
	@AfterClass
	public void waitToClearModal(){
		// wait until modal is gone
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.invisibilityOfElementLocated((By.id("objective-modal"))));
	}
}