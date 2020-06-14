package cds;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class UserInterface extends JFrame implements ActionListener, ListSelectionListener{
	int screenWidth;
	int screenHeight;
	JPanel filePanel, fileButtonPanel, ButtonPanel,centerPanel,pdfPanel, userPanel;
	JScrollPane pdfImgPanel, noteJSP;
	Container frame;
	JButton ServerFileButton, LocalFileButton, nextBtn, preBtn, commentBtn;
	JList userList;
	JLabel fileLabel;
	JTextArea note;//일단 실제 pdf대신 textArea로 놓는다
	JLabel pdf;
	FileDialog dialog;
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	// 사용자가 불러온 PDF 페이지 별 이미지 및 파일 정보 저장하고 있는 객체
	Pdf currPDF;
	String selectedFile;
	
	UserInterface(String title, CMClientStub m_clientStub,
	   CMClientEventHandler m_eventHandler) {
		setTitle(title);
		this.m_clientStub = m_clientStub;
		this.m_eventHandler = m_eventHandler;
		Toolkit kit = this.getToolkit();//시스템 정보를 가져옴, AWT에 있다.
		Dimension screenSize= kit.getScreenSize();//반환값이 Dimension(폭과 높이정보를 가진 하나의 타입)
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		this.setLocation(100, 100);// 창을 어디에 생성되게 할것인가?
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 구성 패널 모두 초기화
		init();
		
		pack();
		this.setVisible(true);
		//여기까지가 창 만들기.
	}
	
	public void fileSwing() {
		filePanel = new JPanel(new BorderLayout());
		fileButtonPanel = new JPanel(new FlowLayout());
		ServerFileButton = new JButton("서버에서 파일 선택");
		LocalFileButton = new JButton("로컬에서 파일 선택");
		fileLabel = new JLabel();
		fileButtonPanel.add(LocalFileButton);
		fileButtonPanel.add(ServerFileButton);
		filePanel.add(fileLabel, BorderLayout.WEST);
		filePanel.add(fileButtonPanel, BorderLayout.EAST);
		
		frame.add(filePanel, BorderLayout.NORTH);		
	}
	
	public void pdfSwing() {
		pdfPanel = new JPanel(new GridBagLayout());
		
		// PDF 패널 부분 크기 고정해야
		// PDF 이미지 크기 따라서 창 크기 왔다갔다 하지 않
		pdfPanel.setPreferredSize(new Dimension(1024, this.screenHeight));
		
		// GridBagLayout Cell 별로 속성 정의하는 변수 초기화
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		for(int i = 0; i < 3; i++) {
			gbc[i] = new GridBagConstraints();
		}
		
		// PDF 보여주는 패널 구성
		pdf = new JLabel();
		pdfImgPanel = new JScrollPane(pdf);
		
		//PDF 패널이 가장 위에 위치
		gbc[0].gridx = 0;
		gbc[0].gridy = 0;
		gbc[0].fill = GridBagConstraints.BOTH;
		gbc[0].weightx = 1;
		gbc[0].weighty = 0.6;
		
		
		// PDF 페이지 넘겨주는 버튼 패널 구성
		preBtn = new JButton("이전");
		nextBtn = new JButton("다음");
		
		ButtonPanel = new JPanel(new FlowLayout());
		ButtonPanel.add(preBtn);
		ButtonPanel.add(nextBtn);
		gbc[1].gridx = 0;
		gbc[1].gridy = 1;
		
		// 주석 패널 구성
		note = new JTextArea("COMMENT", 10, 40);
		noteJSP = new JScrollPane(note, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		gbc[2].gridx = 0;
		gbc[2].gridy = 2;
		gbc[2].fill = GridBagConstraints.BOTH;
		gbc[2].weighty = 0.2;
		
		
		pdfPanel.add(pdfImgPanel, gbc[0]);
		pdfPanel.add(ButtonPanel, gbc[1]);
		pdfPanel.add(noteJSP, gbc[2]);
		
		frame.add(pdfPanel, BorderLayout.WEST);
	}
	
	public void userSwing() {
		userPanel = new JPanel(new BorderLayout());
		String[] users = {"user1", "user2", "user3"};
		//users 리스트에 현재 사용자를 넣는 과정
		userList = new JList(users);
		
		userList.addListSelectionListener(this);
		userPanel.add(userList, BorderLayout.NORTH);

		commentBtn = new JButton("주석달기");
		userPanel.add(commentBtn, BorderLayout.SOUTH);
		frame.add(userPanel, BorderLayout.EAST);
	}
	
	public void init() {
		frame = this.getContentPane();
		frame.setLayout(new BorderLayout());
		
		fileSwing();
		
		pdfSwing();
		
		userSwing();
		
		// 버튼 클릭 리스너 추가
		LocalFileButton.addActionListener(this);
		ServerFileButton.addActionListener(this);
		preBtn.addActionListener(this);
		nextBtn.addActionListener(this);
		commentBtn.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == LocalFileButton) {
			//로컬의 pdf 파일을 불러 올수 있다.
			JFileChooser filedlg = new JFileChooser();
			int result = filedlg.showOpenDialog(this);//int를 반환, ok or cancel
			if(result == JFileChooser.APPROVE_OPTION) {
				//사용자가 열기에 해당하는 버튼을 눌렀다면.
				File file = filedlg.getSelectedFile();
				String path = file.getPath();
				fileLabel.setText(path);
				
				//pdf를 띄우는 부분
				currPDF = new Pdf(path);
				pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			}
		}else if(e.getSource() == ServerFileButton) {
			//서버에서 pdf파일을 불러옴
			CMDummyEvent due = new CMDummyEvent();
			due.setDummyInfo("FileListRequest");
			due.setID(1);
			this.m_clientStub.send(due, "SERVER");
		} else if(e.getSource() == preBtn) {
			//pdf 이전 페이지 
			currPDF.decrementPageNum();
			pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			
		}else if(e.getSource() == nextBtn) {
			//pdf 다음 페이지
			currPDF.incrementPageNum();
			pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			
		}else if(e.getSource() == commentBtn) {
			//주석달기 버튼
			
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(!e.getValueIsAdjusting()) {
			//특정 사용자를 선택하면 해당 사용자가 주석을 달은 부분을 보여준다.
			//주석을 어디에 저장하는지 몰라서 일단 사용자가 선택하면 해당 사용자이름을 콘솔에 띄움
			System.out.println(userList.getSelectedValue());
		}
	}
	
}
