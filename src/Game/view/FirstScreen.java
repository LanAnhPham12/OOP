package Game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;

import Game.model.ImageLoader;

public class FirstScreen extends JPanel {
	private BufferedImage image;
	private JButton jButton = new JButton("START GAME");
	private JButton exitButton = new JButton("EXIT");

	public JButton getjButton() {
		return jButton;
	}

	public JButton getExitButton() {
		return exitButton;
	}

	public FirstScreen() {
		image = ImageLoader.loadImage("/title/arrow.png");
		Font font = new Font("SansSerif", Font.BOLD, 16);

		jButton.setFont(font);
		exitButton.setFont(font);
		setSize(400, 500);
		setLayout(null);

		jButton.setBounds(120, 320, 150, 40);
		jButton.setBackground(Color.blue);
		jButton.setForeground(Color.white);

		exitButton.setBounds(120, 370, 150, 40);
		exitButton.setBackground(Color.blue);
		exitButton.setForeground(Color.white);

		add(jButton);
		add(exitButton);

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}
