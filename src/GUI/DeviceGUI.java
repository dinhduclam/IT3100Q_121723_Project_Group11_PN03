package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import struct.*;

public class DeviceGUI extends JPanel implements ActionListener, KeyListener {
	private JPanel pnSouth, pnButtonList, pnFillInfor, pnTitle, pnLeft, pnExportAndPay, pnRight, pnFind;
	private JButton add, update, clear, delete, export, pay;
	private JLabel status;
	private JScrollPane scrollPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private TextField[] textFields = new TextField[10];
	private TextField makeFind, nameFind;
	
	private String deviceName;
	private int type;
	private int numberOfState;
	private String[] colTitle;
	private DeviceList deviceList = new DeviceList();
	private List<Integer> indxList = new ArrayList<>();
	
	DeviceGUI(String deviceName, int type, int numberOfState, String[] colTitle) {
		this.deviceName = deviceName;
		this.type = type;
		this.numberOfState = numberOfState;
		this.colTitle = colTitle;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		createPnLeft();
		/*************/
		JPanel separator = new JPanel();
		separator.setPreferredSize(new Dimension(15, 0));
		add(separator);
		/*************/
		createPnRight();
	}

	private void createPnLeft() {
		pnLeft = new JPanel();
		pnLeft.setLayout(new BorderLayout(0, 0));

		
		/* Set title for the Panel
		 * title is deviceName (a parameter of DeviceGUI contructor) 
		 * Title of object Laptop is LAPTOP
		 * Title of object Phone is PHONE
		 * 
		 */
		pnTitle = new JPanel();
		pnTitle.setPreferredSize(new Dimension(0, 30));
		pnLeft.add(pnTitle, BorderLayout.NORTH);
		pnTitle.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel title = new JLabel(deviceName.toUpperCase());
		title.setFont(new Font("Tahoma", Font.BOLD, 16));
		pnTitle.add(title);

		/* Create JPanel to fill in information */
		
		pnFillInfor = new JPanel();
		pnFillInfor.setLayout(new GridLayout(0, 2, 10, 10));
		
		pnLeft.add(pnFillInfor, BorderLayout.CENTER);
		
		for (int i=0; i<numberOfState; i++) {
			pnFillInfor.add(new JLabel(colTitle[i]));
			textFields[i] = new TextField();
			pnFillInfor.add(textFields[i]);
		}

		/* Create Button */
		pnSouth = new JPanel();
		pnSouth.setLayout(new BorderLayout(0, 10));
		pnSouth.setPreferredSize(new Dimension(0, 230));

		pnLeft.add(pnSouth, BorderLayout.SOUTH);

		status = new JLabel("");
		status.setPreferredSize(new Dimension(0, 40));
		pnSouth.add(status, BorderLayout.NORTH);

		/* Create 6 buttons: Add, Update, Clear, Delete, Export, Payment
		 * pnButtonList contains 4 buttons: Add, Update, Clear, Delete
		 * 
		 */
		pnButtonList = new JPanel();
		pnButtonList.setLayout(new GridLayout(2, 2, 10, 10));
		
		pnExportAndPay = new JPanel();
		pnExportAndPay.setLayout(new GridLayout(2, 1, 10, 10));
		pnExportAndPay.setPreferredSize(new Dimension(0, 85));
		
		add = new Button(Button.NOMAL_BUTTON, this, "Add");
		update = new Button(Button.NOMAL_BUTTON, this, "Update");
		clear = new Button(Button.NOMAL_BUTTON, this, "Clear");
		delete = new Button(Button.NOMAL_BUTTON, this, "Delete");
		pay = new Button(Button.NOMAL_BUTTON, this, "Pay");
		export = new Button(Button.NOMAL_BUTTON, this, "Export (Excel)", new ImageIcon("src/icon/excel.png"));
		
		pnButtonList.add(add);
		pnButtonList.add(update);
		pnButtonList.add(clear);
		pnButtonList.add(delete);
		
		pnExportAndPay.add(pay);
		pnExportAndPay.add(export);
		
		pnSouth.add(pnButtonList);
		pnSouth.add(pnExportAndPay, BorderLayout.SOUTH);
		/*--------------*/

		add(pnLeft);
	}

	private void createPnRight() {
		pnRight = new JPanel();
		pnRight.setLayout(new BorderLayout());
		
		pnFind = new JPanel();
		pnFind.setLayout(new FlowLayout(FlowLayout.LEADING, 70, 0));
		pnFind.setPreferredSize(new Dimension(0, 40));
		
		pnFind.add(new JLabel("Find by Make:"));
		makeFind = new TextField();
		makeFind.addKeyListener(this);
		pnFind.add(makeFind);
		
		pnFind.add(new JLabel("Find by Name:"));
		nameFind = new TextField();
		nameFind.addKeyListener(this);
		pnFind.add(nameFind);
		pnRight.add(pnFind, BorderLayout.NORTH);
		

		table = new JTable();
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(colTitle);
		table.setModel(tableModel);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(600, 0));
		pnRight.add(scrollPane);

		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int rowIndx = table.getSelectedRow();
				for (int i=0; i<numberOfState; i++) textFields[i].setText(table.getValueAt(rowIndx, i).toString());
			}
		});
		add(pnRight);
	}
	
	
	// clear all text in the textfield (make, manufacturing year, price, color,...)
	private void clear() {
		for (int i=0; i<numberOfState; i++)
			textFields[i].setText("");
		status.setText("");
	}

	// check
	private boolean check() {
		String noti = "Please fill all the form!";
		for (int i=0; i<numberOfState; i++)
			if (textFields[i].getText().equals("")) {
				textFields[i].requestFocus();
				status.setText(noti);
				return false;
			}
		return true;
	}

	private Device getInfo() {
		String t[] = getInfoReturnString();
		if (type == Device.LAPTOP_TYPE) return new struct.Laptop(t);
		else if (type == Device.PHONE_TYPE) return new struct.Phone(t);
		else return null;
	}
	
	private String[] getInfoReturnString() {
		String t[] = new String[numberOfState];
		for (int i=0; i<numberOfState; i++)
			t[i] = textFields[i].getText();
		return t;
	}
	
	private void load(List<Integer> indxList) {
		this.indxList = indxList;
		tableModel.setNumRows(0);

		for (Integer indx : indxList) {
			tableModel.addRow(deviceList.getDevice(indx).getStringArray());
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == add) {
			if (check()) {
				deviceList.add(getInfo());
				load(deviceList.findMakeandName(makeFind.getText(), nameFind.getText()));
				clear();
			}
			
		}
		else if (arg0.getSource() == clear) {
			clear();
		}
		else if (arg0.getSource() == update) {
			int rowIndx = table.getSelectedRow();
			if (rowIndx < 0) status.setText("Please select the row first!");
			else if (check()) {
				deviceList.modify((int) indxList.get(rowIndx), getInfo());
				load(deviceList.findMakeandName(makeFind.getText(), nameFind.getText()));
			}
		}
		else if (arg0.getSource() == delete) {
			int rowIndx = table.getSelectedRow();
			while (rowIndx >= 0) {
				deviceList.rm((int) indxList.get(rowIndx));
				tableModel.removeRow(rowIndx);
				rowIndx = table.getSelectedRow();
			}
		}
		else if (arg0.getSource() == export) {
			deviceList.show();
			System.out.println("--------");
			status.setText(String.valueOf(deviceList.getProfit()));
		}
		else if (arg0.getSource() == pay) {
			
			int rowIndx = table.getSelectedRow();
			if (rowIndx < 0) status.setText("Please select the row first!");
			else {
				new Payment(colTitle, deviceList.getDevice((int) indxList.get(rowIndx)).getStringArray());
				deviceList.pay((int) indxList.get(rowIndx));
				load(deviceList.findMakeandName(makeFind.getText(), nameFind.getText()));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		load(deviceList.findMakeandName(makeFind.getText(), nameFind.getText()));
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
