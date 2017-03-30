import java.io.*;
import java.util.ArrayList;

public class PC{
	public static void main(String []args){
		int linecount=0;
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		ArrayList<String> possibleOperations = new ArrayList<String>();
		possibleOperations.add("add");
		possibleOperations.add("sub");
		possibleOperations.add("load");
		possibleOperations.add("cmp");
		String currentLine;
		final registerPattern = new Pattern("r[0-32]");
	

		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			while((currentLine = br.readLine())!=null){
				linecount++;
				currentLine = currentLine.trim();
				currentLine = currentLine.toLower();
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
				System.out.println();
				if(arr.size()==3){
					Instruction instr = new Instruction(arr.get(0),arr.get(1), arr.get(2));
					instructions.add(instr);
				}else{
					System.out.println("Error");
				}
			}

		}catch(Exception e){

		}
	}	


}