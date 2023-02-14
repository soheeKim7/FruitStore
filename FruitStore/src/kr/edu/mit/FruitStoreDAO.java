package kr.edu.mit;

import java.util.ArrayList;
import java.util.List;

public interface FruitStoreDAO {
	
	////////////퀄리별로 구현////////////
	//과일등록
	void insertFruit(FruitVO vo);
	//과일목록보여주기
	ArrayList<FruitVO> listFruit();
	//수량업데이트
	void updateQuantityFruit(FruitVO vo);
	
	//과일별 총가격알려주기
	int totalFruit(FruitVO vo);
	double totalFruit(FruitVO vo, double salerate);
	
	//판매처리
	//1.판매내용 추가 - 2.판매내용 추가 키값 알아오기 - 3.교차테이블 갱신(추가) - 4.재고수정처리
	void insertSales(int fruit_code,int quantity);  //키값을 리턴
	void insertSales(int fruit_code,int quantity,double salerate);
	
	
	//총판매금액
	long totalPrice();
	long totalPrice2();
	
	//정가 총판매금액
	long nomarlPrice();
	
	//할인 총판매금액
	long salePrice();
	
	//매출내역보기
	List<SalesVO> listSales();
	
	//해당 과일이름의 과일코드 가져오기
	Integer getFruitCode(String fruit_name);
	
	//금액 한글 변환하기
	String changemoney(long money);
	
	//숫자 화폐 단위로 끊어표시
	String checknum(double money);
	
	//가장 큰 과일코드 가져오기
	Integer maxFruit_code();
	
	//가장 작은 과일코드 가져오기
	Integer minFruit_code();
	
	//해당 과일코드의 과일코드 가져오기
	Integer codeFruit(int fruit_code);
	
	//해당 과일코드의 과일이름 가져오기
	String codeFruitName(int fruit_code);
	    
	
}
