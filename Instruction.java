import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Instruction{
	private String operation;
	private String op1;
	private String op2;
	private Integer val1;
	private Integer val2;
	private String state;

	public Instruction(String operation, String op1, String op2){
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
		this.val1 = 0;
		this.val2 = 0;
	}

	public void printInstr(){
		System.out.println("operation: " + this.operation + "\n" + "op1: " + this.op1 + "\n" + "op2: " + this.op2 + "\n");
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

	public void setValue1(Integer val1){
		this.val1 = val1;
	}

	public void setValue2(Integer val2){
		this.val2 = val2;
	}

	public void load(LinkedList<Register> registers, HashMap<String,Integer> flags) {
		if (isRegister(this.op2))
		{	
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op2))
				{
					this.val2 = reg.getRegValue();
				}
			}
		}
			this.val1 = this.val2;
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op1))
				{
					reg.setRegValue(this.val1);
				}
			}
		
		// this.printStatus(flags);
	}

	public void add(LinkedList<Register> registers, HashMap<String,Integer> flags) {
		if (isRegister(this.op2))
		{	
			int regValue=0;
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op2))
				{
					regValue = reg.getRegValue();
				}
			}
			this.val1 = this.val1 + regValue;
		}
		else
		{
			this.val1 = this.val1 + this.val2;
		}
		for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op1))
				{
					reg.setRegValue(this.val1);
				}
			}
		// this.printStatus(flags);
	}

	public void sub(LinkedList<Register> registers, HashMap<String,Integer> flags) {
		if (isRegister(this.op2))
		{
			int regValue=0;
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op2))
				{
					regValue = reg.getRegValue();
				}
			}
			this.val1 = this.val1 - regValue;
		}
		else
		{
			this.val1 = this.val1 - this.val2;
		}
		for(int i=0;i<registers.size();i++)
		{
			Register reg = registers.get(i);
			if(reg.getRegName().equals(this.op1))
			{
				reg.setRegValue(this.val1);
			}
		}
		// this.printStatus(flags);
	}

	public void cmp(LinkedList<Register> registers, HashMap<String,Integer> flags) {
		/*Compares the values of two registers by subtracting the value of the second register from the value of the
		first register. If the resultis zero (0),the ZF is setto 1, 0 otherwise (default). Ifthe resultis negative,the NF is set
		to 1, 0 otherwise (default). No change (default value) for NF and ZF for a positive difference.*/
		int diff = 0;
		if (isRegister(this.op1) && isRegister(this.op2))								// both are registers
		{
			int regValue1=0, regValue2=0;
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op1))
				{
					regValue1 = reg.getRegValue();
				}
				else if(reg.getRegName().equals(this.op2))
				{
					regValue2 = reg.getRegValue();
				}
			}
			diff = regValue1 - regValue2;
		}
		else if (isRegister(this.op1) && !isRegister(this.op2))					// 2nd operand is not a register
		{
			int regValue1=0;
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(this.op1))
				{
					regValue1 = reg.getRegValue();
				}
			}
			diff = regValue1 - this.val2;
		}
		// sets the ZF and NF based on the difference
		if (diff == 0) flags.put("ZF", 1);
		else if (diff < 0) flags.put("NF", 1);
		// System.out.println("diff is: " + diff);
		System.out.println();
		// this.printStatus(flags);
	}

	public void setState(String state){
		this.state = state;
	}

	private boolean isRegister(String operand)
	{
		Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}
}
