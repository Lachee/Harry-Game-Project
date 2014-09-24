package com.voidpixel.harry.main;

import javax.swing.JFrame;

import com.voidpixel.harry.game.MainGame;

public class Program extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	// THIS IS A COMMENT
	public static int TARGET_FRAMERATE = 60;
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static String TITLE = "Village";

	public int framerate = 60;
	public boolean running = false;

	private Thread thread;

	public Listener listener;
	public Canvas canvas;
	public MainGame game;

	public Program(String title) {
		super(title + ": LOADING");

		TITLE = title;
		
		canvas = new Canvas(this);
		this.setContentPane(canvas);

		game = new MainGame(this, canvas);

		listener = new Listener(this);
		this.addKeyListener(listener);
		
		thread = new Thread(this);
		thread.start();
	}

	public static void main(String[] args) {
		System.out.println("Loading Program...");

		Program program = new Program("Harry Game");
		program.setSize(WIDTH, HEIGHT);
		program.setLocationRelativeTo(null);
		program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		program.setVisible(true);

		System.out.println("Loading Finished.");
	}

	public void render() {
		canvas.render();
	}

	public void update(double delta) {
		game.update(delta);
	}

	@Override
	public void run() {
		running = true;

		// Timing varibles
		long startTime = System.nanoTime(), waitTime, urdTime = 0;
		int targetTime = 1000 / TARGET_FRAMERATE;

		long tTime = 0;
		int fCount = 0;

		while (running) {
			// Increment the amount of frames that have passed by one
			fCount++;

			// Add the time it took to complete the last frame
			tTime += System.nanoTime() - startTime;

			// Turn the totaled time between frame and convert it into seconds.
			// If a second has passed, make the framerate the amount of frames
			// passed and reset all varibles.
			if ((double) tTime / (double) 1e9 >= 1) {
				framerate = fCount;
				tTime = 0;
				fCount = 0;

				this.setTitle(TITLE + ": " + framerate + " FPS");
			}

			// Calculate the delta (time to complete last frame)
			double delta = (double) (System.nanoTime() - startTime)
					/ (double) 1e9;

			// Get the start time to compare against
			startTime = System.nanoTime();

			// Update THEN render the game
			update(delta);
			render();

			// Get the difference in time from the start time to now
			// Also convert it into milliseconds so that sleep can use it
			urdTime = (System.nanoTime() - startTime) / (long) 1e6;

			// Calculate the difference in the time and target time so we can
			// sleep just the right amount
			waitTime = targetTime - urdTime;

			try {
				// Sleep the thread as long as waitTime isn't a negative value
				if (waitTime >= 0)
					Thread.sleep(waitTime);
			} catch (Exception e) {
				// Print any errors
				e.printStackTrace();
			}
		}
	}

}
