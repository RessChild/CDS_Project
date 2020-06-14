package cds;
// CMAppEventHandler �씤�꽣�럹�씠�뒪瑜� �젙�쓽�븯�뒗 �빖�뱾�윭

import java.awt.Desktop;
import java.awt.desktop.FilesEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMClientEventHandler implements CMAppEventHandler {

	private clientMain m_client = null;
	private CMClientStub m_clientStub = null;
	private UserInterface UI;
	
	private File c_pdf = null; // �쁽�옱 �궗�슜以묒씤 �뙆�씪
	private int c_fnum = -1; // �꽑�깮�븳 �뙆�씪�쓽 �씤�뜳�뒪 踰덊샇
	private Vector<String> c_user; // pdf 李몄뿬�옄 �젙蹂�
	private String c_content = null; // �쁽�옱 �럹�씠吏��쓽 湲곕줉
	
	public CMClientEventHandler(clientMain c, CMClientStub cs) {
		// TODO Auto-generated constructor stub
		this.m_client = c;
		this.m_clientStub = cs;
		this.c_user = new Vector<String>();
	}
	
	public void setUI(UserInterface ui) {
		this.UI = ui;
	}
	
	@Override
	public void processEvent(CMEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getType()) { // �씠踰ㅽ듃 泥섎━
		case CMInfo.CM_DUMMY_EVENT:
			dummyEvent(arg0);
			break;
		case CMInfo.CM_SESSION_EVENT:
			break;
		case CMInfo.CM_FILE_EVENT:
			processFileEvent(arg0);
			//			fileEvent(arg0);
			break;
		}
	}

	public void dummyEvent(CMEvent e) {
		CMDummyEvent de = (CMDummyEvent) e; // �씠踰ㅽ듃 �삎蹂��솚

		System.out.println("**** �겢�씪�씠�뼵�듃 痢� �닔�떊 硫붿떆吏� : " + de.getDummyInfo()); // 硫붿떆吏� 異쒕젰

		String[] strs = de.getDummyInfo().split("#"); // �꺏�쓣 湲곗��쑝濡� 履쇨갔

		switch(de.getID()) {
		case 1:
			String []fileList = de.getDummyInfo().split("#");
			for(String file: fileList) {
				System.out.print(file + ", ");
			}
			System.out.println();
			m_client.showFileList(fileList);
      break;
		case 2: // 李멸��옄 �젙蹂� �엯�젰
			for(String str: strs) {
				if(str.isEmpty()) continue;
				c_user.add(str);
			}
			break;
		case 3: // 二쇱꽍 �젙蹂�
			break;
		case 4: // �떊洹� �쑀�� �젙蹂�
			c_user.add(strs[0]);
			System.out.println("**** �겢�씪�씠�뼵�듃 : �떊洹쒖쑀�� 異붽�"); // 硫붿떆吏� 異쒕젰	
			break;
		default:
			System.out.println("******** [DummyEvent] Can't Find to Do");
			break;
		}
		
		System.out.println(c_user); // 硫붿떆吏� 異쒕젰
	}
	
	public void fileEvent(CMEvent e) {
		CMFileEvent fe = (CMFileEvent) e;
		System.out.print("**** �겢�씪�씠�뼵�듃 �닔�떊 �뙆�씪 : " + fe.getFileBlock().toString());
	}
	
	
	// �뿬湲곗꽌 �뙆�씪 �쟾�넚�슂泥��뿉 ���븳 push �뿀媛� �젙蹂대�� �궡蹂대궡�빞 �븿..
	private void processFileEvent(CMEvent cme)
	{
		CMFileEvent fe = (CMFileEvent) cme;
		CMConfigurationInfo confInfo = null;
		CMFileTransferInfo fInfo = m_clientStub.getCMInfo().getFileTransferInfo();
		int nOption = -1;
		long lTotalDelay = 0;
		long lTransferDelay = 0;
		
		switch(fe.getID())
		{
		case CMFileEvent.REQUEST_PERMIT_PUSH_FILE:
			StringBuffer strReqBuf = new StringBuffer(); 
			strReqBuf.append("["+fe.getFileSender()+"] wants to send a file.\n");
			strReqBuf.append("file path: "+fe.getFilePath()+"\n");
			strReqBuf.append("file size: "+fe.getFileSize()+"\n");
			System.out.println(strReqBuf.toString());
			
			m_clientStub.replyEvent(fe, 1); // 1�씠硫� 李ъ꽦 0�씠硫� 嫄곗젅			
			break;
		}
		return;
	}
}
