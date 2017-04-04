import java.util.*;
import java.util.regex.*;

public class Verifier{
	ArrayList<String> possibleOperations = new ArrayList<String>(Arrays.asList("add", "sub", "load", "cmp"));
	final Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");

	public boolean checkValidity(Instruction instr){
		if(!validateOperation(instr.getOperation())) return false;
		if(instr.getOperation().equals("cmp")){
			if(!validateRegister(instr.getOperand1()) && !validateImmediate(instr.getOperand1())) return false;
		}else{
			if(!validateRegister(instr.getOperand1())) return false;
		}
		if(!validateRegister(instr.getOperand2()) && !validateImmediate(instr.getOperand2())) return false;
		return true;
	}


	public boolean validateOperation(String operation){
		if(!possibleOperations.contains(operation)) System.out.println("FAILED AT "+operation);
		if(!possibleOperations.contains(operation)) return false;
		return true;
	}

	public boolean validateRegister(String operand){
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}

	public boolean validateImmediate(String operand){
		try{	
			Integer.parseInt(operand);
			return true;
		}catch(NumberFormatException e){
    		return false;
    	}
	}

}