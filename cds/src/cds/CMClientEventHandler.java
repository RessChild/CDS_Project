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
		switch(arg0.getType()) { // 이벤트 처리
		case CMInfo.CM_DUMMY_EVENT:
			dummyEvent(arg0);
			break;
		case CMInfo.CM_SESSION_EVENT:
			break;
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // 이벤트 형변환
		System.out.println("클라이언트 측 수신 메시지 (시간) : " + de.getDummyInfo()); // 메시지 출력
	}
}
