package maurmaur_P;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class marumaru extends JFrame {
	private Object textLog;
	Font font = new Font("돋움", Font.BOLD, 18);

	marumaru() {
		setTitle("MaruMaru Downloader");

		Container c = getContentPane();
		c.setLayout(null);

		// URL입력란
		JLabel urlName = new JLabel("url");
		urlName.setBounds(10, 10, 100, 30);
		urlName.setHorizontalAlignment(SwingConstants.CENTER);
		urlName.setFont(font);

		JTextField url = new JTextField();
		url.setBounds(120, 10, 500, 30);

		c.add(urlName);
		c.add(url);

		// 저장위치 입력란
		JLabel saveLocationName = new JLabel("저장 위치");
		saveLocationName.setBounds(10, 50, 100, 30);
		saveLocationName.setHorizontalAlignment(SwingConstants.CENTER);
		saveLocationName.setFont(font);

		JTextField saveLocation = new JTextField();
		saveLocation.setBounds(120, 50, 500, 30);

		c.add(saveLocationName);
		c.add(saveLocation);

		// 시작버튼
		JButton start = new JButton("시작");
		start.setBounds(250, 90, 200, 30);
		start.setFont(font);
		c.add(start);

		// 결과알림 영역
		JTextArea txtLog = new JTextArea();

		JScrollPane scrollPane = new JScrollPane(txtLog);
		scrollPane.setBounds(10, 130, 665, 420);

		this.getContentPane().add(scrollPane);
		txtLog.setCaretPosition(txtLog.getDocument().getLength());

		// 기본설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 600);
		setVisible(true);

		ActionListener listener = e -> {
			// 저장위치에 폴더가 없으면 만드는 매서드
			String path = saveLocation.getText();
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			try { // 큰try // 여러 권 처리하는 IF
				Document doc = Jsoup.connect(url.getText()).get();
				Element element = doc.select("div.content").get(0);
				Elements N = element.select("a");

				for (Element e1 : N) { // for문
					String url2 = e1.getElementsByAttribute("href").attr("href");
					String namae = e1.text();

					if (url2.contains("http://")) { // "a"에서 만화의 링크를 골라내는 if

						String path2 = path + "\\" + namae;
						File file2 = new File(path2);
						if (!file2.exists()) {
							file2.mkdirs();
						}

						try {// 한 권의 img를 저장하는 try
							Document doc2 = Jsoup.connect(url2).get();
							Element element2 = doc2.select("div.article-gallery").get(0);
							Elements img = element2.select("img");

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
										path2 + "\\" + namae + "_" + fileNum + format);

								BufferedOutputStream bos = new BufferedOutputStream(os);
								int byteImg;

								byte[] buf = new byte[conn.getContentLength()];
								while ((byteImg = bis.read(buf)) != -1) {
									bos.write(buf, 0, byteImg);
								}
								txtLog.append(namae + "_" + fileNum + " 저장완료" + "  src : " + imgUrl + "\n");
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

					} // if_A

				} // for문

			} catch (IOException e2) {
				System.out.println(e2.getMessage());
				txtLog.append(e2.getMessage() + "\n");
			}
		};

		start.addActionListener(listener);

	}

	public static void main(String[] args) {
		new marumaru();

	}

}