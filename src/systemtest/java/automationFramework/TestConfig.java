package automationFramework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestConfig {
	
	public static WebDriver driver;
	public static String baseURL;
	
	@BeforeSuite
	public String getBaseURL(){
		
		return baseURL = "http://mycareer-uat.duns.uk.sopra/";
	}
	
	@BeforeSuite
	public WebDriver startDriver(){
		
		// start chrome driver		
		String exePath = "D:\\drivers\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		ChromeOptions options = new ChromeOptions(); options.addArguments("disable-infobars");
		driver=new ChromeDriver(options);
		return driver;
	}
	
	@AfterSuite
	public void closeDriver(){
		driver.quit();
	}
	
}
