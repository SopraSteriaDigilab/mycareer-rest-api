package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DeleteObj extends TestConfig{

	@Parameters({"objOrDN"})
	@Test
	public void testDelObj(String objOrDN) throws InterruptedException {

		WebElement archiveTab = driver.findElement(By.xpath("//*[@id='nav-item-list']/li[5]"));

		archiveTab.click();
		
		// get ID number
		String objId = LatestObjective.getIdArch(objOrDN);

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


		// locate most recently added objective delete button
		List<WebElement> deleteBtnList = driver.findElements(By.id("delete-"+objOrDN));
		WebElement newestDeleteBtn = deleteBtnList.get(deleteBtnList.size()-1);

		// scroll to delete btn
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newestDeleteBtn);
		Thread.sleep(500);

		// click delete (workaround as webdriver thinks it is not visible)
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", newestDeleteBtn);
		Thread.sleep(500);

		// wait until modal text field loaded
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("deletingText"))));
		Thread.sleep(5);

		// locate modal text field
		WebElement deletedText = driver.findElement(By.id("deletingText"));

		// check modal has appeared for completed objective		
		Assert.assertNotNull(deletedText);

		// add note
		deletedText.sendKeys("Automated note for deletion of objective "+objId);
		Thread.sleep(500);

		// find Complete button to confirm completion
		WebElement submitDeleteBtn = driver.findElement(By.id("delete"));
		
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		
		// click Complete to confirm completion
		submitDeleteBtn.click();
		
		// get toaster text
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Objective deleted";

		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);


	}

}
