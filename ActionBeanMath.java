package edu.uic.ids.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;
@ManagedBean
@SessionScoped
public class ActionBeanMath {
	private DescrStatsBean descrStatsBean;;
	private List<DescrStatsBean> descrStatsBeanList;
	private List<String> columnNames;
	private String descrStats[];
	private DatabaseImport databaseImport;
	private FacesContext context;
	private dbAccessActionbean dbAccessActionbean;

	private boolean renderStats;
	private boolean renderRegression;
	
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbAccessActionbean = (dbAccessActionbean) m.get("dbAccessActionbean");
		

	}


	public ActionBeanMath() {
		descrStatsBean = null;
		descrStatsBeanList = null;
		renderStats = false;
		renderRegression = false;
		columnNames = null;
		descrStats = DescrStatsBean.getColumnnames();
		columnNames = new ArrayList<String>(descrStats.length);
		for (int i = 0; i < descrStats.length; i++) {
			columnNames.add(descrStats[i]);
		}
	}

public String generateDescriptiveStatistics() {
// declare temp variables
	dbAccessActionbean.selectAllColumn1();
	
renderStats = false;
String dataRow [] = null;
descrStatsBeanList = new ArrayList<DescrStatsBean> ();
//List<String> headerList = databaseImport.getHeader();
List<String> headerList = dbAccessActionbean.getColumnNamesSelected();
for(int y=0;y<headerList.size();y++){
	System.out.println(headerList.get(y));
}
//List<String[]> dataList = databaseImport.getDataList();
List<String[]> dataList = dbAccessActionbean.getQueries();
int numberObs = dataList.size();
System.out.println(numberObs);
double dataValues [] = null;
String data = null;
for(int i=0; i<headerList.size(); i++) {
descrStatsBean = new DescrStatsBean();
descrStatsBean.setDataset(dbAccessActionbean.getTableName());
descrStatsBean.setVariable(headerList.get(i));
descrStatsBeanList.add(descrStatsBean);
dataValues = new double [numberObs];
for(int r = 0; r < numberObs; r++) {
	System.out.println(i+" "+ r);
data = dataList.get(r)[i];
dataValues[r] = Double.parseDouble(data);
}

// either declare above as temp or use directly
// StatUtils used here statically but could instantiate
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
descrStatsBean.setNumberObs(numberObs);
descrStatsBean.setMinValue(minValue);
descrStatsBean.setMaxValue(maxValue);
descrStatsBean.setMean(mean);
descrStatsBean.setVariance(variance);
descrStatsBean.setStandardDeviation(standardDeviation);
descrStatsBean.setMedian(median);
descrStatsBean.setQ1(q1);
descrStatsBean.setQ3(q3);
descrStatsBean.setIqr(iqr);
descrStatsBean.setRange(range);
}
renderStats = true;
return "SUCCESS";
}
	// . . .

public DescrStatsBean getDescrStatsBean() {
	return descrStatsBean;
}

public void setDescrStatsBean(DescrStatsBean descrStatsBean) {
	this.descrStatsBean = descrStatsBean;
}

public List<DescrStatsBean> getDescrStatsBeanList() {
	return descrStatsBeanList;
}

public void setDescrStatsBeanList(List<DescrStatsBean> descrStatsBeanList) {
	this.descrStatsBeanList = descrStatsBeanList;
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
}