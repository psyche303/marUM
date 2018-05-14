package maurmaur_P;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;

public class test {
	
	
	public static void main(String[] args) {
		try {
			  System.setProperty("webdriver.chrome.driver", "‪‪C:\\Users\\Administrator\\Downloads\\selenium-java-3.12.0\\chromedriver.exe");

			        System.out.println(System.getProperty("webdriver.chrome.driver"));

			        WebDriver driver = new ChromeDriver();

			        System.out.println("Chrome is selected");                              

			        driver.get("http://www.google.com");

			        System.out.println("Google is selected");                              

			        // Find the text input element by its name

			        WebElement element = driver.findElement(By.name("q"));
			        System.out.println("\"q\" is found");

			        // Enter any key to search
			        
			        element.click();
			        System.out.println("clicked");
			        
			        element.clear();
			        System.out.println("cleared");
			        
			        element.sendKeys("입력되어라 얍!!");

			        // Submit the form to get the result 

			        element.submit();

			      // Check the title of the page

			        System.out.println("Page title is: " + driver.getTitle());
			  
			 } catch(WebDriverException e) {
			  System.out.println("에러가 발생해부렀어");
			 }
			}
			}