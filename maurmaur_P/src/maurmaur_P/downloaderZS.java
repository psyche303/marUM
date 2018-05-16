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
		long start = System.currentTimeMillis();
		for (int i = 0; i < comicList.size(); i++) {
			try {// 한 권의 img를 저장하는 try
				Document doc2 = Jsoup.connect(comicList.get(i)).get();
				Element element2 = doc2.select("div#post").get(0);
				Elements img = element2.select("img");
				int fileNum = 1;
				for (Element e2 : img) {
					String url3 = e2.getElementsByAttribute("src").attr("src");
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
						fileNum++;
						continue;
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
				txtLog.append(namae.get(i) + " 저장완료\n");
				txtLog.setCaretPosition(txtLog.getDocument().getLength());

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