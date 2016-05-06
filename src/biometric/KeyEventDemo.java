package biometric;

import java.awt.BorderLayout;
//import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class KeyEventDemo extends JFrame implements KeyListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static long timePressedCurrent = 0;
	public static long timePressedPrev = 0;
	public static long timeAtRelease1 = 0;
	public static long timeAtRelease2 = 0;
	public static int totalTimeDown = 0;
	public static ArrayList<KeyData> allKeyData = new ArrayList<KeyData>();
	public static KeyData tempKeyData;
	public static int tracker = 0;
	public static int tempLocation;
	public static long tempBeforeAfter;
	public static int deleteThis = 0;
	private int tempGetKeyCode;
	JTextArea displayArea;
	JTextField typingArea;
	static final String newline = System.getProperty("line.separator");

	public static void main(String[] args) {
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
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		KeyEventDemo frame = new KeyEventDemo("KeyEventDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		frame.addComponentsToPane();

		// Display the window.
		frame.pack();
		frame.setVisible(true);
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
		// displayInfo(e, "KEY TYPED: ");
	}

	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		timePressedCurrent = System.nanoTime();
		tempKeyData = new KeyData();
		if(tracker!=0){
			tempKeyData.keyPressedCode = tempGetKeyCode;
			tempKeyData.location = tempLocation;
			tempKeyData.timeKeyDown = totalTimeDown;
			if(tracker <= 1)
				tempKeyData.timeBeforeKeyPressed = 0;
			else 
				tempKeyData.timeBeforeKeyPressed = (int)(timePressedPrev - (timePressedPrev - allKeyData.get(allKeyData.size()-1).timeAfterKeyPressed));
			tempKeyData.timeAfterKeyPressed = (int)((timePressedCurrent - timePressedPrev)/1000000);
			allKeyData.add(tempKeyData);
			System.out.println(tempKeyData.toString());
		}
		tracker++;
		
//		if (isFirstKey == false)
//			tempKeyData.timeAfterKeyPressed = (int) (tempBeforeAfter = ((timePressedCurrent - timePressedPrev)/1000000));
		
		timePressedPrev = timePressedCurrent;
		
		if (deleteThis++ % 15 == 14) {
			//printAllKeyData();
		}
	
		// displayInfo(e, "KEY PRESSED: ");
	}

	public void printAllKeyData() {
		int i = 0;
		for(i = 0; i < allKeyData.size()-1; i++){
			System.out.println(allKeyData.get(i).toString());
		}
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {
		timeAtRelease1 = System.nanoTime();
		totalTimeDown = (int) ((timeAtRelease1 - timePressedCurrent) / 1000000);
		tempGetKeyCode = e.getKeyCode();
		tempLocation = e.getKeyLocation();
		switch (tempLocation) {
		case KeyEvent.KEY_LOCATION_STANDARD:
			tempKeyData.keyPressedCode = 0;
			break;
		case KeyEvent.KEY_LOCATION_LEFT:
			tempKeyData.keyPressedCode = 1;
			break;
		case KeyEvent.KEY_LOCATION_RIGHT:
			tempKeyData.keyPressedCode = 2;
			break;
		case KeyEvent.KEY_LOCATION_NUMPAD:
			tempKeyData.keyPressedCode = 3;
			break;
		}
		
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

		String locationString = "key location: ";
		int location = e.getKeyLocation();
		if (location == KeyEvent.KEY_LOCATION_STANDARD) {
			locationString += "standard";
		} else if (location == KeyEvent.KEY_LOCATION_LEFT) {
			locationString += "left";
		} else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
			locationString += "right";
		} else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
			locationString += "numpad";
		} else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
			locationString += "unknown";
		}

		displayArea.append(keyStatus + newline + "    " + keyString + newline + "    " + modString + newline + "    "
				+ actionString + newline + "    " + locationString + newline + "    " + timeElapsedString + newline);
		displayArea.setCaretPosition(displayArea.getDocument().getLength());
	}
}