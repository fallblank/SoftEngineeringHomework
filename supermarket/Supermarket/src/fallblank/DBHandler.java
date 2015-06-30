package fallblank;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DBHandler {
	private String mDriver; // 数据库连接驱动
	private String mUrl; // 指定数据库路径
	private String mUser; // 数据库登陆用户名
	private String mPassword; // 数据库登陆密码

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
		 * 作用：构造DBhandler对象，并初始化statement对象
		 * */
		try {
			initParam("D:/Data/Eclipse Android/Supermarket/mysql.ini");
			Class.forName(mDriver);
			mConnection = DriverManager.getConnection(mUrl,mUser,mPassword);
			mStatement = mConnection.createStatement();
		} catch (Exception e) {
			System.out.println("数据库初始化出错");
			e.printStackTrace();
		}
	}
	
	public Statement getStatement(){
		return mStatement;
	}

	private void initParam(String paramFile) throws Exception {
		/**
		 * 功能：初始化数据库连接信息，包括：驱动名称，连接的数据库名称，用户名，密码。 注意事项：该配置文件在项目下，名叫：mysql。init
		 * */
		Properties props = new Properties();
		props.load(new FileInputStream(paramFile));
		mDriver = props.getProperty("driver");
		mUrl = props.getProperty("url");
		mUser = props.getProperty("user");
		mPassword = props.getProperty("password");
	}

}
