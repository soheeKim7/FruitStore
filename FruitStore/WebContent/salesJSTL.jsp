<%@page import="java.text.DecimalFormat"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="java.util.ArrayList"%>
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
<title>과일구매</title>
<style>
	@keyframes blink-effect {50% {  opacity: 0;	 }	}
    .mline { text-decoration:line-through; }
    .sale { color:red;
   		    animation: blink-effect 0.5s step-end infinite; }   
</style>
</head>
<body>
<%	
	FruitStoreDAO dao=new FruitStoreDAOImpl();
	ArrayList<FruitVO> list=dao.listFruit();
	request.setAttribute("list",list);
	
	DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
	
	String comcodeS=request.getParameter("comcode");
	String comrateS=request.getParameter("comrate");		
%>

<table border="1" style="float:left; text-align: center;">
<tr> <th>과일코드</th>  <th>이름</th>  <th>가격</th>  <th>재고</th>  
</tr>
<%	
	boolean check=true;
	int comcode=0;
	double comrate=0.0;		
	
	if(comcodeS==null && comrateS==null){
		do{
			comcode=(int)(Math.random()*(dao.maxFruit_code()-dao.minFruit_code()+1)+dao.minFruit_code());
			check=true;
			//(int)(Math.random()*(최대값-최소값+1)+최소값)			
			if(dao.codeFruit(comcode)==null){
				check=false;
			}
		}while(check==false);
		
		comrate=(int)((Math.random()*5)+1)*10;
	}else{
		comcode=Integer.parseInt(comcodeS);
		comrate=Double.parseDouble(comrateS);
	}	
	request.setAttribute("comcode", comcode);
	request.setAttribute("comrate", comrate);
%>
<c:forEach items="${list }" var="vo">
	<c:choose>
		<c:when test="${vo.fruit_code==comcode }">
			<tr> <td><div>${vo.fruit_code }</div> <b class="sale"> <fmt:formatNumber type="number" maxFractionDigits="0"  value="${comrate}" />%세일</b></td> 
				 <td>${vo.fruit_name}</td> <td><div class="mline"><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</div>
 	 			 <b><fmt:formatNumber value="${vo.price*((100.0-comrate)/100)}" pattern="#,###" />원</b></td>  <td>${vo.quantity}</td>  
 	 		</tr>
 		</c:when>
		<c:otherwise>
			<tr> <td>${vo.fruit_code}</td> <td>${vo.fruit_name}</td> <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td> <td>${vo.quantity}</td> </tr>				
		</c:otherwise>	
	</c:choose>
</c:forEach>
</table>
<br>

<form action="" method="post">
과일코드 : <input type="number" name="fruit_code">, 구매수량 : <input type="number" name="sales_quantity">
<input type="hidden" name="comcode" value=<%=comcode %>>
<% comrate= (100-comrate*100); %>
<input type="hidden" name="comrate" value=<%=comrate %>>
<input type="submit" value="확인"> <br>
</form>



<%	
	String fruit_code=request.getParameter("fruit_code");
	String sales_quantity=request.getParameter("sales_quantity");
		
	if((fruit_code!=null && sales_quantity!=null)){
		if((!fruit_code.equals("") && !sales_quantity.equals(""))){ 
		//(fruit_code.length()!=0 && sales_quantity.length()!=0)      %>
	<form action="salesPro.jsp" method="post"> 
	<input type="hidden" name="fruit_code" value=<%=fruit_code %>>
	<input type="hidden" name="sales_quantity" value=<%=sales_quantity %>>
	<input type="hidden" name="com_code" value=<%=comcodeS %>>
	<input type="hidden" name="com_rate" value=<%=comrateS %>>
		
	<% FruitVO vo=new FruitVO();
		vo.setFruit_code(Integer.parseInt(fruit_code));
		vo.setQuantity(Integer.parseInt(sales_quantity));
		double salerate=Double.parseDouble(comrateS);		
		double price=0;
		if(fruit_code.equals(comcodeS)){
			price=dao.totalFruit(vo, salerate);
		}else{
			price=dao.totalFruit(vo);
		}		
	%>
	<br>	
	과일코드 : <%=fruit_code %>, 과일이름 : <%=dao.codeFruitName(Integer.parseInt(fruit_code)) %>, 구매수량 : <%=sales_quantity %> 	<br>
	<%
		if(fruit_code.equals(comcodeS)){ %>
			적용된 할인율은 <%=(int)salerate %>% 입니다. <br>
	<%	}else{ %>
			적용된 할인율은 없습니다. <br>
	<%	}
	%>
	총 가격은 <%=dc.format(price) %>원입니다. 구매하시겠습니까? 
	<input type="submit" value="구매">

	</form>
<%		}
	}
%>


</body>
</html>