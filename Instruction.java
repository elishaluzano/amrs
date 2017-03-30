public class Instruction{
	String operation;
	String op1;
	String op2;

	public Instruction(String operation, String op1, String op2){
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
	}

	public void printShit(){
		System.out.println(this.operation+" "+this.op1+" "+this.op2);
	}
}