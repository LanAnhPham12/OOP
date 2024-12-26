package Game.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game.model.Observer;
import Game.model.Shape;
import Game.model.ShapeFactory;
import Game.view.Window;

public class Board extends JPanel implements Observer, KeyListener {
	public static final int GAME_STATUS_PLAY = 0, GAME_STATUS_OVER = 1, GAME_STATUS_PAUSE = 2;
	// GAME_STATUS_PLAY: game đang chạy
	// GAME_STATUS_OVER: game đã kết thúc
	// GAME_STATUS_PAUSE: game đang bị tạm dừng
	public int status = GAME_STATUS_PLAY; // status: trạng thái của game, mặc định là GAME_STATUS_PLAY
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
	// BOARD_WIDTH: độ rộng của game
	// BOARD_HEIGHT: chiều cao của game
	// BLOCK_SIZE: kích thước của khối hình trong game
	private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
	// board: một mảng hai chiều Color để lưu trữ các màu sắc cho các khối hình
	// trong game

	private Timer looper;
	// looper: đối tượng Timer để tạo hiệu ứng game chạy liên tục
	public static final int FPS = 60; // FPS: tốc độ tối đa của game trên mỗi giây
	public static int delay = 1000 / FPS;
	// color for shape
	private Color[] colors = { Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
			Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc") };
	private Shape[] shapes = new Shape[7];

	private Shape currentShape, nextShape;
	ShapeFactory shapeFactory = new ShapeFactory(this);

	private Random random = new Random();
	private int score = 0;
	// font binh thuong
	private Font fontForButton = new Font("SansSerif", Font.BOLD, 17);
	// font khi re chuot vao
	private Font fontEnterButton = new Font("SansSerif", Font.BOLD, 20);
	private Window window;
	public int normal = 600;
	public int fast = 50;
	private int delayTimeForMovement = normal;
	private long beginTime;
	private JLabel refreshJLabel = new JLabel("Refresh");
	private JLabel pauseJLabel = new JLabel("Pause");
	private JLabel exitJLabel = new JLabel("Exit");
	// tao array chua cac day so random
	private int[] randomNum = new int[10000];
	// vi tri danh cho shape tiep theo (dung cho man hinh nho)
	private int nextIndex = 1;

	public Board(Shape shape) {

		this.currentShape = shape;

		shape.registerObserver(this);
		currentShape = shapeFactory.createShape(0); // tạo một hình I shape

		// ghi so random vao arrray
		for (int i = 0; i < randomNum.length; i++)
			randomNum[i] = random.nextInt(7);

		shapes[0] = new Shape(new int[][] { { 1, 1, 1, 1 } }, this, colors[0]); // I Shape
		shapes[1] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 1, 0 } }, this, colors[1]); // T Shape
		shapes[2] = new Shape(new int[][] { { 1, 1, 1 }, { 1, 0, 0 } }, this, colors[2]); // L Shape
		shapes[3] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 0, 1 } }, this, colors[3]); // J Shape
		shapes[4] = new Shape(new int[][] { { 0, 1, 1 }, { 1, 1, 0 } }, this, colors[4]); // S Shape
		shapes[5] = new Shape(new int[][] { { 1, 1, 0 }, { 0, 1, 1 } }, this, colors[5]); // Z Shape
		shapes[6] = new Shape(new int[][] { { 1, 1 }, { 1, 1 } }, this, colors[6]); // O Shape
		looper = new Timer(delay, new GameLooper());

		currentShape = shapeFactory.createShape(0); // tạo một hình I shape

		// dieu kien de looper chay
		if (status == GAME_STATUS_PLAY) {
			looper.start();
		} else if (status == GAME_STATUS_PAUSE) {
			looper.stop();
		}

		setLayout(null);
		// tao label cho button
		refreshJLabel.setBounds(BLOCK_SIZE * 11, BLOCK_SIZE * 13, BLOCK_SIZE * 3, BLOCK_SIZE);
		refreshJLabel.setFont(fontForButton);
		refreshJLabel.setForeground(Color.white);
		refreshJLabel.addMouseListener(new actionForRefresh());
		add(refreshJLabel);

		pauseJLabel.setBounds(BLOCK_SIZE * 11, BLOCK_SIZE * 15, BLOCK_SIZE * 3, BLOCK_SIZE);
		pauseJLabel.setFont(fontForButton);
		pauseJLabel.setForeground(Color.white);
		pauseJLabel.addMouseListener(new actionForPause());
		add(pauseJLabel);

		exitJLabel.setBounds(BLOCK_SIZE * 11, BLOCK_SIZE * 17, BLOCK_SIZE * 3, BLOCK_SIZE);
		exitJLabel.setFont(fontForButton);
		exitJLabel.setForeground(Color.red);
		exitJLabel.addMouseListener(new actionForExit());
		add(exitJLabel);

	}

	private class GameLooper implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			update();
			repaint();
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		checkGameOver();
		if (status == GAME_STATUS_PLAY) {
			currentShape.update();
		}
	}

	// goi ra shape moi
	public void setCurrentShape() {
		currentShape = shapes[randomNum[nextIndex]];
		nextIndex++;
		currentShape.reset();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());

		// fill block
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}

		currentShape.render(g);

		// ve khung cho jlabel de tao nut (draw buttons)
		if (status == GAME_STATUS_OVER || status == GAME_STATUS_PLAY || status == GAME_STATUS_PAUSE) {
			g.setColor(Color.blue);
			g.fillRoundRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE * 13, BLOCK_SIZE * 4, BLOCK_SIZE, 10, 10);

			g.setColor(Color.green);
			g.fillRoundRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE * 15, BLOCK_SIZE * 4, BLOCK_SIZE, 10, 10);

			g.setColor(Color.pink);
			g.fillRoundRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE * 17, BLOCK_SIZE * 4, BLOCK_SIZE, 10, 10);
		}

		/*
		 * neu game dang chay thi score de ben phai man hinh neu game over thi xoa score
		 * roi dua vao giua
		 */
		if (status != GAME_STATUS_OVER) {
			g.setColor(Color.red);
			g.setFont(new Font("SansSerif", Font.ITALIC, 22));
			g.drawRoundRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE * 8, BLOCK_SIZE * 4, BLOCK_SIZE, 10, 10);
			g.drawString("Score: " + getScore(), BLOCK_SIZE * 10 + BLOCK_SIZE / 3 * 2, BLOCK_SIZE * 9 - 7);
		} else {
			g.drawString("", BLOCK_SIZE * 11, BLOCK_SIZE * 3);
		}

		// ve o vuong cho game 10x20
		drawBoard(g);
		// check game over in ra game over va score
		if (status == GAME_STATUS_OVER) {
			g.setColor(Color.red);
			g.setFont(new Font("SansSerif", Font.BOLD, 44));
			g.drawString("GAME OVER", BLOCK_SIZE / 2, BLOCK_SIZE * 9);
			g.setColor(Color.pink);
			g.drawString(" Score: " + getScore(), BLOCK_SIZE * 2, BLOCK_SIZE * 11);

		}

		// ve man hinh nho
		drawSecondMonitor(g);

	}

	// ve man hinh nho
	public void drawSecondMonitor(Graphics g) {
		// draw second monitor

		nextShape = shapes[randomNum[nextIndex]]; // lay shape tiep theo dua theo vi tri next

		// ve khung cho man hinh
		g.setColor(Color.black);
		g.fillRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE * 5, BLOCK_SIZE * 6);
		g.setColor(Color.white);
		g.drawRect(BLOCK_SIZE * 10 + BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE * 5, BLOCK_SIZE * 6);

		// ve block (giong shape.render();)
		g.setColor(nextShape.getColor());
		for (int row = 0; row < nextShape.getCoords().length; row++) {
			for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
				if (nextShape.getCoords()[row][col] != 0) {
					g.fillRect(col * BLOCK_SIZE + BLOCK_SIZE * 11, row * BLOCK_SIZE + BLOCK_SIZE * 2, BLOCK_SIZE,
							BLOCK_SIZE);
				}
			}
		}
		// ve duong thang nam doc ||
		for (int i = 11; i < 16; i++) {

			g.setColor(Color.white);
			g.drawLine(BLOCK_SIZE * i, BLOCK_SIZE, BLOCK_SIZE * i, BLOCK_SIZE * 7);
		}
		// ve duong thang nam ngang =
		for (int i = 2; i < 7; i++) {
			g.setColor(Color.white);
			g.drawLine(BLOCK_SIZE * 11, BLOCK_SIZE * i, BLOCK_SIZE * 15, BLOCK_SIZE * i);
		}
	}

	public void drawBoard(Graphics g) {

		// draw board
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			g.setColor(Color.white);
			g.drawLine(0, BLOCK_SIZE * row, BOARD_WIDTH * BLOCK_SIZE, row * BLOCK_SIZE);
		}
		for (int col = 0; col < BOARD_WIDTH + 1; col++) {
			g.setColor(Color.white);
			g.drawLine(BLOCK_SIZE * col, 0, BLOCK_SIZE * col, BOARD_HEIGHT * BLOCK_SIZE - BLOCK_SIZE);
		}
	}

	public void checkGameOver() {
		for (int row = 0; row < currentShape.getCoords().length; row++) {
			for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
				if (currentShape.getCoords()[row][col] != 0) {
					if (board[currentShape.getY() + row][currentShape.getX() + col] != null)
						status = GAME_STATUS_OVER;
				}
			}
		}
	}

	// don sach man hinh cho nut refresh
	public void clean() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				board[row][col] = null;
			}
		}
	}

	// kiem tra status cho nut pause
	public void checkStatus() {
		if (status == GAME_STATUS_PLAY) {
			status = GAME_STATUS_PAUSE;
		} else if (status == GAME_STATUS_PAUSE) {
			status = GAME_STATUS_PLAY;
		}
	}

	// getter setter
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Color[][] getBoard() {
		return board;
	}

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getFast() {
		return fast;
	}

	public void setFast(int fast) {
		this.fast = fast;
	}

	public int getDelayTimeForMovement() {
		return delayTimeForMovement;
	}

	public void setDelayTimeForMovement(int delayTimeForMovement) {
		this.delayTimeForMovement = delayTimeForMovement;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	// action cho nut di chuyen (dieu khien shape)
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedUp();

		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.moveRight();

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.moveLeft();

		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotateShape();

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedDown();
		}
	}

	// action cho nut pause
	private class actionForPause implements MouseListener {

		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub

			// khi bam nut pause neu status la play thi looper stop sau khi stop thi doi
			// status thanh pause.
			if (status == GAME_STATUS_PLAY) {
				looper.stop();
				checkStatus();
				// Neu status dang la pause thi looper start va doi status thanh play
			} else if (status == GAME_STATUS_PAUSE) {
				looper.start();
				checkStatus();
			}

		}

		public void mousePressed(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			pauseJLabel.setForeground(Color.black);
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			pauseJLabel.setForeground(Color.white);
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			pauseJLabel.setFont(fontEnterButton);
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			pauseJLabel.setFont(fontForButton);
		}
	}

	// action cho nut refresh
	private class actionForRefresh implements MouseListener {

		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			// neu game dang chay thi don sach block, goi block moi ra, va score = 0
			if (status == GAME_STATUS_PLAY) {
				clean();
				setCurrentShape();
				setScore(0);
			}
			// neu game over thi clean block, doi lai status thanh play (de looper chay) va
			// score = 0
			if (status == GAME_STATUS_OVER) {
				clean();
				status = GAME_STATUS_PLAY;
				setScore(0);
			}
			// neu game dang dung lai thi don sach, goi block moi ra, cho score ve 0, va
			// looper chay
			if (status == GAME_STATUS_PAUSE) {
				status = GAME_STATUS_PLAY;
				clean();
				setCurrentShape();
				setScore(0);
				looper.start();
			}
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			refreshJLabel.setForeground(Color.black);
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			refreshJLabel.setForeground(Color.white);
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			refreshJLabel.setFont(fontEnterButton);
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			refreshJLabel.setFont(fontForButton);
		}
	}

	private class actionForExit implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// action cho nut exit: lay frame tu window va dispose(exit)
			window.getWindow().dispose();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			exitJLabel.setForeground(Color.white);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			exitJLabel.setForeground(Color.black);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			exitJLabel.setFont(fontEnterButton);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			exitJLabel.setFont(fontForButton);
		}
	}
}
