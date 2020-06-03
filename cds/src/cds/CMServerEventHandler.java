package cds;
import java.time.LocalDate;
import java.time.LocalTime;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
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
		}
	}
	
	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e;
		System.out.println("서버 측 수신 메시지 : "+de.getDummyInfo()); // 얻은 메시지 확인

		CMDummyEvent nde = new CMDummyEvent(); // 더미이벤트 만들기
		// nde.setID(0); 로 아이디 세팅하고, 나중에 구분하기..

		nde.setDummyInfo("내용 채우기~~~"); // 문자열로 더미이벤트 생성
		System.out.println("서버 ----> 클라이언트 : "+nde.getDummyInfo());
		m_serverStub.send(nde, de.getSender()); // 요청자한테 전송
	}
}
