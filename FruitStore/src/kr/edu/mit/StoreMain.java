package kr.edu.mit;

import java.util.List;
import java.util.Scanner;

public class StoreMain {

	public static void main(String[] args) {
		//1.과일입고, 2.재고파악, 3.판매하기, 4.매출확인
		Scanner in = new Scanner(System.in);
		
		FruitStoreDAO dao = new FruitStoreDAOImpl();
		
		int menuNum;
		do {
			//매뉴출력하기
			System.out.println("1.과일입력");
			System.out.println("2.재고파악");
			System.out.println("3.판매하기");
			System.out.println("4.매출확인");
			System.out.print("매뉴를 선택해주세요.(0은 종료): ");
			
			//사용자입력받고 해당메뉴 실행하기
			menuNum=in.nextInt();
			if(menuNum==1) {
				System.out.println("과일입력기능입니다.");
				//1.과일목록보여주기
				//2.선택할지 추가할지 입력받고
				//3-1.선택일 경우
				//    입고수량 받고 DB처리(업데이트) ->보내줘야할 내용 과일코드,수량
				//3-2.추가일 경우
				//    과일이름,가격,수량 받고 DB처리(삽입) 
		
			}else if(menuNum==2) {
				System.out.println("재고파악기능입니다.");
			}else if(menuNum==3) {
				//1.과일목록보여주기 -(DB)과일목록보여주기 
				List<FruitVO> list =dao.listFruit();
				for(FruitVO vo: list) {
					System.out.println(vo);
				}
				System.out.println("과일목록입니다.과일번호를 선택해주세요 : ");
				
				//2.사용자가 선택(코드,개수)
				int fruit_code=in.nextInt(); //과일번호 입력
				System.out.println("구매할 수량을 입력해 주세요: ");
				int quantity=in.nextInt(); //과일수량
								
				//3.지불금액안내 -(DB)과일별 총가격알려주기
				FruitVO vo = new FruitVO();
				vo.setFruit_code(fruit_code);
				vo.setQuantity(quantity);
				System.out.println("총구매금액은"+dao.totalFruit(vo)+"입니다.");
				System.out.println("구매하시겠습니까?(1:구매, 2:취소)");
				
				if(in.nextInt()==1) {
					//4.판매완료 -(DB)판매처리
					System.out.println("판매DB작성 중");
					dao.insertSales(fruit_code, quantity);
				}
			}else if(menuNum==4) {
				System.out.println("매출확인기능입니다.");
			}else if(menuNum==0) {
				System.out.println("이용해주셔서 감사합니다.");
			}
		}while(menuNum!=0);
	}

}
