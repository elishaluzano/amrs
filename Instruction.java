public class Instruction{
	private String operation;
	private String op1;
	private String op2;

	public Instruction(String operation, String op1, String op2){
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
	}

	public void printShit(){
		System.out.println(this.operation+" "+this.op1+" "+this.op2);
	}

	public String getOperation(){
		return this.operation;
	}

	public String getOperand1(){
		return this.op1;
	}

	public String getOperand2(){
		return this.op2;
	}
}