import java.io.*;
import java.util.*;
import java.util.regex.*;
public class MonitorInstruction{
	private LinkedList<LinkedList<Instruction>> clockCycles;


	public MonitorInstruction(){
		this.clockCycles = new LinkedList<LinkedList<Instruction>>();
	}

	public Integer fetch(Instruction instr, Integer pc)
	{
		instr.setState("fetch");
		return pc+=1;
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
		instr.setStall(false);
		for(int i=0;i<registers.size();i++)
		{
			Register reg = registers.get(i);
			if(reg.getRegName().equals(instr.getOperand1()))
			{
				// instr.val1 = reg.getRegValue();
				registers.get(i).setBusy(true);
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

		for(int i=0;i<registers.size();i++)
		{
			if(registers.get(i).getRegName().equals(instr.getOperand1()))
			{
				registers.get(i).setBusy(false);
			}
		}
	}

	public void memoryAccess(LinkedList<Register> registers, HashMap<String, Integer> flags, Instruction instr){
		instr.setState("memory access");
	}

	public void writeBack(LinkedList<Register> registers, HashMap<String, Integer> flags, Instruction instr){
		instr.setState("writeback");
		if(instr.getOperation() != "cmp"){
			if(instr.getValue1()>99){
				instr.setValue1(99);
				flags.put("OF", 1);
			}else if(instr.getValue1()<-99){
				instr.setValue1(-99);
				flags.put("OF", 1);
			}

			for(int i=0;i<registers.size();i++)
			{
				Register reg = registers.get(i);
				if(reg.getRegName().equals(instr.getOperand1()))
				{
					reg.setRegValue(instr.getValue1());
				}
			}
		}else{
			if (instr.getTemp() == 0) flags.put("ZF", 1);
			else if (instr.getTemp() < 0) flags.put("NF", 1);
		}
	}

	public void printStatus(HashMap<String, Integer> flags, Instruction instr) {
		//System.out.println(this.getOperand1()+" "+this.op2);
		System.out.println("\n" + instr.getOperation() + " " + instr.getOperand1() + " " + instr.getOperand2());
		System.out.println(instr.getOperand1() + ": " + instr.getValue1() + "\n" + instr.getOperand2() + ": " + instr.getValue2());
		System.out.println("ZF: " + flags.get("ZF") + "\n" + "NF: " + flags.get("NF") + "\n" + "OF: " + flags.get("OF"));
		System.out.println("Status: " + instr.getState());
		System.out.println("Stall: " + instr.isStall());

	}

	private boolean isRegister(String operand)
	{
		Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}
}
