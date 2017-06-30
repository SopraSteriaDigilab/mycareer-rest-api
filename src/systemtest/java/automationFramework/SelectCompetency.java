package automationFramework;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class SelectCompetency extends TestConfig{


	@Test
	public void testCompetency0() throws InterruptedException {
		
		String starId = "0";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);
		
		Thread.sleep(500);

	}
	
	@Test
	public void testCompetency1() throws InterruptedException {
		
		String starId = "1";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);
		Thread.sleep(500);
	}
	
	@Test
	public void testCompetency2() throws InterruptedException {
		
		String starId = "2";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}
	
/*	@Test
	public void testCompetency3() throws InterruptedException {
		
		String starId = "3";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}
	
	@Test
	public void testCompetency4() throws InterruptedException {
		
		String starId = "4";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}
	
	@Test
	public void testCompetency5() throws InterruptedException {
		
		String starId = "5";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}
	
	@Test
	public void testCompetency6() throws InterruptedException {
		
		String starId = "6";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}
	
	@Test
	public void testCompetency7() throws InterruptedException {
		
		String starId = "7";

		ArrayList<String> resultsEAB = selectCompTest(starId);
		
		// get test results for comparison
		String expectedToastText = resultsEAB.get(0);
		String actualToastTextA = resultsEAB.get(1);
		String actualToastTextB = resultsEAB.get(2);

		// Test for correct toaster text
		Assert.assertEquals(actualToastTextA,expectedToastText);
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextB);

		Thread.sleep(500);
	}*/
	
	

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

	public String getCompetencyName(String starId){

		String cName = null;

		switch(starId){
		case "0": cName = "Accountability";
		break;
		case "1": cName = "Effective Communication";
		break;
		case "2": cName = "Leadership";
		break;
		case "3": cName = "Service Excellence";
		break;
		case "4": cName = "Business Awareness";
		break;
		case "5": cName = "Future Orientation";
		break;
		case "6": cName = "Innovation and Change";
		break;
		case "7": cName = "Teamwork";
		break;
		}


		return cName;

	}

	public ArrayList selectCompTest(String starId) throws InterruptedException{

		// wait until star button loaded
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.visibilityOfElementLocated((By.id("star-"+starId))));

		// get number of toasters currently displayed (required for getLatest)
		int numberOfToasters = ToastContainer.getCurrentNoOfToasters();
		
		while(numberOfToasters >= 3){
			numberOfToasters = ToastContainer.getCurrentNoOfToasters();
			Thread.sleep(500);
		}
		
		// TEST A: SELECT COMPETENCY
		// click to select star0
		WebElement starBtn0 = driver.findElement(By.id("star-"+starId));
		starBtn0.click();
		// wait for toaster to appear and get latest toaster text
		String actualToastTextA = ToastContainer.getLatest(numberOfToasters);
		Thread.sleep(500);
		// define expected text for this toaster
		String competencyName = getCompetencyName(starId);
		String expectedToastText = "'"+competencyName+"' Competency updated";
		
		
		// TEST B: DESELECT COMPETENCY
		numberOfToasters = ToastContainer.getCurrentNoOfToasters();
		
		while(numberOfToasters >= 3){
			numberOfToasters = ToastContainer.getCurrentNoOfToasters();
			Thread.sleep(500);
		}

		// click to deselect star
		starBtn0.click();
		String actualToastTextB = ToastContainer.getLatest(numberOfToasters);
		// expected text for this toaster is same as before		
		Thread.sleep(500);

		ArrayList<String> resultsEAB = new ArrayList<String>();

		resultsEAB.add(expectedToastText);
		resultsEAB.add(actualToastTextA);
		resultsEAB.add(actualToastTextB);

		return resultsEAB;




	}

}
