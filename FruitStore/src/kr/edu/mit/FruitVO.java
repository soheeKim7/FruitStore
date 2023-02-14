package kr.edu.mit;

public class FruitVO {
	//코드
	private int fruit_code;
	
	//이름
	private String fruit_name;
	
	//가격
	private double price;
	
	//수량
	private int quantity;
	
	private int salerate_apply;
	
	private double salerate;

	public int getSalerate_apply() {
		return salerate_apply;
	}

	public void setSalerate_apply(int salerate_apply) {
		this.salerate_apply = salerate_apply;
	}

	public double getSalerate() {
		return salerate;
	}

	public void setSalerate(double salerate) {
		this.salerate = salerate;
	}

	public int getFruit_code() {
		return fruit_code;
	}

	public void setFruit_code(int fruit_code) {
		this.fruit_code = fruit_code;
	}

	public String getFruit_name() {
		return fruit_name;
	}

	public void setFruit_name(String fruit_name) {
		this.fruit_name = fruit_name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "FruitVO [fruit_code=" + fruit_code + ", fruit_name=" + fruit_name + ", price=" + price + ", quantity="
				+ quantity + "]";
	}
	
	
	
	
}
