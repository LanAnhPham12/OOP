package Game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import Game.controller.Board;
import Game.model.Shape;

public class Window {
	public JFrame window;
	static Board board;
	static Shape shape;
	private static final int WIDTH = 490, HEIGHT = 629;

	/**
	 * 
	 */

	public Window() {
		shape = new Shape();
		board = new Board(shape);
		window = new JFrame("Tetris Game");
		board.setWindow(this);

		window.setSize(WIDTH, HEIGHT);
		board.setLayout(null);
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.add(board);
		window.addKeyListener(board);
		window.setVisible(true);
	}

	public static void main(String[] args) {
		// tao man hinh bat dau
		JFrame jFrame = new JFrame("Tetris Game");
		jFrame.setSize(400, 500);
		jFrame.setLayout(new BorderLayout());
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		jFrame.setLocationRelativeTo(null);
		jFrame.setResizable(false);

		FirstScreen firstScreen = new FirstScreen();
		firstScreen.setBackground(Color.black);
		firstScreen.getjButton().addActionListener(new ActionListener() {
			// jbutton la nut start luoi chinh qua :))
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				// khi bam nut start thi mo window game va tat man hinh start
				new Window();
				jFrame.dispose();
			}
		});
		firstScreen.getExitButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jFrame.dispose();
			}
		});
		jFrame.add(firstScreen, BorderLayout.CENTER);

		jFrame.setVisible(true);
	}

	// get jframe de dung cho nut exit trong game
	public JFrame getWindow() {
		return window;
	}

}
