package pacman.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Display {

	private JFrame frame;
	private Canvas canvas;

	private String title;
	private int width, height;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;

		createDisplay();
	}

	public void createDisplay() {
		this.frame = new JFrame(this.title);
		this.frame.setSize(this.width, this.height);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);
		this.frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/icon/icon.png")));

		this.canvas = new Canvas();
		this.canvas.setSize(new Dimension(this.width, this.height));
		this.canvas.setMaximumSize(new Dimension(this.width, this.height));
		this.canvas.setMinimumSize(new Dimension(this.width, this.height));
		this.canvas.setFocusable(false);

		this.frame.add(this.canvas);
		this.frame.pack();

		this.frame.setVisible(true);
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

}
