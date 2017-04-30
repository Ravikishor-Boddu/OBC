package obc;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class Function 
{
	
	public static void login(String CorpId, String UserId, String Passwd, WebDriver driver)
	{
		//Click on Corporate User Login
		driver.findElement(By.xpath("//a[text()='Corporate User Login']")).click();
		//Enter Corporate Id
		driver.findElement(By.name("AuthenticationFG.CORP_CORP_ID")).sendKeys(CorpId);
		//Enter User Id
		driver.findElement(By.name("AuthenticationFG.CORP_USER_ID")).sendKeys(UserId);
		//Click on Login button
		driver.findElement(By.name("Action.STU_VALIDATE_CREDENTIALS")).click();
		//Enter Password
		driver.findElement(By.name("AuthenticationFG.ACCESS_CODE")).sendKeys(Passwd);
		//Click on Login button
		driver.findElement(By.name("Action.VALIDATE_STU_CREDENTIALS")).click();
	}
	
	
	public static void generateStatement(String accno, String from_date, String to_date, WebDriver driver) throws Exception 
	{
	
		driver.findElement(By.linkText("Operative Accounts")).click();
		//Enter Account number
		driver.findElement(By.name("AccountSummaryFG.ACCOUNT_NUMBER")).sendKeys(accno);
		//Click on search button
		driver.findElement(By.id("LOAD_ACCOUNTS")).click();
		
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,100)");
		//Click on view transaction history
		Actions act = new Actions(driver);
		WebElement accno_hist = driver.findElement(By.className("menuPullDownHead"));		
		act.moveToElement(accno_hist).perform();
		//Thread.sleep(1000);
		
		WebElement trans_hist=driver.findElement(By.xpath("//ul[@id='menuchoices_1']//li[1]/a"));
		act.moveToElement(trans_hist).build().perform();
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", trans_hist);
			
		//Enter From Date
		WebElement f_date = driver.findElement(By.id("TransactionHistoryFG.FROM_TXN_DATE"));
		f_date.clear();
		f_date.sendKeys(from_date);
		
		//Enter To Date
		WebElement t_date = driver.findElement(By.id("TransactionHistoryFG.TO_TXN_DATE"));
		t_date.clear();
		t_date.sendKeys(to_date);
		//Click on Generate Statement
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@id='SEARCH' and @class='formbtn']")).click();
		Thread.sleep(1000);
		//Select statement download format
		WebElement saveAs_dd = null;
		try
		{
			saveAs_dd = driver.findElement(By.id("TransactionHistoryFG.OUTFORMAT"));
			Select save = new Select(saveAs_dd);
			save.selectByVisibleText("CSV file");
			//Click on OK button
			driver.findElement(By.id("okButton")).click();
		}
		catch(Exception e)
		{
			String err_msg = driver.findElement(By.cssSelector("div.redbg")).getText();
			System.out.println("Exception Occured While Generating Statement For A/c no:"+accno+"--\n"+err_msg);	
			Assert.assertTrue(false);			
		}	
		//driver.findElement(By.id("HREF_Logout")).click();
	}
	
}
