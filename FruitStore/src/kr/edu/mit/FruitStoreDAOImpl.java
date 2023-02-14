package kr.edu.mit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FruitStoreDAOImpl implements FruitStoreDAO {
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet result=null;
	DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
	
	@Override //과일등록
	public void insertFruit(FruitVO vo) {
		//DB연결해서 과일등록
		//JAVA에서 DB연결하는 방법 JDBC
		//1.DB연결
		//1-1. JDBC드라이버 로드
		//1-2. 연결해서 Connection 객체생성
		//2.쿼리작업
		//2-1. 커넥션객체를 가지고 Statement 객체생성
		//2-2. 스테이먼트 객체를 가지고 query 작업 (select 문의결과는 ResultSet 객체로 받아서 작업)
		dbConn();
		try {
			pstmt=conn.prepareStatement("insert into fruit(fruit_name,price,quantity) value(?,?,?)");
			pstmt.setString(1, vo.getFruit_name());  //? 채우기
			pstmt.setDouble(2, vo.getPrice()); 
			pstmt.setInt(3, vo.getQuantity());  			
			pstmt.executeUpdate(); //삽입,삭제,수정시에는 exceuteUpdate()를 -반환값 int 처리된 행의개수
			                      //read(select)시에는 executeQuery() 이용 -반환값 ResultSet 객체를 결과값을 돌려준다.			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//3.사용후 DB연결 끊기
		// ResultSet,Statement,Connection 객체 닫아주기
		dbClose();		
	}

	@Override  //과일목록보여주기
	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		//1.db연결
		dbConn();		
		try {
			//2.쿼리작업후 결과가져오기(ResultSet) 
			pstmt=conn.prepareStatement("select fruit_code,fruit_name,price,quantity from fruit order by fruit_code");
			ResultSet rs=pstmt.executeQuery(); 
			//3.리턴타입으로 변환하기
			while(rs.next()) {                 //next() 다음행을 가르킴 , 리턴은 다음행가르키는 성공이면 true 없으면 false
				FruitVO vo = new FruitVO();				
				vo.setFruit_code(rs.getInt("fruit_code"));
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setPrice(rs.getInt("price"));
				vo.setQuantity(rs.getInt("quantity"));
				list.add(vo);
				
				//System.out.println(code+" "+name+" "+price+" "+quantity);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//3.리턴타입으로 변환하기
		//4.db닫기
		dbClose();
		//5.변환한거 리턴
		
		return list;
	}
	
	//db연결
	void dbConn() {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");
			System.out.println("연결성공");                                
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//db닫기
	void dbClose() {
		if(result!= null) try{ result.close(); } catch (SQLException e){e.printStackTrace();} 
		if(pstmt != null) try{ pstmt.close();  } catch (SQLException e){e.printStackTrace();} 
		if(conn  != null) try{ conn.close();  } catch (SQLException e){e.printStackTrace();} 
	}

	@Override //수량업데이트
	public void updateQuantityFruit(FruitVO vo) {
		dbConn();
		try {
			pstmt=conn.prepareStatement("update fruit set quantity=quantity+? where fruit_code=?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}		
	}

	@Override //과일별 총가격알려주기
	public int totalFruit(FruitVO vo) {
		dbConn();
		int total=-1;
		try {
			pstmt=conn.prepareStatement("select price*? from fruit where fruit_code=?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());  //Query 완성
			result=pstmt.executeQuery();
			result.next(); //첫번째 행을 가르키고
			total=result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			dbClose();
		}
		return total;		
	}
	
	@Override  //과일별 총가격알려주기+할인율에 따라
	public double totalFruit(FruitVO vo, double salerate) {
		dbConn();
		int total=-1;
		try {
			salerate= (100.0-salerate)/100;
			pstmt=conn.prepareStatement("select price*?*? from fruit where fruit_code=?");
			pstmt.setDouble(1, salerate);
			pstmt.setInt(2, vo.getQuantity());  
			pstmt.setInt(3, vo.getFruit_code());  			
			result=pstmt.executeQuery();
			result.next(); //첫번째 행을 가르키고
			total=result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			dbClose();
		}
		return total;	
	}

	@Override //판매처리
	public void insertSales(int fruit_code,int quantity) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //오토커밋 금지
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //입력된 키값 확인
			result.next();
			int key=result.getInt(1);
						
			//pstmt.executeLargeUpdate("insert into fruit_has_sales values("  +fruit_code+  "," +key+  ")");
			//or		
			pstmt.close();
			pstmt=conn.prepareStatement("insert into fruit_has_sales values(?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt=conn.prepareStatement("insert into salerate(fruit_code,sales_code,salerate_apply,salerate) values(?,?,?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.setInt(3, 0);
			pstmt.setDouble(4, 0);
			pstmt.executeUpdate();
									
			pstmt.close();
			pstmt=conn.prepareStatement("update fruit set quantity=quantity-? where fruit_code=?");
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();
			
			conn.commit(); //정상이면 커밋
			
		} catch (SQLException e) {
			System.out.println("판매실패");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //중간에 문제가 생기면 롤백
			e.printStackTrace();
		} finally {
			dbClose();
		}		
	}
	
	@Override //판매처리 + 할인율
	public void insertSales(int fruit_code, int quantity, double salerate) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //오토커밋 금지
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //입력된 키값 확인
			result.next();
			int key=result.getInt(1);			
	
						
			//pstmt.executeLargeUpdate("insert into fruit_has_sales values("  +fruit_code+  "," +key+  ")");
			//or		
			pstmt.close();
			pstmt=conn.prepareStatement("insert into fruit_has_sales values(?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt=conn.prepareStatement("insert into salerate(fruit_code,sales_code,salerate_apply,salerate) values(?,?,?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.setInt(3, 1);
			pstmt.setDouble(4, salerate);
			pstmt.executeUpdate();
									
			pstmt.close();
			pstmt=conn.prepareStatement("update fruit set quantity=quantity-? where fruit_code=?");
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();						
			
			conn.commit(); //정상이면 커밋
			
		} catch (SQLException e) {
			System.out.println("판매실패");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //중간에 문제가 생기면 롤백
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		
	}

	@Override  //총판매금액
	public long totalPrice() {
		dbConn();
		long totalPrice=-1;
		try {
			//수행할 쿼리를 만들고
			String query="select sum(price*sales_quantity)" + 
					" from fruit " + 
					"     join" + 
					"	 (select fruit_fruit_code, sales_date, sales_quantity" + 
					"	  from fruit_has_sales" + 
					"		   join " + 
					"           sales " + 
					"           on fruit_has_sales.sales_sales_code=sales.sales_code" + 
					"      ) t1" + 
					"      on fruit.fruit_code=t1.fruit_fruit_code  ";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			result=pstmt.executeQuery(); //쿼리를 수행해서 결과를 가져오고
			result.next(); //결과에 첫번째 행을 가르키고
			totalPrice=result.getLong(1); //첫번째 속성값을 long타입으로 변화해서 읽어온다.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}
	
	@Override  //총판매금액2
	public long totalPrice2() {
		dbConn();
		long totalPrice=-1;
		try {
			//정가 판매할때 (salerate_apply=0 일때 price*sales_quantity 합)	
			pstmt=conn.prepareStatement("select sum(sales_price) " + 
					"from (select salerate_code,fruit_code,fruit_name,sales_code, sales_quantity, salerate_apply, salerate, price, sales_date " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code)) t2 join " + 
					"(select salerate_code, price*sales_quantity as sales_price " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=0 " + 
					"union all " + 
					"select salerate_code, price*((100-salerate)/100)*sales_quantity as salerate_price " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=1) t3 " + 
					"using(salerate_code)");
			result=pstmt.executeQuery(); 
			result.next(); 
			totalPrice = result.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}

	@Override //정가판매 총판매금액
	public long nomarlPrice() {
		dbConn();
		long totalPrice=-1;
		try {
			//정가 판매할때 (salerate_apply=0 일때 price*sales_quantity 합)	
			pstmt=conn.prepareStatement("select sum(price*sales_quantity) " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=0 ");
			result=pstmt.executeQuery(); 
			result.next(); 
			totalPrice=result.getLong(1);			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}

	@Override //할인판매 총판매금액
	public long salePrice() {
		dbConn();
		long totalPrice=-1;
		try {				
			//할인 판매할때 (salerate_apply=1 일때 price*((100-salerate)/100)*sales_quantity 합)
			result.close();
			pstmt.close();
			pstmt=conn.prepareStatement("select sum(price*((100-salerate)/100)*sales_quantity) " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=1 ");
			result=pstmt.executeQuery(); 
			result.next(); 
			totalPrice = result.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}

	@Override //매출내역보기
	public List<SalesVO> listSales() {
		ArrayList<SalesVO> list = new ArrayList<>();
		dbConn();
		try {
			pstmt=conn.prepareStatement("select fruit_name,fruit_code,sales_quantity,salerate,sales_date,sales_price " + 
					"from (select salerate_code,fruit_code,fruit_name,sales_code, sales_quantity, salerate_apply, salerate, price, sales_date " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code)) t2 join " + 
					"(select salerate_code, price*sales_quantity as sales_price " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=0 " + 
					"union all " + 
					"select salerate_code, price*((100-salerate)/100)*sales_quantity as salerate_price " + 
					"from fruit join " + 
					"(select  salerate_code,fruit_code,sales_code, sales_date, sales_quantity, salerate_apply, salerate " + 
					"from salerate join sales using(sales_code)) t1 " + 
					"using(fruit_code) " + 
					"where salerate_apply=1) t3 " + 
					"using(salerate_code) order by salerate_code ");
			result=pstmt.executeQuery();
			SimpleDateFormat timefo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dateS;
			while(result.next()) {
				SalesVO vo = new SalesVO();
				vo.setFruit_name(result.getString("fruit_name"));
				vo.setFruit_code(result.getInt("fruit_code"));				
				dateS=timefo.format(result.getTimestamp("sales_date"));				
				vo.setSales_date(dateS);
				vo.setSales_quantity(result.getInt("sales_quantity"));
				vo.setTotal(result.getInt("sales_price"));
				vo.setSalerate(result.getDouble("salerate"));
				list.add(vo);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}			
		return list;
	}

	@Override //해당 과일이름의 과일코드 가져오기
	public Integer getFruitCode(String fruit_name) {
		dbConn();
		Integer code=-1;
		try {
			pstmt=conn.prepareStatement("select fruit_code from fruit where fruit_name=?");
			pstmt.setString(1, fruit_name);
			result=pstmt.executeQuery();
			result.next();
			code=result.getInt(1);
		} catch (SQLException e) {
			code=null;
		}finally {
			dbClose();
		}
		return code;
	}

	@Override  //한글로 금액 표시
	public String changemoney(long money) {
		
		String[] han1 = { "", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
		String[] han2 = { "천", "", "십", "백"};
		String[] han3 = { "", "만", "억", "조"};

		String smoney=money+"";
		String moneyHan="";
				
		int len=smoney.length(); 
		
		if(len>16) {
			System.out.println("단위변환이 불가능합니다. 천조이후의 단위를 추가하십시오");
			return "금액 단위 부족";
		}
		
		for(int i=0;i<smoney.length();i++) {
			int num=Integer.parseInt(smoney.substring(i, i+1));		
			moneyHan += han1[num];			
			
			int rest=len%4;
			if(num!=0) 
				moneyHan += han2[rest];			
			else 
				moneyHan += han2[1];	
			len--;
			
			switch(len) {
				case 4:
					num=1;
					break;
				case 8:
					num=2;
					break;
				case 12:
					num=3;
					break;
				default:
					num=0;				
			}
			if(num!=0)
				moneyHan += han3[num]+" ";
			else
				moneyHan += han3[num];		
		}	
		moneyHan += " 원";

		if(moneyHan.substring(0,2).equals("일만"))
			moneyHan=moneyHan.replace("일만", "만");
		moneyHan=moneyHan.replace("일천", "천");
		moneyHan=moneyHan.replace("일백", "백");
		moneyHan=moneyHan.replace("일십", "십");
		moneyHan=moneyHan.replace(" 만", "");
		moneyHan=moneyHan.replace(" 억", "");			
				
		return moneyHan;    		
	}

	@Override
	public String checknum(double money) {
		DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
		String num=dc.format(money);
		return num;
	}

	@Override  //가장 큰 과일코드 가져오기
	public Integer maxFruit_code() {
		dbConn();
		Integer code=-1;
		try {
			pstmt=conn.prepareStatement("select max(fruit_code) from fruit");
			result=pstmt.executeQuery();
			result.next();
			code=result.getInt(1);
		} catch (SQLException e) {
			code=null;
		}finally {
			dbClose();
		}
		return code;
	}

	@Override  //가장 작은 과일코드 가져오기
	public Integer minFruit_code() {
		dbConn();
		Integer code=-1;
		try {
			pstmt=conn.prepareStatement("select min(fruit_code) from fruit");
			result=pstmt.executeQuery();
			result.next();
			code=result.getInt(1);
		} catch (SQLException e) {
			code=null;
		}finally {
			dbClose();
		}
		return code;
	}
	
	@Override  //해당 과일코드의 과일코드 가져오기
	public Integer codeFruit(int fruit_code) {
		dbConn();
		Integer code=-1;
		try {
			pstmt=conn.prepareStatement("select fruit_code from fruit where fruit_code=?;");
			pstmt.setInt(1, fruit_code);
			result=pstmt.executeQuery();
			result.next();
			code=result.getInt(1);
		} catch (SQLException e) {
			code=null;
		}finally {
			dbClose();
		}
		return code;
	}	

	@Override //해당 과일코드의 과일이름 가져오기
	public String codeFruitName(int fruit_code) {
		dbConn();
		String name=null;
		try {
			pstmt=conn.prepareStatement("select fruit_name from fruit where fruit_code=?");
			pstmt.setInt(1, fruit_code);
			result=pstmt.executeQuery();
			result.next();
			name=result.getString(1);
		} catch (SQLException e) {
			name=null;
		}finally {
			dbClose();
		}
		return name;
	}
	
}

/*
 * @Override  //총판매금액2
	public long totalPrice2() {
		dbConn();
		long totalPrice=-1;
		try {
			//정가 판매할때 1.(salerate_apply=0 일때 sales_price 합)	
			pstmt=conn.prepareStatement("select sum(sales_price) " + 
					"from salerate join " + 
					"(select fruit_name,fruit_code,sales_date, sales_quantity,price,price*sales_quantity as sales_price " + 
					"from fruit join " + 
					"(select  fruit_fruit_code, sales_date, sales_quantity " + 
					"from fruit_has_sales join sales on fruit_has_sales.sales_sales_code=sales.sales_code) t1 " + 
					"on fruit.fruit_code=t1.fruit_fruit_code) t2 " + 
					"using(fruit_code) " + 
					"where salerate_apply=0 and Sales_date!=Salerate_date");
			result=pstmt.executeQuery(); 
			result.next(); 
			totalPrice=result.getLong(1); 
			
			//정가 판매할때 2-1. (salerate_apply=0 일때, 판매일자=세일일자 일자가져오기)
			ArrayList<String> sales_date=new ArrayList<String>();			
								
			result.close();
			pstmt.close();
			pstmt=conn.prepareStatement("select sales_date " + 
					"from salerate join " + 
					"(select fruit_name,fruit_code,sales_date, sales_quantity,price,price*sales_quantity as sales_price  " + 
					"from fruit join " + 
					"(select  fruit_fruit_code, sales_date, sales_quantity " + 
					"from fruit_has_sales join sales on fruit_has_sales.sales_sales_code=sales.sales_code) t1 " + 
					"on fruit.fruit_code=t1.fruit_fruit_code) t2 " + 
					"using(fruit_code) " + 
					"where sales_date=salerate_date ");
			result=pstmt.executeQuery();			
			while(result.next()) {
				sales_date.add(result.getString(1));
			}
			
			//정가 판매할때 2-2. (salerate_apply=0 일때, 판매일자=세일일자 일자별 계산된 sales_price)
			for(int i=0;i<sales_date.size();i++) {
			result.close();
			pstmt.close();
			pstmt=conn.prepareStatement("select sales_price " + 
					"from salerate join " + 
					"(select fruit_name,fruit_code,sales_date, sales_quantity,price,price*sales_quantity as sales_price " + 
					"from fruit join " + 
					"(select  fruit_fruit_code, sales_date, sales_quantity " + 
					"from fruit_has_sales join sales on fruit_has_sales.sales_sales_code=sales.sales_code) t1 " + 
					"on fruit.fruit_code=t1.fruit_fruit_code) t2 " + 
					"using(fruit_code) " + 
					"where sales_date=? and salerate_apply=0;");
			pstmt.setString(1, sales_date.get(i));
			result=pstmt.executeQuery();
			result.next();			
			totalPrice -= result.getLong(1);
			}
			
			//할인된 가격들의 합
			result.close();
			pstmt.close();
			pstmt=conn.prepareStatement("select sum(price*(100-salerate)/100*sales_quantity) " + 
					"from salerate join " + 
					"(select fruit_name,fruit_code,sales_date, sales_quantity,price,price*sales_quantity as sales_price " + 
					"from fruit join " + 
					"(select  fruit_fruit_code, sales_date, sales_quantity " + 
					"from fruit_has_sales join sales on fruit_has_sales.sales_sales_code=sales.sales_code) t1 " + 
					"on fruit.fruit_code=t1.fruit_fruit_code) t2 " + 
					"using(fruit_code) " + 
					"where salerate_apply=1 and Sales_date=Salerate_date ");
			result=pstmt.executeQuery(); 
			result.next(); 
			totalPrice += result.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}
 */

/*
 * 	@Override //매출내역보기
	public List<SalesVO> listSales() {
		ArrayList<SalesVO> list = new ArrayList<>();
		dbConn();
		try {
			pstmt=conn.prepareStatement("select fruit_name,fruit_code,sales_date, sales_quantity,price*sales_quantity " + 
					"from fruit join" + 
					"(select  fruit_fruit_code, sales_date, sales_quantity " + 
					"from fruit_has_sales join sales on fruit_has_sales.sales_sales_code=sales.sales_code) t1 " + 
					"on fruit.fruit_code=t1.fruit_fruit_code");
			result=pstmt.executeQuery();
			while(result.next()) {
				SalesVO vo = new SalesVO();
				vo.setFruit_name(result.getString("fruit_name"));
				vo.setFruit_code(result.getInt("fruit_code"));
				vo.setSales_date(result.getDate("sales_date"));
				vo.setSales_quantity(result.getInt("sales_quantity"));
				vo.setTotal(result.getInt("price*sales_quantity"));
				list.add(vo);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}			
		return list;
	}
 */

/* 
			result=pstmt.executeQuery("select last_insert_id()"); //입력된 키값 확인
			result.next();
			int key=result.getInt(1);
			
			pstmt.close();
			pstmt=conn.prepareStatement("insert into salerate(fruit_code,salerate_apply,salerate) values (?,0,0)");
			pstmt.setInt(1, key); 
			pstmt.executeUpdate(); 
 */

/*
 * @Override //판매처리 + 할인율
	public void insertSales(int fruit_code, int quantity, double salerate) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //오토커밋 금지
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //입력된 키값 확인
			result.next();
			int key=result.getInt(1);
			
			result.close();
			pstmt.close();
			pstmt=conn.prepareStatement("select sales_date from sales where sales_code=?");
			pstmt.setInt(1, key);
			result=pstmt.executeQuery();
			result.next();
			SimpleDateFormat timefo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date date = new Date();
			String dateS=timefo.format(result.getTimestamp(1));
//			try {
//				date = timefo.parse(dateS);
//			} catch (ParseException e) {
//				System.out.println("날짜오류 발생");
//			}
						
			//pstmt.executeLargeUpdate("insert into fruit_has_sales values("  +fruit_code+  "," +key+  ")");
			//or		
			pstmt.close();
			pstmt=conn.prepareStatement("insert into fruit_has_sales values(?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.executeUpdate();
									
			pstmt.close();
			pstmt=conn.prepareStatement("update fruit set quantity=quantity-? where fruit_code=?");
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt=conn.prepareStatement("insert into salerate(fruit_code,salerate_apply,salerate) values (?,?,?)");
			pstmt.setInt(1, fruit_code); 
			pstmt.setInt(2, 1); 
			pstmt.setDouble(3, salerate); 
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt=conn.prepareStatement("update salerate set salerate_date=? where fruit_code=? and salerate_apply=1");
			pstmt.setString(1, dateS);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();				
			
			conn.commit(); //정상이면 커밋
			
		} catch (SQLException e) {
			System.out.println("판매실패");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //중간에 문제가 생기면 롤백
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		
	}
 * 
 */

