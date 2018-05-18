package maurmaur_P;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class downloaderZS implements Runnable {
	String ThreadName;
	ArrayList<String> comicList = new ArrayList<String>();
	ArrayList<String> path = new ArrayList<String>();
	ArrayList<String> namae = new ArrayList<String>();
	JTextArea txtLog = new JTextArea();
	String format, confirm;
	File f;

	downloaderZS(ArrayList<String> comicList, ArrayList<String> path, ArrayList<String> namae, JTextArea txtLog,
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
		for (int i = 0; i < comicList.size(); i++) {
			try {
//				링크에서 이미지 소스를 읽기
				Document doc2 = Jsoup.connect(comicList.get(i)).get();
				Element element2 = doc2.select("div#post").get(0);
				Elements img = element2.select("img");
//				파일 저장명에 부여될 번호
				int fileNum = 1;
				for (Element e2 : img) {
//					읽어낸 url을 String으로 변환
					String url3 = e2.getElementsByAttribute("src").attr("src");
//					url을 URL로 변환
					URL imgUrl = new URL(url3);
//					이미지 포멧에 따라 저장될 이미지의 확장자 설정
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
						fileNum++;
						continue;
					}

					// 이미지 다운로드
					else {
						HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
						conn.addRequestProperty("User-Agent", "Mozilla/4.76");
//						System.out.println(conn.getContentLength()); 문제가 생기면 음수 반환
						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream os = new FileOutputStream(
								path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format);
						BufferedOutputStream bos = new BufferedOutputStream(os);
						int byteImg;
						byte[] buf = new byte[1024];
						while ((byteImg = bis.read(buf)) != -1) {
							bos.write(buf, 0, byteImg);
						}
//						각 이미지가 다운됐을 때 메인UI에 메시지 출력
						txtLog.append(namae.get(i) + "_" + fileNum + " 저장완료" + "  src : " + imgUrl + "\n");
						txtLog.setCaretPosition(txtLog.getDocument().getLength());
						fileNum += 1;
						bos.close();
						os.close();
						bis.close();
						is.close();
					}
				}
//				각 권(각 화)이(가) 다운완료 됐을 때 메인UI에 메시지 출력
				txtLog.append(namae.get(i) + " 저장완료\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());
			} catch (IOException e2) {
				System.out.println(e2.getMessage());
				txtLog.append(e2.getMessage() + "\n");
			}
		}
//		Thread에 할당된 모든 이미지를 다운받고 소요된 총 시간을 메인UI에 출력
		long end = System.currentTimeMillis();
		txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end - start) / 1000.0 + "초\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}
}