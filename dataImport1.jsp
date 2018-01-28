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
	<h:form enctype="multipart/form-data">
		<h:panelGrid columns="2"
			style="background-color: yellow;
			border-bottom-style: solid;
			border-top-style: solid;
			border-left-style: solid;
			border-right-style: solid">
			<h:outputLabel value="select file to upload:" />
			<t:inputFileUpload id="fileUpload" label="File to upload"
				storage="default" value="#{databaseImport.uploadedFile}" size="60" />
			<h:outputLabel value="file label:" />
			<h:inputText id="fileLabel" value="#{databaseImport.fileLabel}" size="60" />
			<h:outputLabel value="data label:" />
			<h:inputText id="datalabel" value=" " size="60" />
			<h:outputLabel value="FileType:" />
			<h:selectOneListbox value="#{databaseImport.fileType}" size="3" style="width:50%">
						<f:selectItem itemValue="MetaData" itemLabel="MetaData" />
						<f:selectItem itemValue="Data" itemLabel="Data" />
											</h:selectOneListbox>
					<h:outputLabel value="FileFormat:" />
					<h:selectOneListbox value="#{databaseImport.fileFormat1}" size="4" style="width:50%">
						<f:selectItem itemValue="Exceltab" itemLabel="Excel TAB" />
						<f:selectItem itemValue="Excelcsv" itemLabel="Excel CSV" />
						</h:selectOneListbox>
					<h:outputLabel value="HeaderRow:" />
					<h:selectOneListbox value="#{databaseImport.headerRow}" size="2" style="width:50%">
						<f:selectItem itemValue="Yes" itemLabel="Yes" />
						<f:selectItem itemValue="No" itemLabel="No" />
					</h:selectOneListbox>
					<br />
					<br />
			<h:commandButton id="upload"
				action="#{databaseImport.processFileUpload1}" value="Submit" />
		</h:panelGrid>
		<h:panelGrid columns="2"
			style="background-color: steelblue;
border-bottom-style: solid;
border-top-style: solid;
border-left-style: solid;
border-right-style: solid"
			width="800" rendered="#{databaseImport.fileImport }">
			<h:outputLabel value="Number of records:" />
			<h:outputText value="#{databaseImport.numberRows }" />
			<h:outputLabel value="fileLabel:" />
			<h:outputText value="#{databaseImport.fileLabel }" />
			<h:outputLabel value="fileName:" />
			<h:outputText value="#{databaseImport.fileName }" />
			<h:outputLabel value="fileSize:" />
			<h:outputText value="#{databaseImport.fileSize }" />
			<h:outputLabel value="fileContentType:" />
			<h:outputText value="#{databaseImport.fileContentType }" />
			<h:outputLabel value="tempFilePath:" />
			<h:outputText value="#{databaseImport.filePath }" />
			<h:outputLabel value="tempFileName:" />
			<h:outputText value="#{databaseImport.tempFileName }" />
			<h:outputLabel value="facesContext:" />
			<h:outputText value="#{databaseImport.facesContext }" />
		</h:panelGrid>
		<br />
		<h:outputText rendered="#{databaseImport.fileImportError }"
			value="" />
		</h:form>
		
		<br>
		<a href="mathAnalysis.jsp">Data Analytics</a>
		<br>
		<br>
		<a href="Displayt.jsp">Display</a>
		<br>
</f:view>

</body>
</html>