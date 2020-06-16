package cds;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.konkuk.ccslab.cm.entity.CMSession;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMServerEventHandler implements CMAppEventHandler {

	private serverMain m_server = null;
	private CMServerStub m_serverStub = null;
	
	private HashMap<String, ServerPDF> s_pdf; // <파일 이름, ServerPDF 객체> 형태로 저장
	private Vector<String> s_user; // 참여자 목록
	
	private static int FILE_LIST_REQ_ID = 1;
	
	public CMServerEventHandler(serverMain s, CMServerStub ss) {
		// TODO Auto-generated constructor stub
		m_server = s;
		m_serverStub = ss;
		
		s_pdf = new HashMap<String, ServerPDF>();
		s_user = new Vector<String>();
		
		setFilePath();
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
		
		
		if (de.getID() == FILE_LIST_REQ_ID) {
			Set<String> fileList = s_pdf.keySet(); // 서버가 가진 모든 파일의 키값을 전달
			System.out.println("************ [서버핸들러] 모든 파일 키값 반환!");
			CMDummyEvent nde = new CMDummyEvent();
			nde.setID(FILE_LIST_REQ_ID);
			
			StringBuilder msg = new StringBuilder();
			for(String file : fileList) {
				msg.append(file + "#");
			}
			System.out.println("****** [FILELIST EVENT MSG] *******");
			System.out.println("MSG : " + msg);
			
			nde.setDummyInfo(msg.toString());
			m_serverStub.send(nde, de.getSender());
		}
		else if(de.getID() == RequestID.ADD_COMMENT) {
			String msg = de.getDummyInfo();
			System.out.println("******* [ADD COMMENT EVENT] ********");
			System.out.println("Message : "+ msg);
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(msg);
				JSONObject jsonObj = (JSONObject) obj;
				
				// 클라이언트에서 보낸 <파일 이름, 페이지 번호, 주석> 데이터 파싱
				String fileName = (String) jsonObj.get("fileName");
				int pageNum = ((Long) jsonObj.get("pageNum")).intValue();
				String comment = (String) jsonObj.get("comment");

				// 파일 이름으로 서버에 해당 파일 있는지 확인
				ServerPDF req_pdf = s_pdf.get(fileName);
				
				if(req_pdf != null) {
					req_pdf.addComment(de.getSender(), comment, pageNum);
				}
				else {
					// 클라이언트에게 파일 전송 메세지 요청
					
				}
				
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
		}
		else if(de.getID() == RequestID.REQ_COMMENT) {
			String msg = de.getDummyInfo();
			System.out.println("******* [REQUEST COMMENT EVENT] ********");
			System.out.println("Message : "+ msg);
			
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(msg);
				JSONObject jsonObj = (JSONObject) obj;
				
				// 주석 보려고 하는 <파일 이름, 주석> 데이터 파싱
				String fileName = (String) jsonObj.get("fileName");
				String userName = (String) jsonObj.get("user");
				
				// 파일 이름으로 서버에 해당 파일 있는지 확인
				ServerPDF req_pdf = s_pdf.get(fileName);
				
				if(req_pdf != null) {
					HashMap<Integer, String> comments = req_pdf.getComment(userName);
					
					// 요청한 유저가 남겨놓은 주석이 있을 때만 전송
					if (comments != null) {
						JSONObject sendJsonObj = new JSONObject();
						JSONArray jsonArr = new JSONArray();
						for(Entry<Integer, String> comment : comments.entrySet()) {
							JSONObject commentObj = new JSONObject();
							commentObj.put("page", comment.getKey());
							commentObj.put("comment", comment.getValue());
							jsonArr.add(commentObj);
						}
						sendJsonObj.put("comments", jsonArr);
						
						CMDummyEvent nde = new CMDummyEvent();
						nde.setID(RequestID.REQ_COMMENT);
						nde.setDummyInfo(sendJsonObj.toString());
						
						m_serverStub.send(nde, de.getSender());
						
						System.out.println("Server sent : " + sendJsonObj.toString());
					}
				} else {
					// 테스트
				}
			}
			catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if (de.getID() == RequestID.SERVER_FILE_REQ) {
			String f = s_pdf.get(de.getDummyInfo()).getFilePath();
			System.out.println(f);
			pushFile(f, de.getSender());
		}
	}
	
	public void sessionEvent(CMEvent e) {
		CMSessionEvent se = (CMSessionEvent) e;
		System.out.println("SessionEvent 발생 : "+ se.getID());
		switch (se.getID()) {
		case CMSessionEvent.LOGIN:	// 1번 이벤트
			CMDummyEvent nde = new CMDummyEvent();
			nde.setID(RequestID.REQ_ALL_USERS);
			
			StringBuilder sb = new StringBuilder();
			for (String user : s_user) { // 모든 파일의 이름을 문자열로 변환
				sb.append(user + "#");
			}
			nde.setDummyInfo(sb.toString());
			m_serverStub.send(nde, se.getSender()); // 메시지 전송
			System.out.println("******************** [로그인] 서버-->클라이언트 : 유저 리스트 전송 완료");

			if(!s_user.contains(se.getSender())) { // 사용자 이름 추가
				s_user.add(se.getSender());
				CMDummyEvent nde2 = new CMDummyEvent();
				nde2.setID(RequestID.REGISTER_USER); // 새로운 인원이 추가됨
				nde2.setDummyInfo(se.getSender());
				m_serverStub.broadcast(nde2); // 메시지 전송
				System.out.println("******************** [로그인] 신규 유저 정보 전송");
				
			}
		case CMSessionEvent.LOGOUT: // 2번 이벤트
			break;
		case CMSessionEvent.ADD_BLOCK_SOCKET_CHANNEL: // 22번 이벤트
			break;
		case CMSessionEvent.JOIN_SESSION: // 6번 이벤트
			System.out.println("새로운 대상이 참여 : " + se.getSender());
			break;
		default:
			System.out.println("그 외의 이벤트");
		}
	}
	
	// 클라이언트의 파일요청에 따라 해당 파일을 찾음
	public void cFileRequest(String find) {
		// 해당 파일을 찾아서 클라이언트에게 전송해주는 과정..
		// 클라이언트 측 UI 가 관건이 될듯 ( 인덱스로 찾냐, 이름으로 찾냐.. )
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent fe = (CMFileEvent) e;
		System.out.println("**** 서버 측 수신 메시지 : 파일 요청 메시지"); // 얻은 메시지 확인

//		CMConfigurationInfo confInfo = null;
//		CMFileTransferInfo fInfo = m_serverStub.getCMInfo().getFileTransferInfo();
//		int nOption = -1;
//		long lTotalDelay = 0;
//		long lTransferDelay = 0;
		
		switch(fe.getID()) {
		case CMFileEvent.REQUEST_PERMIT_PUSH_FILE: // 파일 받을떄
			System.out.println("**************** [서버] 푸시요청"); // 얻은 메시지 확인
			
			StringBuffer strReqBuf = new StringBuffer(); 
			strReqBuf.append("["+fe.getFileSender()+"] wants to send a file.\n");
			strReqBuf.append("file path: "+fe.getFilePath()+"\n");
			strReqBuf.append("file size: "+fe.getFileSize()+"\n");
			System.out.println(strReqBuf.toString());
			
			m_serverStub.replyEvent(fe, 1); // 1이면 찬성 0이면 거절			
			break;
		case CMFileEvent.REQUEST_PERMIT_PULL_FILE: // 파일 줄 때 (서버->클라이언트)
			System.out.println("**************** [서버] 풀 요청.."); // 얻은 메시지 확인
			
			CMFileEvent nfe = new CMFileEvent();
			nfe.setID(fe.REPLY_PERMIT_PULL_FILE);
//			nfe.setFilePath("test.pdf");
			System.out.println("**** 클라이언트 ----> 서버 : "+nfe.getFilePath());
			m_serverStub.send(nfe, fe.getSender()); // 요청자한테 전송
			break;
		case CMFileEvent.START_FILE_TRANSFER_CHAN: // 16번 이벤트
			System.out.println("**** [서버] 클라이언트에서 파일이 들어와버렷!");
			break;
		case CMFileEvent.END_FILE_TRANSFER_CHAN: // 18번 이벤트
			System.out.println("**** [서버] 클라이언트에서 파일이 다들어왔다굿!");
			
			String fileName = m_serverStub.getTransferedFileHome() +"/" + fe.getSender() +"/"+ fe.getFileName();
			System.out.println("************* [서버] 도착한 파일 이름 : " + fileName);
			s_pdf.put(fe.getFileName(), new ServerPDF(fileName, new File(fileName))); // 새로 얻은 파일을 리스트에 추가
			
			System.out.print(s_pdf);
			break;
		case CMFileEvent.REPLY_PERMIT_PUSH_FILE: // 4번 이벤트
			System.out.println("**** [서버] REPLY_PERMIT_PUSH_FILE");			
			break;
		case CMFileEvent.END_FILE_TRANSFER_CHAN_ACK: // 19번 이벤트
			System.out.println("**** [서버] 파일 전송 허락받았다굿!");
			break;
		default:
			System.out.println("**** 둘다 아니야~~ : " + fe.getID()); // 얻은 메시지 확인			
			break;
		}
	}	
	
	public void pushFile(String f, String who) // 파일 푸시
	{
		byte byteFileAppendMode = CMInfo.FILE_DEFAULT;
		boolean bReturn = false;

		bReturn = m_serverStub.pushFile(f, who, byteFileAppendMode);
		if (!bReturn) {
			System.out.println("*************** 파일 전송 에러! **************");
		}
	}
	
	public void setFilePath()
	{
		String p = "./server-file-path/";
		m_serverStub.setTransferedFileHome(Paths.get(p));
	}
}
