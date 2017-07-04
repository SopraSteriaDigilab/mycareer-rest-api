package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UpdateStatusComplete extends TestConfig{
	
	@Parameters({"objOrDN"})
	@Test
	public void testComp(String objOrDN) throws InterruptedException {
		
		// extract progress button IDs for this objective		
		String newestObjID = LatestObjective.getId(objOrDN);
		String compDotID = "complete-"+objOrDN+"-dot-"+newestObjID;

		// find and click on completed button
		WebElement compDot = driver.findElement(By.id(compDotID));
		compDot.click();

		Thread.sleep(500);
		
		// wait until modal text field button loaded
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("completedText"))));
		Thread.sleep(5);
		
		// locate modal text field
		WebElement completedText = driver.findElement(By.id("completedText"));

		// check modal has appeared for completed objective		
		Assert.assertNotNull(completedText);
		
		String noteText = null;
		if (objOrDN.equals("dev-need")){
			noteText = "Automated note for completion of development need "+newestObjID;
		} else {
			noteText = "Automated note for completion of objective "+newestObjID;
		}

		// add note
		completedText.sendKeys(noteText);
		Thread.sleep(500);

		// click Complete to confirm completion
		WebElement submitCompleteBtn = driver.findElement(By.id("submit-completed-status-note"));
		submitCompleteBtn.click();

		// get toaster text	
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Objective progress updated";
		if(objOrDN.equals("dev-need")){
			expectedToastText = "Development Need progress updated";
		}
		
		// Test for correct toaster text
		Assert.assertEquals(actualToastText,expectedToastText);

	}
	
}
