<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신규과일 입력처리</title>
</head>
<body>
<%
	//과일입력DB처리
	request.setCharacterEncoding("utf-8");
	FruitStoreDAO dao=new FruitStoreDAOImpl();
	FruitVO vo=new FruitVO();				
	
	vo.setFruit_name(request.getParameter("fruit_name"));
	vo.setPrice(Integer.parseInt(request.getParameter("price")));
	vo.setQuantity(Integer.parseInt(request.getParameter("quantity")));
	dao.insertFruit(vo);
 %>

<script >
	location.href="index.html"

</script>


</body>
</html>