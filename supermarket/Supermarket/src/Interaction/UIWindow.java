package Interaction;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;

import fallblank.DataInteraction;

public class UIWindow extends JFrame {
	private JButton mCheckDateButton;
	private JButton mInStorageButton;
	private JButton mOutStorageButton;
	private JButton mAllContentButton;
	private JButton mConfirmButton;
	private Vector mRowData;
	private Vector mColumnNames;// 建立一个以Vector为输入来源的数据表格，可显示行的名称。
	private JComboBox<String> mChoiceBox;
	private MyTextField mInputField;
	private JTable mResultTable;
	private DataInteraction dataCenter;
	private JMenu mMenuOne, mMenuTwo;// 名字奇葩！
	private JMenuItem mInventoryCheck;

	public UIWindow() {
		dataCenter = new DataInteraction();
		mRowData = new Vector<Vector<String>>();
		mColumnNames = new Vector<String>();
		Vector<String> v = new Vector<String>();
		init();
		this.setTitle(ConstValues.TITLE_WINDOW);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void init() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JMenuBar menuBar = new JMenuBar();
		mMenuOne = new JMenu(ConstValues.MENUONE_NAME);
		mInventoryCheck = new JMenuItem(ConstValues.MENUONE_ITEM_01_NAME);
		mInventoryCheck.addActionListener(new InventoryCheckListener()); 
		mMenuOne.add(mInventoryCheck);
		menuBar.add(mMenuOne);
		mMenuTwo = new JMenu(ConstValues.MENUTWO_NAME);
		mMenuTwo.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent arg0) {
				JOptionPane.showMessageDialog(UIWindow.this,
						"本次作业分工介绍：\n"
						+ "文档书写：桂红丽，卜晓璇\n"
						+ "数据库设计：潘杰\n"
						+ "界面设计：杨皓庆\n"
						+ "程序实现：宋正腾\n",
						"关于",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		menuBar.add(mMenuTwo);
		this.setJMenuBar(menuBar);

		mAllContentButton = new JButton(ConstValues.ALLCONTEXT_BUTTON_TITLE);
		mAllContentButton.addActionListener(new AllContentListener());
		mCheckDateButton = new JButton(ConstValues.CHECKDATE_BUTTON_TITLE);
		mCheckDateButton.addActionListener(new CheckDateListener());
		mInStorageButton = new JButton(ConstValues.INPUT_BUTTON_TITLE);
		mInStorageButton.addActionListener(new InStorageListener());
		mOutStorageButton = new JButton(ConstValues.OUTPUT_BUTTON_TITLE);
		mOutStorageButton.addActionListener(new OutStorageListener());
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(mAllContentButton);
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(mCheckDateButton);
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(mInStorageButton);
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(mOutStorageButton);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(vbox);
		this.add(rightPanel, BorderLayout.EAST);

		mChoiceBox = new JComboBox<String>();
		mChoiceBox.addItem(ConstValues.COMBO_BOX_ITEM_01);
		mChoiceBox.addItem(ConstValues.COMBO_BOX_ITEM_02);
		mChoiceBox.addItemListener(new ChoiceBoxListener());
		mInputField = new MyTextField(30);
		mInputField.setHint(ConstValues.SEARCHTEXTFIELD_HINT_01);
		mConfirmButton = new JButton(ConstValues.CONFIRM_BUTTON_TITLE);
		mConfirmButton.addActionListener(new ComfirmButtonListener());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		topPanel.add(mChoiceBox);
		topPanel.add(mInputField);
		topPanel.add(mConfirmButton);
		this.add(topPanel, BorderLayout.NORTH);

		mResultTable = new JTable(mRowData, mColumnNames);
		mResultTable.setRowHeight(30);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		centerPanel.add(new JScrollPane(mResultTable));
		this.add(centerPanel, BorderLayout.CENTER);
	}

	/** 功能选择ComboBox监听器 */
	class ChoiceBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent item) {
			String strItem = (String) item.getItem();
			if (strItem.equals(ConstValues.COMBO_BOX_ITEM_01)) {
				mInputField.setHint(ConstValues.SEARCHTEXTFIELD_HINT_01);
			} else {
				mInputField.setHint(ConstValues.SEARCHTEXTFIELD_HINT_02);
			}
		}

	}

	
	/** 过期检查 */
	class CheckDateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			ArrayList<HashMap<String, String>> resultList = dataCenter
					.checkValidDate();
			int length = resultList.size();
			mRowData.clear();
			for (int i = 0; i < length; i++) {
				HashMap<String, String> map = resultList.get(i);
				String in_no = map.get("in_no");
				String id = map.get("id");
				String name = map.get("name");
				String valid_date = map.get("valid_date");
				Vector<String> v = new Vector<String>();
				v.add(in_no);
				v.add(id);
				v.add(name);
				v.add(valid_date);
				mRowData.add(v);
			}
			mColumnNames.clear();
			mColumnNames.add("入库单号");
			mColumnNames.add("商品编号");
			mColumnNames.add("商品名称");
			mColumnNames.add("据到期时间");

			DefaultTableModel model = (DefaultTableModel) mResultTable
					.getModel();
			model.fireTableStructureChanged();
			model.fireTableDataChanged();
			mResultTable.removeAll();

			validate();
		}

	}

	/** 全部商品 */
	class AllContentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
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

			DefaultTableModel model = (DefaultTableModel) mResultTable
					.getModel();
			model.fireTableStructureChanged();
			model.fireTableDataChanged();
			mResultTable.removeAll();

			validate();
		}

	}

	
	/** 确认按钮 */
	class ComfirmButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String inputId = mInputField.getText().trim();
			ArrayList<HashMap<String, String>> resultList;
			String choiceStr = (String) mChoiceBox.getSelectedItem();
			if (choiceStr.equals(ConstValues.COMBO_BOX_ITEM_01)) {
				resultList = dataCenter.inStorageInquery(inputId);
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
			}else{
				resultList = dataCenter.outStorageInquery(inputId);
				int length = resultList.size();
				mRowData.clear();
				for (int i = 0; i < length; i++) {
					HashMap<String, String> map = resultList.get(i);
					String out_no = map.get("out_no");
					String id = map.get("id");
					String name = map.get("name");
					String amount = map.get("amount");
					String out_date = map.get("out_date");
					Vector<String> v = new Vector<String>();
					v.add(out_no);
					v.add(id);
					v.add(name);
					v.add(amount);
					v.add(out_date);
					mRowData.add(v);
				}
				mColumnNames.clear();
				mColumnNames.add("出库单号");
				mColumnNames.add("商品编号");
				mColumnNames.add("商品名称");
				mColumnNames.add("出库数量");
				mColumnNames.add("出库日期");
			}
			DefaultTableModel model = (DefaultTableModel) mResultTable
					.getModel();
			model.fireTableStructureChanged();
			model.fireTableDataChanged();
			mResultTable.removeAll();

			validate();
		}

	}

	/** 入库 */
	class InStorageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int x = getX() + (getWidth() / 4);
			int y = getY() + (getHeight() / 4);
			InStorageWindow inStorageWindow = new InStorageWindow(x, y);
		}

	}

	/**出库*/
	class OutStorageListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int x = getX() + (getWidth() / 4);
			int y = getY() + (getHeight() / 4);
			OutStorageWindow outStorageWindow = new OutStorageWindow(x, y);
		}
		
	}

	/**盘存处理*/
	class InventoryCheckListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int x = getX();
			int y = getY();
			InventoryCheckWindow inventoryCheckWindow = new InventoryCheckWindow(x,y);
		}
		
	}
		
}
