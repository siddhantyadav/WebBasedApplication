/*package edu.uic.ids.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;

public class ActionBeanMath2 {
	private DescrStatsBean DescrStatsBean;
	private List<DescrStatsBean> DescrStatsBeanList;
	private List<String> columnNames;
	private String descrStats[];
	private DatabaseImport actionBeanFile;
	private boolean renderStats;
	private boolean renderRegression;
	private int numberObs;
	private List<String> columnNamesSelected;

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	private FacesContext context;
	private DatabaseAccess dbAccessBean;
	//private MessagesBean messageBean;
	private List<String> tableViewList;
	private boolean tableListRendered;
	private String tableNameSelected;

	public String getTableNameSelected() {
		return tableNameSelected;
	}

	public void setTableNameSelected(String tableNameSelected) {
		System.out.println("tableNameSelected" + tableNameSelected);
		this.tableNameSelected = tableNameSelected;
	}

	private String tableinListColumn;
	private boolean queryRendered;
	private String sqlQuery;
	private boolean columnListRendered;
	private List<String> columnNamesTable;

	public List<String> getColumnNamesTable() {
		return columnNamesTable;
	}

	public void setColumnNamesTable(List<String> columnNamesTable) {
		this.columnNamesTable = columnNamesTable;
	}

	public int getNumberObs() {
		return numberObs;
	}

	public void setNumberObs(int numberObs) {
		this.numberObs = numberObs;
	}

	public DescrStatsBean getDescrStatsBean() {
		return DescrStatsBean;
	}

	public void setDescrStatsBean(DescrStatsBean DescrStatsBean) {
		this.DescrStatsBean = DescrStatsBean;
	}

	public List<DescrStatsBean> getDescrStatsBeanList() {
		return DescrStatsBeanList;
	}

	public void setDescrStatsBeanList(List<DescrStatsBean> DescrStatsBeanList) {
		this.DescrStatsBeanList = DescrStatsBeanList;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getDescrStats() {
		return descrStats;
	}

	public void setDescrStats(String[] descrStats) {
		this.descrStats = descrStats;
	}

	public DatabaseImport getActionBeanFile() {
		return actionBeanFile;
	}

	public void setActionBeanFile(FileActionBean actionBeanFile) {
		this.actionBeanFile = actionBeanFile;
	}

	public boolean isRenderStats() {
		return renderStats;
	}

	public void setRenderStats(boolean renderStats) {
		this.renderStats = renderStats;
	}

	public boolean isRenderRegression() {
		return renderRegression;
	}

	public void setRenderRegression(boolean renderRegression) {
		this.renderRegression = renderRegression;
	}

	public StatisticsActionBean() {
		DescrStatsBean = new DescrStatsBean();
		DescrStatsBeanList = null;
		renderStats = false;
		renderRegression = false;
		columnNames = null;
		descrStats = DescrStatsBean.getColumnnames();
		columnNames = new ArrayList<String>(descrStats.length);
		for (int i = 0; i < descrStats.length; i++) {
			columnNames.add(descrStats[i]);
		}
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();
		messageBean = (MessagesBean) m.get("messageBean");
		if (messageBean == null)
			messageBean = new MessagesBean();
		listTables();
	}

	public String listTables() {
		try {
			messageBean.resetAll();
			tableViewList = dbAccessBean.tableList();
			System.out.println("TableList" + tableViewList);
			if (null != tableViewList) {
				tableListRendered = true;
			}
			return "SUCCESS";
		} catch (Exception e) {
			tableListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public String listColumns() {
		System.out.println("test" + tableNameSelected);
		try {
			messageBean.resetAll();
			System.out.println(tableNameSelected);
			if (null != tableNameSelected && !"".equals(tableNameSelected)) {
				columnNamesTable = dbAccessBean.columnList(tableNameSelected);
				System.out.println(columnNamesTable.toString());
				// tableinListColumn = tableName;

				if (null != columnNamesTable) {
					columnListRendered = true;
				}
			} else {
				messageBean.setErrorMessage("Please select Table Name from the list");
				messageBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} catch (Exception e) {
			columnListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";

	}

	public String generateDescriptiveStatistics() {
		renderStats = false;
		String dataRow[] = null;
		DescrStatsBeanList = new ArrayList<DescrStatsBean>();
		String header = "";
		for (int j = 0; j < columnNamesSelected.size(); j++) {
		
				header = columnNamesSelected.get(j);
			
			String query = "select " + header + " from " + tableNameSelected;
			System.out.println(query);
			dbAccessBean.execute(query);
			if (dbAccessBean.getResultSet() != null) {
				System.out.println("not null");
				ResultSet resultSet = dbAccessBean.getResultSet();
				// List<String[]> dataList = actionBeanFile.getDataList();
				// numberObs = dataList.size();
				System.out.println("210" + dbAccessBean.getNumberOfRows());
				double dataValues[] = new double[dbAccessBean.getNumberOfRows()];
				for (int i = 0; i < dbAccessBean.getNumberOfRows(); i++) {
					try {
						resultSet.next();
						dataValues[i] = resultSet.getDouble(1);
						System.out.println("225 " + dataValues[i]);
					} catch (Exception e) {

					}

				}
				// String data = null;
				double minValue = StatUtils.min(dataValues);
				double maxValue = StatUtils.max(dataValues);
				double mean = StatUtils.mean(dataValues);
				double variance = StatUtils.variance(dataValues, mean);
				double standardDeviation = Math.sqrt(variance);
				double median = StatUtils.percentile(dataValues, 50.0);
				double q1 = StatUtils.percentile(dataValues, 25.0);
				double q3 = StatUtils.percentile(dataValues, 75.0);
				double iqr = q3 - q1;
				double range = maxValue - minValue;
				DescrStatsBean.setDataset(tableNameSelected);
				DescrStatsBean.setVariable(header);
				DescrStatsBean.setNumberObs(dbAccessBean.getNumberOfRows());
				DescrStatsBean.setMinValue(minValue);
				DescrStatsBean.setMaxValue(maxValue);
				DescrStatsBean.setMean(mean);
				DescrStatsBean.setVariance(variance);
				DescrStatsBean.setStandardDeviation(standardDeviation);
				DescrStatsBean.setMedian(median);
				DescrStatsBean.setQ1(q1);
				DescrStatsBean.setQ3(q3);
				DescrStatsBean.setIqr(iqr);
				DescrStatsBean.setRange(range);
				try {
					DescrStatsBeanList.add(DescrStatsBean.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				renderStats = true;
				
			}
		}
		return "SUCCESS";
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public DbAccessBean getDbAccessBean() {
		return dbAccessBean;
	}

	public void setDbAccessBean(DbAccessBean dbAccessBean) {
		this.dbAccessBean = dbAccessBean;
	}

	public MessagesBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessagesBean messageBean) {
		this.messageBean = messageBean;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public void setTableViewList(List<String> tableViewList) {
		this.tableViewList = tableViewList;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public void setTableListRendered(boolean tableListRendered) {
		this.tableListRendered = tableListRendered;
	}

	public String getTableinListColumn() {
		return tableinListColumn;
	}

	public void setTableinListColumn(String tableinListColumn) {
		this.tableinListColumn = tableinListColumn;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public void setQueryRendered(boolean queryRendered) {
		this.queryRendered = queryRendered;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public boolean getColumnListRendered() {
		return columnListRendered;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}
}
*/