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
	  String screenshotFilePath = null;
	  final File screenShot = new File("screenshot_" + generatetimeStampBasedRandomNumber() + ".png").getAbsoluteFile();
    
	    try {
	    	driver.get(url);
	    	try {
	    		TimeUnit.SECONDS.sleep(5);
	    		final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    		FileUtils.copyFile(outputFile, screenShot);
	    	} catch (Exception e) {}
	    } finally {
	      driver.close();
	    }

	    String[] str = url.split("/");
	    String title = str[2];
	    
    AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
    
    PutObjectResult objResult = client.putObject( "screenshots-from-tweets" , title , screenShot);
   screenshotFilePath = objResult.getVersionId();
    
    return screenshotFilePath;
  }
  private static String generatetimeStampBasedRandomNumber() {

      Date date = new Date();
      long time = date.getTime();
      Timestamp ts = new Timestamp(time);

      String tst = ts.toString();

      try {
          tst = tst.substring(0, tst.length() - 4);
          tst = tst.replace("-", "");
          tst = tst.replace(" ", "");
          tst = tst.replace(":", "");
      } catch (Exception e) {
          e.printStackTrace();
      }

      return tst;
  }
}
