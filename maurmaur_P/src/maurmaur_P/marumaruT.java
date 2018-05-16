package maurmaur_P;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class marumaruT extends JFrame {
	Font font = new Font("돋움", Font.BOLD, 18);
	ArrayList<String> comicList1 = new ArrayList<String>();
	ArrayList<String> comicList2 = new ArrayList<String>();
	ArrayList<String> comicList3 = new ArrayList<String>();
	ArrayList<String> comicList4 = new ArrayList<String>();
	ArrayList<String> comicList5 = new ArrayList<String>();
	ArrayList<String> namae1 = new ArrayList<String>();
	ArrayList<String> namae2 = new ArrayList<String>();
	ArrayList<String> namae3 = new ArrayList<String>();
	ArrayList<String> namae4 = new ArrayList<String>();
	ArrayList<String> namae5 = new ArrayList<String>();
	ArrayList<String> path5 = new ArrayList<String>();
	ArrayList<String> path6 = new ArrayList<String>();
	ArrayList<String> path7 = new ArrayList<String>();
	ArrayList<String> path8 = new ArrayList<String>();
	ArrayList<String> path9 = new ArrayList<String>();
	ArrayList<ArrayList<String>> listList = new ArrayList<ArrayList<String>>() {
		{
			add(comicList1);
			add(comicList2);
			add(comicList3);
			add(comicList4);
			add(comicList5);
			add(namae1);
			add(namae2);
			add(namae3);
			add(namae4);
			add(namae5);
			add(path5);
			add(path6);
			add(path7);
			add(path8);
			add(path9);
		}
	};

	int listNum = 0;
	String path2, namae;

	marumaruT() {
		setTitle("Comic Catcher 1.00");

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
			for (int q = 0; q < listList.size(); q++) {
				listList.get(q).clear();
			}
			// 저장위치에 폴더가 없으면 만드는 매서드
			String path = saveLocation.getText();
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			if (url.getText().contains("zangsisi.net")) {
				try { // 큰try // 여러 권 처리하는 IF
					Document doc = Jsoup.connect(url.getText()).get();
					Element element = doc.select("div#post").get(0);
					Elements N = element.select("a.tx-link");

					for (Element e1 : N) { // for문
						String url2 = e1.getElementsByAttribute("href").attr("href"); // 하부링크
						namae = e1.text();
						System.out.println("url2 : " + url2);
						System.out.println("namae : " + namae);

						path2 = path + "\\" + namae;
						File file2 = new File(path2);
						if (!file2.exists()) {
							file2.mkdirs();
						}
						if (listNum % 4 == 0) {
							comicList1.add(url2);
							namae1.add(namae);
							path5.add(path2);
							listNum++;
						} else if (listNum % 4 == 1) {
							comicList2.add(url2);
							namae2.add(namae);
							path6.add(path2);
							listNum++;
						} else if (listNum % 4 == 2) {
							comicList3.add(url2);
							namae3.add(namae);
							path7.add(path2);
							listNum++;
						} else if (listNum % 4 == 3) {
							comicList4.add(url2);
							namae4.add(namae);
							path8.add(path2);
							listNum++;
						}

					}

					downloaderZS zSS1 = new downloaderZS(comicList1, path5, namae1, txtLog, "1번 Thread");
					downloaderZS zSS2 = new downloaderZS(comicList2, path6, namae2, txtLog, "2번 Thread");
					downloaderZS zSS3 = new downloaderZS(comicList3, path7, namae3, txtLog, "3번 Thread");
					downloaderZS zSS4 = new downloaderZS(comicList4, path8, namae4, txtLog, "4번 Thread");
					Thread t6 = new Thread(zSS1);
					Thread t7 = new Thread(zSS2);
					Thread t8 = new Thread(zSS3);
					Thread t9 = new Thread(zSS4);

					t6.start();
					t7.start();
					t8.start();
					t9.start();
				} catch (Exception ez) {
					ez.printStackTrace();
				}

			} else {
				try { // 큰try // 여러 권 처리하는 IF
					Document doc = Jsoup.connect(url.getText()).get();
					Element element = doc.select("div.content").get(0);
					Elements N = element.select("a");

					for (Element e1 : N) { // for문
						String url2 = e1.getElementsByAttribute("href").attr("href");
						namae = e1.text();

						if (url2.contains("http://")) { // "a"에서 만화의 링크를 골라내는 if

							path2 = path + "\\" + namae;
							File file2 = new File(path2);
							if (!file2.exists()) {
								file2.mkdirs();
							}
							comicList5.add(url2);
							namae5.add(namae);
							path9.add(path2);
							if (listNum % 4 == 0) {
								comicList1.add(url2);
								namae1.add(namae);
								path5.add(path2);
								listNum++;
							} else if (listNum % 4 == 1) {
								comicList2.add(url2);
								namae2.add(namae);
								path6.add(path2);
								listNum++;
							} else if (listNum % 4 == 2) {
								comicList3.add(url2);
								namae3.add(namae);
								path7.add(path2);
								listNum++;
							} else if (listNum % 4 == 3) {
								comicList4.add(url2);
								namae4.add(namae);
								path8.add(path2);
								listNum++;
							}

						} // if_A

					} // for문

					System.out.println("comicList1 = " + comicList1);
					System.out.println("comicList2 = " + comicList2);
					System.out.println("comicList3 = " + comicList3);
					System.out.println("comicList4 = " + comicList4);
					System.out.println("comicList5 = " + comicList5);

					confirmPassword confirmPwClass = new confirmPassword(comicList5);
					String confirmPw = confirmPwClass.confirmPW;
					System.out.println(confirmPw);

					switch (confirmPw) {
					case "be":
						downloader17 test1 = new downloader17(comicList5, path9, namae5, txtLog, "5번 Thread");
						Thread t5 = new Thread(test1);
						t5.start();
						break;
					case "not":
						downloader d1 = new downloader(comicList1, path5, namae1, txtLog, "1번 Thread");
						downloader d2 = new downloader(comicList2, path6, namae2, txtLog, "2번 Thread");
						downloader d3 = new downloader(comicList3, path7, namae3, txtLog, "3번 Thread");
						downloader d4 = new downloader(comicList4, path8, namae4, txtLog, "4번 Thread");

						Thread t1 = new Thread(d1);
						Thread t2 = new Thread(d2);
						Thread t3 = new Thread(d3);
						Thread t4 = new Thread(d4);

						t1.start();
						t2.start();
						t3.start();
						t4.start();
						break;
					}

				} catch (IOException e2) {
					System.out.println(e2.getMessage());
					txtLog.append(e2.getMessage() + "\n");
				}
			}

		};

		start.addActionListener(listener);

	}

	public static void main(String[] args) {
		new marumaruT();
	}

}