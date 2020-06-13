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
	
	private Vector<File> s_pdf; // 타입 선언 필요
	private Vector<String> s_user; // 각 pdf 별 참여자 목록
	private Vector<Vector<Vector<String>>> s_content; // 각 pdf 내 참여자의 기록
	
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
		System.out.println("서버에 이벤트 발생 : "+ arg0.getType());
		
		switch(arg0.getType()) { // 이벤트 처리
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
		System.out.println("**** [DummyEvent] 서버 측 수신 메시지 : "+de.getDummyInfo()); // 얻은 메시지 확인

		// 파일 전송 테스트 중
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
		case 1: // 현재 보유한 파일리스트 반환
			nde.setID(1);

			for (File pdf : s_pdf) { // 모든 파일의 이름을 문자열로 변환
				System.out.println(pdf);
				sb.append(pdf.getName() + "#");
			}
//			sb.append("abc");
			System.out.println("****요까지");

			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // 메시지 전송
			System.out.println("******************** [더미이벤트] 서버-->클라이언트 : 파일리스트 메시지 전송 완료");
			break;
		case 2: // 사용자 이름 반환
			nde.setID(2);
			
			for (String user : s_user) { // 모든 파일의 이름을 문자열로 변환
				sb.append(user + "#");
			}
			
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // 메시지 전송
			System.out.println("******************** [더미이벤트] 서버-->클라이언트 : 유저 리스트 전송 완료");
			break;
		case 3: // 해당 페이지에 기록된 주석정보
			nde.setID(3);
			String[] wd = de.getDummyInfo().split("#");
			int f = Integer.parseInt(wd[0]),
				u = Integer.parseInt(wd[1]),
				p = Integer.parseInt(wd[2]);
			sb.append(s_content.get(f).get(u).get(p));
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, de.getSender()); // 메시지 전송
			System.out.println("******************** [더미이벤트] 서버-->클라이언트 : 페이지 주석 전송 완료");
			break;
		default:
			System.out.println("******** [DummyEvent] Can't Find to Do");
		}
		 */

	}
	
	public void sessionEvent(CMEvent e) {
		CMSessionEvent se = (CMSessionEvent) e;
		System.out.println("SessionEvent 발생 : "+ se.getID());
		switch (se.getID()) {
		case CMSessionEvent.LOGIN:			
			CMDummyEvent nde = new CMDummyEvent();
			StringBuilder sb = new StringBuilder();
			nde.setID(2);
			for (String user : s_user) { // 모든 파일의 이름을 문자열로 변환
				sb.append(user + "#");
			}
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, se.getSender()); // 메시지 전송
			System.out.println("******************** [로그인] 서버-->클라이언트 : 유저 리스트 전송 완료");

			if(!s_user.contains(se.getSender())) { // 사용자 이름 추가
				s_user.add(se.getSender());
				CMDummyEvent nde2 = new CMDummyEvent();
				nde2.setID(4); // 새로운 인원이 추가됨
				nde2.setDummyInfo(se.getSender());
				m_serverStub.broadcast(nde2); // 메시지 전송
				System.out.println("******************** [로그인] 신규 유저 정보 전송");
				
			}

			
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + se.getID());
		}
	}
	
	// 클라이언트의 파일요청에 따라 해당 파일을 찾음
	public void cFileRequest(String find) {
		// 해당 파일을 찾아서 클라이언트에게 전송해주는 과정..
		// 클라이언트 측 UI 가 관건이 될듯 ( 인덱스로 찾냐, 이름으로 찾냐.. )
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
