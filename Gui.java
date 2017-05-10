import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Gui{
	private JFrame mainFrame = new JFrame("AMRS");
	private Container frame = mainFrame.getContentPane();
	private JPanel menuPanel = new JPanel();
	private JLabel browseLabel = new JLabel("Select file: ");
	private JButton browseButton = new JButton("Browse");

	private String fileName = "";

	public Gui(){
		init();
	}

	private void init() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setPreferredSize(new Dimension(800, 600));

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

		// other tables will go here


		frame.add(menuPanel, BorderLayout.NORTH);
		mainFrame.pack();
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
