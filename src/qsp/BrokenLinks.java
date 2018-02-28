package qsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrokenLinks {
	static
	{
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
	}
	public static void main(String[] args) {
		String brokenLinks="./BrokenLinks.txt";
		String workingLinks="./WorkingLinks.txt";
		String thirdParyLinks="./ThirdPartyLinks.txt";
		File br=new File(brokenLinks);
		File wr=new File(workingLinks);
		File tr=new File(thirdParyLinks);
		FileWriter writer;
		try {
			writer = new FileWriter(br);
			BufferedWriter buffbroken=new BufferedWriter(writer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer = new FileWriter(wr);
			BufferedWriter buffworking=new BufferedWriter(writer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int status=200;
		HttpURLConnection huc=null;
		String homePage="https://www.facebook.com/";
		WebDriver driver=new ChromeDriver();
		driver.get("https://www.facebook.com/");
		List<WebElement> allLinks = driver.findElements(By.tagName("a"));
//		List<WebElement> allLinks = driver.findElements(By.xpath("//a"));
		
		for(WebElement link:allLinks)
		{
			String url = link.getAttribute("href");
			if(url==null || url.isEmpty())
			{
				
				System.out.println("Anchor is not configured "+url);
				continue;
			}
			if(!url.startsWith(homePage))
			{
				try {
					writer = new FileWriter(tr,true);
					BufferedWriter buffthird=new BufferedWriter(writer);
					buffthird.write(url+"\n");
					buffthird.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Third party links, so skipping "+url);
				continue;
			}
			if(url.startsWith(homePage))
			{
				try {
					URL u=new URL(url);
					huc=(HttpURLConnection)u.openConnection();
					huc.setRequestMethod("HEAD");
					huc.connect();
					status =huc.getResponseCode();
					if(status>=400)
					{
						System.out.println(url+" Broke Link");
					}
					else
					{
						System.out.println(url+" Working Fine");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		int noOfLinks=allLinks.size();
		System.out.println("Total no of links "+noOfLinks);
		driver.close();
	}

}
