package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SendFeedback extends TestConfig{

	@Test
	public void sFB() throws InterruptedException{
		// wait until send-feedback button loaded
		WebDriverWait wait2 = new WebDriverWait(driver, 4000);
		wait2.until(ExpectedConditions.visibilityOfElementLocated((By.id("send-feedback"))));
		
		// click to send feedback
		WebElement sendFBBtn = driver.findElement(By.id("send-feedback"));
		sendFBBtn.click();
		Thread.sleep(500);
		
		// locate modal emails field
		WebElement sendingTo = driver.findElement(By.xpath("//*[@id='send-to-validate']/div/input"));
		Thread.sleep(500);
		sendingTo.sendKeys("finlay.harris@soprasteria.com");
		
		// locate modal feedback field
		WebElement sendingText = driver.findElement(By.id("sendingText"));
		// add feedback
		sendingText.sendKeys("Automated feedback entry");
		Thread.sleep(500);
		
		// submit feedback
		WebElement submitBtn = driver.findElement(By.id("submit-send-feedback"));
		submitBtn.click();
		
		// get toaster text	
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Feedback added";
		
		System.out.println("    " + actualToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);
		

	}
	
	@BeforeTest
	public void navToFeedbackTab() throws InterruptedException{
		// get current url
		String currentUrl = driver.getCurrentUrl();
		String expectedUrl = "http://mycareer-uat.duns.uk.sopra/myfeedback";

		if(!currentUrl.equals(expectedUrl)){
			// go to objectives tab
			WebElement feedbackTab = driver.findElement(By.id("myfeedback"));
			feedbackTab.click();
			Thread.sleep(5);

			// wait until loading spinner is gone
			WebDriverWait wait1 = new WebDriverWait(driver, 4000);
			wait1.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loading-spinner"))));
		}
	} 

}
