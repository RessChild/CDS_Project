package cds;

import java.io.File;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import kr.ac.konkuk.ccslab.cm.entity.CMList;
import kr.ac.konkuk.ccslab.cm.entity.CMRecvFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMSendFileInfo;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class serverMain {
	private CMServerEventHandler m_eventHandler;
	private CMServerStub m_serverStub;
	
	private Vector<File> s_pdf; // 타입 선언 필요
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
	
	// 파일관련 함수
	public void setFilePath()
	{
		// 파일 경로 세팅 
		String strPath = null;

		strPath = JOptionPane.showInputDialog("file path: ");
		if(strPath == null)
		{
			return;
		}
		
		m_serverStub.setTransferedFileHome(Paths.get(strPath));
		
	}

	public void pushFile() // 파일 푸시
	{
		String strFilePath = null;
		File files;
		byte byteFileAppendMode = -1;
		boolean bReturn = false;
		
		byteFileAppendMode = CMInfo.FILE_DEFAULT;

		/*
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		CMConfigurationInfo confInfo = m_serverStub.getCMInfo().getConfigurationInfo();
		File curDir = new File(confInfo.getTransferedFileHome().toString());
		fc.setCurrentDirectory(curDir);
		int fcRet = fc.showOpenDialog(this);
		if(fcRet != JFileChooser.APPROVE_OPTION) return;
		files = fc.getSelectedFiles(); // 여기서 파일 선택 구문인데..
		 */
		
		files = new File("./test.pdf");
		
		strFilePath = files.getPath();
		bReturn = m_serverStub.pushFile(strFilePath, "보낼 대상 이름", byteFileAppendMode);
		if (!bReturn) {
			System.out.println("*************** 파일 전송 에러! **************");
		}
	}
}
