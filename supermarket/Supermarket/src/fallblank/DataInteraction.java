package fallblank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.mysql.fabric.xmlrpc.base.Data;

public class DataInteraction {
	private DBHandler mDBHanler;
	private Statement mStatement;
	/** 表命映射数组，于常量对应 */
	private String[] mTableName;

	/** 标识记录条数的变量 */
	public int length;
	
	/**记录executeSQL的结果*/
	public ResultSet mResultSet;
	
	public String mCheatTableName;

	/** 一族标识入库对应表的常量 */
	public static final int AQUATIC = 0;
	public static final int MEAT = 1;
	public static final int FRUIT = 2;
	public static final int DRY = 3;
	public static final int CLOTH = 4;
	public static final int DEPARTMENT = 5;
	public static final int ELETRIC = 6;

	/** 获得数据库连接实例 */
	public DataInteraction() {
		mDBHanler = DBHandler.getInstace();
		mStatement = mDBHanler.getStatement();
		mTableName = new String[] { "aquatic", "meat", "fruit", "dry", "cloth",
				"department", "electric" };
	}

	/** 日期字符串装换为日期 */
	private Date parseDate(String strDate) {
		String pattern = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 计算指定日期到今天之间的天数差 */
	private int daysToToday(Date date) {
		Date today = new Date();
		return (int) ((today.getTime() - date.getTime()) / 86400000);
	}

	public String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	/** 查询已过期或即将过期的商品 */
	public ArrayList<HashMap<String, String>> checkValidDate() {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		String in_no = null;
		String id = null;
		String name = null;
		String inventory = null;
		String p_date = null;
		String exp_date = null;

		for (int i = 0; i < 4; i++) {
			String SQLString = "select * from " + mTableName[i] + ";";
			try {
				ResultSet resultSet = mStatement.executeQuery(SQLString);
				while (resultSet.next()) {
					in_no = resultSet.getString(1);
					id = resultSet.getString(2);
					name = resultSet.getString(3);
					inventory = resultSet.getString(4);
					p_date = resultSet.getString(5);
					exp_date = resultSet.getString(6);
					HashMap<String, String> map = new HashMap<String, String>();
					int expDate = Integer.parseInt(exp_date);
					Date date = parseDate(p_date);
					int days = daysToToday(date);
					if ((days + 5) >= expDate) {
						map.put("in_no", in_no);
						map.put("id", id);
						map.put("name", name);
						map.put("inventory", inventory);
						map.put("p_date", p_date);
						map.put("exp_date", exp_date);
						map.put("valid_date", "" + (expDate - days));
						resultList.add(map);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;

	}

	/** 全部商品 */
	public ArrayList<HashMap<String, String>> getAllRecords() {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		String id = null;
		String inventory = null;
		String SQLString = "select * from total_amount;";

		try {
			ResultSet rs = mStatement.executeQuery(SQLString);
			while (rs.next()) {
				id = rs.getString(1);
				inventory = rs.getString(2);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("inventory", inventory);
				resultList.add(map);
			}
		} catch (SQLException e) {
			System.out.println("全部商品查询出错");
			e.printStackTrace();
		}
		return resultList;
	}

	/** 获得注定编号商品的数量 */
	public ArrayList<HashMap<String, String>> getItemAmount(String _id) {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		String amount = null;
		String SQLString = "select inventory from total_amount where id='"
				+ _id + "';";
		try {
			ResultSet rs = mStatement.executeQuery(SQLString);
			while (rs.next()) {
				amount = rs.getString(1);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("amount", amount);
				resultList.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/** 给出商品ID或入库单号查询入库历史 */
	public ArrayList<HashMap<String, String>> inStorageInquery(String _id) {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		String in_no = null;
		String id = null;
		String name = null;
		String inventory = null;
		String p_date = null;
		String SQLString = null;
		mCheatTableName = null;
		for (int i = 0; i < mTableName.length; i++) {
			if (_id.length() < 8) {
				SQLString = "select * from " + mTableName[i] + " where id='"
						+ _id + "';";
			} else {
				SQLString = "select * from " + mTableName[i] + " where in_no='"
						+ _id + "';";
			}
			try {
				ResultSet rs = mStatement.executeQuery(SQLString);
				while (rs.next()) {
					mCheatTableName = mTableName[i];
					in_no = rs.getString(1);
					id = rs.getString(2);
					name = rs.getString(3);
					inventory = rs.getString(4);
					p_date = rs.getString(5);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("in_no", in_no);
					map.put("id", id);
					map.put("name", name);
					map.put("inventory", inventory);
					map.put("p_date", p_date);
					resultList.add(map);
				}
			} catch (SQLException e) {
				System.out.println("给出商品ID查询入库历史出错");
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/** 给出商品ID或出库单号查询出库历史 */
	public ArrayList<HashMap<String, String>> outStorageInquery(String _id) {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		String out_no = null;
		String id = null;
		String name = null;
		String amount = null;
		String out_date = null;
		String SQLString = null;
		if (_id.length() < 8) {
			SQLString = "select * from outstore" + " where id='" + _id + "';";
		} else {
			SQLString = "select * from outstore" + " where out_no='" + _id
					+ "';";
		}
		try {
			ResultSet rs = mStatement.executeQuery(SQLString);
			while (rs.next()) {
				out_no = rs.getString(1);
				id = rs.getString(2);
				name = rs.getString(3);
				amount = rs.getString(4);
				out_date = rs.getString(5);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("out_no", out_no);
				map.put("id", id);
				map.put("name", name);
				map.put("amount", amount);
				map.put("out_date", out_date);
				resultList.add(map);
			}
		} catch (SQLException e) {
			System.out.println("出库历史查询出错");
			e.printStackTrace();
		}
		return resultList;
	}

	/** 获取当天的第几笔交易 */
	public int getInStorgeTimes(Date date) {

		String todayStr = getDateString(date);
		int times = 0;
		Vector<String> vector = new Vector<String>();

		for (int i = 0; i < mTableName.length; i++) {
			String SQLString = "select in_no from " + mTableName[i] + ";";
			try {
				ResultSet rs = mStatement.executeQuery(SQLString);
				while (rs.next()) {
					vector.add(rs.getString(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (String string : vector) {
			if (string.contains(todayStr)) {
				times++;
			}
		}
		return times;
	}

	/** 获取当天的第几笔出库记录 */
	public int getOutStorgeTimes(Date date) {

		String todayStr = getDateString(date);
		int times = 0;
		Vector<String> vector = new Vector<String>();
		String SQLString = "select out_no from  outstore;";
		try {
			ResultSet rs = mStatement.executeQuery(SQLString);
			while (rs.next()) {
				vector.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("统计出库操作出错");
			e.printStackTrace();
		}
		for (String string : vector) {
			if (string.contains(todayStr)) {
				times++;
			}
		}
		return times;
	}

	/** 插入记录 */
	public void executeSQL(String SQLString) {
		try {
			mStatement.execute(SQLString);
			mResultSet = mStatement.getResultSet();
		} catch (SQLException e) {
			System.out.println("SQL执行出错");
			e.printStackTrace();
		}
	}

	/** 出库操作 */
	public String outStorage(String _id, String _amount) {
		int amount = Integer.parseInt(_amount);
		int times = getOutStorgeTimes(new Date());
		Vector<String> vector01 = new Vector<String>();
		Vector<String> vector02 = new Vector<String>();
		String out_no = null;
		String name = null;
		label: for (int i = 0; i < mTableName.length; i++) {
			String SQLString = "select * from " + mTableName[i] + " where id='"
					+ _id + "' order by(in_no) asc;";
			try {
				ResultSet rs = mStatement.executeQuery(SQLString);
				while (rs.next()) {
					String in_no = rs.getString(1);
					name = rs.getString(3);
					int inventory = Integer.parseInt(rs.getString(4));
					if (inventory <= amount) {
						vector01.add(in_no);// 记录一会删除
						vector02.add(mTableName[i]);
						amount -= inventory;
					} else {
						String updateString = "update " + mTableName[i]
								+ " set inventory=" + (inventory - amount)
								+ " where in_no='" + in_no + "';";
						amount=0;
						executeSQL(updateString);
						break label;
					}
				}
			} catch (SQLException e) {
				System.out.println("出库操作不对");
				e.printStackTrace();
			}
		}
		for (int i = 0; i < vector01.size(); i++) {
			String delStr = "delete from " + vector02.get(i) + " where in_no='"
					+ vector01.get(i) + "';";
			executeSQL(delStr);

		}
		if (times < 9) {
			out_no = "0" + getDateString(new Date()) + "0" + (times + 1) + _id;
		} else {
			out_no = "0" + getDateString(new Date()) + "" + (times + 1) + _id;
		}
		String recordString = "insert into outstore values('" + out_no + "','"
				+ _id + "','" + name + "'," + _amount + ",'"
				+ getDateString(new Date()) + "');";
		executeSQL(recordString);
		return amount + "," + out_no;
	}
}
