package cds;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FileDialog extends JDialog implements ActionListener, ListSelectionListener{

	JPanel listPanel, buttonPanel;
	JList List;
	String[] fileList;
	JButton confirm, cancel;
	UserInterface UI;
	String selectedFile;
	FileDialog(UserInterface UI, String[] fileList){
		this.UI = UI;
		this.fileList = fileList;
		init();
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	public void init() {
		buttonPanel = new JPanel(new FlowLayout());
		listPanel = new JPanel(new BorderLayout());
		confirm = new JButton("Confirm");
		cancel = new JButton("cancel");
		buttonPanel.add(confirm);
		buttonPanel.add(cancel);
		
		List = new JList(fileList);
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		listPanel.add(List, BorderLayout.NORTH);
		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		List.addListSelectionListener(this);
		this.add(listPanel, BorderLayout.CENTER);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				UI.dialog = null;//parent가 가진 dialog를 null
				dispose();//창을 닫는다.
			}
		});
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == confirm) {
			UI.selectedFile = this.selectedFile;
			UI.sendSelect(this.selectedFile);
			UI.dialog = null;
			dispose();
		}else if(e.getSource() == cancel) {
			UI.dialog = null;
			dispose();
		}
		
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(!e.getValueIsAdjusting()) {
			//특정 사용자를 선택하면 해당 사용자가 주석을 달은 부분을 보여준다.
			//주석을 어디에 저장하는지 몰라서 일단 사용자가 선택하면 해당 사용자이름을 콘솔에 띄움
			this.selectedFile = List.getSelectedValue().toString();
		}
	}

}
