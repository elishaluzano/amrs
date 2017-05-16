import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PC{

	static LinkedList<Register> registers = new LinkedList<Register>();
	static LinkedList<Instruction> instructions = new LinkedList<Instruction>();
	static HashMap<String,Integer> flags = new HashMap<String,Integer>();
	static LinkedList<Point> hazards = new LinkedList<Point>();
	static MonitorInstruction mi = new MonitorInstruction();
	static Verifier verifier = new Verifier();
	static Gui mainGui = new Gui();
	static Integer programCounter = 0;
	static int linecount=0;
	static int stall=0;
	static String currentLine;
	static Boolean flag = false;
	static int sec = 0;

	public static void main(String []args){

		//Initialization of flags
		flags.put("ZF", 0);
		flags.put("NF", 0);
		flags.put("OF", 0);
		flags.put("MAR", 0);
		flags.put("MBR", 0);

		//Initialization of Registers
		for(int i=1; i<33; i++){
			registers.add(new Register("r"+i,0));
		}

		mainGui.showGui();
		readFile(mainGui.initialFile());
		startPC();

	}

	// allows user to choose file input
	public static void readFile(String fn) {

			try{
					BufferedReader br = new BufferedReader(new FileReader(fn));
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

			}catch(Exception e){
				System.out.println(e.toString());
			}

		}

	public static void startPC() {

		mi.setCC(instructions);
		while(mi.getCC().getLast().getLast().getState() != "done")
		{
			Boolean stallFlag = false;
			sec ++;
			System.out.println("\n---------------SECONDS: " + sec);
			int misize = mi.getCC().getLast().size();
			for(int i=0; i<misize; i++)
			{
				Instruction instr = mi.getCC().getLast().get(i);
				if(instr.isStall()) stallFlag = true;
				if(instr.getState() == "waiting" && flag == false){
					programCounter = mi.fetch(mi.getCC().getLast().get(i),programCounter);
					flag = true;
				}
				else if(instr.getState() == "fetch"){

					if(i!=0)
					{
						boolean lflag = false;
						for(int j=1;j<5;j++)		// checks the prev 5 instr
						{
							int index = i-j;
							if(index >= 0)
							{
								Instruction previnstr = mi.getCC().getLast().get(index);
								if((instr.getOperand1().equals(previnstr.getOperand1()) || instr.getOperand2().equals(previnstr.getOperand1())) && previnstr.getState() != "done")
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}else if(previnstr.getState() == "decode")
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}else if(previnstr.getState() == instr.getState())
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}
							}
						}
						if (lflag) continue;
					}
					mi.decode(registers, mi.getCC().getLast().get(i));
				}
				else if(instr.getState() == "decode"){
					if(i!=0)
					{
						boolean lflag = false;
						for(int j=1;j<5;j++)
						{
							int index = i-j;
							if(index >= 0)
							{
								Instruction previnstr = mi.getCC().getLast().get(index);
								if((instr.getOperand1().equals(previnstr.getOperand1()) || instr.getOperand2().equals(previnstr.getOperand1())) && previnstr.getState() != "done")
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}else if(previnstr.getState() == "execute")
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}else if(previnstr.getState() == instr.getState())
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}
							}
						}
						if (lflag) continue;
					}
					mi.execute(registers, flags, mi.getCC().getLast().get(i));
				}

				else if(instr.getState() == "execute"){
					if(i!=0)
					{
						boolean lflag = false;
						for(int j=1;j<5;j++)
						{
							int index = i-j;
							if(index >= 0)
							{
								Instruction previnstr = mi.getCC().getLast().get(index);
								if((instr.getOperand1().equals(previnstr.getOperand1()) || instr.getOperand2().equals(previnstr.getOperand1())) && previnstr.getState() != "done")
								{
									System.out.println("Stall instr "+instr.getOperation());
									instr.setStall(true);
									lflag = true;
									break;
								}
							}
						}
						if (lflag) continue;
					}
					mi.memoryAccess(registers, flags, mi.getCC().getLast().get(i));
				}

				else if(instr.getState() == "memory access"){
					if(i!=0)
					{
						Instruction previnstr = mi.getCC().getLast().get(i-1);
						if((instr.getOperand1().equals(previnstr.getOperand1()) || instr.getOperand2().equals(previnstr.getOperand1())) && previnstr.getState() != "done")
						{
							System.out.println("Stall instr "+instr.getOperation());
							instr.setStall(true);
							continue;
						}
					}
					mi.writeBack(registers, flags, mi.getCC().getLast().get(i));
				}

				else if(instr.getState() == "writeback"){
					instr.setState("done");
				}
			}

			if(stallFlag){
				stall ++;
			}

			for(int i=0; i<mi.getCC().getLast().size(); i++){
				mi.printStatus(flags, mi.getCC().getLast().get(i));
			}
			System.out.println("\nPC: "+programCounter);
			flag = false;
			mi.setCC(mi.getCC().getLast());
		}
		printSummary();
	}

	// resets the values in PC (for another input file)
	public static void restartPC() {

		instructions.clear();
		registers.clear();
		hazards.clear();
		mi.getCC().clear();

		programCounter = 0;
		linecount=0;
		stall=0;
		sec=0;
		flag = false;

		//Initialization of flags
		flags.put("ZF", 0);
		flags.put("NF", 0);
		flags.put("OF", 0);
		flags.put("MAR", 0);
		flags.put("MBR", 0);

		//Initialization of Registers
		for(int i=1; i<33; i++){
			registers.add(new Register("r"+i,0));
		}
	}

	public static void printSummary() {
		System.out.println("--------------------SUMMARY--------------------------");

		for(int i=0; i<instructions.size(); i++){
			for(int j=0; j<instructions.size(); j++){
				if(i != j){
					Point p1 = new Point(i,j);
					Point p2 = new Point(j,i);
					if(hazards.contains(p1) || hazards.contains(p2)){
						continue;
					}
					if(instructions.get(i).getOperand1().equals(instructions.get(j).getOperand1())){
						System.out.println("WAW: " + (i+1) + " & " + (j+1) + " " + instructions.get(i).getOperand1());
					}

					else if(instructions.get(i).getOperand1().equals(instructions.get(j).getOperand2())){
						System.out.println("RAW: " + (i+1) + " & " + (j+1) + " " + instructions.get(i).getOperand1());
					}

					else if(instructions.get(i).getOperand2().equals(instructions.get(j).getOperand1())){
						System.out.println("WAR: " + (i+1) + " & " + (j+1) + " " + instructions.get(i).getOperand1());
					}
					hazards.add(p1);
				}
			}
		}

		System.out.println("\n\nSTALLS: " + stall);
		System.out.println("CPI: " + ((float)instructions.size() / (float)sec));
		System.out.println("TOTAL CC: " + sec);

	}
}
