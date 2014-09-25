package com.voidpixel.harry.game;

public class Tile {
	public int color;
	public double scale;
	public double maxScale = 1;
	
	public Tile(int color, double scale) {
		this.color = color;
		this.scale = scale;
	}
	
	public Tile(int color) {		
		this.color = color;
		this.scale = 1;
	}
	
	public boolean animate(double delta, boolean negative) {
		if(negative) {
			scale = scale - delta;
			if(scale < 0) scale = 0;
			return scale == 0;
		}else{
			scale = scale + delta;
			if(scale > maxScale) scale = maxScale;
			return scale == maxScale;
		}
	}
}


