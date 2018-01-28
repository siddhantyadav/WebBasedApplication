<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Tables</title>
</head>

<f:view>
<font face="calibri" color="green">
	<h2 align="center">Data Analysis</h2>
	<hr>
	<a href="login.jsp">Logout</a>
	<a href="index.jsp">Home Page</a>
	<a href="DisplayImport.jsp">Back</a>

	<hr />
	<BR />
	<div align="center">
		<h:form>
			<h:panelGrid columns="3">
				<h:commandButton type="submit" value="Table List"
					action="#{dbAccessActionbean.listTables}" />
				<h:commandButton type="submit" value="Column List"
					action="#{dbAccessActionbean.listColumns}" />
				<%-- <h:commandButton type="submit" value="View selected Table"
					action="#{dbAccessActionbean.selectAllColumn1}" /> --%>
				
			</h:panelGrid>
			<h:panelGrid columns="20">

				<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
					value="#{dbAccessActionbean.tableName}"
					rendered="#{dbAccessActionbean.tableListRendered}">
					<f:selectItems value="#{dbAccessActionbean.tableViewList}" />
				</h:selectOneListbox>

				<h:selectManyListbox size="10" styleClass="selectManyListbox"
					value="#{dbAccessActionbean.columnNamesSelected}"
					rendered="#{dbAccessActionbean.columnListRendered}">
					<f:selectItems value="#{dbAccessActionbean.columnNames}" />
				</h:selectManyListbox>
			</h:panelGrid>
				<h:outputLabel value="Number of Rows : " />
			<h:outputText value="#{dbAccessActionbean.noOfRows}" />
			<h:outputLabel value="Number of Columns :  " />
			<h:outputText value="#{dbAccessActionbean.noOfCols}" />
			<br/>
			<br />
			<hr />
		<h:commandButton id="descriptiveStatistics"
				action ="#{actionBeanMath.generateDescriptiveStatistics}"
				value="Descriptive Statistics" />
				<br>
				<br>
			<a href="Regression.jsp">Regression</a>
			<%-- <h:commandButton id="regresssionAnalysis"
				action="#{actionBeanMath.performRegressionAnalysis}"
				value="Regression Analysis" /> --%>
		
			<div
				style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
			<t:dataTable value="#{actionBeanMath.descrStatsBeanList}" var="row"
			rendered="#{actionBeanMath.renderStats}" border="1" cellspacing="0"
			cellpadding="1" columnClasses="columnClass1 border"
			headerClass="headerClass" footerClass="footerClass"
			rowClasses="rowClass2" styleClass="dataTableEx" width="900">
			<t:columns var="col" value="#{actionBeanMath.columnNames}">
				<f:facet name="header">
					<t:outputText styleClass="outputHeader" value="#{col}" />
				</f:facet>
				<t:outputText styleClass="outputText" value="#{row[col]}" />
			</t:columns>
		</t:dataTable>	
			</div>
			
		
		</h:form>
	</div>


</f:view>
</body>
</html>