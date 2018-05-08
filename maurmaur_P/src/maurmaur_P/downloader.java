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

public class downloader implements Runnable {
	List<String> comicList = new ArrayList<String>();
	List<String> path = new ArrayList<String>();
	List<String> namae = new ArrayList<String>();
	private Object textLog;
	JTextArea txtLog = new JTextArea();
	String ThreadName;

	downloader(List<String> comicList, List<String> path, List<String> namae, JTextArea txtLog, String ThreadName) {
		this.comicList = comicList;
		this.path = path;
		this.namae = namae;
		this.txtLog = txtLog;
		this.ThreadName = ThreadName;
	}

	@Override
	public void run() {
		for (int i = 0; i < comicList.size(); i++) {
			try {// 한 권의 img를 저장하는 try
				Document doc2 = Jsoup.connect(comicList.get(i)).get();
				Element element2 = doc2.select("div.article-gallery").get(0);
				Elements img = element2.select("img");
				// System.out.println(comicList.get(i));

				int fileNum = 1;
				for (Element e2 : img) {

					String url3 = e2.getElementsByAttribute("src").attr("data-src");

					url3 = "http://wasabisyrup.com" + url3;

					url3 = url3.replace(" copy", "%20copy");

					URL imgUrl = new URL(url3);

					String format = ".jpg";

					HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
					System.out.println(conn.getContentLength());

					InputStream is = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					FileOutputStream os = new FileOutputStream(
							path.get(i) + "\\" + namae.get(i) + "_" + fileNum + format);

					BufferedOutputStream bos = new BufferedOutputStream(os);
					int byteImg;

					byte[] buf = new byte[conn.getContentLength()];
					while ((byteImg = bis.read(buf)) != -1) {
						bos.write(buf, 0, byteImg);
					}
					txtLog.append(namae.get(i) + "_" + fileNum + " 저장완료" + "  src : " + imgUrl + "\n");
					fileNum += 1;
					bos.close();
					os.close();
					bis.close();
					is.close();

				}

			} catch (IOException e2) {
				System.out.println(e2.getMessage());
				txtLog.append(e2.getMessage() + "\n");
			}
		}

		txtLog.append(ThreadName + " is Done\n");

	}

}
