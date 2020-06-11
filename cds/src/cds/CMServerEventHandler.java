package cds;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

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
	
	public CMServerEventHandler(serverMain s, CMServerStub ss) {
		// TODO Auto-generated constructor stub
		m_server = s;
		m_serverStub = ss;
	}
	
	@Override
	public void processEvent(CMEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getType()) { // 이벤트 처리
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
		System.out.println("**** [DummyEvent] 서버 측 수신 메시지 : "+de.getDummyInfo()); // 얻은 메시지 확인

		/*
		CMDummyEvent nde = new CMDummyEvent(); // 더미이벤트 만들기
		// nde.setID(0); 로 아이디 세팅하고, 나중에 구분하기..

		nde.setDummyInfo("내용 채우기~~~"); // 문자열로 더미이벤트 생성
		System.out.println("**** 서버 ----> 클라이언트 : "+ nde.getDummyInfo());
		m_serverStub.send(nde, de.getSender()); // 요청자한테 전송
		*/
		
		// 파일 전송 테스트 중
		String p = "./server-file-path/";
		pushFile(p+"test.pdf", de.getSender());
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent de = (CMFileEvent) e;
		System.out.println("**** 서버 측 수신 메시지 : 파일 요청 메시지"); // 얻은 메시지 확인


		CMFileEvent fe = new CMFileEvent();
		fe.setID(fe.REPLY_PERMIT_PULL_FILE);
//		fe.setFilePath("test.pdf");
		System.out.println("**** 서버 ----> 클라이언트 : "+fe.getFilePath());
		m_serverStub.send(fe, de.getSender()); // 요청자한테 전송
	}
	
	// 파일관련 함수
	public void setFilePath()
	{
		// 파일 경로 세팅 
		String strPath = null;

		strPath = JOptionPane.showInputDialog("file path: ");
		if(strPath == null)
		{
			return;
		}
		
		m_serverStub.setTransferedFileHome(Paths.get(strPath));
		
	}

	public void pushFile(String f, String who) // 파일 푸시
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
		files = fc.getSelectedFiles(); // 여기서 파일 선택 구문인데..
		 */
		
		files = new File(f);
		
		strFilePath = files.getPath();
		bReturn = m_serverStub.pushFile(strFilePath, who, byteFileAppendMode);
		if (!bReturn) {
			System.out.println("*************** 파일 전송 에러! **************");
		}
	}
}
