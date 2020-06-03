package cds;
// CMAppEventHandler 인터페이스를 정의하는 핸들러

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
		CMDummyEvent de = (CMDummyEvent) e; // 이벤트 형변환
		System.out.println("클라이언트 측 수신 메시지 (시간) : " + de.getDummyInfo()); // 시간 출력
	}
}
