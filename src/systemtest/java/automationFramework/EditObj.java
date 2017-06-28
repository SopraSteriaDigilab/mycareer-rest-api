package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class EditObj extends TestConfig{

	@Parameters({"objOrDN"})
	@Test
	public void testEditObj(String objOrDN) throws InterruptedException {

		// get ID number
		String objId = LatestObjective.getId(objOrDN);

		WebElement objChevron = null;
		WebElement objPanel = driver.findElement(By.id(objOrDN+"-no-"+objId));

		// locate most recently added objective chevron button
		List<WebElement> chevronList = driver.findElements(By.xpath("//*[@id='panel']/div[1]/div/div[3]/a"));
		for(WebElement el : chevronList){

			// change next line so it finds it for any objId
			if(el.getAttribute("href").equals("http://mycareer-uat.duns.uk.sopra/myobjectives#collapse-"+objOrDN+"-"+objId)){
				objChevron = el;
				break;
			}
		}

		// scroll to panel
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objPanel);
		Thread.sleep(500);

		// click chevron (workaround as webdriver thinks it is not visible)
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", objChevron);
		Thread.sleep(500);

		// locate most recently added objective Edit button
		String editBtnId = "edit-objective-button-"+objId;
		if(objOrDN.equals("dev-need")){
			editBtnId = "edit-dev-need-"+objId;
		}
		WebElement newestEditBtn = driver.findElement(By.id(editBtnId));

		// scroll to edit btn
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newestEditBtn);
		Thread.sleep(500);

		// click Edit (workaround as webdriver thinks it is not visible)
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", newestEditBtn);
		Thread.sleep(500);

		// wait until form opens
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.visibilityOfElementLocated((By.id("objective-title"))));

		//enter title
		WebElement objTitle = driver.findElement(By.id("objective-title"));
		objTitle.sendKeys(" - edited");
		Thread.sleep(500);

		// enter text
		WebElement objText = driver.findElement(By.id("objective-text"));
		objText.sendKeys(" - edited");
		Thread.sleep(1000);
		
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();

		// submit obj
		WebElement objSubmit = driver.findElement(By.id("submit-obj"));
		objSubmit.click();

		// get toaster text
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Objective edited"; // incorrect toaster message

		// Test for correct toaster text
		Assert.assertEquals(actualToastText,expectedToastText);

		// Test that the objective is no longer visible

	}

	@Parameters({"objOrDN"})
	@AfterClass
	public void closePanel(String objOrDN) throws InterruptedException{
		// get ID number
		String objId = LatestObjective.getId(objOrDN);

		WebElement objChevron = null;

		// locate most recently added objective chevron button
		List<WebElement> chevronList = driver.findElements(By.xpath("//*[@id='panel']/div[1]/div/div[3]/a"));
		for(WebElement el : chevronList){

			// change next line so it finds it for any objId
			if(el.getAttribute("href").equals("http://mycareer-uat.duns.uk.sopra/myobjectives#collapse-"+objOrDN+"-"+objId)){
				objChevron = el;
				break;
			}
		}		

		// click chevron (workaround as webdriver thinks it is not visible) to close objective
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", objChevron);
		Thread.sleep(500);

	}

}
