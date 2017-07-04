package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AddDevNeed extends TestConfig{
	
	@Test
	public void testDevNeed() throws InterruptedException {
		
		// wait until add dev needs button loaded
		WebDriverWait wait2 = new WebDriverWait(driver, 4000);
		wait2.until(ExpectedConditions.visibilityOfElementLocated((By.id("add-dev-need"))));
		
		// click to add dev need
		WebElement devBtn = driver.findElement(By.id("add-dev-need"));
		devBtn.click();
		
		// wait until form opens
		WebDriverWait wait3 = new WebDriverWait(driver, 4000);
		wait3.until(ExpectedConditions.visibilityOfElementLocated((By.id("development-need-title"))));
		
		//enter title
		WebElement devTitle = driver.findElement(By.id("development-need-title"));
		devTitle.sendKeys("Automated dev need title");
		Thread.sleep(5);
		
		// enter text
		WebElement devText = driver.findElement(By.id("development-need-text"));
		devText.sendKeys("Automated dev need text");
		
		WebElement catBtn = driver.findElement(By.id("online-radio"));
		catBtn.click();
		Thread.sleep(5);
		
		WebElement devDate = driver.findElement(By.id("development-need-date"));
		Thread.sleep(5);
		
		// submit dev need
		WebElement devSubmit = driver.findElement(By.id("submit-dev-need"));
		devSubmit.click();
		
		// get toaster text	
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Development need added";
		
		Thread.sleep(500);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);
				
			}
	
	@BeforeTest
	public void navToDevNeedsTab() throws InterruptedException{

		// get current url
		String currentUrl = driver.getCurrentUrl();
		String expectedUrl = "http://mycareer-uat.duns.uk.sopra/mydevelopmentneeds";

		if(!currentUrl.equals(expectedUrl)){
			// go to dev needs tab
			WebElement devTab = driver.findElement(By.id("mydevelopmentneeds"));
			devTab.click();
			Thread.sleep(5);

			// wait until loading spinner is gone
			WebDriverWait wait1 = new WebDriverWait(driver, 4000);
			wait1.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loading-spinner"))));
		}
	}

}