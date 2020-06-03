package cds;

import java.util.Vector;

import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class serverMain {
	private CMServerEventHandler m_eventHandler;
	private CMServerStub m_serverStub;
	
	//private Vector<> s_pdf; // 타입 선언 필요
	private Vector<Vector<String>> s_user; // 각 pdf 별 참여자 목록
	private Vector<Vector<Vector<String>>> s_content; // 각 pdf 내 참여자의 기록
	
	public serverMain() { // 객체 생성
		m_serverStub = new CMServerStub();
		m_eventHandler = new CMServerEventHandler(this, m_serverStub);
	}
	
	public static void main(String[] args) {
		serverMain server = new serverMain();
		server.m_serverStub.setAppEventHandler(server.m_eventHandler);
		
		server.m_serverStub.startCM(); // 서버 돌리기 시작
		System.out.println("Server 실행 중");
	}
}
