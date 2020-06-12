package cds;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Vector;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMServerEventHandler implements CMAppEventHandler {

	private serverMain m_server = null;
	private CMServerStub m_serverStub = null;
	
	private Vector<String> s_fname; // ���ϸ�
	private Vector<File> s_pdf; // Ÿ�� ���� �ʿ�
	private Vector<Vector<String>> s_user; // �� pdf �� ������ ���
	private Vector<Vector<Vector<String>>> s_content; // �� pdf �� �������� ���
	
	public CMServerEventHandler(serverMain s, CMServerStub ss) {
		// TODO Auto-generated constructor stub
		m_server = s;
		m_serverStub = ss;
	}
	
	@Override
	public void processEvent(CMEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getType()) { // �̺�Ʈ ó��
		case CMInfo.CM_DUMMY_EVENT:
			dummyEvent(arg0);
			break;
		case CMInfo.CM_SESSION_EVENT:
			break;
		case CMInfo.CM_FILE_EVENT:
			fileEvent(arg0);
			break;
		}
	}
	
	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e;
		System.out.println("**** [DummyEvent] ���� �� ���� �޽��� : "+de.getDummyInfo()); // ���� �޽��� Ȯ��

		/*
		CMDummyEvent nde = new CMDummyEvent(); // �����̺�Ʈ �����
		// nde.setID(0); �� ���̵� �����ϰ�, ���߿� �����ϱ�..

		nde.setDummyInfo("���� ä���~~~"); // ���ڿ��� �����̺�Ʈ ����
		System.out.println("**** ���� ----> Ŭ���̾�Ʈ : "+ nde.getDummyInfo());
		m_serverStub.send(nde, de.getSender()); // ��û������ ����
		*/
		
		// ���� ���� �׽�Ʈ ��
		String p = "./server-file-path/";
		pushFile(p+"test.pdf", de.getSender());
		
		switch(de.getID()) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		default:
			System.out.println("******** [DummyEvent] Can't Find to Do");
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
}
