
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>구매처리</title>
</head>
<body>

<%

FruitStoreDAO dao=new FruitStoreDAOImpl();
int fruit_code=Integer.parseInt(request.getParameter("fruit_code"));
int sales_quantity=Integer.parseInt(request.getParameter("sales_quantity"));
int comcode=Integer.parseInt(request.getParameter("com_code"));
double salerate=Double.parseDouble(request.getParameter("com_rate"));
if(fruit_code==comcode){
	dao.insertSales(fruit_code, sales_quantity, salerate);
}else{
	dao.insertSales(fruit_code,sales_quantity);
}	


%>

구매가 완료되었습니다. 감사합니다. <br>
<a href="index.html">메뉴로 돌아가기</a>

</body>
</html>