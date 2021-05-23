package JudicialBranch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Testcase {

	String url;
	String[] towns;
	int days;

	public void InitialSetup() {
		String path = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", path + "\\src\\test\\resources\\chromedriver.exe");
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(path + "\\src\\test\\resources\\application.properties");
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		url = prop.getProperty("URL");
		days = Integer.parseInt(prop.getProperty("days"));
		towns = prop.get("cityNames").toString().split(",");
	}

	@Test
	public void selectCities() {
		InitialSetup();
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		List<WebElement> linksFromSite = driver.findElements(By.xpath("//div[@id='ctl00_cphBody_Panel1']//a"));
		String newWindowOpen = Keys.chord(Keys.SHIFT, Keys.ENTER);
		for (String town : towns)
			for (WebElement linkName : linksFromSite)
				if (town.equals(linkName.getText().trim()))
					linkName.sendKeys(newWindowOpen);
		String newTab = Keys.chord(Keys.CONTROL, Keys.ENTER);
		Set<String> windowsIDs = driver.getWindowHandles();
		for (String id : windowsIDs) {
			driver.switchTo().window(id);
			List<WebElement> saleDates = driver
					.findElements(By.xpath("//*[@id='ctl00_cphBody_GridView1']//td[2]/span"));
			for (int i = 1; i <= saleDates.size(); i++)
				if (verifyDateRange(saleDates.get(i - 1).getText().split("\n")[0]))
					driver.findElement(By.xpath("(//*[@id='ctl00_cphBody_GridView1']//td[5]/a)[" + i + "]"))
							.sendKeys(newTab);
		}
	}

	public boolean verifyDateRange(String saleDateText) {
		boolean flag = false;
		Calendar c = Calendar.getInstance();
		Date todaysDate = c.getTime();
		c.add(Calendar.DATE, days);
		Date toDate = c.getTime();
		Date dateFromSite = null;
		try {
			dateFromSite = new SimpleDateFormat("MM/dd/yyyy").parse(saleDateText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (todaysDate.compareTo(dateFromSite) < 0 && toDate.compareTo(dateFromSite) > 0)
			flag = true;
		return flag;
	}
}
