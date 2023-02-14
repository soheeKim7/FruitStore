<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
<%@page import="kr.edu.mit.SalesVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>매출확인</title>
<style >

@keyframes blink-effect {
  50% {
    opacity: 0;
  }
}

.blink {
  animation: blink-effect 0.8s step-end infinite;
}

</style>
</head>
<body>

<%
	FruitStoreDAO dao=new FruitStoreDAOImpl();
	List<SalesVO> list=dao.listSales();
%>

<table border="1">
<tr> <th>과일코드</th> <th>이름</th> <th>판매수량</th> <th>할인율</th> <th>판매금액</th> <th>판매일자</th>
</tr>

<%
	DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###"); 
	for(SalesVO vo : list){ %>
		<tr> <td align="center"><%=vo.getFruit_code() %></td> <td align="center"><%=vo.getFruit_name() %></td> 
		<td align="center"><%=vo.getSales_quantity() %></td> 
		<td align="center"><%=(int)vo.getSalerate()%>%</td> 
		<td align="center"><%=dc.format(vo.getTotal()) %>원 (<%=dao.changemoney((long)vo.getTotal()) %>)  </td> 
		<td align="center"><%=vo.getSales_date() %></td>
		</tr>
<% }
%>

<tr> <td colspan="4" align="center"> <b>정규판매 총 매출금액</b> </td>
 <td colspan="2" align="center" > <b> <%=dc.format(dao.nomarlPrice()) %>원 
 (<%=dao.changemoney((long)dao.nomarlPrice()) %>)
  </b></td>
</tr>

<tr> <td colspan="4" align="center"> <b>세일판매 총 매출금액</b> </td>
 <td colspan="2" align="center" > <b> <%=dc.format(dao.salePrice()) %>원 
 (<%=dao.changemoney((long)dao.salePrice()) %>)
  </b></td>
</tr>

<tr> <td colspan="4" align="center"> <b>총 매출금액</b> </td>
 <td colspan="2" align="center" > <b class="blink" style=color:coral;> <%=dc.format(dao.totalPrice2()) %>원 
 (<%=dao.changemoney((long)dao.totalPrice2()) %>)
  </b></td>
</tr>

<tr> <td colspan="6"><button type="button" onclick="gotitle()" style="float:right">메뉴로 돌아가기</button></td>
</tr>

</table>



<script >
function gotitle(){
	history.back(); //뒤로가기
}
</script>


</body>
</html>