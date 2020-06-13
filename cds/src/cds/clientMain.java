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
	public clientMain() { // 객체 생성
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientEventHandler(this, m_clientStub);
		UI = new UserInterface("test", m_clientStub, m_eventHandler);
	}
	
	public static void main(String[] args) {
		clientMain client = new clientMain();

		// CM Stub 에 이벤트 핸들러 등록
		client.m_clientStub.setAppEventHandler(client.m_eventHandler);
		
		client.getLoginInfo();
		
		client.m_clientStub.startCM(); // 실행
		
		if (client.userName != null) {
			client.m_clientStub.loginCM(client.userName, "");
		}
				
		// CMDummyEvent 객체를 만들어서 전송
		// 메시지를 실제로 보내는건 Stub 내의 cast 함수로 전송 가능
		// 이때, 본인 정보가 필요한데, 이건 CM 내에 info 객체로 CMUser 란 객체로 받아 알 수 있음
		
		//client.dummyEvent("message");
		
		System.out.println("클라이언트 실행 중");
	}
	
	public void showFileList(String[] fileList) {
		UI.dialog = new FileDialog(UI, fileList);
	}
	
	public void dummyEvent(String message) { // 더미이벤트 생성 및 전송 함수
		CMDummyEvent due = new CMDummyEvent();
		// due.setDummyInfo("서버로 전송하는 메시지");
		// due.setID(0);
		due.setDummyInfo("FileListRequest");
		due.setID(1);
		
		System.out.println("**** [DummyEvent] 클라이언트 ----> 서버  : " + due.getDummyInfo());
		//m_clientStub.send(due,"SERVER"); // 서버로 전송	
	}

	public void fileEvent() { // 더미이벤트 생성 및 전송 함수
		CMFileEvent fe = new CMFileEvent();
		fe.setID(fe.REQUEST_PERMIT_PULL_FILE);
		
		System.out.println("**** 클라이언트 ----> 서버 : file 데이터 전송 완료 ");
		m_clientStub.send(fe,"SERVER"); // 서버로 전송	
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
