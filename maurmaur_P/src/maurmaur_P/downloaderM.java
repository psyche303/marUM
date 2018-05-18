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

public class downloaderM implements Runnable {
	String ThreadName;
	ArrayList<String> comicList = new ArrayList<String>();
	ArrayList<String> path = new ArrayList<String>();
	ArrayList<String> namae = new ArrayList<String>();
	JTextArea txtLog = new JTextArea();
	String format, confirm;
	File f;

	downloaderM(ArrayList<String> comicList, ArrayList<String> path, ArrayList<String> namae, JTextArea txtLog,
			String ThreadName) {
		this.comicList = comicList;
		this.path = path;
		this.namae = namae;
		this.txtLog = txtLog;
		this.ThreadName = ThreadName;
	}

	@Override
	public void run() {
		// 쓰레드가 작업을 완료했을 때, 작업에 걸린 시간을 체크
		long start = System.currentTimeMillis();
		// 할당된 ArrayList의 url개수만큼 실행
		for (int i = 0; i < comicList.size(); i++) {
			try {
				Document doc2 = Jsoup.connect(comicList.get(i)).get();
				// 이미지가 있는 곳에서 이미지링크를 찾기
				Element element2 = doc2.select("div.article-gallery").get(0);
				Elements img = element2.select("img");
				// 이미지 파일을 저장할 때 부여될 파일번호
				int fileNum = 1;
				for (Element e2 : img) {
					// 읽어낸 이미지url을 열 수 있는 url형태로 가공
					String url3 = "http://wasabisyrup.com" + e2.getElementsByAttribute("src").attr("data-src");
					url3 = url3.replace(" copy", "%20copy");
					// String상태의 이미지url을 URL형태로 변형
					URL imgUrl = new URL(url3);
					// url에 있는 파일 포멧을 저장할 때 사용
					if (url3.contains(".jpg")) {
						format = ".jpg";
					} else if (url3.contains(".jpeg")) {
						format = ".jpeg";
					} else if (url3.contains(".gif")) {
						format = ".gif";
					} else if (url3.contains(".png")) {
						format = ".png";
					}

					// 존재하는 파일일 경우, 이미지 저장하는 부분을 넘어감
					confirm = path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format;
					f = new File(confirm);
					if (f.exists()) {
						// 메인UI에 txtLog출력
						txtLog.append(namae.get(i) + "_" + fileNum + "은 이미 있습니다.\n");
						txtLog.setCaretPosition(txtLog.getDocument().getLength());
					}
					// 이미지 다운로드				
						HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
						conn.addRequestProperty("User-Agent", "Mozilla/4.76");
						// System.out.println(conn.getContentLength()); 문제가 있을 경우 음수가 출력됨
						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream os = new FileOutputStream(
								path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format);
						BufferedOutputStream bos = new BufferedOutputStream(os);
						int byteImg;
						// 버퍼에 1024바이트 단위로 읽음
						byte[] buf = new byte[1024];
						while ((byteImg = bis.read(buf)) != -1) {
							bos.write(buf, 0, byteImg);
						}
						// 저장완료 시 메인UI에 txtLog출력
						txtLog.append(namae.get(i) + "_" + fileNum + " 저장완료" + "  src : " + imgUrl + "\n");
						txtLog.setCaretPosition(txtLog.getDocument().getLength());
						fileNum += 1;
						bos.close();
						os.close();
						bis.close();
						is.close();					
				}
				// 한 권(한 화)의 작업이 끝나면 종료메시지를 txtLog에 출력
				txtLog.append(namae.get(i) + " 저장완료\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());

			} catch (IOException e2) {
				System.out.println(e2.getMessage());
				txtLog.append(e2.getMessage() + "\n");
			}
		}
		// 모든 다운로드가 완료되면 소요시간을 txtLog에 출력
		long end = System.currentTimeMillis();
		txtLog.append(ThreadName + " is Done ____ " + "실행 시간 : " + (end - start) / 1000.0 + "초\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

}