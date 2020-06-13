package cds;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMServerEventHandler implements CMAppEventHandler {

	private serverMain m_server = null;
	private CMServerStub m_serverStub = null;
	
	private Vector<File> s_pdf; // Ÿ�� ���� �ʿ�
	private Vector<String> s_user; // �� pdf �� ������ ���
	private Vector<Vector<Vector<String>>> s_content; // �� pdf �� �������� ���
	
	public CMServerEventHandler(serverMain s, CMServerStub ss) {
		// TODO Auto-generated constructor stub
		m_server = s;
		m_serverStub = ss;
		
		s_pdf = new Vector<File>();
		s_user = new Vector<String>();
		s_content = new Vector<Vector<Vector<String>>>();
	}
	
	@Override
	public void processEvent(CMEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("������ �̺�Ʈ �߻� : "+ arg0.getType());
		
		switch(arg0.getType()) { // �̺�Ʈ ó��
		case CMInfo.CM_DUMMY_EVENT:
			dummyEvent(arg0);
			break;
		case CMInfo.CM_SESSION_EVENT:
			sessionEvent(arg0);
			break;
		case CMInfo.CM_FILE_EVENT:
			fileEvent(arg0);
			break;
		}
	}
	
	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e;
		System.out.println("**** [DummyEvent] ���� �� ���� �޽��� : "+de.getDummyInfo()); // ���� �޽��� Ȯ��

		// ���� ���� �׽�Ʈ ��
//		String p = "./server-file-path/";
//		pushFile(p+"test.pdf", de.getSender());
		
		List<String> fileList = new ArrayList<>();
		fileList.add("file1");
		fileList.add("file2");
		fileList.add("fil3");
		
		if (de.getDummyInfo().equals("FileListRequest")) {
			CMDummyEvent nde = new CMDummyEvent();
			nde.setID(1);
			StringBuilder msg = new StringBuilder();
			for(String file : fileList) {
				msg.append(file + "#");
			}
			System.out.println("****** [FILELIST EVENT MSG] *******");
			System.out.println("MSG : msg");
			
			nde.setDummyInfo(msg.toString());
			m_serverStub.send(nde, de.getSender());
		}
		
		/*
		 		CMDummyEvent nde = new CMDummyEvent();
		StringBuilder sb = new StringBuilder();
		
		switch(de.getID()) {
		case 1: // ���� ������ ���ϸ���Ʈ ��ȯ
			nde.setID(1);

			for (File pdf : s_pdf) { // ��� ������ �̸��� ���ڿ��� ��ȯ
				System.out.println(pdf);
				sb.append(pdf.getName() + "#");
			}
//			sb.append("abc");
			System.out.println("****�����");

			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // �޽��� ����
			System.out.println("******************** [�����̺�Ʈ] ����-->Ŭ���̾�Ʈ : ���ϸ���Ʈ �޽��� ���� �Ϸ�");
			break;
		case 2: // ����� �̸� ��ȯ
			nde.setID(2);
			
			for (String user : s_user) { // ��� ������ �̸��� ���ڿ��� ��ȯ
				sb.append(user + "#");
			}
			
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // �޽��� ����
			System.out.println("******************** [�����̺�Ʈ] ����-->Ŭ���̾�Ʈ : ���� ����Ʈ ���� �Ϸ�");
			break;
		case 3: // �ش� �������� ��ϵ� �ּ�����
			nde.setID(3);
			String[] wd = de.getDummyInfo().split("#");
			int f = Integer.parseInt(wd[0]),
				u = Integer.parseInt(wd[1]),
				p = Integer.parseInt(wd[2]);
			sb.append(s_content.get(f).get(u).get(p));
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // �޽��� ����
			System.out.println("******************** [�����̺�Ʈ] ����-->Ŭ���̾�Ʈ : ������ �ּ� ���� �Ϸ�");
			break;
		default:
			System.out.println("******** [DummyEvent] Can't Find to Do");
		}
		 */

	}
	
	public void sessionEvent(CMEvent e) {
		CMSessionEvent se = (CMSessionEvent) e;
		System.out.println("SessionEvent �߻� : "+ se.getID());
		switch (se.getID()) {
		case CMSessionEvent.LOGIN:			
			CMDummyEvent nde = new CMDummyEvent();
			StringBuilder sb = new StringBuilder();
			nde.setID(2);
			for (String user : s_user) { // ��� ������ �̸��� ���ڿ��� ��ȯ
				sb.append(user + "#");
			}
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, se.getSender()); // �޽��� ����
			System.out.println("******************** [�α���] ����-->Ŭ���̾�Ʈ : ���� ����Ʈ ���� �Ϸ�");

			if(!s_user.contains(se.getSender())) { // ����� �̸� �߰�
				s_user.add(se.getSender());
				CMDummyEvent nde2 = new CMDummyEvent();
				nde2.setID(4); // ���ο� �ο��� �߰���
				nde2.setDummyInfo(se.getSender());
				m_serverStub.broadcast(nde2); // �޽��� ����
				System.out.println("******************** [�α���] �ű� ���� ���� ����");
				
			}

			
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + se.getID());
		}
	}
	
	// Ŭ���̾�Ʈ�� ���Ͽ�û�� ���� �ش� ������ ã��
	public void cFileRequest(String find) {
		// �ش� ������ ã�Ƽ� Ŭ���̾�Ʈ���� �������ִ� ����..
		// Ŭ���̾�Ʈ �� UI �� ������ �ɵ� ( �ε����� ã��, �̸����� ã��.. )
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent de = (CMFileEvent) e;
		System.out.println("**** ���� �� ���� �޽��� : ���� ��û �޽���"); // ���� �޽��� Ȯ��

		CMFileEvent fe = new CMFileEvent();
		fe.setID(fe.REPLY_PERMIT_PULL_FILE);
//		fe.setFilePath("test.pdf");
		System.out.println("**** ���� ----> Ŭ���̾�Ʈ : "+fe.getFilePath());
		m_serverStub.send(fe, de.getSender()); // ��û������ ����
	}
	
	// ���ϰ��� �Լ�
	public void setFilePath()
	{
		// ���� ��� ���� 
		String strPath = null;

		strPath = JOptionPane.showInputDialog("file path: ");
		if(strPath == null)
		{
			return;
		}
		
		m_serverStub.setTransferedFileHome(Paths.get(strPath));
		
	}

	public void pushFile(String f, String who) // ���� Ǫ��
	{
		String strFilePath = null;
		File files;
		byte byteFileAppendMode = -1;
		boolean bReturn = false;
		
		byteFileAppendMode = CMInfo.FILE_DEFAULT;

		/*
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		CMConfigurationInfo confInfo = m_serverStub.getCMInfo().getConfigurationInfo();
		File curDir = new File(confInfo.getTransferedFileHome().toString());
		fc.setCurrentDirectory(curDir);
		int fcRet = fc.showOpenDialog(this);
		if(fcRet != JFileChooser.APPROVE_OPTION) return;
		files = fc.getSelectedFiles(); // ���⼭ ���� ���� �����ε�..
		 */
		
		files = new File(f);
		
		strFilePath = files.getPath();
		bReturn = m_serverStub.pushFile(strFilePath, who, byteFileAppendMode);
		if (!bReturn) {
			System.out.println("*************** ���� ���� ����! **************");
		}
	}
	
	private void processSessionEvent(CMEvent cme) {
		CMSessionEvent se = (CMSessionEvent) cme;
		switch(se.getID()) 
		{
		case CMSessionEvent.LOGIN:
			System.out.println(se.getUserName() + " requested login");
			break;
		default:
			return;
		}
	}
}
