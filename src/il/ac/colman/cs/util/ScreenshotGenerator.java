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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

import io.github.bonigarcia.wdm.WebDriverManager;


public class ScreenshotGenerator {
	static WebDriver driver;
	public static String takeScreenshot(String url) {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		File screenShot = new File("screenshot_" + generatetimeStampBasedRandomNumber() + ".png").getAbsoluteFile();
			try {
				driver.get(url);
			    try {
			    	TimeUnit.SECONDS.sleep(5);
			    	screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			    } catch (Exception e) {}
			} finally {
				driver.close();
			}
		return screenShot.getPath();
	}
  
	private static String generatetimeStampBasedRandomNumber() {
		Date date = new Date();
		long time = date.getTime();
	    Timestamp ts = new Timestamp(time);
	    String tst = ts.toString();
	    tst = tst.substring(0, tst.length() - 4);
	    tst = tst.replace("-", "");
	    tst = tst.replace(" ", "");
	    tst = tst.replace(":", "");
	    return tst;
	}
}
