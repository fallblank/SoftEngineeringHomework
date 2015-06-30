package Interaction;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import fallblank.DataInteraction;

public class InventoryCheckWindow extends JFrame {

	private JTable mResultTable;
	private Vector mRowData;
	private Vector mColumnNames;
	private JTextField mIdTextFiled;
	private JTextField mIn_noTextField;
	private JTextField mInventoryTextField;
	private JButton mConfirmButton;
	private DataInteraction dataCenter;

	public InventoryCheckWindow(int x, int y) {
		mRowData = new Vector<Vector<String>>();
		mColumnNames = new Vector<String>();
		dataCenter = new DataInteraction();
		init();
		this.setTitle("盘存操作");
		this.setLocation(x, y);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		showData();

	}

	private void init() {
		mResultTable = new JTable(mRowData, mColumnNames);
		mResultTable.setRowHeight(30);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		centerPanel.add(new JScrollPane(mResultTable));
		this.add(centerPanel, BorderLayout.CENTER);

		JLabel label01 = new JLabel("输入商品编号：");
		mIdTextFiled = new JTextField(18);
		mIdTextFiled.addFocusListener(new IdTextFiledListener());
		JPanel panel01 = new JPanel();
		panel01.add(label01);
		panel01.add(mIdTextFiled);
		JLabel label02 = new JLabel("输入入库单号：");
		mIn_noTextField = new JTextField(18);
		JPanel panel02 = new JPanel();
		panel02.add(label02);
		panel02.add(mIn_noTextField);
		JLabel label03 = new JLabel("输入正确数量：");
		mInventoryTextField = new JTextField(18);
		JPanel panel03 = new JPanel();
		panel03.add(label03);
		panel03.add(mInventoryTextField);
		mConfirmButton = new JButton("修正");
		mConfirmButton.addActionListener(new ConfirmButton01Listener());
		Box vbox = Box.createVerticalBox();
		vbox.add(panel01);
		vbox.add(panel02);
		vbox.add(panel03);
		vbox.add(mConfirmButton);
		this.add(vbox, BorderLayout.SOUTH);

	}

	private void showData() {
		ArrayList<HashMap<String, String>> resultList = dataCenter
				.getAllRecords();
		int length = resultList.size();
		mRowData.clear();
		for (int i = 0; i < length; i++) {
			HashMap<String, String> map = resultList.get(i);
			String id = map.get("id");
			String inventory = map.get("inventory");
			Vector<String> v = new Vector<String>();
			v.add(id);
			v.add(inventory);
			mRowData.add(v);
		}
		mColumnNames.clear();
		mColumnNames.add("商品编号");
		mColumnNames.add("库存量");

		DefaultTableModel model = (DefaultTableModel) mResultTable.getModel();
		model.fireTableStructureChanged();
		model.fireTableDataChanged();
		mResultTable.removeAll();

		validate();
	}

	class IdTextFiledListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
			showData();
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			String inputId = mIdTextFiled.getText().trim();
			ArrayList<HashMap<String, String>> resultList = dataCenter
					.inStorageInquery(inputId);
			int length = resultList.size();
			mRowData.clear();
			for (int i = 0; i < length; i++) {
				HashMap<String, String> map = resultList.get(i);
				String in_no = map.get("in_no");
				String id = map.get("id");
				String name = map.get("name");
				String inventory = map.get("inventory");
				String p_date = map.get("p_date");
				Vector<String> v = new Vector<String>();
				v.add(in_no);
				v.add(id);
				v.add(name);
				v.add(inventory);
				v.add(p_date);
				mRowData.add(v);
			}
			mColumnNames.clear();
			mColumnNames.add("入库单号");
			mColumnNames.add("商品编号");
			mColumnNames.add("商品名称");
			mColumnNames.add("库存");
			mColumnNames.add("生产日期");
			DefaultTableModel model = (DefaultTableModel) mResultTable
					.getModel();
			model.fireTableStructureChanged();
			model.fireTableDataChanged();
			mResultTable.removeAll();

			validate();

		}

	}

	class ConfirmButton01Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String in_no = mIn_noTextField.getText().trim();
			String inventory = mInventoryTextField.getText().trim();
			String tableName = dataCenter.mCheatTableName;
			if (tableName != null) {
				String SQLString = "update " + tableName + " set inventory="
						+ inventory + " where in_no='" + in_no + "';";
				dataCenter.executeSQL(SQLString);
				JOptionPane.showMessageDialog(InventoryCheckWindow.this,
						"已成功将"+in_no+"编号商品数量修正为"+inventory,
						"成功",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(InventoryCheckWindow.this,
						"请确定你输入的信息是否正确",
						"失败",
						JOptionPane.ERROR_MESSAGE);
			}
			mIdTextFiled.setText("");
			mIn_noTextField.setText("");
			mInventoryTextField.setText("");
			return;
		}

	}
}
