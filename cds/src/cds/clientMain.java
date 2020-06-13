package cds;

import java.io.File;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import kr.ac.konkuk.ccslab.cm.entity.CMList;
import kr.ac.konkuk.ccslab.cm.entity.CMRecvFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMSendFileInfo;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class clientMain {
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	String userName;
	UserInterface UI;
	public clientMain() { // ��ü ����
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientEventHandler(this, m_clientStub);
		UI = new UserInterface("test", m_clientStub, m_eventHandler);
	}
	
	public static void main(String[] args) {
		clientMain client = new clientMain();

		// CM Stub �� �̺�Ʈ �ڵ鷯 ���
		client.m_clientStub.setAppEventHandler(client.m_eventHandler);
		
		client.getLoginInfo();
		
		client.m_clientStub.startCM(); // ����
		
		if (client.userName != null) {
			client.m_clientStub.loginCM(client.userName, "");
		}
				
		// CMDummyEvent ��ü�� ���� ����
		// �޽����� ������ �����°� Stub ���� cast �Լ��� ���� ����
		// �̶�, ���� ������ �ʿ��ѵ�, �̰� CM ���� info ��ü�� CMUser �� ��ü�� �޾� �� �� ����
		
		//client.dummyEvent("message");
		
		System.out.println("Ŭ���̾�Ʈ ���� ��");
	}
	
	public void showFileList(String[] fileList) {
		UI.dialog = new FileDialog(UI, fileList);
	}
	
	public void dummyEvent(String message) { // �����̺�Ʈ ���� �� ���� �Լ�
		CMDummyEvent due = new CMDummyEvent();
		// due.setDummyInfo("������ �����ϴ� �޽���");
		// due.setID(0);
		due.setDummyInfo("FileListRequest");
		due.setID(1);
		
		System.out.println("**** [DummyEvent] Ŭ���̾�Ʈ ----> ����  : " + due.getDummyInfo());
		//m_clientStub.send(due,"SERVER"); // ������ ����	
	}

	public void fileEvent() { // �����̺�Ʈ ���� �� ���� �Լ�
		CMFileEvent fe = new CMFileEvent();
		fe.setID(fe.REQUEST_PERMIT_PULL_FILE);
		
		System.out.println("**** Ŭ���̾�Ʈ ----> ���� : file ������ ���� �Ϸ� ");
		m_clientStub.send(fe,"SERVER"); // ������ ����	
	}

	public void testSetFilePath()
	{
		String strPath = null;
		
		strPath = ".";
		m_clientStub.setTransferedFileHome(Paths.get(strPath));
	}
	
	public void getLoginInfo() {
		
		JTextField userNameField = new JTextField();
		Object[] message = {
				"User Name:", userNameField
		};
		int option = JOptionPane.showConfirmDialog(null, message, "Login Input", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			userName = userNameField.getText();
		}
		
	}
    /*
	public void testPushFile()
	{
		String strFilePath = null;
		File[] files = null;
		String strReceiver = null;
		byte byteFileAppendMode = -1;
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		boolean bReturn = false;

		System.out.println("====== push a file\n");
		

		JTextField freceiverField = new JTextField();
		String[] fAppendMode = {"Default", "Overwrite", "Append"};		
		JComboBox<String> fAppendBox = new JComboBox<String>(fAppendMode);

		Object[] message = { 
				"File Receiver(empty for default server): ", freceiverField,
				"File Append Mode: ", fAppendBox 
				};
		int option = JOptionPane.showConfirmDialog(null, message, "File Push", JOptionPane.OK_CANCEL_OPTION);
		if(option == JOptionPane.CANCEL_OPTION || option != JOptionPane.OK_OPTION)
		{
			printMessage("canceled.\n");
			return;
		}
		
		strReceiver = freceiverField.getText().trim();
		if(strReceiver.isEmpty())
			strReceiver = interInfo.getDefaultServerInfo().getServerName();
		
		switch(fAppendBox.getSelectedIndex())
		{
		case 0:
			byteFileAppendMode = CMInfo.FILE_DEFAULT;
			break;
		case 1:
			byteFileAppendMode = CMInfo.FILE_OVERWRITE;
			break;
		case 2:
			byteFileAppendMode = CMInfo.FILE_APPEND;
			break;			
		}
		
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		CMConfigurationInfo confInfo = m_clientStub.getCMInfo().getConfigurationInfo();
		File curDir = new File(confInfo.getTransferedFileHome().toString());
		fc.setCurrentDirectory(curDir);
		int fcRet = fc.showOpenDialog(this);
		if(fcRet != JFileChooser.APPROVE_OPTION) return;
		files = fc.getSelectedFiles();
		if(files.length < 1) return;
		for(int i=0; i < files.length; i++)
		{
			strFilePath = files[i].getPath();
			bReturn = m_clientStub.pushFile(strFilePath, strReceiver, byteFileAppendMode);
			if(!bReturn)
			{
				printMessage("push file error! file("+strFilePath+"), receiver("
						+strReceiver+")\n");
			}
		}
	}
	*/

}
