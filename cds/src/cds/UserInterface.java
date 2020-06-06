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
	JTextArea note, pdf;//�ϴ� ���� pdf��� textArea�� ���´�
	
	UserInterface(String title) {
		setTitle(title);
		Toolkit kit = this.getToolkit();//�ý��� ������ ������, AWT�� �ִ�.
		Dimension screenSize= kit.getScreenSize();//��ȯ���� Dimension(���� ���������� ���� �ϳ��� Ÿ��)
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		//�ٵ� â�� ũ�⸦ �� ����Ϳ� ������� ������ �Ҽ��� ������?
		this.setSize(screenWidth/2, screenHeight);
		this.setLocation(100, 100);// â�� ��� �����ǰ� �Ұ��ΰ�?
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		this.setVisible(true);
		//��������� â �����.
	}
	public void fileSwing() {
		filePanel = new JPanel(new BorderLayout());
		fileButton = new JButton("���� ����");
		fileLabel = new JLabel();
		filePanel.add(fileLabel, BorderLayout.WEST);
		filePanel.add(fileButton, BorderLayout.EAST);
		frame.add(filePanel, BorderLayout.NORTH);
		fileButton.addActionListener(this);
	}
	public void pdfSwing() {
		pdfPanel = new JPanel(new BorderLayout());
		ButtonPanel = new JPanel(new FlowLayout());
		preBtn = new JButton("����");
		nextBtn = new JButton("����");
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
		//users ����Ʈ�� ���� ����ڸ� �ִ� ����
		userList = new JList(users);
		
		userList.addListSelectionListener(this);
		userPanel.add(userList, BorderLayout.NORTH);

		commentBtn = new JButton("�ּ��ޱ�");
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
			//�ƹ������� �ִ� �׸� ������ �ҷ� �ü� �ִ�.
			JFileChooser filedlg = new JFileChooser();
			int result = filedlg.showOpenDialog(this);//int�� ��ȯ, ok or cancel
			if(result == JFileChooser.APPROVE_OPTION) {
				//����ڰ� ���⿡ �ش��ϴ� ��ư�� �����ٸ�.
				File file = filedlg.getSelectedFile();
				String path = file.getPath();
				fileLabel.setText(path);
				
				//pdf�� ���� �κ�
				
			}
		}else if(e.getSource() == preBtn) {
			//pdf���������� 
		}else if(e.getSource() == nextBtn) {
			//pdf ���� ������
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
