import java.io.*;
import java.util.*;

public class PC{

	public static void main(String []args){

		LinkedList<Register> registers = new LinkedList<Register>();
		HashMap<String,Integer> flags = new HashMap<String,Integer>();
		MonitorInstruction mi = new MonitorInstruction();

		//Initialization of flags
		flags.put("ZF", 0);
		flags.put("NF", 0);

		//Initialization of Registers
		for(int i=1; i<33; i++){
			registers.add(new Register("r"+i,0));
		}

		int linecount=0;
		String currentLine;
		Verifier verifier = new Verifier();
		LinkedList<Instruction> instructions = new LinkedList<Instruction>();

		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			while((currentLine = br.readLine())!=null){
				linecount++;
				ArrayList<String> arr = new ArrayList<String>();
				currentLine = currentLine.replaceAll("\\s"," ");
				currentLine = currentLine.toLowerCase();
				if(currentLine.equals("")) continue;
				String[] temp = currentLine.split("");
				String acc = "";

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

			/*System.out.println("\n\n-----Code-----");
			for(Instruction i : instructions){
				i.printInstr();
			}*/

		}catch(Exception e){

		}
		Boolean flag = false;
		int sec = 0;

		mi.setCC(instructions);
		while(mi.getCC().getLast().getLast().getState() != "decode") {
			sec ++;
			System.out.println("SECONDS: " + sec);
			for(int i=0; i<mi.getCC().getLast().size(); i++){
				if(mi.getCC().getLast().get(i).getState() == "waiting" && flag == false){
					mi.fetch(mi.getCC().getLast().get(i));
					flag = true;
				}
				else if(mi.getCC().getLast().get(i).getState() == "fetch"){
					mi.decode(registers, mi.getCC().getLast().get(i));
				}

				else if(mi.getCC().getLast().get(i).getState() == "decode"){
					mi.execute(registers, flags, mi.getCC().getLast().get(i));
				}
			}
			for(int i=0; i<mi.getCC().getLast().size(); i++){
				mi.printStatus(flags, mi.getCC().getLast().get(i));
			}
			flag = false;
			mi.setCC(mi.getCC().getLast());
		}
	}
}
