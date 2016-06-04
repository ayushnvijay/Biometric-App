package biometric;

import java.awt.BorderLayout;
//import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;

public class KeyEventDemo extends JFrame implements KeyListener, ActionListener {
	/**
	 * 
	 */
	private static Scanner sc = new Scanner(System.in);;
	private static final long serialVersionUID = 1L;
	private static long timePressedCurrent = 0;
	private static long timePressedPrev = 0;
	private static long timeAtRelease1 = 0;
	private static int totalTimeDown = 0;
	private static ArrayList<KeyData> allKeyData = new ArrayList<KeyData>();
	private static KeyData tempKeyData;
	private static int tracker = 0;
	private static int deleteThis = 0;
	private int tempGetKeyCode;
	private static int letterCounter = 0;
	private static int threshold = 0;
	private static SQliteDB keystrokeDatabase;
	static String username;
	static int averageDown = 0;
	static int downCounter = 0;
	static int averageBefore = 0;
	static int beforeCounter = 0;
	static int averageAfter = 0;
	static int afterCounter = 0;
	static int input;
	private static Connection conn;
	private static Statement stmt;
	private static int score;
	private JTextArea displayArea;
	private JTextField typingArea;
	private static KeyEventDemo frame;
	private static final String newline = System.getProperty("line.separator");

	public static void main(String[] args) throws Exception {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		tempKeyData = new KeyData();
		// Schedule a job for event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		keystrokeDatabase = new SQliteDB();
		input = 0;
		String response = "";
		while (true) {
			System.out.println("1)Make a new user profile\n2)Authenticate");
			response = sc.nextLine();
			try {
				input = Integer.parseInt(response);
			} catch (Exception ignore) {
			}
			if (input == 1 || input == 2)
				break;
		}
		// sc.close();
		if (input == 1) {
			makeProfile();
			authenticate();
		} else {
			authenticate();
		}

	}

	private static void makeProfile() {
		System.out.print("Enter a username: ");
		username = sc.next();
		keystrokeDatabase.open();
		if (keystrokeDatabase.createUserTable(username)) {
			System.out.println("DATABASE CREATED.....");
		} else {
			System.out.println("Some Error Ocurred in creation of table");
		}
		keystrokeDatabase.close();
		System.out.println("\nType the following text and press Enter when you are finished\n");
		System.out.println(
				"The widow rung a bell for supper, and you had to come to time.\nWhen you got to the table you couldn't go right to eating,"
						+ " \nbut you had to wait for the widow to tuck down her head and grumble\na little over the victuals, though there warn't really anything "
						+ "the matter with them");
		frame.setVisible(true);
	}

	private static void authenticate() {
		System.out.print("Enter your username: ");
		username = sc.next();
		System.out.println("\nType the following text and press Enter when you are finished\n");
		System.out.println(
				"The widow rung a bell for supper, and you had to come to time.\nWhen you got to the table you couldn't go right to eating,"
						+ " \nbut you had to wait for the widow to tuck down her head and grumble\na little over the victuals, though there warn't really anything "
						+ "the matter with them");
		frame.setVisible(true);
		// calculateScore();
	}
	
	private static boolean calculateScore(){
		int i = 0;
		int totalTrue = 0;
		int totalFalse = 0;
		KeyData temp = new KeyData();
		double beforeDiff;
		double beforeDivisor;
		double afterDiff;
		double afterDivisor;
		double downDiff;
		double downDivisor;
		downCounter = 0;
		afterCounter = 0;
		beforeCounter = 0;
		averageBefore = 0;
		averageAfter = 0;
		averageDown = 0;
		int count = 0;
		while(i < allKeyData.size()){
			temp = allKeyData.get(i);
			int[] arr = keystrokeDatabase.getData(username, temp.keyPressedCode+"", temp.nextkeyPressedCode+"");
			if(arr == null){
				letterCounter--;
			}
			else{
//				System.out.println("Input character is "+ (char)temp.keyPressedCode);
//				System.out.println("Next character is "+ (char)temp.nextkeyPressedCode);
//				System.out.println("Stored before time is " + arr[0]);
//				System.out.println("Current typed before time is "+ temp.timeBeforeKeyPressed);
//				System.out.println("Stored after time is " + arr[1]);
//				System.out.println("Current typed after is "+ temp.timeAfterKeyPressed);
//				System.out.println("Stored down time is " + arr[2]);
//				System.out.println("Current typed down time is "+ temp.timeKeyDown);
//				
				
				beforeDiff = Math.abs(temp.timeBeforeKeyPressed - arr[0]);
				beforeDivisor = arr[0];
//				System.out.println("before = " + beforeDivisor);
				afterDiff = Math.abs(temp.timeAfterKeyPressed - arr[1]);
				afterDivisor = arr[1];
//				System.out.println("after = " + afterDivisor);
				downDiff = Math.abs(temp.timeKeyDown - arr[2]);
				downDivisor = arr[2];
//				System.out.println("down = " + downDivisor);
//				System.out.println("Key pressed code is " +temp.keyPressedCode);
//				System.out.println("next key pressed code is" + temp.nextkeyPressedCode);
				//System.out.println("\nbefRat % = " + (100 *(beforeDiff/beforeDivisor)));
				//System.out.println("aftRat % = " + (100 *(afterDiff/afterDivisor)));
				//System.out.println("downRat % = " + (100 *(downDiff/downDivisor)));
//				if(beforeDiff/beforeDivisor < 0.2 && afterDiff/afterDivisor < 0.2 && downDiff/downDivisor < 0.2){
//					//System.out.println("TRUE");
//					totalTrue++;
//				}
//				else{
//					totalFalse++;
//				}
				
				System.out.println("before,after,down % = " + (100 *(beforeDiff/beforeDivisor)) + " " + 
						(100 *(afterDiff/afterDivisor)) + " " + (100 *(downDiff/downDivisor)));
				beforeCounter += (100 *(beforeDiff/beforeDivisor));
				afterCounter += (100 *(afterDiff/afterDivisor));
				downCounter += (100 * (downDiff/downDivisor));
				count = 0;
				if ((beforeDiff/beforeDivisor) < 0.3) {
					count++;
				}
				if ((afterDiff/afterDivisor) < 0.3) {
					count++;
				}
				if ((downDiff/downDivisor) < 0.3) {
					count++;
				}
				
				if (count >= 2) {
					totalTrue++;
				} else {
					totalFalse++;
				}
			}
			i++;
		}
		averageBefore = beforeCounter / letterCounter;
		averageAfter = afterCounter / letterCounter;
		averageDown = downCounter / letterCounter;
		System.out.println("letterCounter = " + letterCounter);
		System.out.println("average beforeDiff % = " + averageBefore);
		System.out.println("average afterDiff % = " + averageAfter);
		System.out.println("average downDiff % = " + averageDown);
		System.out.println("% Accuracy = " + (100 * ((double)totalTrue)/(totalTrue+totalFalse)));
		System.out.println("Total true: " + totalTrue);
		System.out.println("Total False: " + totalFalse);
		
		return (totalTrue/ (totalTrue+totalFalse)) > 0.70;
	}
	private static boolean calculateScore2() {
		/*int beforeDifference = 0, afterDifference = 0, downDifference = 0;
		int tempBeforeProfile = 0, tempAfterProfile = 0, tempDownProfile = 0;
		int tempBeforeTyper = 0, tempAfterTyper = 0, tempDownTyper = 0;
		int i = 0;
		score = 0;
		
		
		while (i < allKeyData.size()) {
			// get temp Before, after and down from Profile database
			tempKeyData = allKeyData.get(i);
			tempBeforeTyper = tempKeyData.timeBeforeKeyPressed;
			tempAfterTyper = tempKeyData.timeAfterKeyPressed;
			tempDownTyper = tempKeyData.timeKeyDown;
			int[] arr = keystrokeDatabase.getData(username, tempKeyData.keyPressedCode+"", tempKeyData.nextkeyPressedCode+"");
			if (arr == null) {
				letterCounter--;
				i++;
				continue;
			} else {
				tempBeforeProfile = arr[0];
				tempAfterProfile = arr[1];
				tempDownProfile = arr[2];
			}
			i++;
//			System.out.println("profile b,a,d " + tempBeforeProfile + " " + 
//			tempAfterProfile + " " + tempDownProfile);
			System.out.println("before, after, down: " + beforeDifference +
					" " + afterDifference + " " + downDifference);
			
			beforeDifference = Math.abs(tempBeforeTyper - tempBeforeProfile);
			afterDifference = Math.abs(tempAfterTyper - tempAfterProfile);
			downDifference = Math.abs(tempDownTyper - tempDownProfile);
			
			beforeCounter += beforeDifference;
			afterCounter += afterDifference;
			downCounter += downDifference;
			
			if (beforeDifference < 1000)
				score += beforeDifference;
			if (afterDifference < 1000)
				score += afterDifference;
			score += downDifference;
		}
		
		averageBefore = (beforeCounter / letterCounter);
		averageAfter = (afterCounter / letterCounter);
		averageDown = (downCounter / letterCounter);
		
		int[] arr = new int[3];
		arr[0] = averageBefore;
		arr[1] = averageAfter;
		arr[2] = averageDown;
		keystrokeDatabase.insertIntoAvgTable(username, arr);
		arr = keystrokeDatabase.getAvg(username);
		threshold = letterCounter * (arr[0] + arr[1] + arr[2]);
		System.out.println("threshold = " + threshold + " score = " + score);
		System.out.println("letterCounter = " + letterCounter);
		System.out.println("b,a,d: " + arr[0] + " " + arr[1] + " " + arr[2]);
		return (score < threshold);*/
		return false;
	}

	///////////////////
	///////////////////////////////////////////////
	////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	//////////////////////////
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		frame = new KeyEventDemo("KeyEventDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		frame.addComponentsToPane();

		// Display the window.
		frame.pack();
		// frame.setVisible(true);
	}

	private void addComponentsToPane() {

		JButton button = new JButton("Clear");
		button.addActionListener(this);

		typingArea = new JTextField(20);
		typingArea.addKeyListener(this);

		// Uncomment this if you wish to turn off focus
		// traversal. The focus subsystem consumes
		// focus traversal keys, such as Tab and Shift Tab.
		// If you uncomment the following line of code, this
		// disables focus traversal and the Tab events will
		// become available to the key event listener.
		// typingArea.setFocusTraversalKeysEnabled(false);

		displayArea = new JTextArea();
		displayArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new Dimension(375, 125));

		getContentPane().add(typingArea, BorderLayout.PAGE_START);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(button, BorderLayout.PAGE_END);
	}

	public KeyEventDemo(String name) {
		super(name);
	}

	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {
		letterCounter++;
		// displayInfo(e, "KEY TYPED: ");
	}

	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		timePressedCurrent = System.nanoTime();
		tempKeyData = new KeyData();
		tempKeyData.nextkeyPressedCode = e.getKeyCode();

		if (tracker != 0) {
			tempKeyData.keyPressedCode = tempGetKeyCode;
			tempKeyData.timeKeyDown = totalTimeDown;
			if (tracker <= 1)
				tempKeyData.timeBeforeKeyPressed = 0;
			else
				tempKeyData.timeBeforeKeyPressed = (int) (timePressedPrev
						- (timePressedPrev - allKeyData.get(allKeyData.size() - 1).timeAfterKeyPressed));
			tempKeyData.timeAfterKeyPressed = (int) ((timePressedCurrent - timePressedPrev) / 1000000);
			allKeyData.add(tempKeyData);
			System.out.println(tempKeyData.toString());
		}
		tracker++;

		// if (isFirstKey == false)
		// tempKeyData.timeAfterKeyPressed = (int) (tempBeforeAfter =
		// ((timePressedCurrent - timePressedPrev)/1000000));

		timePressedPrev = timePressedCurrent;
		if (tempKeyData.nextkeyPressedCode == 10) {
			// Enter has been pressed
			keystrokeDatabase.open();
			if(input == 1) {
				for (int i = 0; i < allKeyData.size() - 1; i++)
					keystrokeDatabase.insertIntoTable(username, allKeyData.get(i));
				System.out.println("Done adding into db");
			}
			
			if (input == 2 && calculateScore()) {
				for (int i = 0; i < allKeyData.size() - 1; i++)
					keystrokeDatabase.insertIntoTable(username, allKeyData.get(i));
				System.out.println("Done adding into db");
			}
			keystrokeDatabase.close();
		}
		if (deleteThis++ % 15 == 14) {
			// printAllKeyData();
		}

		// displayInfo(e, "KEY PRESSED: ");
	}

	public void printAllKeyData() {
		int i = 0;
		for (i = 0; i < allKeyData.size() - 1; i++) {
			System.out.println(allKeyData.get(i).toString());
		}
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {
		timeAtRelease1 = System.nanoTime();
		totalTimeDown = (int) ((timeAtRelease1 - timePressedCurrent) / 1000000);
		tempGetKeyCode = e.getKeyCode();

		displayInfo(e, "KEY RELEASED: ");
	}

	/** Handle the button click. */
	public void actionPerformed(ActionEvent e) {
		// Clear the text components.
		displayArea.setText("");
		typingArea.setText("");
		// Return the focus to the typing area.
		typingArea.requestFocusInWindow();

	}

	/*
	 * We have to jump through some hoops to avoid trying to print non-printing
	 * characters such as Shift. (Not only do they not print, but if you put
	 * them in a String, the characters afterward won't show up in the text
	 * area.)
	 */
	private void displayInfo(KeyEvent e, String keyStatus) {

		// You should only rely on the key char if the event
		// is a key typed event.
		int id = e.getID();
		String keyString;
		String timeElapsedString;
		timeElapsedString = "Time Elapsed: " + totalTimeDown;
		if (id == KeyEvent.KEY_TYPED) {
			char c = e.getKeyChar();
			keyString = "key character = '" + c + "'";
		} else {
			int keyCode = e.getKeyCode();
			keyString = "key code = " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")";
		}

		int modifiersEx = e.getModifiersEx();
		String modString = "extended modifiers = " + modifiersEx;
		String tmpString = KeyEvent.getModifiersExText(modifiersEx);
		if (tmpString.length() > 0) {
			modString += " (" + tmpString + ")";
		} else {
			modString += " (no extended modifiers)";
		}

		String actionString = "action key? ";
		if (e.isActionKey()) {
			actionString += "YES";
		} else {
			actionString += "NO";
		}

		displayArea.append(keyStatus + newline + "    " + keyString + newline + "    " + modString + newline + "    "
				+ actionString + newline + "    " + timeElapsedString + newline);
		displayArea.setCaretPosition(displayArea.getDocument().getLength());
	}
}