package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UpdateStatusProposed extends TestConfig{

	@Parameters({"objOrDN"})
	@Test
	public void testProg(String objOrDN) throws InterruptedException {
		// objOrDn should have the value "obj" or "dev-need"
		
		// extract progress button IDs for this objective		
		String newestObjID = LatestObjective.getId(objOrDN);
		String proposedDotID = "proposed-"+objOrDN+"-dot-"+newestObjID;
		
		// find and click on in-progress button
		WebElement proposedDot = driver.findElement(By.id(proposedDotID));
		proposedDot.click();
		
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
