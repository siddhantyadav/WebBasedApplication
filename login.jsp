<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">




<title>Login</title>
</head>
<body>
	<f:view>
		<f:verbatim>
			<center>
				<a href="index.jsp">Home Page</a>


			</center>
		</f:verbatim>
		<h:form>

			<br>
			<br>

			<div align="center">
				<h:panelGrid columns="2">
					<h:outputText value="User name:" style="font-family: Calibri"></h:outputText>
					<h:inputText id="UserName" value="#{databaseinfo.userName}"
						style=" font-weight: Bold; color: Orange; background: Steelblue;"></h:inputText>
					<h:outputText value="Password:" style="font-family: Calibri"></h:outputText>
					<h:inputSecret id="Password" required="true" value="#{databaseinfo.password}"			
                                        requiredMessage="password is required." style=" font-weight: Bold; color: Orange; background: Steelblue;" 
                                        validatorMessage=" incorrect password " ></h:inputSecret>
				<h:message showSummary="true" style="color: Red;" showDetail="false"  for="Password"  /><br />
					<%-- <h:inputSecret id="Password" value="#{databaseinfo.password}"
						style="color: White; background:  Blue;"></h:inputSecret>
 --%>
					<h:outputText value="Host:" style="font-family: Calibri"></h:outputText>
					<h:inputText id="Host" value="#{databaseinfo.host}"
						style=" font-weight: Bold; color: Orange; background: Steelblue;"></h:inputText>
					<h:outputText value="Schema:" style="font-family: Calibri"></h:outputText>
					<h:inputText id="Schema" value="#{databaseinfo.dbSchema}"
						style=" font-weight: Bold; color: Orange; background: Steelblue;"></h:inputText>
					<h:outputText value="DBMS:" style="font-family: Calibri"></h:outputText>
					<h:selectOneListbox value="#{databaseinfo.dbms}" size="1"
						style=" font-weight: Bold; color: Orange; background: Steelblue;">
						<f:selectItem itemValue="MySQL" itemLabel="MySQL" />
						<f:selectItem itemValue="DB2" itemLabel="DB2" />
						<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
					</h:selectOneListbox>


					<h:commandButton type="submit" value="Login"
						action="#{databaseAccess.connectDb}"></h:commandButton>





					<a href="DisplayImport.jsp">Next</a>



				</h:panelGrid>
			</div>

		</h:form>

	</f:view>
</body>
</html>