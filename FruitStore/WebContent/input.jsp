<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일입력</title>
</head>
<body>
<body>
1. 신규과일 등록하기 <br>
<form action="inputPro.jsp" method="post">
	과일이름 : <input type="text" name="fruit_name"> <br>
	과일가격 : <input type="number" name="price"> <br>
	과일수량 : <input type="number" name="quantity"> <br>
	<input type="submit" value="입력">
</form>
<br>

<form action="list.jsp" method="post">
<input type="submit" value="과일목록보기">
</form>
2. 기존과일 등록하기 <br>
<form action="inputPro2.jsp" method="post">
과일코드 : <input type="number" name="fruit_code"> <br>
과일수량 : <input type="number" name="quantity"> <br>
<input type="submit" value="입력">
</form>

</body>

</body>
</html>