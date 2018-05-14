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

public class downloader implements Runnable {
	private Object textLog;
	String ThreadName;
	List<String> comicList = new ArrayList<String>();
	List<String> path = new ArrayList<String>();
	List<String> namae = new ArrayList<String>();
	JTextArea txtLog = new JTextArea();
	String format, confirm;
	File f;

	downloader(List<String> comicList, List<String> path, List<String> namae, JTextArea txtLog, String ThreadName) {
		this.comicList = comicList;
		this.path = path;
		this.namae = namae;
		this.txtLog = txtLog;
		this.ThreadName = ThreadName;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < comicList.size(); i++) {
			try {// 한 권의 img를 저장하는 try
				Document doc2 = Jsoup.connect(comicList.get(i)).get();
				Element element2 = doc2.select("div.article-gallery").get(0);
				Elements pw = element2.select("input");
				if (pw != null) {
					System.out.println(pw);
					try {
						System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
						System.out.println(System.getProperty("webdriver.chrome.driver"));
						WebDriver driver = new ChromeDriver();
						System.out.println("Chrome is selected");
						driver.get(comicList.get(i));
						System.out.println(comicList.get(i)+"is selected");
						WebElement sElement = driver.findElement(By.name("pass"));
						System.out.println("\"pass\" is found");
						sElement.click();
						sElement.clear();
						sElement.sendKeys("qndxkr");
						sElement.submit();
						System.out.println("submit까지 완료");
						System.out.println("Page title is : "+driver.getTitle());
						
						
					}catch(WebDriverException sE) {
						System.out.println("에러발생");
					}
				}
				
				Elements img = element2.select("img");				
				int fileNum = 1;
				for (Element e2 : img) {

					String url3 = "http://wasabisyrup.com" + e2.getElementsByAttribute("src").attr("data-src");
					url3 = url3.replace(" copy", "%20copy");
					URL imgUrl = new URL(url3);

					if (url3.contains(".jpg")) {
						format = ".jpg";
					} else if (url3.contains(".jpeg")) {
						format = ".jpeg";
					} else if (url3.contains(".gif")) {
						format = ".gif";
					} else if (url3.contains(".png")) {
						format = ".png";
					}

					// 이미 있는 파일이라면 넘어감.
					confirm = path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format;
					f = new File(confirm);
					if (f.exists()) {
						System.out.println(confirm + "은 이미 존재해서 넘어감");
					}
					// 이미지 다운로드
					else {
						HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
						conn.addRequestProperty("User-Agent", "Mozilla/4.76");
						System.out.println(conn.getContentLength());

						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream os = new FileOutputStream(
								path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format);

						BufferedOutputStream bos = new BufferedOutputStream(os);
						int byteImg;

						// byte[] buf = new byte[conn.getContentLength()]; // 범위오류 때문에 1024byte단위로 읽게
						// 수정.
						byte[] buf = new byte[1024];
						while ((byteImg = bis.read(buf)) != -1) {
							bos.write(buf, 0, byteImg);
						}
						txtLog.append(namae.get(i) + "_" + fileNum + " 저장완료" + "  src : " + imgUrl + "\n");
						txtLog.setCaretPosition(txtLog.getDocument().getLength());
						fileNum += 1;
						bos.close();
						os.close();
						bis.close();
						is.close();
					}

				}

			} catch (IOException e2) {
				System.out.println(e2.getMessage());
				txtLog.append(e2.getMessage() + "\n");
			}
		}

		long end = System.currentTimeMillis();
		txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end - start) / 1000.0 + "초\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());

	}

}
