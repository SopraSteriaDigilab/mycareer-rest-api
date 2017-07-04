package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToastContainer extends TestConfig{

	public static String getLatest(int numberOfToasters) throws InterruptedException {

		// gets the toast message from the last toaster to appear

		int newNumberOfToasters = numberOfToasters;

		List<WebElement> allToasts = null;

		// wait until toaster appears
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("toast-container"))));

		while(newNumberOfToasters == numberOfToasters){
			// get all toaster classes
			allToasts = driver.findElements(By.className("toast-message"));
			newNumberOfToasters = allToasts.size();
			if(newNumberOfToasters<numberOfToasters){
				numberOfToasters = newNumberOfToasters;
			}
		}

		// pick latest toaster from list
		WebElement latestToast = allToasts.get(0);
		String latestToastText = latestToast.getText();

		return latestToastText;


	}

	public static int getCurrentNoOfToasters() {
		// get all toaster classes
		List<WebElement> allToasts = driver.findElements(By.className("toast-message"));

		int currentNoOfToasters = allToasts.size();

		return currentNoOfToasters;
	}



}
