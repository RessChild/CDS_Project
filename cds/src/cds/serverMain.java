package cds;

import java.io.File;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import kr.ac.konkuk.ccslab.cm.entity.CMList;
import kr.ac.konkuk.ccslab.cm.entity.CMRecvFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMSendFileInfo;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class serverMain {
	private CMServerEventHandler m_eventHandler;
	private CMServerStub m_serverStub;
	
	private Vector<File> s_pdf; // Ÿ�� ���� �ʿ�
	private Vector<Vector<String>> s_user; // �� pdf �� ������ ���
	private Vector<Vector<Vector<String>>> s_content; // �� pdf �� �������� ���
	
	public serverMain() { // ��ü ����
		m_serverStub = new CMServerStub();
		m_eventHandler = new CMServerEventHandler(this, m_serverStub);
	}
	
	public static void main(String[] args) {
		serverMain server = new serverMain();
		server.m_serverStub.setAppEventHandler(server.m_eventHandler);
		
		server.m_serverStub.startCM(); // ���� ������ ����
		System.out.println("Server ���� ��");
		
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

	public void pushFile() // ���� Ǫ��
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
		
		files = new File("./test.pdf");
		
		strFilePath = files.getPath();
		bReturn = m_serverStub.pushFile(strFilePath, "���� ��� �̸�", byteFileAppendMode);
		if (!bReturn) {
			System.out.println("*************** ���� ���� ����! **************");
		}
	}
}
