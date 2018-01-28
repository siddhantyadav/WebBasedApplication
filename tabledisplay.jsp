<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
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
			<h3>IDS-572 -s17g201</h3>
			<a href="table display">Home</a>
			
	</center>
	</f:verbatim>
	 <h:form>
	      
	<h:panelGrid columns="2">
		<h:outputText value="Enter User name:"></h:outputText>
		<h:inputText  id="UserName" value="#{databaseinfo.userName}"></h:inputText>
		<h:outputText value="Password:"></h:outputText>
		<h:inputSecret id="Password" value="#{databaseinfo.password}"></h:inputSecret>
		<h:outputText  value="Host:"></h:outputText>
		<h:inputText  id="Host" value="#{databaseinfo.host}"></h:inputText>
		
	<h:commandButton type="submit" value="Login" action ="#{databaseActionBean.login}" ></h:commandButton>
	
	</h:panelGrid>
	
</h:form>

</f:view>
</body>
</html>