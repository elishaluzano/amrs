import java.io.*;
import java.util.*;

public class PC{
	public static void main(String []args){
		int linecount=0;
		String currentLine;
		Verifier verifier = new Verifier();
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();

		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			while((currentLine = br.readLine())!=null){
				linecount++;
				currentLine = currentLine.trim();
				currentLine = currentLine.toLowerCase();
				currentLine = currentLine.replace("\n", "");
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
					if(verifier.checkValidity(instr)) instructions.add(instr);
					else{
						System.out.print(arr.toString());
						System.out.println(" is an invalid instruction set!");
					}
				}else{
					System.out.println("Error");
				}
			}

		}catch(Exception e){

		}
	}
}