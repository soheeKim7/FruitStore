<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>기존과일 입력처리</title>
</head>
<body>
<%
	FruitStoreDAO dao=new FruitStoreDAOImpl();
	FruitVO vo=new FruitVO();
	
	vo.setFruit_code(Integer.parseInt(request.getParameter("fruit_code")));
	vo.setQuantity(Integer.parseInt(request.getParameter("quantity")));
	dao.updateQuantityFruit(vo);

%>

<script >
	location.href="index.html"

</script>

</body>
</html>