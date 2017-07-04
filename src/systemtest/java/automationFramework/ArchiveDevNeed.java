package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class ArchiveDevNeed extends TestConfig{
	
	@Parameters({"objOrDN"})
	@Test
	public void testArchDevNeed(String objOrDN) throws InterruptedException {
		
		// get ID number
		String devNeedId = LatestObjective.getId(objOrDN);
		
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

		// locate most recently added objective archive button
		List<WebElement> archiveBtnList = driver.findElements(By.id("archive-"+objOrDN));
		WebElement newestArchiveBtn = archiveBtnList.get(archiveBtnList.size()-1);
		
		// scroll to archive btn
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newestArchiveBtn);
		Thread.sleep(500);
				
		// get number of current toasters as required by getLatest
		int noOfToasters = ToastContainer.getCurrentNoOfToasters();
		
		// click Archive (workaround as webdriver thinks it is not visible)
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", newestArchiveBtn);
		Thread.sleep(500);
		
		// get toaster text
		String actualToastText = ToastContainer.getLatest(noOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Development Need updated";
		
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText, actualToastText);


	}
	
}