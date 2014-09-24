package com.voidpixel.harry.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Listener implements KeyListener{

	//TODO: Make the listener have an array of implements, and each class registers itself
	public final Program program;
	public Listener(Program p) {
		this.program = p;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		program.game.keyReleased(e);
	}

}
