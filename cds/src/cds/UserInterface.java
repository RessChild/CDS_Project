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
	JTextArea note;//�ϴ� ���� pdf��� textArea�� ���´�
	JLabel pdf;
	FileDialog dialog;
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	// ����ڰ� �ҷ��� PDF ������ �� �̹��� �� ���� ���� �����ϰ� �ִ� ��ü
	Pdf currPDF;
	String selectedFile;
	
	UserInterface(String title, CMClientStub m_clientStub,
	   CMClientEventHandler m_eventHandler) {
		setTitle(title);
		this.m_clientStub = m_clientStub;
		this.m_eventHandler = m_eventHandler;
		Toolkit kit = this.getToolkit();//�ý��� ������ ������, AWT�� �ִ�.
		Dimension screenSize= kit.getScreenSize();//��ȯ���� Dimension(���� ���������� ���� �ϳ��� Ÿ��)
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		this.setLocation(100, 100);// â�� ��� �����ǰ� �Ұ��ΰ�?
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ���� �г� ��� �ʱ�ȭ
		init();
		
		pack();
		this.setVisible(true);
		//��������� â �����.
	}
	
	public void fileSwing() {
		filePanel = new JPanel(new BorderLayout());
		fileButtonPanel = new JPanel(new FlowLayout());
		ServerFileButton = new JButton("�������� ���� ����");
		LocalFileButton = new JButton("���ÿ��� ���� ����");
		fileLabel = new JLabel();
		fileButtonPanel.add(LocalFileButton);
		fileButtonPanel.add(ServerFileButton);
		filePanel.add(fileLabel, BorderLayout.WEST);
		filePanel.add(fileButtonPanel, BorderLayout.EAST);
		
		frame.add(filePanel, BorderLayout.NORTH);		
	}
	
	public void pdfSwing() {
		pdfPanel = new JPanel(new GridBagLayout());
		
		// PDF �г� �κ� ũ�� �����ؾ�
		// PDF �̹��� ũ�� ���� â ũ�� �Դٰ��� ���� ��
		pdfPanel.setPreferredSize(new Dimension(1024, this.screenHeight));
		
		// GridBagLayout Cell ���� �Ӽ� �����ϴ� ���� �ʱ�ȭ
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		for(int i = 0; i < 3; i++) {
			gbc[i] = new GridBagConstraints();
		}
		
		// PDF �����ִ� �г� ����
		pdf = new JLabel();
		pdfImgPanel = new JScrollPane(pdf);
		
		//PDF �г��� ���� ���� ��ġ
		gbc[0].gridx = 0;
		gbc[0].gridy = 0;
		gbc[0].fill = GridBagConstraints.BOTH;
		gbc[0].weightx = 1;
		gbc[0].weighty = 0.6;
		
		
		// PDF ������ �Ѱ��ִ� ��ư �г� ����
		preBtn = new JButton("����");
		nextBtn = new JButton("����");
		
		ButtonPanel = new JPanel(new FlowLayout());
		ButtonPanel.add(preBtn);
		ButtonPanel.add(nextBtn);
		gbc[1].gridx = 0;
		gbc[1].gridy = 1;
		
		// �ּ� �г� ����
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
		//users ����Ʈ�� ���� ����ڸ� �ִ� ����
		userList = new JList(users);
		
		userList.addListSelectionListener(this);
		userPanel.add(userList, BorderLayout.NORTH);

		commentBtn = new JButton("�ּ��ޱ�");
		userPanel.add(commentBtn, BorderLayout.SOUTH);
		frame.add(userPanel, BorderLayout.EAST);
	}
	
	public void init() {
		frame = this.getContentPane();
		frame.setLayout(new BorderLayout());
		
		fileSwing();
		
		pdfSwing();
		
		userSwing();
		
		// ��ư Ŭ�� ������ �߰�
		LocalFileButton.addActionListener(this);
		ServerFileButton.addActionListener(this);
		preBtn.addActionListener(this);
		nextBtn.addActionListener(this);
		commentBtn.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == LocalFileButton) {
			//������ pdf ������ �ҷ� �ü� �ִ�.
			JFileChooser filedlg = new JFileChooser();
			int result = filedlg.showOpenDialog(this);//int�� ��ȯ, ok or cancel
			if(result == JFileChooser.APPROVE_OPTION) {
				//����ڰ� ���⿡ �ش��ϴ� ��ư�� �����ٸ�.
				File file = filedlg.getSelectedFile();
				String path = file.getPath();
				fileLabel.setText(path);
				
				//pdf�� ���� �κ�
				currPDF = new Pdf(path);
				pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			}
		}else if(e.getSource() == ServerFileButton) {
			//�������� pdf������ �ҷ���
			CMDummyEvent due = new CMDummyEvent();
			due.setDummyInfo("FileListRequest");
			due.setID(1);
			this.m_clientStub.send(due, "SERVER");
		} else if(e.getSource() == preBtn) {
			//pdf ���� ������ 
			currPDF.decrementPageNum();
			pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			
		}else if(e.getSource() == nextBtn) {
			//pdf ���� ������
			currPDF.incrementPageNum();
			pdf.setIcon(new ImageIcon(currPDF.getCurrPageImage()));
			
		}else if(e.getSource() == commentBtn) {
			//�ּ��ޱ� ��ư
			
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(!e.getValueIsAdjusting()) {
			//Ư�� ����ڸ� �����ϸ� �ش� ����ڰ� �ּ��� ���� �κ��� �����ش�.
			//�ּ��� ��� �����ϴ��� ���� �ϴ� ����ڰ� �����ϸ� �ش� ������̸��� �ֿܼ� ���
			System.out.println(userList.getSelectedValue());
		}
	}
	
}
