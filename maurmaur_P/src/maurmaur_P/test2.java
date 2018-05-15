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

		System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");

		File f = new File(".\\gg.txt");

		if (f.exists()) {
			System.out.println("File existed");
		} else {
			System.out.println("File not found!");
		}

		System.out.println(System.getProperty("webdriver.chrome.driver"));
		WebDriver driver = new ChromeDriver();
		System.out.println("Chrome is selected");
		driver.get("http://wasabisyrup.com/archives/W70aImx_IwE");
		System.out.println("http://wasabisyrup.com/archives/W70aImx_IwE" + "is selected");
		WebElement sElement = driver.findElement(By.id("gallery_vertical"));
		System.out.println("\"gallery_vertical\" is found");
		System.out.println("sElement : " + sElement.toString());
		String a = sElement.getAttribute("innerHTML");
		System.out.println("a: " + a);

		String sp[] = a.split("src=");
		List<String> spp = new ArrayList<String>();
		int st, ed;
		String a1, h;
		String format = "";

		System.out.println("아래에 sp 표시\n");
		for (int i = 0; i < sp.length; i++) {
			h = "http://wasabisyrup.com" + sp[i].substring(1, sp[i].length());
			// System.out.println("sp[" + i + "] : " + sp[i]);
			System.out.println("h : " + h);
			if (h.contains(".jpg")) {
				format = ".jpg";
			} else if (h.contains(".jpeg")) {
				format = ".jpeg";
			} else if (h.contains(".png")) {
				format = ".png";
			} else if (h.contains(".bmp")) {
				format = ".bmp";
			} else if (h.contains(".png")) {
				format = ".png";
			}
			if (h.contains(format)) {
				ed = h.indexOf(format);
				a1 = h.substring(0, ed) + format;
				spp.add(a1);
			}
		}

		for (int i = 0; i < spp.size() - 1; i++) {
			for (int j = 1; j < spp.size(); j++) {
				if (spp.get(i).equals(spp.get(j))) {
					spp.remove(j);
				} else if (spp.get(i).equals("")) {
					spp.remove(i);
				} else if (spp.get(j).equals("")) {
					spp.remove(j);
				}
			}
		}
		System.out.println("All process is Done");
		System.out.println(spp);

		driver.quit();
		System.out.println("driver 종료");

		try {
			for (int i = 0; i < spp.size(); i++) {
				URL a2 = new URL(spp.get(i));

				HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
				conn.addRequestProperty("User-Agent", "Mozilla/4.76");
				System.out.println(conn.getContentLength());

				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				FileOutputStream os = new FileOutputStream("E:\\크롤링 연습\\붕탁 테스트_" + i + ".jpg");

				BufferedOutputStream bos = new BufferedOutputStream(os);
				int byteImg;

				byte[] buf = new byte[1024];
				while ((byteImg = bis.read(buf)) != -1) {
					bos.write(buf, 0, byteImg);
				}
				bos.close();
				os.close();
				bis.close();
				is.close();
			}
		} catch (Exception e1) {
			System.out.println("오류");
		}
	}
}
