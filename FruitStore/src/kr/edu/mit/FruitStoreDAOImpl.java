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
	
	@Override //���ϵ��
	public void insertFruit(FruitVO vo) {
		//DB�����ؼ� ���ϵ��
		//JAVA���� DB�����ϴ� ��� JDBC
		//1.DB����
		//1-1. JDBC����̹� �ε�
		//1-2. �����ؼ� Connection ��ü����
		//2.�����۾�
		//2-1. Ŀ�ؼǰ�ü�� ������ Statement ��ü����
		//2-2. �����̸�Ʈ ��ü�� ������ query �۾� (select ���ǰ���� ResultSet ��ü�� �޾Ƽ� �۾�)
		dbConn();
		try {
			pstmt=conn.prepareStatement("insert into fruit(fruit_name,price,quantity) value(?,?,?)");
			pstmt.setString(1, vo.getFruit_name());  //? ä���
			pstmt.setDouble(2, vo.getPrice()); 
			pstmt.setInt(3, vo.getQuantity());  			
			pstmt.executeUpdate(); //����,����,�����ÿ��� exceuteUpdate()�� -��ȯ�� int ó���� ���ǰ���
			                      //read(select)�ÿ��� executeQuery() �̿� -��ȯ�� ResultSet ��ü�� ������� �����ش�.			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//3.����� DB���� ����
		// ResultSet,Statement,Connection ��ü �ݾ��ֱ�
		dbClose();		
	}

	@Override  //���ϸ�Ϻ����ֱ�
	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		//1.db����
		dbConn();		
		try {
			//2.�����۾��� �����������(ResultSet) 
			pstmt=conn.prepareStatement("select fruit_code,fruit_name,price,quantity from fruit order by fruit_code");
			ResultSet rs=pstmt.executeQuery(); 
			//3.����Ÿ������ ��ȯ�ϱ�
			while(rs.next()) {                 //next() �������� ����Ŵ , ������ �����డ��Ű�� �����̸� true ������ false
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
		//3.����Ÿ������ ��ȯ�ϱ�
		//4.db�ݱ�
		dbClose();
		//5.��ȯ�Ѱ� ����
		
		return list;
	}
	
	//db����
	void dbConn() {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");
			System.out.println("���Ἲ��");                                
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//db�ݱ�
	void dbClose() {
		if(result!= null) try{ result.close(); } catch (SQLException e){e.printStackTrace();} 
		if(pstmt != null) try{ pstmt.close();  } catch (SQLException e){e.printStackTrace();} 
		if(conn  != null) try{ conn.close();  } catch (SQLException e){e.printStackTrace();} 
	}

	@Override //����������Ʈ
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

	@Override //���Ϻ� �Ѱ��ݾ˷��ֱ�
	public int totalFruit(FruitVO vo) {
		dbConn();
		int total=-1;
		try {
			pstmt=conn.prepareStatement("select price*? from fruit where fruit_code=?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());  //Query �ϼ�
			result=pstmt.executeQuery();
			result.next(); //ù��° ���� ����Ű��
			total=result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			dbClose();
		}
		return total;		
	}
	
	@Override  //���Ϻ� �Ѱ��ݾ˷��ֱ�+�������� ����
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
			result.next(); //ù��° ���� ����Ű��
			total=result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			dbClose();
		}
		return total;	
	}

	@Override //�Ǹ�ó��
	public void insertSales(int fruit_code,int quantity) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //����Ŀ�� ����
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //�Էµ� Ű�� Ȯ��
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
			
			conn.commit(); //�����̸� Ŀ��
			
		} catch (SQLException e) {
			System.out.println("�ǸŽ���");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //�߰��� ������ ����� �ѹ�
			e.printStackTrace();
		} finally {
			dbClose();
		}		
	}
	
	@Override //�Ǹ�ó�� + ������
	public void insertSales(int fruit_code, int quantity, double salerate) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //����Ŀ�� ����
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //�Էµ� Ű�� Ȯ��
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
			
			conn.commit(); //�����̸� Ŀ��
			
		} catch (SQLException e) {
			System.out.println("�ǸŽ���");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //�߰��� ������ ����� �ѹ�
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		
	}

	@Override  //���Ǹűݾ�
	public long totalPrice() {
		dbConn();
		long totalPrice=-1;
		try {
			//������ ������ �����
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
			result=pstmt.executeQuery(); //������ �����ؼ� ����� ��������
			result.next(); //����� ù��° ���� ����Ű��
			totalPrice=result.getLong(1); //ù��° �Ӽ����� longŸ������ ��ȭ�ؼ� �о�´�.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}
	
	@Override  //���Ǹűݾ�2
	public long totalPrice2() {
		dbConn();
		long totalPrice=-1;
		try {
			//���� �Ǹ��Ҷ� (salerate_apply=0 �϶� price*sales_quantity ��)	
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

	@Override //�����Ǹ� ���Ǹűݾ�
	public long nomarlPrice() {
		dbConn();
		long totalPrice=-1;
		try {
			//���� �Ǹ��Ҷ� (salerate_apply=0 �϶� price*sales_quantity ��)	
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

	@Override //�����Ǹ� ���Ǹűݾ�
	public long salePrice() {
		dbConn();
		long totalPrice=-1;
		try {				
			//���� �Ǹ��Ҷ� (salerate_apply=1 �϶� price*((100-salerate)/100)*sales_quantity ��)
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

	@Override //���⳻������
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

	@Override //�ش� �����̸��� �����ڵ� ��������
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

	@Override  //�ѱ۷� �ݾ� ǥ��
	public String changemoney(long money) {
		
		String[] han1 = { "", "��", "��", "��", "��", "��", "��", "ĥ", "��", "��"};
		String[] han2 = { "õ", "", "��", "��"};
		String[] han3 = { "", "��", "��", "��"};

		String smoney=money+"";
		String moneyHan="";
				
		int len=smoney.length(); 
		
		if(len>16) {
			System.out.println("������ȯ�� �Ұ����մϴ�. õ�������� ������ �߰��Ͻʽÿ�");
			return "�ݾ� ���� ����";
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
		moneyHan += " ��";

		if(moneyHan.substring(0,2).equals("�ϸ�"))
			moneyHan=moneyHan.replace("�ϸ�", "��");
		moneyHan=moneyHan.replace("��õ", "õ");
		moneyHan=moneyHan.replace("�Ϲ�", "��");
		moneyHan=moneyHan.replace("�Ͻ�", "��");
		moneyHan=moneyHan.replace(" ��", "");
		moneyHan=moneyHan.replace(" ��", "");			
				
		return moneyHan;    		
	}

	@Override
	public String checknum(double money) {
		DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
		String num=dc.format(money);
		return num;
	}

	@Override  //���� ū �����ڵ� ��������
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

	@Override  //���� ���� �����ڵ� ��������
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
	
	@Override  //�ش� �����ڵ��� �����ڵ� ��������
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

	@Override //�ش� �����ڵ��� �����̸� ��������
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
 * @Override  //���Ǹűݾ�2
	public long totalPrice2() {
		dbConn();
		long totalPrice=-1;
		try {
			//���� �Ǹ��Ҷ� 1.(salerate_apply=0 �϶� sales_price ��)	
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
			
			//���� �Ǹ��Ҷ� 2-1. (salerate_apply=0 �϶�, �Ǹ�����=�������� ���ڰ�������)
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
			
			//���� �Ǹ��Ҷ� 2-2. (salerate_apply=0 �϶�, �Ǹ�����=�������� ���ں� ���� sales_price)
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
			
			//���ε� ���ݵ��� ��
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
 * 	@Override //���⳻������
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
			result=pstmt.executeQuery("select last_insert_id()"); //�Էµ� Ű�� Ȯ��
			result.next();
			int key=result.getInt(1);
			
			pstmt.close();
			pstmt=conn.prepareStatement("insert into salerate(fruit_code,salerate_apply,salerate) values (?,0,0)");
			pstmt.setInt(1, key); 
			pstmt.executeUpdate(); 
 */

/*
 * @Override //�Ǹ�ó�� + ������
	public void insertSales(int fruit_code, int quantity, double salerate) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //����Ŀ�� ����
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //�Էµ� Ű�� Ȯ��
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
//				System.out.println("��¥���� �߻�");
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
			
			conn.commit(); //�����̸� Ŀ��
			
		} catch (SQLException e) {
			System.out.println("�ǸŽ���");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //�߰��� ������ ����� �ѹ�
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		
	}
 * 
 */

