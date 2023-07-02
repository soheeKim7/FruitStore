<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<% 
boolean test=true;
request.setAttribute("test", test); 
int com=(int)(Math.random()*10+1);
request.setAttribute("com", com);
//(int)(Math.random()*(최대값-최소값+1)+최소값)	

%>
<c:forEach begin="${test==true }" end="${test==false }">
	${com }
	${test=true}
	<c:if test="${com==2 }">
		${test==false }
	</c:if>
</c:forEach>
	${test }
</body>
</html>