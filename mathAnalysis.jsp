<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<f:view>


		<h:form>
			<h:commandButton id="descriptiveStatistics"
				action="#{actionBeanMath1.generateDescriptiveStatistics}"
				value="Descriptive Statistics" />
				<br>
				<br>
				<a href="Regression.jsp">Back</a>
			<%-- <h:commandButton id="regresssionAnalysis"
				action="#{}"
				value="Regression Analysis" /> --%>
		</h:form>
		<t:dataTable value="#{actionBeanMath1.descrStatsBeanList}" var="row"
			rendered="#{actionBeanMath1.renderStats}" border="1" cellspacing="0"
			cellpadding="1" columnClasses="columnClass1 border"
			headerClass="headerClass" footerClass="footerClass"
			rowClasses="rowClass2" styleClass="dataTableEx" width="900">
			<t:columns var="col" value="#{actionBeanMath1.columnNames}">
				<f:facet name="header">
					<t:outputText styleClass="outputHeader" value="#{col}" />
				</f:facet>
				<t:outputText styleClass="outputText" value="#{row[col]}" />
			</t:columns>
		</t:dataTable>
<br>
<a href="DisplayImport.jsp">Back</a>


	</f:view>
</body>
</html>