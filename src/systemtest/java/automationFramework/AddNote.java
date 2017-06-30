package automationFramework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AddNote extends TestConfig{

	@Test
	public void testNote() throws InterruptedException {
		
		// wait until notes button loaded
		WebDriverWait wait0 = new WebDriverWait(driver, 4000);
		wait0.until(ExpectedConditions.visibilityOfElementLocated((By.id("notes-open"))));
		
		// wait until toaster cleared - this is due to placement of toasters and notes button
		WebDriverWait wait1 = new WebDriverWait(driver, 4000);
		wait1.until(ExpectedConditions.invisibilityOfElementLocated((By.id("toast-container"))));
		
		// click to open notes
		WebElement noteBtn = driver.findElement(By.id("notes-open"));
		noteBtn.click();
		
		// wait until form opens
		WebDriverWait wait2 = new WebDriverWait(driver, 4000);
		wait2.until(ExpectedConditions.visibilityOfElementLocated((By.id("note-text"))));
		
		//enter title
		WebElement noteText = driver.findElement(By.id("note-text"));
		noteText.sendKeys("Automated note");
		Thread.sleep(5);
		
		// line required for getLatest toaster text
		int numberOfToasters = ToastContainer.getCurrentNoOfToasters();
		
		// submit note
		WebElement noteSubmit = driver.findElement(By.id("submit-note"));
		noteSubmit.click();
		
		// get toaster text
		String actualToastTextN = ToastContainer.getLatest(numberOfToasters);
		// define expected text for this toaster
		String expectedToastText = "Note added";
		
		// click to open notes
		WebElement noteClose = driver.findElement(By.id("notes-close"));
		noteClose.click();
		
		// Test for correct toaster text
		Assert.assertEquals(expectedToastText,actualToastTextN);

	}

}
