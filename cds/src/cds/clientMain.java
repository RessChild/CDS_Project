package cds;

import java.util.Vector;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class clientMain {
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	
	//private c_pdf = null; // 타입 선언 필요
	private Vector<String> c_user = null; // pdf 참여자 정보
	private Vector<Vector<String>> c_content = null; // 각 페이지별 타 사용자의 기록
	private Vector<String> c_history = null; // 현재 사용자의 페이지별 기록
	
	public clientMain() { // 객체 생성
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientEventHandler();
	}
	
	public static void main(String[] args) {
		clientMain client = new clientMain();
		
		// CM Stub 에 이벤트 핸들러 등록
		client.m_clientStub.setAppEventHandler(client.m_eventHandler);
		client.m_clientStub.startCM(); // 실행
		client.m_clientStub.loginCM("201511231", "201511231"); // 서버에 로그인

		// CMDummyEvent 객체를 만들어서 전송
		// 메시지를 실제로 보내는건 Stub 내의 cast 함수로 전송 가능
		// 이때, 본인 정보가 필요한데, 이건 CM 내에 info 객체로 CMUser 란 객체로 받아 알 수 있음
		
		System.out.println("클라이언트 실행 중");
	}
	
	public void dummyEvent() { // 더미이벤트 생성 및 전송 함수
		CMDummyEvent due = new CMDummyEvent();
		due.setDummyInfo("서버로 전송하는 메시지");
		// due.setID(0);
		
		System.out.println("클라이언트 ----> 서버 : " + due.getDummyInfo());
		m_clientStub.send(due,"SERVER"); // 서버로 전송	
	}
}
