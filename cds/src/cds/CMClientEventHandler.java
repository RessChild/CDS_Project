package cds;
// CMAppEventHandler �������̽��� �����ϴ� �ڵ鷯

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

public class CMClientEventHandler implements CMAppEventHandler {

	@Override
	public void processEvent(CMEvent arg0) {
		// TODO Auto-generated method stub
		if ( arg0.getType() == CMInfo.CM_DUMMY_EVENT ) {
			dummyEvent(arg0);
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // �̺�Ʈ ����ȯ
		System.out.println("Ŭ���̾�Ʈ �� ���� �޽��� (�ð�) : " + de.getDummyInfo()); // �ð� ���
	}
}
