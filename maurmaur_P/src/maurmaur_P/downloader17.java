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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class downloader17 implements Runnable {
	private Object textLog;
	String ThreadName, confirm, finImgSrc, cImgSrc;
	ArrayList<String> comicList = new ArrayList<String>();
	ArrayList<String> path = new ArrayList<String>();
	ArrayList<String> namae = new ArrayList<String>();
	JTextArea txtLog = new JTextArea();
	String format = "";
	File f;
	ArrayList<String> imgSrc = new ArrayList<String>();
	int endIndex;
	WebDriver driver;
	WebElement submitElement, sElement;

	downloader17(ArrayList<String> comicList, ArrayList<String> path, ArrayList<String> namae, JTextArea txtLog,
			String ThreadName) {
		this.comicList = comicList;
		this.path = path;
		this.namae = namae;
		this.txtLog = txtLog;
		this.ThreadName = ThreadName;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();

		// for (int i = 0; i < comicList.size(); i++) {
		try {// 첫 권을 저장하는 try
			System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
			System.out.println(System.getProperty("webdriver.chrome.driver"));
			WebDriver driver = new ChromeDriver();
			System.out.println("Chrome is selected");
			driver.get(comicList.get(0));
			System.out.println(comicList.get(0) + "is selected");

			WebElement pwElement = driver.findElement(By.name("pass"));
			WebElement submitElement = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/form/input"));
			System.out.println("\"pass\" is found");
			pwElement.click();
			pwElement.clear();
			pwElement.sendKeys("qndxkr");
			submitElement.click();

			while (true) {
				try {
					sElement = driver.findElement(By.id("gallery_vertical"));
					if (sElement != null) {
						break;
					}
					Thread.sleep(1000);
				} catch (Exception z) {
					z.printStackTrace();
				}

			}

			// WebElement sElement = driver.findElement(By.id("gallery_vertical"));
			System.out.println("\"gallery_vertical\" is found");
			System.out.println("sElement : " + sElement.toString());
			String inHtml = sElement.getAttribute("innerHTML");
			System.out.println("-----------------------------------------------------------");
			System.out.println("innerHTML: " + inHtml);
			System.out.println("-----------------------------------------------------------");

			String splitHTML[] = inHtml.split("src=");

			System.out.println("아래에 sp 표시\n");
			for (int i = 0; i < splitHTML.length; i++) {
				cImgSrc = "http://wasabisyrup.com" + splitHTML[i].substring(1, splitHTML[i].length());
				System.out.println("cImgSrc : " + cImgSrc);
				if (cImgSrc.contains(".jpg")) {
					format = ".jpg";
				} else if (cImgSrc.contains(".jpeg")) {
					format = ".jpeg";
				} else if (cImgSrc.contains(".png")) {
					format = ".png";
				} else if (cImgSrc.contains(".bmp")) {
					format = ".bmp";
				} else if (cImgSrc.contains(".png")) {
					format = ".png";
				}
				if (cImgSrc.contains(format)) {
					endIndex = cImgSrc.indexOf(format);
					finImgSrc = cImgSrc.substring(0, endIndex) + format;
					imgSrc.add(finImgSrc);
				}
			}

			for (int i = 0; i < imgSrc.size() - 1; i++) {
				for (int j = 1; j < imgSrc.size(); j++) {
					if (imgSrc.get(i).equals(imgSrc.get(j))) {
						imgSrc.remove(j);
					} else if (imgSrc.get(i).equals("")) {
						imgSrc.remove(i);
					} else if (imgSrc.get(j).equals("")) {
						imgSrc.remove(j);
					}
				}
			}
			System.out.println("All process is Done");
			System.out.println(imgSrc);

			// driver.quit();
			System.out.println("driver 계속");

			try {

				int fileNum = 1;
				for (int i = 0; i < imgSrc.size(); i++) {
					confirm = path.get(0) + "\\" + namae.get(0) + "_" + fileNum + format;
					f = new File(confirm);
					if (f.exists()) {
						System.out.println(confirm + "은 이미 존재해서 넘어감");
						fileNum++;
						continue;
					}

					URL a2 = new URL(imgSrc.get(i));
					System.out.println("imgSrc.get(" + i + ") : " + imgSrc.get(i) + "작업");
					HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
					conn.addRequestProperty("User-Agent", "Mozilla/4.76");

					System.out.println(conn.getContentLength());

					InputStream is = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					FileOutputStream os = new FileOutputStream(
							path.get(0) + "\\" + namae.get(0) + "_" + fileNum + format);

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
					fileNum++;
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

			long end = System.currentTimeMillis();
			txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end - start) / 1000.0 + "초\n");
			txtLog.setCaretPosition(txtLog.getDocument().getLength());
			imgSrc.clear();
			System.out.println("driver 계속/ 다음 권으로/ 처음 끝");

			for (int h = 1; h < namae.size(); h++) {
				System.out.println("반복문 안쪽 시작");
				long start2 = System.currentTimeMillis();
				WebElement nextB = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div[1]/div[2]/a"));
				System.out.println("버튼찾음");
				System.out.println(nextB.getText());
				nextB.click();
				System.out.println("클릭");

				// WebElement sElement2 = driver.findElement(By.id("gallery_vertical"));
				WebElement sElementWait = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						return d.findElement(By.id("gallery_vertical"));
					}
				});

				WebElement sElement2 = driver.findElement(By.id("gallery_vertical"));
				System.out.println("\"gallery_vertical\" is found");
				System.out.println("sElement : " + sElement2.toString());
				String inHtml2 = sElement2.getAttribute("innerHTML");
				System.out.println("-----------------------------------------------------------");
				System.out.println("innerHTML: " + inHtml2);
				System.out.println("-----------------------------------------------------------");

				String splitHTML2[] = inHtml2.split("src=");

				for (int i = 0; i < splitHTML2.length; i++) {
					cImgSrc = "http://wasabisyrup.com" + splitHTML2[i].substring(1, splitHTML2[i].length());
					System.out.println("cImgSrc : " + cImgSrc);
					if (cImgSrc.contains(".jpg")) {
						format = ".jpg";
					} else if (cImgSrc.contains(".jpeg")) {
						format = ".jpeg";
					} else if (cImgSrc.contains(".png")) {
						format = ".png";
					} else if (cImgSrc.contains(".bmp")) {
						format = ".bmp";
					} else if (cImgSrc.contains(".png")) {
						format = ".png";
					}
					if (cImgSrc.contains(format)) {
						endIndex = cImgSrc.indexOf(format);
						finImgSrc = cImgSrc.substring(0, endIndex) + format;
						imgSrc.add(finImgSrc);
					}
				}

				for (int i = 0; i < imgSrc.size() - 1; i++) {
					for (int j = 1; j < imgSrc.size(); j++) {
						if (imgSrc.get(i).equals(imgSrc.get(j))) {
							imgSrc.remove(j);
						} else if (imgSrc.get(i).equals("")) {
							imgSrc.remove(i);
						} else if (imgSrc.get(j).equals("")) {
							imgSrc.remove(j);
						}
					}
				}
				System.out.println("All process is Done");
				System.out.println(imgSrc);

				// driver.quit();
				System.out.println("driver 계속");

				try {

					int fileNum = 1;
					for (int i = 0; i < imgSrc.size(); i++) {
						confirm = path.get(h) + "\\" + namae.get(h) + "_" + fileNum + format;
						f = new File(confirm);
						if (f.exists()) {
							System.out.println(confirm + "은 이미 존재해서 넘어감");
							fileNum++;
							continue;
						}

						URL a2 = new URL(imgSrc.get(i));
						System.out.println("imgSrc.get(" + i + ") : " + imgSrc.get(i) + "작업");
						HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
						conn.addRequestProperty("User-Agent", "Mozilla/4.76");

						System.out.println(conn.getContentLength());

						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream os = new FileOutputStream(
								path.get(h) + "\\" + namae.get(h) + "_" + fileNum + format);

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
						fileNum++;
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}
				long end2 = System.currentTimeMillis();
				txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end2 - start2) / 1000.0 + "초\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());
				imgSrc.clear();

			}

			driver.quit();
			System.out.println("driver 종료");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}