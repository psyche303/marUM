package maurmaur_P;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;

import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;

public class test2 {

	public static void main(String[] args) {
		// try {

		System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
		System.out.println(System.getProperty("webdriver.chrome.driver"));
		WebDriver driver = new ChromeDriver();
		System.out.println("Chrome is selected");
		driver.get("https://movie.naver.com/movie/bi/mi/basic.nhn?code=155123");
		System.out.println("https://movie.naver.com/" + "is selected");
		WebElement sElement = driver.findElement(By.className("photo_video"));
		System.out.println("\"photo_video\" is found");
		System.out.println("sElement : " + sElement.toString());
		String a = sElement.getAttribute("innerHTML");
		System.out.println("a: "+ a);

		String sp[] = a.split("src=");
		List<String> spp = new ArrayList<String>();
		int st, ed;
		String a1;
		
		

		for (int i = 0; i < sp.length; i++) {
//			System.out.println("sp["+i+"] : "+sp[i]);
			if (sp[i].contains(".jpg")) {
				st = sp[i].indexOf("https://");
				ed = sp[i].indexOf(".jpg");
				a1 = sp[i].substring(st,ed+4);
				System.out.println(a1);
				spp.add(a1);
			}
			
		}
		System.out.println("All process is Done");
		System.out.println(spp);

		// int st = a.indexOf("https://");
		// int ed = a.indexOf(".jpg");
		//
		// String a1 = a.substring(st,ed+4);
		// System.out.println(a1);
		//
		//
		//
		// URL a2 = new URL(a1);
		// HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
		// conn.addRequestProperty("User-Agent", "Mozilla/4.76");
		// System.out.println(conn.getContentLength());
		//
		// InputStream is = conn.getInputStream();
		// BufferedInputStream bis = new BufferedInputStream(is);
		// FileOutputStream os = new FileOutputStream(
		// "C:\\Users\\2-10\\Desktop\\크롤링테스트\\붕탁테스트_" + "1" + ".jpg");
		//
		// BufferedOutputStream bos = new BufferedOutputStream(os);
		// int byteImg;
		//
		// // byte[] buf = new byte[conn.getContentLength()]; // 범위오류 때문에 1024byte단위로 읽게
		// // 수정.
		// byte[] buf = new byte[1024];
		// while ((byteImg = bis.read(buf)) != -1) {
		// bos.write(buf, 0, byteImg);
		// }
		// bos.close();
		// os.close();
		// bis.close();
		// is.close();
		// }catch(Exception e1) {
		// System.out.println("오류");
		// }
	}
}
