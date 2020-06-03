package cds;

import java.util.Vector;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class clientMain {
	CMClientStub m_clientStub;
	CMClientEventHandler m_eventHandler;
	
	//private c_pdf = null; // Ÿ�� ���� �ʿ�
	private Vector<String> c_user = null; // pdf ������ ����
	private Vector<Vector<String>> c_content = null; // �� �������� Ÿ ������� ���
	private Vector<String> c_history = null; // ���� ������� �������� ���
	
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
	}
	
	public void dummyEvent() { // �����̺�Ʈ ���� �� ���� �Լ�
		CMDummyEvent due = new CMDummyEvent();
		due.setDummyInfo("������ �����ϴ� �޽���");
		// due.setID(0);
		
		System.out.println("Ŭ���̾�Ʈ ----> ���� : " + due.getDummyInfo());
		m_clientStub.send(due,"SERVER"); // ������ ����	
	}
}
