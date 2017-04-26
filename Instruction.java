import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Instruction{
	private String operation;
	private String op1;
	private String op2;
	private Integer val1;
	private Integer val2;

	public Instruction(String operation, String op1, String op2){
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
		this.val1 = 0;
		this.val2 = 0;
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

	public Integer getValue1(){
		return this.val1;
	}

	public Integer getValue2(){
		return this.val2;
	}

	public void load(HashMap<String,Integer> registers) {
		if (isRegister(this.op2))
		{
			this.val2 = registers.get(this.op2);
			this.val1 = this.val2;
			registers.put(this.op1, this.val1);
		}
		else
		{	
			this.val1 = this.val2;
			registers.put(this.op1, this.val1);
		}
		this.printStatus();
	}

	public void add(HashMap<String,Integer> registers) {
		if (isRegister(this.op2))
		{
			this.val1 = this.val1 + registers.get(this.op2);
			registers.put(this.op1, this.val1);
		}
		else
		{
			this.val1 = this.val1 + this.val2;
			registers.put(this.op1, this.val1);
		}
		this.printStatus();
	}

	public void fetch(HashMap<String,Integer> registers)
	{
		if (isRegister(this.op1))
		{
			this.val1 = registers.get(this.op1);
		}
		else
		{
			this.val1 = Integer.parseInt(this.op1);
		}

		if (isRegister(this.op2))
		{
			this.val2 = registers.get(this.op2);
		}
		else
		{
			this.val2 = Integer.parseInt(this.op2);
		}
	}

	private void printStatus() {
		//System.out.println(this.op1+" "+this.op2);
		System.out.println("\n" + this.operation + " " + this.op1 + " " + this.op2);
		System.out.println("\t" + this.op1 + ": " + this.val1 + " | " + this.op2 + ": " + this.val2);
	}

	private boolean isRegister(String operand)
	{
		Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}
}