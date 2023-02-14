package kr.edu.mit;

import java.util.ArrayList;
import java.util.List;

public interface FruitStoreDAO {
	
	////////////�������� ����////////////
	//���ϵ��
	void insertFruit(FruitVO vo);
	//���ϸ�Ϻ����ֱ�
	ArrayList<FruitVO> listFruit();
	//����������Ʈ
	void updateQuantityFruit(FruitVO vo);
	
	//���Ϻ� �Ѱ��ݾ˷��ֱ�
	int totalFruit(FruitVO vo);
	double totalFruit(FruitVO vo, double salerate);
	
	//�Ǹ�ó��
	//1.�Ǹų��� �߰� - 2.�Ǹų��� �߰� Ű�� �˾ƿ��� - 3.�������̺� ����(�߰�) - 4.������ó��
	void insertSales(int fruit_code,int quantity);  //Ű���� ����
	void insertSales(int fruit_code,int quantity,double salerate);
	
	
	//���Ǹűݾ�
	long totalPrice();
	long totalPrice2();
	
	//���� ���Ǹűݾ�
	long nomarlPrice();
	
	//���� ���Ǹűݾ�
	long salePrice();
	
	//���⳻������
	List<SalesVO> listSales();
	
	//�ش� �����̸��� �����ڵ� ��������
	Integer getFruitCode(String fruit_name);
	
	//�ݾ� �ѱ� ��ȯ�ϱ�
	String changemoney(long money);
	
	//���� ȭ�� ������ ����ǥ��
	String checknum(double money);
	
	//���� ū �����ڵ� ��������
	Integer maxFruit_code();
	
	//���� ���� �����ڵ� ��������
	Integer minFruit_code();
	
	//�ش� �����ڵ��� �����ڵ� ��������
	Integer codeFruit(int fruit_code);
	
	//�ش� �����ڵ��� �����̸� ��������
	String codeFruitName(int fruit_code);
	    
	
}
