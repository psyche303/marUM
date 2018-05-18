package maurmaur_P;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;

import javax.swing.JTextArea;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class downloader17 implements Runnable {
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
//		작업에 소요되는 시간 측정
		long start = System.currentTimeMillis();
		try {
//			Chromedriver 적재
			System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
			WebDriver driver = new ChromeDriver();
//			처음 권(처음 화)의 링크 열기
			driver.get(comicList.get(0));
//			Password 입력란과 Submit 버튼을 찾음
			WebElement pwElement = driver.findElement(By.name("pass"));
			WebElement submitElement = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/form/input"));
//			Password입력 + submit 클릭
			pwElement.click();
			pwElement.clear();
			pwElement.sendKeys("qndxkr");
			submitElement.click();
//			구글캡챠 통과 여부를 로딩되는 이미지의 여부로 1초마다 확인
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
//			이미지가 로딩된 부분의 전체 소스을 String으로 변환
			String inHtml = sElement.getAttribute("innerHTML");
//			전체 소스 중 'src='로 시작하는 부분을 추출
			String splitHTML[] = inHtml.split("src=");
			for (int i = 0; i < splitHTML.length; i++) {
//				링크 앞쪽에 생략된 부분 추가
				cImgSrc = "http://wasabisyrup.com" + splitHTML[i].substring(1, splitHTML[i].length());
//				이미지 파일의 포멧을 저장할 때 반영하기 위해 저장
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
//				링크가 이미지 링크일 경우에만 링크를 가공(cImgSrc에는 뒤쪽에 CSS등이 더 붙어있음), 그리고 저장할 이미지List(imgSrc)에 저장
				if (cImgSrc.contains(format)) {
					endIndex = cImgSrc.indexOf(format);
					finImgSrc = cImgSrc.substring(0, endIndex) + format;
					imgSrc.add(finImgSrc);
				}
			}
//			src와 data-src 내용이 겹치기에 제거
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
//			이미지 저장 시작
			try {
				// 이미지 파일을 저장할 때 부여될 파일번호
				int fileNum = 1;				 
				for (int i = 0; i < imgSrc.size(); i++) {
//					존재하는 파일일 경우, 이미지 저장하는 부분을 넘어감
					confirm = path.get(0) + "\\" + namae.get(0) + "_" + fileNum + format;
					f = new File(confirm);
					if (f.exists()) {
						fileNum++;
						continue;
					}
//					List저장된 이미지url으로부터 이미지를 저장
					URL a2 = new URL(imgSrc.get(i));
					System.out.println("imgSrc.get(" + i + ") : " + imgSrc.get(i) + "작업");
					HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
					conn.addRequestProperty("User-Agent", "Mozilla/4.76");					
//					System.out.println(conn.getContentLength()); 문제가 있을 경우 음수가 출력됨
					InputStream is = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					FileOutputStream os = new FileOutputStream(
							path.get(0) + "\\" + namae.get(0) + "_" + fileNum + format);
					BufferedOutputStream bos = new BufferedOutputStream(os);
					int byteImg;
					// 버퍼에 1024바이트 단위로 읽음
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
				// 저장완료 시 메인UI에 txtLog출력
				txtLog.append(namae.get(0) + " 저장완료\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());

			} catch (Exception e2) {
				e2.printStackTrace();
			}
//			작업 소요에 걸린 시간을 메인UI의 txtLog에 출력
			long end = System.currentTimeMillis();
			txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end - start) / 1000.0 + "초\n");
			txtLog.setCaretPosition(txtLog.getDocument().getLength());
//			이미지 소스가 저장된 List 초기화
			imgSrc.clear();
//			저장이 끝나면 "다음 화"버튼을 눌려서 이미지 저장을 반복
			for (int h = 1; h < namae.size(); h++) {
//				작업 소요시간 측정
				long start2 = System.currentTimeMillis();
//				"다음 화 버튼"찾기, 클릭
				WebElement nextB = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div[1]/div[2]/a"));
				nextB.click();
//				넘어간 페이지의 이미지 소스가 로딩될 때까지 기다림
				WebElement sElementWait = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						return d.findElement(By.id("gallery_vertical"));
					}
				});
//				첫 번째와 동일한 이미지 다움로드 과정
				WebElement sElement2 = driver.findElement(By.id("gallery_vertical"));				
				String inHtml2 = sElement2.getAttribute("innerHTML");
				String splitHTML2[] = inHtml2.split("src=");
				for (int i = 0; i < splitHTML2.length; i++) {
					cImgSrc = "http://wasabisyrup.com" + splitHTML2[i].substring(1, splitHTML2[i].length());					
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
				try {
					int fileNum = 1;
					for (int i = 0; i < imgSrc.size(); i++) {
						confirm = path.get(h) + "\\" + namae.get(h) + "_" + fileNum + format;
						f = new File(confirm);
						if (f.exists()) {
							fileNum++;
							continue;
						}
						URL a2 = new URL(imgSrc.get(i));
						HttpURLConnection conn = (HttpURLConnection) a2.openConnection();
						conn.addRequestProperty("User-Agent", "Mozilla/4.76");
//						System.out.println(conn.getContentLength()); 문제가 있을 경우 음수가 출력됨
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
					txtLog.append(namae.get(h) + " 저장완료\n");
					txtLog.setCaretPosition(txtLog.getDocument().getLength());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
//				한 권(한 화)의 다운로드가 종료되면 걸린 시간을 메인 UI의 txtLog에 출력
				long end2 = System.currentTimeMillis();
				txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end2 - start2) / 1000.0 + "초\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());
//				사용한 imgSrc리스트 초기화
				imgSrc.clear();
			}
//			모든 작업이 끝나면 chromeDriver 종료
			driver.quit();
			System.out.println("driver 종료");
//			작업종료 메시지를 메인UI의 txtLog에 출력
			txtLog.append("작업종료");
			txtLog.setCaretPosition(txtLog.getDocument().getLength());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}