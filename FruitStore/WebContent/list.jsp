<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고파악</title>
</head>
<body>
<button type="button" onclick="gotitle()">메뉴로</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<button type="button" onclick="goback()">뒤로 돌아가기</button>
<%
	//request.setCharacterEncoding("utf-8"); 여기서는 자바꺼 가져오는거라 상관없어
	FruitStoreDAO dao=new FruitStoreDAOImpl();
	ArrayList<FruitVO> list=dao.listFruit();
	request.setAttribute("list", list);
%>

<table border="1" style="text-align: center">
<tr> <th>과일코드</th>  <th>이름</th>  <th>가격</th>  <th>재고</th>  
</tr>
<c:forEach items="${list }" var="vo">
	<tr> <td>${vo.fruit_code}</td> <td>${vo.fruit_name}</td> <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td> <td>${vo.quantity}</td> </tr>
</c:forEach>
</table>
 	
<script >
function gotitle(){
	location.href="index.html";
}

function goback(){
	history.back(); //뒤로가기
}
</script>
</body>
</html>