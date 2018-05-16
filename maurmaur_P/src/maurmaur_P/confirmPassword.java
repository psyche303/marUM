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

public class confirmPassword {
	List<String> comicList = new ArrayList<String>();
	String confirmPW = "";

	confirmPassword(List<String> comicList) {
		this.comicList = comicList;
		try {
			Document doc = Jsoup.connect(comicList.get(0)).get();
			try {
				Element element = doc.select("input").get(0);
				System.out.println("비번 있음");
				confirmPW = "be";

			} catch (Exception e2) {
				System.out.println("비번 없음");
				confirmPW = "not";
				e2.printStackTrace();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
