package test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import kr.edu.mit.FruitStoreDAO;
import kr.edu.mit.FruitStoreDAOImpl;
import kr.edu.mit.FruitVO;

public class Test {

	public static void main(String[] args) {
		//자바클래스 테스트
		
//		int a=1;
//		int b=4;
//		int count=0;
//		int i=0;
//		for(i=a; i<=b;i++){
//			for(int j=1;j<=b;j++){
//				if(i%j==0){
//					count++;
//				}
//				if(count==2){
//					System.out.println(i);
//				}
//			}		
//		}
//		
		double x=2.8;
//		int y=(int)x;
		long y=(long)x;
		System.out.println(y);
		
//		SimpleDateFormat timefo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = new Date();
//		String dateS=timefo.format(result.getTimestamp(1));
//		try {
//			date = timefo.parse(dateS);
//		} catch (ParseException e) {
//			System.out.println("날짜오류 발생");
//		
		
		
		
//		FruitStoreDAO dao = new FruitStoreDAOImpl();
//		FruitVO vo=new FruitVO();
		
		
//		vo.setFruit_name("apple");
//		vo.setPrice(999);
//		vo.setQuantity(99);
		
//		dao.insertFruit(vo);
		
//		System.out.println(dao.getFruitCode("사과"));
		
//		dao.insertSales(29, 19);
		
//		System.out.println(dao.checknum(1546875314));
//		
//		vo.setFruit_code(2);
//		vo.setQuantity(10);
//		System.out.println(dao.totalFruit(vo, 30));
//		boolean check=true;
//		int comcode=0;
//		
//		do{
//			comcode=(int)(Math.random()*(dao.maxFruit_code()-dao.minFruit_code()+1)+dao.minFruit_code());
//			check=true;
//			//(int)(Marh.random()*(최대값-최소값+1)+최소값)
//			System.out.println(comcode);
//			if(dao.codeFruit(comcode)==null){
//				check=false;
//			}
//		}while(check==false);
		
//		System.out.println(dao.codeFruitName(3));

		
	}

}
