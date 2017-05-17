import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

public class Gui{
	private JFrame mainFrame = new JFrame("AMRS");
	private Container frame = mainFrame.getContentPane();
	private JPanel menuPanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JLabel browseLabel = new JLabel("Select file: ");
	private JButton browseButton = new JButton("Browse");
	private JPanel bottomPanel = new JPanel();
	private Integer pageNum = 0;
	private Integer seconds = 0;


		private JTable fdetable;
	private LinkedList<LinkedList<Register>> registersPerSecond = new LinkedList<LinkedList<Register>>();
	private LinkedList<LinkedList<Instruction>> instructions = new LinkedList<LinkedList<Instruction>>();

	private JTable mainRegTable;
	private JTable otherRegTable;

	private String fileName = "";

	public Gui(){
		init();
	}

	private void init() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setPreferredSize(new Dimension(1040, 600));

		// this will hold the contents of mainFrame
		frame.setLayout(new BorderLayout());

		// contains browse button (for file opening)
		menuPanel.add(browseLabel);
		menuPanel.add(browseButton);

		// browse button action listener
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Browse");
				PC.restartPC();
				// restartGui(); -- clear tables and stuff
				chooseFile();
			}
		});

		contentPanel.setBackground(Color.red);	

		// other tables will go here


		frame.add(menuPanel, BorderLayout.NORTH);

		String[] col = {"Register", "Value"};

		Object[][] row = new Object[33][2];

		row[0][0] = "Register";
		row[0][1] = "Value";

		mainRegTable = new JTable(row, col);
		JScrollPane scrollPane = new JScrollPane(mainRegTable);
		mainRegTable.setFillsViewportHeight(true);
		frame.add(mainRegTable, BorderLayout.WEST);

		String[] col2 = {"Register", "Value"};

		Object[][] row2 = {
			{"MAR", 0},
			{"MBR", 0},
			{"PC", 0},
			{"ZF", 0},
			{"NF", 0},
			{"OF", 0},
		};




		otherRegTable = new JTable(row2, col2);

		JScrollPane scrollPane2 = new JScrollPane(otherRegTable);
		otherRegTable.setFillsViewportHeight(true);
		frame.add(otherRegTable, BorderLayout.EAST);

		mainReg();

		//mainReg();
		mainFrame.pack();

	}

	public void getReg(LinkedList<LinkedList<Register>> r, LinkedList<LinkedList<Instruction>> i){
		instructions = i;
		registersPerSecond = r;
		seconds = registersPerSecond.size();
		mainReg();

		bottomNav();



		if(instructions.size() != 0){
		Object[] colheader = new Object[seconds+1];

		for(int j=0; j<seconds+1; j++) {
			if(j==0) colheader[j] = "Instruction";
			else colheader[j] = j;
		}





		Object[][] data = new Object[instructions.get(0).size()+1][seconds+1];

		for(int j=0; j<instructions.get(0).size(); j++){
			if(j==0) data[j][0] = "";
			data[j+1][0] = instructions.get(0).get(j).getOperation() + " " + instructions.get(0).get(j).getOperand1() + " " + instructions.get(0).get(j).getOperand2();
		}



		for(int j=0; j<instructions.size(); j++){
			for(int x=0; x<instructions.get(j).size(); x++){
				if(instructions.get(j).get(x).isStall()) data[x+1][j+1] = "S";
				else if(instructions.get(j).get(x).getState().equals("fetch")) data[x][j+1] = "F";
				else if(instructions.get(j).get(x).getState().equals("decode")) data[x][j+1] = "D";
				else if(instructions.get(j).get(x).getState().equals("execute")) data[x][j+1] = "E";
				else if(instructions.get(j).get(x).getState().equals("memory access")) data[x][j+1] = "M";
				else if(instructions.get(j).get(x).getState().equals("writeback")) data[x][j+1] = "W";
				else data[x][j+1] = " ";
			}
		}

		fdetable = new JTable(data, colheader);


		for(int j=0; j<seconds; j++){
			fdetable.getColumnModel().getColumn(j+1).setMaxWidth(30);
		}
		JScrollPane pane = new JScrollPane(fdetable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		fdetable.setFillsViewportHeight(true);
		fdetable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		frame.add(pane, BorderLayout.CENTER);
		frame.validate();
		frame.repaint();
	}

		TableColumn column = fdetable.getColumnModel().getColumn(0);
		column.setMaxWidth(200);


	}

	public void mainReg(){


		//System.out.println(registersPerSecond.get(0).get(0).getRegValue());






		String[] col = {"Register", "Value"};

		Object[][] row = new Object[33][2];

		row[0][0] = "Register";
		row[0][1] = "Value";

		if(registersPerSecond.size() != 0){
			System.out.println(pageNum);
			for(int i=0; i<(registersPerSecond.get(pageNum)).size(); i++){
				System.out.println(registersPerSecond.get(pageNum).get(i).getRegName() + " " + registersPerSecond.get(pageNum).get(i).getRegValue());
				/*row[i+1][0] = registersPerSecond.get(pageNum).get(i).getRegName();
				row[i+1][1] = Integer.toString(registersPerSecond.get(pageNum).get(i).getRegValue());*/
				mainRegTable.getModel().setValueAt(registersPerSecond.get(pageNum).get(i).getRegName(), i+1, 0);
				mainRegTable.getModel().setValueAt(registersPerSecond.get(pageNum).get(i).getRegValue(), i+1, 1);
			}
		}

		
	}

	public void otherReg(){
		
		
	}


	public void bottomNav(){
		JButton prev = new JButton("<");
		JButton next = new JButton(">");
		JLabel cc = new JLabel("Clock Cycle: 1");

		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pageNum > 0){
					pageNum --;
					mainReg();
					cc.setText("Clock Cycle: " + Integer.toString(pageNum+1));
				}
			}
		});

		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pageNum != (seconds-2)){
					pageNum ++;
					mainReg();
					cc.setText("Clock Cycle: " + Integer.toString(pageNum+1));
				}
			}
		});

		bottomPanel.add(cc);
		bottomPanel.add(prev);
		bottomPanel.add(next);

		JLabel totalCC = new JLabel("Total Clock Cycle: " + (seconds-1));
		bottomPanel.add(totalCC);

		frame.add(bottomPanel, BorderLayout.SOUTH);
	}

	public String initialFile() {
		JFileChooser fileOpener = new JFileChooser();
		int result = fileOpener.showOpenDialog(new JFrame());

		fileOpener.setCurrentDirectory(new File(System.getProperty("user.home")));

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileOpener.getSelectedFile();
			System.out.println("\nOpened " + selectedFile.getAbsolutePath());
			fileName = selectedFile.getAbsolutePath();
		}
		return fileName;
	}


	private void chooseFile() {
		JFileChooser fileOpener = new JFileChooser();
		int result = fileOpener.showOpenDialog(new JFrame());

		fileOpener.setCurrentDirectory(new File(System.getProperty("user.home")));

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileOpener.getSelectedFile();
			System.out.println("\nOpened " + selectedFile.getAbsolutePath());
			fileName = selectedFile.getAbsolutePath();
		}

		PC.readFile(fileName);
		PC.startPC();
	}

	public void showGui() {
		mainFrame.setVisible(true);
	}

}
