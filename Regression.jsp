<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
	<f:view>
		<h:form>
				<h:panelGrid columns="20">
				<h:commandButton value="Table list"
					action="#{actionRegrBean.getTables}" styleClass="button" />
				<h:commandButton value="Colummn list"
					action="#{actionRegrBean.getColumnNames}" styleClass="button"
					disabled="#{actionRegrBean.renderColumnListbutton}" />
				<h:commandButton value="Get Statistics Report"
					action="#{actionRegrBean.generateReport}" styleClass="button"
					disabled="#{actionRegrBean.renderReport}" />
				<h:commandButton
					value="Get Predictor and Response Variables for Regression Analysis"
					action="#{actionRegrBean.displayColumnsforRegression}"
					styleClass="button" />
				<h:commandButton value="Generate Regression Report"
					action="#{actionRegrBean.generateRegressionReport}"
					styleClass="button"
					disabled="#{actionRegrBean.renderRegressionButton}" />
				<h:commandButton value="Reset"
					action="#{actionRegrBean.resetButton}" styleClass="button" />

			</h:panelGrid>

			<pre>
						<h:outputText value="#{actionRegrBean.message}"
					rendered="#{actionRegrBean.renderMessage}" style="color:red" />
					</pre>
			<h:panelGrid columns="4">
				<h:selectOneListbox id="selectOneCb"
					style="width:150px; height:100px"
					value="#{actionRegrBean.tableSelected}"
					rendered="#{actionRegrBean.renderTablename}" size="5">
					<f:selectItems value="#{actionRegrBean.tableList}" />
				</h:selectOneListbox>
				<h:selectManyListbox id="selectcolumns"
					style="width:150px; height:100px"
					value="#{actionRegrBean.columnSelected}"
					rendered="#{actionRegrBean.columnRender}" size="5">
					<f:selectItems value="#{actionRegrBean.columnsList}" />
				</h:selectManyListbox>
				<h:selectOneListbox id="predictor"
					value="#{actionRegrBean.predictorValue}"
					rendered="#{actionRegrBean.renderRegressionColumn}" size="5">
					<f:selectItem itemValue="0" itemLabel="Select Predictor Value" />
					<f:selectItems value="#{actionRegrBean.numericData}" />
				</h:selectOneListbox>
				<h:selectOneListbox id="response"
					value="#{actionRegrBean.responseValue}"
					rendered="#{actionRegrBean.renderRegressionColumn}" size="5">
					<f:selectItem itemValue="0" itemLabel="Select Response Value" />
					<f:selectItems value="#{actionRegrBean.numericData}" />
				</h:selectOneListbox>

			</h:panelGrid>

			<div style="background-attachment: scroll; overflow:auto;
                    		background-repeat: repeat" align="center">
                        <t:dataTable value="#{actionRegrBean.statisticList}" var="rowNumber"
							rendered="#{actionRegrBean.renderTabledata}"
							border="1" cellspacing="0" cellpadding="1"
							headerClass = "headerWidth">
							<h:column><f:facet name="header"><h:outputText value ="Column Selected"/></f:facet>
								<h:outputText value="#{rowNumber.columnSelected}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Minimum Value"/></f:facet>
								<h:outputText value="#{rowNumber.minValue}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Maximum Value"/></f:facet>
								<h:outputText value="#{rowNumber.maxValue}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Mean"/></f:facet>
								<h:outputText value="#{rowNumber.mean}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Variance"/></f:facet>
								<h:outputText value="#{rowNumber.variance}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Standard Deviation"/></f:facet>
								<h:outputText value="#{rowNumber.std}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Q1"/></f:facet>
								<h:outputText value="#{rowNumber.q1}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Q3"/></f:facet>
								<h:outputText value="#{rowNumber.q3}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="Range"/></f:facet>
								<h:outputText value="#{rowNumber.range}"/></h:column>
							<h:column><f:facet name="header"><h:outputText value ="IQR"/></f:facet>
								<h:outputText value="#{rowNumber.iqr}"/></h:column>
						</t:dataTable>
				    </div><br />
			<br />
			<h:outputText value="Regression Equation: "
				rendered="#{actionRegrBean.renderRegressionResult}">
			</h:outputText> &#160;
						<h:outputText value="#{regrStatsBean.regressionEquation}"
				rendered="#{actionRegrBean.renderRegressionResult}">
			</h:outputText>
			<br />
			<br />
			<h:outputText value="Regression Model"
				rendered="#{actionRegrBean.renderRegressionResult}"></h:outputText>
			<h:panelGrid columns="5"
				rendered="#{actionRegrBean.renderRegressionResult}" border="1">
				<h:outputText value="Predictor" />
				<h:outputText value="Co-efficient" />
				<h:outputText value="Standard Error Co-efficient" />
				<h:outputText value="T-Statistic" />
				<h:outputText value="P-Value" />
				<h:outputText value="Constant" />
				<h:outputText value="#{regrStatsBean.intercept}" />
				<h:outputText value="#{regrStatsBean.interceptStandardError}" />
				<h:outputText value="#{regrStatsBean.tStatistic }" />
				<h:outputText value="#{regrStatsBean.interceptPValue }" />
				<h:outputText value="#{actionRegrBean.predictorValue}" />
				<h:outputText value="#{regrStatsBean.slope}" />
				<h:outputText value="#{regrStatsBean.slopeStandardError}" />
				<h:outputText value="#{regrStatsBean.tStatisticPredictor }" />
				<h:outputText value="#{regrStatsBean.pValuePredictor }" />
			</h:panelGrid>
			<br />
			<br />
			<h:panelGrid columns="2"
				rendered="#{actionRegrBean.renderRegressionResult}" border="1">
				<h:outputText value="Model Standard Error:" />
				<h:outputText value="#{regrStatsBean.standardErrorModel}" />
				<h:outputText value="R Square(Co-efficient of Determination)" />
				<h:outputText value="#{regrStatsBean.rSquare}" />
				<h:outputText
					value="R Square Adjusted(Co-efficient of Determination)" />
				<h:outputText value="#{regrStatsBean.rSquareAdjusted}" />
			</h:panelGrid>
			<br />
			<br />
			<h:outputText value="Analysis of Variance"
				rendered="#{actionRegrBean.renderRegressionResult}" />
			<br />
			<h:panelGrid columns="6"
				rendered="#{actionRegrBean.renderRegressionResult}" border="1">
				<h:outputText value="Source" />
				<h:outputText value="Degrees of Freedom(DF)" />
				<h:outputText value="Sum of Squares" />
				<h:outputText value="Mean of Squares" />
				<h:outputText value="F-Statistic" />
				<h:outputText value="P-Value" />
				<h:outputText value="Regression" />
				<h:outputText value="#{regrStatsBean.predictorDF}" />
				<h:outputText value="#{regrStatsBean.regressionSumSquares}" />
				<h:outputText value="#{regrStatsBean.meanSquare }" />
				<h:outputText value="#{regrStatsBean.fValue }" />
				<h:outputText value="#{regrStatsBean.pValue}" />
				<h:outputText value="Residual Error" />
				<h:outputText value="#{regrStatsBean.residualErrorDF}" />
				<h:outputText value="#{regrStatsBean.sumSquaredErrors }" />
				<h:outputText value="#{regrStatsBean.meanSquareError }" />
				<h:outputText value="" />
				<h:outputText value="" />
				<h:outputText value="Total" />
				<h:outputText value="#{regrStatsBean.totalDF}" />
			</h:panelGrid>
		</h:form>
		</f:view>
</body>

</html>