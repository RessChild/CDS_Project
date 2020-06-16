package cds;
// CMAppEventHandler 인터페이스를 정의하는 핸들러

import java.awt.Desktop;
import java.awt.desktop.FilesEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
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
	private String c_fname; // 선택한 파일 명
	private Vector<String> c_user; // pdf 참여자 정보
	private String c_content = null; // 현재 페이지의 기록
	private HashMap<Integer, String> c_comments;
	
	public CMClientEventHandler(clientMain c, CMClientStub cs, UserInterface ui) {
		// TODO Auto-generated constructor stub
		this.m_client = c;
		this.m_clientStub = cs;
		this.UI = ui;
		this.c_user = new Vector<String>();
		this.c_comments = new HashMap<Integer, String>();
		
		setFilePath();
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
			fileEvent(arg0);
			//			fileEvent(arg0);
			break;
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // 이벤트 형변환

		System.out.println("**** 클라이언트 측 수신 메시지 : " + de.getDummyInfo()); // 메시지 출력

		// 등록된 유저 목록 반영
		if(de.getID() == RequestID.REQ_ALL_USERS) {
			String[] strs = de.getDummyInfo().split("#"); // 샵을 기준으로 쪼갬
			for(String str: strs) {
				if(str.isEmpty()) continue;
				c_user.add(str);
			}
			System.out.println(c_user); // 메시지 출력
		}
		// 새로 등록된 유저 반영
		else if(de.getID() == RequestID.REGISTER_USER) {
			c_user.add(de.getDummyInfo());
			DefaultListModel list = new DefaultListModel();
            for (int i =0; i < c_user.size(); i++) {
                list.addElement(c_user.get(i));
            }
			UI.getUserList().setModel(list);
			System.out.println("**** 클라이언트 : 신규유저 추가"); // 메시지 출력	
			System.out.println(c_user); // 메시지 출력
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
				
				for(int i = 0; i <comments.size(); i++) {
					JSONObject commentObj = (JSONObject) comments.get(i);
					this.c_comments.put(((Long) commentObj.get("page")).intValue(), (String )commentObj.get("comment"));
				}
				for(Entry<Integer, String> c : c_comments.entrySet()) {
					System.out.println("page : " + c.getKey() + ", comment : " + c.getValue());
				}
				UI.setComment(c_comments);
				
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
			if(fileList[0] != "") {
				m_client.showFileList(fileList);
			}
			else {
				System.out.println("아직 서버 내에 파일이 없어요");
			}
		}
		else if (de.getID() == RequestID.SELECT_SERVER_FILE) { // 선택한 파일 이름 확인
			this.c_fname = de.getDummyInfo();
			System.out.println("[클라이언트] 선택한 파일 : "+ this.c_fname);
			
			File check = new File(m_clientStub.getTransferedFileHome() + "/" + this.c_fname);
			if(check.exists()) {
				System.out.println("************* [클라이언트] 이미 해당 파일 있으니 그냥 열겠음"); // 얻은 메시지 확인
				// ~~~ UI에 화면 출력하는 과정 거치도록..
				UI.setLabel(check.getPath());
			}
			else {
			System.out.println("************* [클라이언트] 파일 달라고 서버헌태 요청.."); // 얻은 메시지 확인
			
			CMDummyEvent nde = new CMDummyEvent();
			nde.setDummyInfo(this.c_fname);
			nde.setID(RequestID.SERVER_FILE_REQ);
			m_clientStub.send(nde, "SERVER"); // 요청자한테 전송
			}
		}
		else if (de.getID() == RequestID.ADD_COMMENT) {
			String msg = de.getDummyInfo();
			System.out.println("******* [ADD COMMENT EVENT] ********");
			System.out.println("Message : "+ msg);
		}
	}
		
	// 여기서 파일 전송요청에 대한 push 허가 정보를 내보내야 함..
	private void fileEvent(CMEvent cme)
	{
		CMFileEvent fe = (CMFileEvent) cme;
		
		switch(fe.getID())
		{
		case CMFileEvent.REQUEST_PERMIT_PUSH_FILE: // 이게 원래있던거
			StringBuffer strReqBuf = new StringBuffer(); 
			strReqBuf.append("["+fe.getFileSender()+"] wants to send a file.\n");
			strReqBuf.append("file path: "+fe.getFilePath()+"\n");
			strReqBuf.append("file size: "+fe.getFileSize()+"\n");
			System.out.println(strReqBuf.toString());
			
			m_clientStub.replyEvent(fe, 1); // 1이면 찬성 0이면 거절			
			break;
		case CMFileEvent.REPLY_PERMIT_PULL_FILE:
			CMFileEvent nfe = new CMFileEvent();
			nfe.setID(fe.REPLY_PERMIT_PULL_FILE);
			nfe.setFilePath("test.pdf");
			System.out.println("**** 서버 ----> 클라이언트 : "+nfe.getFilePath());
			m_clientStub.send(fe, fe.getSender()); // 요청자한테 전송				
			break;
		case CMFileEvent.END_FILE_TRANSFER_CHAN: // 18번 메시지 ( 파일 전송 종료 )
			System.out.println("******** [클라이언트 파일이벤트] 파일 다받았당 ㅎ : "+ fe.getID());
			UI.setLabel(m_clientStub.getTransferedFileHome() + "/" + this.c_fname);
//			UI.setLabel(m_clientStub.getTransferedFileHome() + "/" + this.c_fname);
			break;
		default:
			System.out.println("******** [클라이언트 파일이벤트] 그 외 기타 : "+ fe.getID());
			break;
		}
		return;
	}
	
	public void setFilePath()
	{
		String p = "./client-file-path/";
		m_clientStub.setTransferedFileHome(Paths.get(p));
	}
}
