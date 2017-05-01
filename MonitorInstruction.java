import java.io.*;
import java.util.*;
import java.util.regex.*;
public class MonitorInstruction{
	private LinkedList<LinkedList<Instruction>> clockCycles;


	public MonitorInstruction(){
		this.clockCycles = new LinkedList<LinkedList<Instruction>>();
	}

	public void fetch(Instruction instr)
	{
		instr.setState("fetch");
	}

	public LinkedList<LinkedList<Instruction>> getCC()
	{
		return clockCycles;
	}

	public void setCC(LinkedList<Instruction> instrs)
	{
		clockCycles.add(instrs);
	}

	public void decode(LinkedList<Register> registers, Instruction instr)
	{
		instr.setState("decode");
		for(int i=0;i<registers.size();i++)
		{
			Register reg = registers.get(i);
			if(reg.getRegName().equals(instr.getOperand1()))
			{
				// instr.val1 = reg.getRegValue();
				instr.setValue1(reg.getRegValue());
			}
		}

		if (isRegister(instr.getOperand2()))
		{
			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(instr.getOperand2()))
				{
					// instr.val2 = reg.getRegValue();
					instr.setValue2(reg.getRegValue());
				}
			}
		}
		else
		{
			// instr.val2 = Integer.parseInt(instr.getOperand2());
			instr.setValue2(Integer.parseInt(instr.getOperand2()));	
		}
	}

	public void execute(LinkedList<Register> registers, HashMap<String, Integer> flags, Instruction instr){
		instr.setState("execute");
		if(instr.getOperation().equals("add")){
			instr.add(registers, flags);
		}
		else if(instr.getOperation().equals("sub")) {
			instr.sub(registers, flags);
		}
		else if(instr.getOperation().equals("load")){
			instr.load(registers, flags);
		}
		else if(instr.getOperation().equals("cmp")){
			instr.cmp(registers, flags);
		}
	}

	public void printStatus(HashMap<String, Integer> flags, Instruction instr) {
		//System.out.println(this.getOperand1()+" "+this.op2);
		System.out.println("\n" + instr.getOperation() + " " + instr.getOperand1() + " " + instr.getOperand2());
		System.out.println(instr.getOperand1() + ": " + instr.getValue1() + "\n" + instr.getOperand2() + ": " + instr.getValue2());
		System.out.println("ZF: " + flags.get("ZF") + "\n" + "NF: " + flags.get("NF"));
		System.out.println("Status: " + instr.getState());

	}

	private boolean isRegister(String operand)
	{
		Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}
}
