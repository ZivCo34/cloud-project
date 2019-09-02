package il.ac.colman.cs.util;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

import io.github.bonigarcia.wdm.WebDriverManager;


public class ScreenshotGenerator {
	static WebDriver driver;
	public static String takeScreenshot(String url, String title) {
		AmazonS3 clientS3 = AmazonS3ClientBuilder.defaultClient();
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.setHeadless(true);
		driver = new ChromeDriver(chromeOptions);
		File screenShot = new File(title + ".png").getAbsoluteFile();
			try {
				driver.get(url);
			    try {
			    	TimeUnit.SECONDS.sleep(5);
			    	screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			    } catch (Exception e) {}
			} finally {
				driver.close();
			}
		clientS3.putObject("screenshots-from-tweets", title, screenShot);
		String screenshotURL = clientS3.getObject("screenshots-from-tweets", title).getRedirectLocation();
		return screenshotURL;
	}
}
