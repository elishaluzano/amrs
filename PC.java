import java.io.*;
import java.util.*;

public class PC{

	public static void main(String []args){

		ArrayList<Register> registers = new ArrayList<Register>();
		HashMap<String,Integer> flags = new HashMap<String,Integer>();

		//Initialization of flags
		flags.put("ZF", 0);
		flags.put("NF", 0);

		//Initialization of Registers
		for(int i=1; i<33; i++){
			registers.add(new Register("r"+toString(i),0));
		}


		int linecount=0;
		String currentLine;
		Verifier verifier = new Verifier();
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();

		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			while((currentLine = br.readLine())!=null){
				linecount++;
				currentLine = currentLine.replaceAll("\\s"," ");
				currentLine = currentLine.toLowerCase();
				if(currentLine.equals("")) continue;
				String[] temp = currentLine.split("");
				String acc = "hello";
				ArrayList<String> arr = new ArrayList<String>();

				for(int i=0; i<temp.length; i++){
					if(!temp[i].equals(" ") && !temp[i].equals(",")){
						acc += temp[i];
					}else if(!acc.equals("")){
						arr.add(acc);
						acc = "";
					}
				}
				if(!acc.equals("")) arr.add(acc);
				if(arr.size()==3){
					Instruction instr = new Instruction(arr.get(0),arr.get(1), arr.get(2));
					if(verifier.checkValidity(instr))
					{
						instructions.add(instr);
						instr.fetch(registers);
						instr.execute(registers, flags);
					}
					else{
						System.out.print(arr.toString());
						System.out.println(" is an invalid instruction set!");
					}
				}else{
					System.out.print(arr.toString());
					System.out.println(" is an invalid instruction set!");
				}
			}

			System.out.println("\n\n-----Code-----");
			for(Instruction i : instructions){
				i.printInstr();
			}

		}catch(Exception e){

		}
	}
}
