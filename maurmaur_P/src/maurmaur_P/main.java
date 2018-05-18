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

public class main extends JFrame {
//	UI에 나타날 글씨 Font
	Font font = new Font("돋움", Font.BOLD, 18);
//	Thread에 넘겨줄 url-List
	ArrayList<String> comicList1 = new ArrayList<String>();
	ArrayList<String> comicList2 = new ArrayList<String>();
	ArrayList<String> comicList3 = new ArrayList<String>();
	ArrayList<String> comicList4 = new ArrayList<String>();
	ArrayList<String> comicList5 = new ArrayList<String>();
//	Thread에 넘겨줄 파일 이름List
	ArrayList<String> namae1 = new ArrayList<String>();
	ArrayList<String> namae2 = new ArrayList<String>();
	ArrayList<String> namae3 = new ArrayList<String>();
	ArrayList<String> namae4 = new ArrayList<String>();
	ArrayList<String> namae5 = new ArrayList<String>();
//	Thread에 넘겨줄 저장경로List
	ArrayList<String> path5 = new ArrayList<String>();
	ArrayList<String> path6 = new ArrayList<String>();
	ArrayList<String> path7 = new ArrayList<String>();
	ArrayList<String> path8 = new ArrayList<String>();
	ArrayList<String> path9 = new ArrayList<String>();
//	for문을 통해 위 세 종류의 List를 초기화하기 위한 List
	ArrayList<ArrayList<String>> listList = new ArrayList<ArrayList<String>>() {
		{
			add(comicList1); add(comicList2); add(comicList3); add(comicList4); add(comicList5);
			add(namae1); add(namae2); add(namae3); add(namae4); add(namae5); add(path5);
			add(path6);	add(path7);	add(path8);	add(path9);
		}
	};
//	페이지에서 읽어낸 정보를 Thread에 나누기 위한 int 
	int listNum = 0;
//	전역변수 설정
	String path2, namae; //path2 = 저장될 파일의 경로, namae = 저장될 파일 이름

	main() {
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
		c.add(urlName); c.add(url);
		// 저장위치 입력란
		JLabel saveLocationName = new JLabel("저장 위치");
		saveLocationName.setBounds(10, 50, 100, 30);
		saveLocationName.setHorizontalAlignment(SwingConstants.CENTER);
		saveLocationName.setFont(font);
		JTextField saveLocation = new JTextField();
		saveLocation.setBounds(120, 50, 500, 30);
		c.add(saveLocationName); c.add(saveLocation);
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
		
//		시작 버튼 부분의 Actionlistener
		ActionListener listener = e -> {
//			한 번 작업을 끝내고 재시작 할 때, 처음에 저장했던 ArrayList들을 초기화
			for (int q = 0; q < listList.size(); q++) {
				listList.get(q).clear();
			}
			// 저장위치에 폴더가 없으면 만들기
			String path = saveLocation.getText();
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
//			url에 장시시가 있는 경우
			if (url.getText().contains("zangsisi.net")) {
//				JSoup시작
				try {
					Document doc = Jsoup.connect(url.getText()).get();
//					세부링크(만화의 각 권(각 화))의 링크를 Elements로 추출
					Element element = doc.select("div#post").get(0);
					Elements N = element.select("a");
					int cNum = 0;
					for (Element e1 : N) {
//						처음에 읽어지는 title을 넘어가기 위한 if
						if(cNum == 0) {
							cNum++;
							continue;
						}
						String url2 = e1.getElementsByAttribute("href").attr("href");
						namae = e1.text();
						path2 = path + "\\" + namae;
						File file2 = new File(path2);
						if (!file2.exists()) {
							file2.mkdirs();
						}
//						"url,파일이름,파일경로"를 각각의 쓰레드에 넘겨주기 위해 ArrayList에 나눠서 할당
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
//					Thread 생성과 실행
					downloaderZS zSS1 = new downloaderZS(comicList1, path5, namae1, txtLog, "1번 Thread");
					downloaderZS zSS2 = new downloaderZS(comicList2, path6, namae2, txtLog, "2번 Thread");
					downloaderZS zSS3 = new downloaderZS(comicList3, path7, namae3, txtLog, "3번 Thread");
					downloaderZS zSS4 = new downloaderZS(comicList4, path8, namae4, txtLog, "4번 Thread");
					
					Thread t6 = new Thread(zSS1); Thread t7 = new Thread(zSS2);
					Thread t8 = new Thread(zSS3); Thread t9 = new Thread(zSS4);
					t6.start();	t7.start();
					t8.start();	t9.start();
				} catch (Exception ez) {
					ez.printStackTrace();
				}

			} else {
//				url에 장시시가 없다면 마루마루로 판단해서 실행
				try {
					Document doc = Jsoup.connect(url.getText()).get();
//					세부링크(만화의 각 권(각 화))의 링크를 Elements로 추출
					Element element = doc.select("div.content").get(0);
					Elements N = element.select("a");
					for (Element e1 : N) {
						String url2 = e1.getElementsByAttribute("href").attr("href");
						namae = e1.text();
//						추출된 하이퍼링크 중 만화가 들어있는 url만 선별하는 if 
						if (url2.contains("http://")) {
							path2 = path + "\\" + namae;
							File file2 = new File(path2);
							if (!file2.exists()) {
								file2.mkdirs();
							}
//							selenium을 사용할 경우 사용할 ArrayList에 "url,파일이름,파일경로" 추가(한 개의 Thread)
							comicList5.add(url2);
							namae5.add(namae);
							path9.add(path2);
//							"url,파일이름,파일경로"를 각각의 쓰레드에 넘겨주기 위해 ArrayList에 나눠서 할당
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
					}
//					Password가 있는지 확인하는 Class
					confirmPassword confirmPwClass = new confirmPassword(comicList5);
					String confirmPw = confirmPwClass.confirmPW;
//					Password가 있는 경우(be)와 없는 경우(not) 서로 다른 download Thread 실행
					switch (confirmPw) {
					case "be":
						downloader17 test1 = new downloader17(comicList5, path9, namae5, txtLog, "5번 Thread");
						Thread t5 = new Thread(test1);
						t5.start();
						break;
					case "not":
						downloaderM d1 = new downloaderM(comicList1, path5, namae1, txtLog, "1번 Thread");
						downloaderM d2 = new downloaderM(comicList2, path6, namae2, txtLog, "2번 Thread");
						downloaderM d3 = new downloaderM(comicList3, path7, namae3, txtLog, "3번 Thread");
						downloaderM d4 = new downloaderM(comicList4, path8, namae4, txtLog, "4번 Thread");

						Thread t1 = new Thread(d1);	Thread t2 = new Thread(d2);
						Thread t3 = new Thread(d3);	Thread t4 = new Thread(d4);
						
						t1.start();	t2.start();
						t3.start();	t4.start();
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
//		메인 시작
		new main();
	}

}