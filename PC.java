import java.io.*;
import java.util.ArrayList;

public class PC{
	public static void main(String []args){
		int linecount=0;
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		String currentLine;

		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			while((currentLine = br.readLine())!=null){
				
				linecount++;
				// // currentLine = currentLine.trim();
				// // currentLine = currentLine.replace("\n", "");
				// String[] line = currentLine.split("\n");
				String[] temp = currentLine.split("");
				System.out.println("currentLine: "+currentLine);
				System.out.println("temp: "+temp.length);
				if(temp.length == 0) continue;
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