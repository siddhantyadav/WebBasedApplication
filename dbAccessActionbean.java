package edu.uic.ids.database;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.jsp.jstl.sql.Result;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class dbAccessActionbean {

	private String tableName;
	private String sqlQuery;
	private FacesContext context;
	private List<String> columnNames;
	private List<String[]> queries;
	private List<String> tableViewList;
	private List<String> columnNamesSelected;
	private int noOfCols = 0;
	private int noOfRows = 0;
	private Result result;
	private ResultSet resultSet1;
	private ResultSetMetaData resultSetMetaData1;
	private String tableinListColumn;
	private String username="";
	
	
	private boolean tableListRendered;
	private boolean columnListRendered = false;
	private boolean queryRendered = false;
	private DatabaseAccess databaseAccess;
	
	
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		databaseAccess = (DatabaseAccess) m.get("databaseAccess");
		listTables();

	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String listTables() {
		try {
			tableViewList = databaseAccess.tableList();
			if (null != tableViewList) {
				tableListRendered = true;
			}
			return "SUCCESS";
		} catch (Exception e) {
			tableListRendered = false;
			return "FAIL";
		}
	}

	public String listColumns() {

		try {
			if (null != tableName && !"".equals(tableName)) {
				columnNames = databaseAccess.columnList(tableName);
				tableinListColumn = tableName;
				queryRendered = false;
				sqlQuery = "";
				if (null != columnNames) {
					columnListRendered = true;
				}
			} else {
				return "FAIL";

			}
		} catch (Exception e) {
			columnListRendered = false;
			return "FAIL";
		}

		return "SUCCESS";

	}

	public String selectAllColumn() {
		for(String str:columnNamesSelected){
			 System.out.println(str);
		}
		listColumns();
		if (null != tableinListColumn && !"".equals(tableinListColumn)) {
			sqlQuery = "select * from " + tableinListColumn + " ;";
			databaseAccess.execute(sqlQuery);
			noOfCols = databaseAccess.getNumOfCols();
			noOfRows = databaseAccess.getNumOfRows();
			System.out.println(noOfRows);
			System.out.println(noOfCols);
			databaseAccess.generateResult();
			result = databaseAccess.getResult();
			columnNamesSelected = databaseAccess.columnList(tableinListColumn);
			queryRendered = true;
			
			
			return "SUCCESS";
			
		} else {
			
			return "FAIL";
		}
	}
	
	public String selectAllColumn1() {
		
		listColumns();
		if (null != tableinListColumn && !"".equals(tableinListColumn)) {
			sqlQuery = "select ";
			
			for(int i =0;i<columnNamesSelected.size();i++){
				if(i<columnNamesSelected.size()-1)
				sqlQuery +=columnNamesSelected.get(i)+",";
				else
					sqlQuery +=columnNamesSelected.get(i)+" ";
				 //System.out.println(str);
			}
			System.out.println(sqlQuery+" query");
			sqlQuery+="from " + tableinListColumn + ";";
			databaseAccess.execute(sqlQuery);
			noOfCols = databaseAccess.getNumOfCols();
			noOfRows = databaseAccess.getNumOfRows();
			System.out.println(noOfRows);
			System.out.println(noOfCols);
			databaseAccess.generateResult();
			result = databaseAccess.getResult();
			//columnNamesSelected = databaseAccess.columnList(tableinListColumn);
			queryRendered = true;
			int numOfRows1;
			int numOfCols;
			resultSet1=databaseAccess.getResultSet();
			resultSetMetaData1=databaseAccess.getResultSetMetaData();
			
			try {
				System.out.println("sqlQuery"+sqlQuery);
				databaseAccess.execute(sqlQuery);
				resultSet1=databaseAccess.getResultSet();
				queries = new ArrayList<String[]>();
				
				if (resultSet1!= null) {
					System.out.println("not null");
					numOfCols = resultSetMetaData1.getColumnCount();
					resultSet1.last();
					numOfRows1 = resultSet1.getRow();
					resultSet1.beforeFirst();
					while (resultSet1.next()) {
						String[] output = new String[numOfCols];

						for (int i = 0; i < numOfCols; i++) {
							 output[i]=resultSet1.getString(i + 1);
					
						}
						queries.add(output);
						//System.out.println(queries.get(8));
					}

					System.out.println(numOfRows1+" "+numOfCols);
				}
			return "SUCCESS";
			}catch(Exception e){
				System.out.println("game of thrones");
				e.printStackTrace();
				
			return "FAIL";
			}
			}
		 else {
			
			return "FAIL";
		}
	}

	public List<String[]> getQueries() {
		return queries;
	}


	public void setQueries(List<String[]> queries) {
		this.queries = queries;
	}


	public String logout() {
		databaseAccess.close();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "LOGOUT";
	}

	public String back() {
		return "BACK";
	}

	public String next() {
		return "NEXT";
	}

	public Result getResult() {
		return result;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public int getNoOfCols() {
		return noOfCols;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public String dropTables() {

		String status = "SUCCESS";
		try {
			databaseAccess.getConnection().setAutoCommit(false);
			if (null != tableName && !tableName.isEmpty()) {
				databaseAccess.execute("set foreign_key_checks=0");
				status = databaseAccess.execute("drop table " + tableName);
				if (status.equals("FAIL")) {
					databaseAccess.getConnection().rollback();
					databaseAccess.getConnection().setAutoCommit(true);
					return "FAIL";
				}
				listTables();
				databaseAccess.getConnection().commit();
				databaseAccess.getConnection().setAutoCommit(true);

			} else {
				
				status = "FAIL";
			}
		} catch (Exception e) {
			
			status = "FAIL";
		}
		return status;
	}

	

	}

	

