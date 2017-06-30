package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestFeedback extends TestConfig{

	@Test
	public void rFB() throws InterruptedException{
		
		// wait until request-feedback button loaded
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.visibilityOfElementLocated((By.id("request-feedback"))));
		
		// click to send feedback
		WebElement reqFBBtn = driver.findElement(By.id("request-feedback"));
		reqFBBtn.click();
		Thread.sleep(500);
		
		// locate modal emails field
		WebElement requestingTo = driver.findElement(By.xpath("//*[@id='request-to-validate']/div/input"));
		Thread.sleep(500);		
		// add email address
		requestingTo.sendKeys("finlay.harris@soprasteria.com");
		
		// locate modal feedback request field
		WebElement requestingText = driver.findElement(By.id("requestingText"));
		// add feedback request
		requestingText.sendKeys("Automated feedback request");
		
		Thread.sleep(500);		
		// submit feedback request
		WebElement devSubmit = driver.findElement(By.id("submit-request-feedback"));
		devSubmit.click();
		
		// get toaster text	
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Your feedback request has been processed.";
		
		Thread.sleep(1000);	
		
		System.out.println("    " + actualToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);
		

	}

}
