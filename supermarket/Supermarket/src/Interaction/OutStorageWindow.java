package Interaction;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fallblank.DataInteraction;

public class OutStorageWindow extends JFrame {
	private JTextField mIdTextField;
	private JTextField mAmoutField;
	private JButton mComfirmButton;
	private JButton mCancerButton;
	private JLabel mMessageLabel;
	private DataInteraction dataCenter;

	public OutStorageWindow(int x, int y) {
		init();
		pack();
		setTitle("出库");
		setLocation(x, y);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		dataCenter = new DataInteraction();
	}

	private void init() {
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		mMessageLabel = new JLabel("提示信息：");
		JPanel panel_01 = new JPanel();
		panel_01.setLayout(layout);
		panel_01.add(mMessageLabel);

		mIdTextField = new MyTextField(15);
		mIdTextField.addFocusListener(new IdTextFieldListener());
		JLabel label_01 = new JLabel("商品编号：");
		JPanel panel_02 = new JPanel();
		panel_02.setLayout(layout);
		panel_02.add(label_01);
		panel_02.add(mIdTextField);

		mAmoutField = new MyTextField(15);
		JLabel label_02 = new JLabel("出库数量：");
		JPanel panel_03 = new JPanel();
		panel_03.setLayout(layout);
		panel_03.setLayout(layout);
		panel_03.add(label_02);
		panel_03.add(mAmoutField);

		mComfirmButton = new JButton("确认");
		mComfirmButton.addActionListener(new ComfirmButtonListener());
		mCancerButton = new JButton("取消");
		mCancerButton.addActionListener(new CancerButtonListtener());
		JPanel panel_04 = new JPanel();
		panel_04.add(mComfirmButton);
		panel_04.add(mCancerButton);
		Box vbox = Box.createVerticalBox();
		vbox.add(panel_01);
		vbox.add(panel_02);
		vbox.add(panel_03);
		vbox.add(panel_04);
		this.add(vbox);
	}

	/** 库存提示信息 */
	class IdTextFieldListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			ArrayList<HashMap<String, String>> resultList = dataCenter
					.getItemAmount(mIdTextField.getText().trim());
			if (resultList.size() == 1) {
				mMessageLabel.setText("当前商品库存为："
						+ resultList.get(0).get("amount") + "件");
				mComfirmButton.setEnabled(true);
			} else {
				mMessageLabel.setText("请检查输入商品编号是否准确！");
				mComfirmButton.setEnabled(false);
			}
		}
	}

	/** 取消按钮 */
	class CancerButtonListtener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			OutStorageWindow.this.dispose();
		}

	}

	/** 确定按钮 */
	class ComfirmButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String id = mIdTextField.getText().trim();
			String amount = mAmoutField.getText().trim();
			String result = dataCenter.outStorage(id, amount);
			String[] strArray = result.split(",");
			int v1 = Integer.parseInt(amount);
			int v2 = Integer.parseInt(strArray[0]);
			JOptionPane.showMessageDialog(OutStorageWindow.this, "出库 单号："
					+ strArray[1] + "\n出库数量：" + (v1 - v2), "入库成功",
					JOptionPane.INFORMATION_MESSAGE);
			ArrayList<HashMap<String, String>> resultList = dataCenter
					.getItemAmount(mIdTextField.getText().trim());
			int remain = Integer.parseInt(resultList.get(0).get("amount"));
			String SQLString = "select * from safty where id='" + id + "';";
			dataCenter.executeSQL(SQLString);
			ResultSet rs = dataCenter.mResultSet;
			int saftyInventory = 0;
			try {
				while (rs.next()) {
					saftyInventory = Integer.parseInt(rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(remain<saftyInventory){
				JOptionPane.showMessageDialog(OutStorageWindow.this,
						"商品"+id+"现库存量为："+remain+",不足库存保留字："+saftyInventory+"\n请及时补充!",
						"警告",
						JOptionPane.WARNING_MESSAGE);
			}
			mIdTextField.setText("");
			mAmoutField.setText("");
		}

	}
}
