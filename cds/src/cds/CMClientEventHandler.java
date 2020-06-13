package cds;
// CMAppEventHandler �������̽��� �����ϴ� �ڵ鷯

import java.awt.Desktop;
import java.awt.desktop.FilesEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMClientEventHandler implements CMAppEventHandler {

	private clientMain m_client = null;
	private CMClientStub m_clientStub = null;

	private File c_pdf = null; // Ÿ�� ���� �ʿ�
	private Vector<String> c_user = null; // pdf ������ ����
	private Vector<Vector<String>> c_content = null; // �� �������� Ÿ ������� ���
	private Vector<String> c_history = null; // ���� ������� �������� ���
	
	public CMClientEventHandler(clientMain c, CMClientStub cs) {
		// TODO Auto-generated constructor stub
		m_client = c;
		m_clientStub = cs;
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
			processFileEvent(arg0);
			//			fileEvent(arg0);
			break;
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // �̺�Ʈ ����ȯ
		System.out.println("**** Ŭ���̾�Ʈ �� ���� �޽��� (�ð�) : " + de.getDummyInfo()); // �޽��� ���
		
		switch(de.getID()) {
		case 0:
			break;
		case 1:
			String []fileList = de.getDummyInfo().split("#");
			for(String file: fileList) {
				System.out.print(file + ", ");
			}
			System.out.println();
			m_client.showFileList(fileList);
			break;
		case 2:
			break;
		default:
			System.out.println("******** [DummyEvent] Can't Find to Do");
		}
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent fe = (CMFileEvent) e;
		System.out.print("**** Ŭ���̾�Ʈ ���� ���� : " + fe.getFileBlock().toString());
	}
	
	
	// ���⼭ ���� ���ۿ�û�� ���� push �㰡 ������ �������� ��..
	private void processFileEvent(CMEvent cme)
	{
		CMFileEvent fe = (CMFileEvent) cme;
		CMConfigurationInfo confInfo = null;
		CMFileTransferInfo fInfo = m_clientStub.getCMInfo().getFileTransferInfo();
		int nOption = -1;
		long lTotalDelay = 0;
		long lTransferDelay = 0;
		
		switch(fe.getID())
		{
		case CMFileEvent.REQUEST_PERMIT_PUSH_FILE:
			StringBuffer strReqBuf = new StringBuffer(); 
			strReqBuf.append("["+fe.getFileSender()+"] wants to send a file.\n");
			strReqBuf.append("file path: "+fe.getFilePath()+"\n");
			strReqBuf.append("file size: "+fe.getFileSize()+"\n");
			System.out.println(strReqBuf.toString());
			
			m_clientStub.replyEvent(fe, 1); // 1�̸� ���� 0�̸� ����			
			break;
		}
		return;
	}
}
