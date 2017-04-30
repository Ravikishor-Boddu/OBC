package obc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class OBCTrans 
{
	WebDriver driver;
	final static String CORPID = JOptionPane.showInputDialog("Enter OBC Corporate ID:"); 
	final static String USERID = JOptionPane.showInputDialog("Enter OBC User ID:");
	final static String PASSWORD = JOptionPane.showInputDialog("Enter OBC Password:");
	
	
	
	@BeforeClass
	public void setUp()
	{
	    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	    chromePrefs.put("profile.default_content_settings.popups", 0);
	    chromePrefs.put("download.default_directory", System.getProperty("user.dir")+"\\TestData\\Downloads");
	    chromePrefs.put("credentials_enable_service", false);
	    chromePrefs.put("profile.password_manager_enabled", false);
	    ChromeOptions options = new ChromeOptions();
	    HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
	    options.setExperimentalOption("prefs", chromePrefs);
	    options.addArguments("--test-type");
	    options.addArguments("--disable-extensions"); //to disable browser extension popup
	    options.addArguments("--disable-save-password-bubble");
	    options.addArguments("--dns-prefetch-disable");
	  
	    DesiredCapabilities cap = DesiredCapabilities.chrome();
	    cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
	    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	    cap.setCapability(ChromeOptions.CAPABILITY, options);
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\drivers\\chromedriver.exe");
		driver = new ChromeDriver(cap);
		//driver = new HtmlUnitDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.get("https://www.obconline.co.in");
		Function.login(CORPID, USERID, PASSWORD, driver);
	}
	
/*	@Test
	public void login()
	{
		Function.login(CORPID, USERID, PASSWORD, driver);
	}
	*/
	@Test(dataProvider="Account_details")//(dependsOnMethods="login")//
	public void runOBC(String accno, String frm_dt, String to_dt) throws Exception //
	{
		//Function.login(CORPID, USERID, PASSWORD, driver);
		try {
			Function.generateStatement(accno, frm_dt, to_dt, driver);
			//Function.generateStatement("12025015000455", "8/3/2017", "8/3/2017", driver);
			Thread.sleep(2000);
		} catch (Exception e) {
			File screen_shot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try 
			{
				FileUtils.copyFile(screen_shot, new File(System.getProperty("user.dir")+"\\ErrorScreenshot\\"+accno+".png"));
			} 
			catch (IOException e1) 
			{				
				System.out.println("Error while taking screenshot");				
			}	
			//e.printStackTrace();
		}		
	}
	
	@DataProvider(name="Account_details")
	public Object[][] transData()
	{
		ExcelReader er = new ExcelReader();
		int rows = er.getRows();
		int cols = er.getCells(1);
		Object[][] obj = new Object[rows-1][cols];		
		for(int i=1; i<rows; i++)
		{			
			for(int j=0; j<cols; j++)
			{
			  obj[i-1][j] = er.getCellData(i, j);
			}
		}
		return obj;
	}
	
	@AfterClass
	public void clear()
	{
		driver.findElement(By.id("HREF_Logout")).click();
		driver.close();
	}
	
	
}
