package com.dig.www.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.dig.www.util.ImageLibrary;
import com.dig.www.util.GameControllerRunnable;
import com.dig.www.util.SoundPlayer;

public class DigIt extends JFrame {

	private static final long serialVersionUID = 1L;
	private MPanel activePanel;

	private String userName;
	private Thread controllerThread;
	public static final ImageLibrary lib;
	public static final SoundPlayer soundPlayer;
	public static final String NAME = "Dig It";
	
	static {
		lib = ImageLibrary.getInstance();
		soundPlayer = new SoundPlayer();
	}

	public DigIt() {
		activePanel = new GameStartBoard(this);
		getContentPane().add(BorderLayout.CENTER, activePanel);

		JOptionPane.showMessageDialog(this, "Please plug in any game controllers", NAME, JOptionPane.INFORMATION_MESSAGE);

		controllerThread = new Thread(new GameControllerRunnable(this));
		controllerThread.start();
		
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 650);
		setLocationRelativeTo(null);
		setTitle(NAME);
		setResizable(false);
		setFocusable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new DigIt();
	}

	public void newGame() {

		nullBoards();
		
		activePanel = new Board(this, userName, 0);

		getContentPane().add(BorderLayout.CENTER, activePanel);
	}

	public void quit() {

		nullBoards();

		activePanel = new GameStartBoard(this);
		add(activePanel);
		activePanel.requestFocusInWindow();

		System.gc();
		validate();
		repaint();
	}

	public void nullBoards() {

		if (activePanel != null) {
			activePanel.setFocusable(false);
			getContentPane().remove(activePanel);
			activePanel = null;
			setVisible(false);
			setVisible(true);
		}

		repaint();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void victory(String saveName) {
		// TODO Add victory cutscene here

		nullBoards();
		setVisible(true);

		quit();
	}

	public DigIt getMe() {
		return this;
	}

	public void keyPress(int key) {
		activePanel.keyPress(key);
	}

	public void keyRelease(int key) {
		activePanel.keyRelease(key);
	}
}