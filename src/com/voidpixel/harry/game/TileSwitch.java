package com.voidpixel.harry.game;

public class TileSwitch {
	public int x, y;
	public Tile originalTile;
	public Tile newTile;
	
	public boolean translateOriginal = true;
	public boolean translateNew = false;
	
	public double speed = 4;
	
	public TileSwitch(int x, int y, Tile originalTile, Tile newTile) {
		this.originalTile = originalTile;
		this.newTile = newTile;
		this.x = x;
		this.y = y;
	}
	
	public boolean animate(double delta) {

		if(translateOriginal) {
			if(originalTile.animate(delta * speed, true)) {
				translateOriginal = false;
				translateNew = true;

			}
		}else{
			if(newTile.animate(delta* speed, false)) {
				translateNew = false;
				return true;
			}
		}
		
		return false;
	}
	
	public double getScale() {
		return translateOriginal ? originalTile.scale : newTile.scale;
	}
	
	public int getColor() {
		return translateOriginal ? originalTile.color : newTile.color;
	}
}
