package cds;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class clientMain {
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	
	public clientMain() { // ��ü ����
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientEventHandler();
	}
	
	public static void main(String[] args) {
		clientMain client = new clientMain();
		
		// CM Stub �� �̺�Ʈ �ڵ鷯 ���
		client.m_clientStub.setAppEventHandler(client.m_eventHandler);
		client.m_clientStub.startCM(); // ����
		client.m_clientStub.loginCM("201511231", "201511231"); // ������ �α���

		// CMDummyEvent ��ü�� ���� ����
		// �޽����� ������ �����°� Stub ���� cast �Լ��� ���� ����
		// �̶�, ���� ������ �ʿ��ѵ�, �̰� CM ���� info ��ü�� CMUser �� ��ü�� �޾� �� �� ����
		
		System.out.println("Ŭ���̾�Ʈ ���� ��");
		
		// �����̺�Ʈ ���� ����
		CMDummyEvent due = new CMDummyEvent();
		due.setDummyInfo("������ �����ϴ� �޽���");
		System.out.println("Ŭ���̾�Ʈ ----> ���� : " + due.getDummyInfo());
		client.m_clientStub.send(due,"SERVER");
		
	}
}
