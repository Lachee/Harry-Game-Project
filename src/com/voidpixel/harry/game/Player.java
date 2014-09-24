package com.voidpixel.harry.game;

public class Player {
	public String name;
	public int color;
	public int x, y;
	
	public Player(int x, int y, int color, String name) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.color = color;
	}
}
