package edu.uic.ids.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class DatabaseAccess {

	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSetMetaData resultSetMetaData;
	private List<String> columnNamesSelected;
	private FacesContext context;
	private String jdbcDriver;
	private String url;
	private String schema;
	private Databaseinfo databaseinfo;
	private static final String MY_SQL = "MySQL";
	private static final String DB2 = "DB2";
	private static final String ORACLE = "Oracle";
	private String message;
	private static final String ACCESS_DENIED = "28000";
	private static final String INVALID_DB_SCHEMA = "42000";
	private static final String TIMEOUT = "08S01";
	private ResultSet resultSet;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private int numOfCols = 0;
	private int numOfRows = 0;
	private Result result;
	private int changedRows;
	public int getChangedRows() {
		return changedRows;
	}

	public void setChangedRows(int changedRows) {
		this.changedRows = changedRows;
	}

	public Result getResult() {
		return result;
	}
	
	public int getNumOfCols() {
		return numOfCols;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		databaseinfo = (Databaseinfo) m.get("databaseinfo");
	}


	public String connectDb() {
		//List<String> result;
		String dbms = databaseinfo.getDbms();
		schema = databaseinfo.getDbSchema();
		message = "";
		switch (dbms) {
		case MY_SQL:
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + databaseinfo.getHost() + ":3306" + "/"
					+ databaseinfo.getDbSchema() + "?&useSSL=false";
			System.out.println("url :" + url);
			// MY_SQL port: ":3306/"
			break;
		case DB2:
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://" + databaseinfo.getHost() + ":50000" + "/"
					+ databaseinfo.getDbSchema();
			// DB2 port: ":50000/"
			break;
		case ORACLE:
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + databaseinfo.getHost() + ":1521:" + "/"
					+ databaseinfo.getDbSchema();
			// ORACLE port: ":1521:"
			break;
		}
		try {

			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, databaseinfo.getUserName(), databaseinfo.getPassword());
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			
			// Newly added
			System.out.println("Connection Successful");
			
		
			
			System.out.println("Press next");
			
			//Newly added ends
			
			return "SUCCESS";
		} catch (ClassNotFoundException ce) {
			message = "Database: " + databaseinfo.getDbms() + " not supported.";
			System.out.println("Message in Classnotfound: " + message);
			return "FAIL";
		} catch (SQLException se) {
			if (se.getSQLState().equals(ACCESS_DENIED)) {
				message = "Incorrect password";
			} else if (se.getSQLState().equals(INVALID_DB_SCHEMA)) {
				message = "Incorrect databaseschema";
			} else if (se.getSQLState().equals(TIMEOUT)) {
				message = "Incorrect Check host and port";
			} else {
				message = "SQL Exception occurred!\n" + "Error Code: " + se.getErrorCode() + "\n" + "SQL State: "
						+ se.getSQLState() + "\n" + "Message :" + se.getMessage() + "\n\n";
			}
			System.out.println("Message in SQL: " + message);
			return "FAIL";
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			System.out.println("Message in Exception: " + message);
			close();
			return "FAIL";
		}

	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void close() {
		try {
			if (statement != null) {

				statement.close();
			}
			if (connection != null) {

				connection.close();
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			System.out.println("Message in close: " + message);

		}
	}

	public Connection getConnection() {
		return connection;
	}

	public String getMessage() {
		return message;
	}

	public List<String> tableList() {
		List<String> tableList = null;
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getTables(null, databaseinfo.getUserName(), null, TABLE_TYPES);
				resultSet.last();
				int numberOfRows = resultSet.getRow();
				tableList = new ArrayList<String>(numberOfRows);
				resultSet.beforeFirst();
				String tableName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						tableName = resultSet.getString("TABLE_NAME");
						System.out.println("Tablename: " +tableName);
						if (!databaseinfo.getDbms().equalsIgnoreCase("oracle") || tableName.length() < 4)
							tableList.add(tableName);
						else if (!tableName.substring(0, 4).equalsIgnoreCase("BIN$"))
							tableList.add(tableName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
		}
		return tableList;
	}
	
		
	public List<String> columnList(String tableName) {
		List<String> columnList = new ArrayList<String>();
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getColumns(null, databaseinfo.getDbSchema(), tableName, null);

				String columnName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						columnName = resultSet.getString("COLUMN_NAME");
						columnList.add(columnName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
		}
		return columnList;
	}
	
	public ResultSet getTables()
	{
		try {
			DatabaseMetaData meta = (DatabaseMetaData) connection.getMetaData();
			resultSet = meta.getTables(null, null, "%", null);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" +
					"SQL State: " + se.getSQLState() + "\n" +
					"Message :" + se.getMessage() + "\n\n" +
					"SQLException while getting tables.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}
		
	public ResultSet getTableData(String sqlQuery)
	{
		try {
			resultSet = statement.executeQuery(sqlQuery);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" +
					"SQL State: " + se.getSQLState() + "\n" +
					"Message :" + se.getMessage() + "\n\n" +
					"SQLException while getting table data.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}
	
	public ResultSet processSelect(String query)
	{
		try {
			resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" +
					"SQL State: " + se.getSQLState() + "\n" +
					"Message :" + se.getMessage() + "\n\n" +
					"SQLException while processing query.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}
	
	public ResultSet getColumnNames(String sqlQuery)
	{
		try
		{
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" +
					"SQL State: " + se.getSQLState() + "\n" +
					"Message :" + se.getMessage() + "\n\n" +
					"SQLException while getting table columns.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}
		
		
	
	
	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String execute(String query) {
		try {
			if (connection != null && statement != null) {
				if (query.toLowerCase().startsWith("select")) {
					resultSet = statement.executeQuery(query);
					if (resultSet != null) {
						resultSetMetaData = resultSet.getMetaData();
						numOfCols = resultSetMetaData.getColumnCount();
						resultSet.last();
						numOfRows = resultSet.getRow();
						resultSet.beforeFirst();
						columnNamesSelected = new ArrayList<String>(numOfCols);
						for (int i = 0; i < numOfCols; i++) {
							columnNamesSelected.add(resultSetMetaData.getColumnName(i + 1));
						}
					}
				} else {
					// UPDATE,INSERT,DELETE
					statement.executeUpdate(query);
				}
			}
			return "SUCCESS";
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";		
			e.printStackTrace();
			return "FAIL";
		}
	}
	public String processQuery(String query) {
		System.out.println("***Entered processQuery()***" + query);
		String status = "FAIL";
		try {
			if (this.connection == null) {
				connectDb();
			}
		} catch (NullPointerException e) {
			this.message = e.getMessage();
		}
		String queryType = query.split(" ")[0];
		switch (queryType.toLowerCase()) {
		case "select":
			try {
				this.resultSet = this.statement.executeQuery(query);
				System.out.println("resultSet: " + resultSet);
				if (this.resultSet != null) {
					getColumnNames();
					status = "SUCCESS";
				}
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "insert":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "update":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "delete":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "create":
			try {
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "drop":
			try {
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.message = e.getMessage();
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return status;
	}
	
	private void getColumnNames() {
		System.out.println("*** Entered getColumnNames() ***");
		try {
			if (this.resultSet != null) {
				this.resultSetMetaData = this.resultSet.getMetaData();
				int columnCount = this.resultSetMetaData.getColumnCount();
				System.out.println("resultSetMetaData: " + resultSetMetaData);
				System.out.println(" columnCount : " + columnCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void generateResult() {
		if (resultSet != null) {
			result = ResultSupport.toResult(resultSet);
		}
	}
	
	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}	
	
	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}
}
