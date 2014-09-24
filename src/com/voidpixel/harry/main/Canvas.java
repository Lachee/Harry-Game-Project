package com.voidpixel.harry.main;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class Canvas extends JComponent{
	private static final long serialVersionUID = 1L;

	public Program program;
	public Image screen;
	
	public Canvas(Program program){
		this.program = program;
		createScreen();
		
	}
	
	public void createScreen() {
		screen = this.createVolatileImage(getWidth(), getHeight());
	}

	public void paintComponent(Graphics g) {
		createScreen();
	}
	
	public void render() {
		if(screen == null) return;
		Graphics g = screen.getGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		program.game.render(g);
		g.dispose();
		
		g = this.getGraphics();
		//g.setXORMode(Color.black);
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
	}
	

	public static void drawCircle(Graphics g, int x, int y, int radius) {
		g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static void fillCircle(Graphics g, int x, int y, int radius) {
		g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static void drawChar(Graphics g, char ch, int x, int y) {
		int width = g.getFontMetrics().charWidth(ch);
		int height = g.getFontMetrics().getHeight();
		g.drawString(ch + "", x - (width / 2), y + (height/4));
	}
	
}
