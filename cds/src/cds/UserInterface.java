package cds;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserInterface extends JFrame implements ActionListener, ListSelectionListener{
	int screenWidth;
	int screenHeight;
	JPanel filePanel, ButtonPanel,centerPanel,pdfPanel, userPanel;
	Container frame;
	JButton fileButton, nextBtn, preBtn, commentBtn;
	JList userList;
	JLabel img;
	JLabel fileLabel;
	JTextArea note, pdf;//일단 실제 pdf대신 textArea로 놓는다
	
	UserInterface(String title) {
		setTitle(title);
		Toolkit kit = this.getToolkit();//시스템 정보를 가져옴, AWT에 있다.
		Dimension screenSize= kit.getScreenSize();//반환값이 Dimension(폭과 높이정보를 가진 하나의 타입)
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		//근데 창의 크기를 내 모니터에 상대적인 값으로 할수는 없을까?
		this.setSize(screenWidth/2, screenHeight);
		this.setLocation(100, 100);// 창을 어디에 생성되게 할것인가?
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		this.setVisible(true);
		//여기까지가 창 만들기.
	}
	public void fileSwing() {
		filePanel = new JPanel(new BorderLayout());
		fileButton = new JButton("파일 선택");
		fileLabel = new JLabel();
		filePanel.add(fileLabel, BorderLayout.WEST);
		filePanel.add(fileButton, BorderLayout.EAST);
		frame.add(filePanel, BorderLayout.NORTH);
		fileButton.addActionListener(this);
	}
	public void pdfSwing() {
		pdfPanel = new JPanel(new BorderLayout());
		ButtonPanel = new JPanel(new FlowLayout());
		preBtn = new JButton("이전");
		nextBtn = new JButton("다음");
		ButtonPanel.add(preBtn);
		ButtonPanel.add(nextBtn);
		pdfPanel.add(ButtonPanel, BorderLayout.CENTER);
		note = new JTextArea("COMMENT", 10, 40);
		pdf = new JTextArea("PDF", 36, 40);
		pdfPanel.add(pdf, BorderLayout.NORTH);
		pdfPanel.add(note, BorderLayout.SOUTH);
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
		commentBtn.addActionListener(this);
		userPanel.add(commentBtn, BorderLayout.SOUTH);
		frame.add(userPanel);
	}
	public void init() {
		frame = this.getContentPane();
		frame.setLayout(new BorderLayout());
		fileSwing();
		pdfSwing();
		userSwing();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == fileButton) {
			//아무곳에나 있는 그림 파일을 불러 올수 있다.
			JFileChooser filedlg = new JFileChooser();
			int result = filedlg.showOpenDialog(this);//int를 반환, ok or cancel
			if(result == JFileChooser.APPROVE_OPTION) {
				//사용자가 열기에 해당하는 버튼을 눌렀다면.
				File file = filedlg.getSelectedFile();
				String path = file.getPath();
				fileLabel.setText(path);
				
				//pdf를 띄우는 부분
				
			}
		}else if(e.getSource() == preBtn) {
			//pdf다음페이지 
		}else if(e.getSource() == nextBtn) {
			//pdf 이전 페이지
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
