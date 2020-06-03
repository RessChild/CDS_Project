package cds;

import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class serverMain {
	private CMServerEventHandler m_eventHandler;
	private CMServerStub m_serverStub;
	
	public serverMain() { // ��ü ����
		m_serverStub = new CMServerStub();
		m_eventHandler = new CMServerEventHandler(this, m_serverStub);
	}
	
	public static void main(String[] args) {
		serverMain server = new serverMain();
		server.m_serverStub.setAppEventHandler(server.m_eventHandler);
		
		server.m_serverStub.startCM(); // ���� ������ ����
		System.out.println("Server ���� ��");
	}
}
