package il.ac.colman.cs.util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;


public class ScreenshotGenerator {
  public static String takeScreenshot(String url) {
    String screenshotFilePath = null;
    final File screenShot = new File("screenshot.png").getAbsoluteFile();
    
    final WebDriver driver = new FirefoxDriver();
    
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

    AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
    
    PutObjectResult objResult = client.putObject( "screenshots-from-tweets" , url , screenShot);
    screenshotFilePath = objResult.getVersionId();
    
    return screenshotFilePath;
  }
}
