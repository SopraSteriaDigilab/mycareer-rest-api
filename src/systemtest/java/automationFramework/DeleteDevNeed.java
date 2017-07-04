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

public class DeleteDevNeed extends TestConfig{

	@Parameters({"objOrDN"})
	@Test
	public void testDelDevNeed(String objOrDN) throws InterruptedException {

		WebElement archiveTab = driver.findElement(By.xpath("//*[@id='myapp']/div[1]/div[2]/div/ul/li[5]/a"));

		archiveTab.click();
		
		// get ID number
		String devNeedId = LatestObjective.getIdArch(objOrDN);

		WebElement devNeedChevron = null;
		WebElement devNeedPanel = driver.findElement(By.id(objOrDN+"-no-"+devNeedId));

		// locate most recently added objective chevron button
		List<WebElement> chevronList = driver.findElements(By.xpath("//*[@id='panel']/div[1]/div/div[3]/a"));
		for(WebElement el : chevronList){

			// change next line so it finds it for any objId
			if(el.getAttribute("href").equals("http://mycareer-uat.duns.uk.sopra/mydevelopmentneeds#collapse-"+objOrDN+"-"+devNeedId)){
				devNeedChevron = el;
				break;
			}
		}

		// scroll to panel
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", devNeedPanel);
		Thread.sleep(500);

		// click chevron (workaround as webdriver thinks it is not visible)
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", devNeedChevron);
		Thread.sleep(500);


		// locate most recently added objective delete button
		List<WebElement> deleteBtnList = driver.findElements(By.id("delete-obj"));
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
		deletedText.sendKeys("Automated note for deletion of development need "+devNeedId);
		Thread.sleep(500);

		// click Complete to confirm completion
		WebElement submitDeleteBtn = driver.findElement(By.id("delete"));
		submitDeleteBtn.click();

		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();

		// get toaster text
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Development Need deleted";

		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);


	}

}
