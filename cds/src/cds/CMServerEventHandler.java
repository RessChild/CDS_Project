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
		switch(arg0.getType()) { // �̺�Ʈ ó��
		case CMInfo.CM_DUMMY_EVENT:
			dummyEvent(arg0);
			break;
		case CMInfo.CM_SESSION_EVENT:
			break;
		}
	}
	
	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e;
		System.out.println("���� �� ���� �޽��� : "+de.getDummyInfo()); // ���� �޽��� Ȯ��

		CMDummyEvent nde = new CMDummyEvent(); // �����̺�Ʈ �����
		// nde.setID(0); �� ���̵� �����ϰ�, ���߿� �����ϱ�..

		nde.setDummyInfo("���� ä���~~~"); // ���ڿ��� �����̺�Ʈ ����
		System.out.println("���� ----> Ŭ���̾�Ʈ : "+nde.getDummyInfo());
		m_serverStub.send(nde, de.getSender()); // ��û������ ����
	}
}
