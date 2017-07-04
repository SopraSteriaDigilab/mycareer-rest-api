package automationFramework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LatestObjective extends TestConfig {

	public static String getId(String objOrDN) {

		String cssSelector = ".panel-group.tab-pane.fade.unarchived-obj-item";
		int substringIndex = 15;

		if(objOrDN.equals("dev-need")){
			cssSelector = ".panel-group.tab-pane.fade.dev-need.unarchived-dev-item";
			substringIndex = 22;
		}

		// locate most recently added objective progress button
		List<WebElement> objList = driver.findElements(By.cssSelector(cssSelector));
		WebElement newestObj = objList.get(objList.size()-1);

		// extract progress button IDs for this objective		
		String newestObjID = newestObj.getAttribute("id").substring(substringIndex);

		return newestObjID;
		
	}
	
	public static String getIdArch(String objOrDN) {

		String cssSelector = ".panel-group.tab-pane.fade.archived-obj-item";
		int substringIndex = 15;

		if(objOrDN.equals("dev-need")){
			cssSelector = ".panel-group.tab-pane.fade.dev-need.archived-dev-item";
			substringIndex = 22;
		}

		// locate most recently added objective progress button
		List<WebElement> objList = driver.findElements(By.cssSelector(cssSelector));
		WebElement newestObj = objList.get(objList.size()-1);

		// extract progress button IDs for this objective		
		String newestObjID = newestObj.getAttribute("id").substring(substringIndex);

		return newestObjID;
		
	}

}
