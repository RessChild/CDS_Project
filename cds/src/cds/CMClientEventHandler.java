package cds;
// CMAppEventHandler 인터페이스를 정의하는 핸들러

import java.awt.Desktop;
import java.awt.desktop.FilesEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	private UserInterface UI;
	
	private File c_pdf = null; // 현재 사용중인 파일
	private int c_fnum = -1; // 선택한 파일의 인덱스 번호
	private Vector<String> c_user; // pdf 참여자 정보
	private String c_content = null; // 현재 페이지의 기록
	
	public CMClientEventHandler(clientMain c, CMClientStub cs, UserInterface ui) {
		// TODO Auto-generated constructor stub
		this.m_client = c;
		this.m_clientStub = cs;
		this.UI = ui;
		this.c_user = new Vector<String>();
	}
	
	public void setUI(UserInterface ui) {
		this.UI = ui;
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
			processFileEvent(arg0);
			//			fileEvent(arg0);
			break;
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // 이벤트 형변환

		System.out.println("**** 클라이언트 측 수신 메시지 : " + de.getDummyInfo()); // 메시지 출력

		String[] strs = de.getDummyInfo().split("#"); // 샵을 기준으로 쪼갬

		// 등록된 유저 목록 반영
		if(de.getID() == RequestID.REQ_ALL_USERS) {
			for(String str: strs) {
				if(str.isEmpty()) continue;
				c_user.add(str);
			}
		}
		// 새로 등록된 유저 반영
		else if(de.getID() == RequestID.REGISTER_USER) {
			c_user.add(strs[0]);
			UI.userSwing(c_user);
			System.out.println("**** 클라이언트 : 신규유저 추가"); // 메시지 출력	
		}
		// 선택한 유저의 주석 목록 반영
		else if(de.getID() == RequestID.REQ_COMMENT) {
			String msg = de.getDummyInfo();
			System.out.println("******* [REQ COMMENT EVENT] ********");
			System.out.println("Message : "+ msg);
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(msg);
				JSONObject jsonObj = (JSONObject) obj;
				JSONArray comments = (JSONArray) jsonObj.get("comments");
				
				List<String> userComments = new ArrayList<>();
				for(int i = 0; i < comments.size(); i++) {
					userComments.add((String) comments.get(i)); 
				}
				for(int i = 0; i < userComments.size(); i++) {
					System.out.print(userComments.get(i));
				}
				System.out.println();
				
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		// 서버에 저장되어 있는 PDF 파일 목록 가져옴
		else if(de.getID() == RequestID.FILE_LIST_REQ) {
			String []fileList = de.getDummyInfo().split("#");
			for(String file: fileList) {
				System.out.print(file + ", ");
			}
			System.out.println();
			m_client.showFileList(fileList);
		}
		
		System.out.println(c_user); // 메시지 출력
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent fe = (CMFileEvent) e;
		System.out.print("**** 클라이언트 수신 파일 : " + fe.getFileBlock().toString());
	}
	
	
	// 여기서 파일 전송요청에 대한 push 허가 정보를 내보내야 함..
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
			
			m_clientStub.replyEvent(fe, 1); // 1이면 찬성 0이면 거절			
			break;
		}
		return;
	}
}
