import java.io.*;
import java.util.*;

public class PC{

	public static void main(String []args){
		
		HashMap<String,Integer> registers = new HashMap<String,Integer>();
		registers.put("r1",0);
		registers.put("r2",0);
		registers.put("r3",0);
		registers.put("r4",0);
		registers.put("r5",0);
		registers.put("r6",0);
		registers.put("r7",0);
		registers.put("r8",0);
		registers.put("r9",0);
		registers.put("r10",0);
		registers.put("r11",0);
		registers.put("r12",0);
		registers.put("r13",0);
		registers.put("r14",0);
		registers.put("r15",0);
		registers.put("r16",0);
		registers.put("r17",0);
		registers.put("r18",0);
		registers.put("r19",0);
		registers.put("r20",0);
		registers.put("r21",0);
		registers.put("r22",0);
		registers.put("r23",0);
		registers.put("r24",0);
		registers.put("r25",0);
		registers.put("r26",0);
		registers.put("r27",0);
		registers.put("r28",0);
		registers.put("r29",0);
		registers.put("r30",0);
		registers.put("r31",0);
		registers.put("r32",0);

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
				String acc = "";
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
					}
					else{
						System.out.print(arr.toString());
						System.out.println(" is an invalid instruction set!");
					}
				}else{
					System.out.println("Error");
				}
			}

			for(Instruction i : instructions){
				i.printShit();
			}

		}catch(Exception e){

		}
	}
}