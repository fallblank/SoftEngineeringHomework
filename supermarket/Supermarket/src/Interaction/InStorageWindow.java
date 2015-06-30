package Interaction;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fallblank.DataInteraction;

public class InStorageWindow extends JFrame {
	private JComboBox<String> mChoiceTable;
	private JTextField[] mInputField;
	private JButton mConfirmButton;
	private JButton mCancerButton;

	public InStorageWindow(int x, int y) {
		init();
		pack();
		setTitle("入库");
		setLocation(x, y);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void init() {
		mChoiceTable = new JComboBox<String>();
		for (int i = 0; i < ConstValues.TABLENAME.length; i++) {
			mChoiceTable.addItem(ConstValues.TABLENAME[i]);
		}
		mChoiceTable.addItemListener(new ChoiceTableListener());
		mInputField = new JTextField[] { new JTextField(15),
				new JTextField(15), new JTextField(15), new JTextField(15),
				new JTextField(15) };
		mConfirmButton = new JButton(ConstValues.CONFIRM_BUTTON_TITLE);
		mConfirmButton.addActionListener(new ComfirmButtonListener());
		mCancerButton = new JButton(ConstValues.CANCER_BUTTON_TITLE);
		mCancerButton.addActionListener(new CancerButtonListtener());
		JPanel[] panels = new JPanel[7];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
		}
		JLabel[] labels = new JLabel[6];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
		}

		labels[0].setText("商品种类：");
		panels[0].add(labels[0]);
		panels[0].add(mChoiceTable);
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		panels[0].setLayout(layout);

		labels[1].setText("商品编号：");
		panels[1].add(labels[1]);
		panels[1].add(mInputField[0]);
		panels[1].setLayout(layout);

		labels[2].setText("商品名称：");
		panels[2].add(labels[2]);
		panels[2].add(mInputField[1]);
		panels[2].setLayout(layout);

		labels[3].setText("入库数量：");
		panels[3].add(labels[3]);
		panels[3].add(mInputField[2]);
		panels[3].setLayout(layout);

		labels[4].setText("生产日期：");
		panels[4].add(labels[4]);
		panels[4].add(mInputField[3]);
		panels[4].setLayout(layout);

		labels[5].setText("保质时长：");
		panels[5].add(labels[5]);
		panels[5].add(mInputField[4]);
		panels[5].setLayout(layout);

		panels[6].add(mConfirmButton);
		panels[6].add(mCancerButton);

		Box vbox = Box.createVerticalBox();
		for (int i = 0; i < panels.length; i++) {
			vbox.add(panels[i]);
		}
		this.add(vbox);
	}

	class ChoiceTableListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			int index = mChoiceTable.getSelectedIndex();
			if (index > 3) {
				mInputField[4].setText("该类商品没有保质期");
				mInputField[4].setEditable(false);
				mInputField[4].setEnabled(false);
			} else {
				mInputField[4].setText("");
				mInputField[4].setEditable(true);
				mInputField[4].setEnabled(true);
			}
		}

	}

	class ComfirmButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			DataInteraction datacenter = new DataInteraction();
			int times = datacenter.getInStorgeTimes(new Date());
			String in_no;
			String tableName = (String) mChoiceTable.getSelectedItem();
			String[] str = new String[5];
			String SQLString;
			for (int i = 0; i < str.length - 1; i++) {
				str[i] = mInputField[i].getText().trim();
			}
			if (times >= 9) {
				in_no = "1" + datacenter.getDateString(new Date()) + ""
						+ (times + 1) + str[0];
			} else {
				in_no = "1" + datacenter.getDateString(new Date()) + "0"
						+ (times + 1) + str[0];
			}
			if (mChoiceTable.getSelectedIndex() < 4) {
				str[4] = mInputField[4].getText().trim();
				SQLString = "insert into " + tableName + " values('" + in_no
						+ "','" + str[0] + "','" + str[1] + "'," + str[2]
						+ ",'" + str[3] + "'," + str[4] + ");";
			} else {
				str[4] = "";
				SQLString = "insert into " + tableName + " values('" + in_no
						+ "','" + str[0] + "','" + str[1] + "'," + str[2]
						+ ",'" + str[3] + "';";
			}
			datacenter.executeSQL(SQLString); 
			JOptionPane.showMessageDialog(InStorageWindow.this, "入库 单号："
					+ in_no, "入库成功", JOptionPane.INFORMATION_MESSAGE);
			for(int i=0;i<mInputField.length;i++){
				mInputField[i].setText("");
			}

		}

	}

	class CancerButtonListtener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			InStorageWindow.this.dispose();
		}

	}

}
