package fallblank;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DBHandler {
	private String mDriver; // ���ݿ���������
	private String mUrl; // ָ�����ݿ�·��
	private String mUser; // ���ݿ��½�û���
	private String mPassword; // ���ݿ��½����

	private static DBHandler sDBHandler;
	private Connection mConnection;
	private Statement mStatement;

	public static DBHandler getInstace() {
		if (sDBHandler == null) {
			sDBHandler = new DBHandler();
		}
		return sDBHandler;

	}

	private DBHandler() {
		/**
		 * ���ã�����DBhandler���󣬲���ʼ��statement����
		 * */
		try {
			initParam("D:/Data/Eclipse Android/Supermarket/mysql.ini");
			Class.forName(mDriver);
			mConnection = DriverManager.getConnection(mUrl,mUser,mPassword);
			mStatement = mConnection.createStatement();
		} catch (Exception e) {
			System.out.println("���ݿ��ʼ������");
			e.printStackTrace();
		}
	}
	
	public Statement getStatement(){
		return mStatement;
	}

	private void initParam(String paramFile) throws Exception {
		/**
		 * ���ܣ���ʼ�����ݿ�������Ϣ���������������ƣ����ӵ����ݿ����ƣ��û��������롣 ע������������ļ�����Ŀ�£����У�mysql��init
		 * */
		Properties props = new Properties();
		props.load(new FileInputStream(paramFile));
		mDriver = props.getProperty("driver");
		mUrl = props.getProperty("url");
		mUser = props.getProperty("user");
		mPassword = props.getProperty("password");
	}

}
