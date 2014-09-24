package com.voidpixel.harry.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.voidpixel.harry.interfaces.*;
import com.voidpixel.harry.main.Canvas;
import com.voidpixel.harry.main.Program;

public class MainGame{
	
	//Make this a singleton
	public static MainGame instance;
	
	public Program program;
	public Canvas canvas;
	
	//How long (in seconds) a tick should be.
	public double tickRate = 0;
	protected long tickStart = 0;
	
	protected int frameCount = 0;
	public boolean secondFlash = false;
	public boolean drawGrid = false;
	
	public int width = 6;
	public int height = 6;
	
	public int[][] map;
	
	public Color[] colors = new Color[] { Color.GRAY, Color.ORANGE, Color.DARK_GRAY, Color.BLUE, Color.GREEN };	
	public ArrayList<Player> players;
	public int playerTurn = 0;
	public TurnPhase turnPhase = TurnPhase.Move;
	
	public MainGame(Program program, Canvas canvas) {
		MainGame.instance = this;
		
		this.program = program;
		this.canvas = canvas;
		
		map = new int[width][height];
		map[0][0] = 1;
		map[width - 1][0] = 2;
		map[0][height - 1] = 3;
		map[width - 1][height - 1] = 4;
		
		players = new ArrayList<Player>();
		players.add(new Player(0, 0, 1, "Mountain Player"));
		players.add(new Player(width - 1, 0, 2, "Swamp Player"));
		players.add(new Player(0, height - 1, 3, "Island Player"));
		players.add(new Player(width - 1, height - 1, 4, "Forest Player"));	
	
	}
	
	public void update(double delta) {
		frameCount++;
		if(frameCount >= program.framerate) {
			secondFlash = !secondFlash;
			frameCount = 0;
		}
	}
	
	public void endPhase() {
		if(turnPhase == TurnPhase.Move)
			turnPhase = TurnPhase.Claim;
		else if(turnPhase == TurnPhase.Claim)
			endTurn();
	}
	
	public void beginTurn() {
		if(!canMove(Direction.Up) && !canMove(Direction.Down) && !canMove(Direction.Left) && !canMove(Direction.Right))
			loseGame();
	}
	
	public void loseGame() {
		//Remove all of the player's tiles
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(map[x][y] == players.get(playerTurn).color)
					map[x][y] = 0;
			}
		}

		//Remove the player
		players.remove(playerTurn);
	}
	
	public void endTurn() {
		turnPhase = TurnPhase.Move;
		playerTurn++;
		if(playerTurn >= players.size()) playerTurn = 0;
		beginTurn();
	}
	
	public void render(Graphics g) {
		renderMap(g);
		renderPlayers(g);
		
		String phase = "";
		switch(turnPhase) {
		case Move :  
			phase = "Move Phase: Use the numpad to move your person";
			break;
			
		case Claim :
			phase = "Claim Phase: Use the numpad to delcare direction. 7, 9, 1 and 3 for diagonals";
			break;
		}
		
		g.drawString(phase, 10, 20);
	}
	
	public void renderPlayers(Graphics g) {
		
		int centerX = canvas.getWidth() / 2;
		int centerY = canvas.getHeight() / 2;
				
		int ta = canvas.getHeight() - 50;
		
		int x = centerX - ta/2;
		int y = centerY - ta/2;

		int sx = (ta) / width;
		int sy = (ta) / height;
		
		for(int  i = 0; i < players.size(); i++) {
			Player p = players.get(i);

			
			
			if(secondFlash && playerTurn == i) 
				g.setColor(Color.yellow);
			else
				g.setColor(colors[p.color]);
			
			//Player
			renderPlayer(g, p, x, y, sx, sy, false);			


			//Oval surronding
			if(secondFlash && playerTurn == i) 
				g.setColor(Color.yellow);
			else
				g.setColor(Color.black);
			
			renderPlayer(g, p, x, y, sx, sy, true);
		}
	}
	
	public void renderMap(Graphics g) {
		int centerX = canvas.getWidth() / 2;
		int centerY = canvas.getHeight() / 2;
				
		int ta = canvas.getHeight() - 50;
		
		int x = centerX - ta/2;
		int y = centerY - ta/2;
		
		int tsX = (ta) / width;
		int tsY = (ta) / height;
		
		for(int xx = 0; xx < width; xx++) {
			for(int yy = 0; yy < height; yy++) {
				renderTile(g, x, y, xx, yy, tsX, tsY);
			}
		}
	}
	
	void renderPlayer(Graphics g, Player p, int x, int y, int sx, int sy, boolean outline) {
		if(outline)
			g.drawOval(x + p.x * sx + sx / 4, y + p.y * sy + sy / 4, sx / 2, sy / 2);
		else
			g.fillOval(x + p.x * sx + sx / 4, y + p.y * sy + sy / 4, sx / 2, sy / 2);
	}
	
	void renderTile(Graphics g, int x, int y, int tx, int ty, int sx, int sy) {
	
		Color color = colors[map[tx][ty]];
		g.setColor(color);
		g.fillRect(x + tx * sx, y + ty * sy, sx, sy);
		g.setColor(Color.black);
		g.drawRect(x + tx * sx, y + ty * sy, sx, sy);
	}
	

	public void keyReleased(KeyEvent e) {
		if(turnPhase == TurnPhase.Move)
			keyMoved(e);
		else if(turnPhase == TurnPhase.Claim)
			keyClaim(e);
	
	}
	
	public void keyMoved(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_NUMPAD8 : 
			System.out.println("Player is moving UP"); 
			move(Direction.Up);
			break;
			
		case KeyEvent.VK_NUMPAD6 : 
			System.out.println("Player is moving LEFT"); 
			move(Direction.Left);
			break;
			
		case KeyEvent.VK_NUMPAD2 : 
			System.out.println("Player is moving DOWN"); 
			move(Direction.Down);
			break;
			
		case KeyEvent.VK_NUMPAD4 : 
			System.out.println("Player is moving RIGHT"); 
			move(Direction.Right);
			break;
		}
	}
	
	public void keyClaim(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_NUMPAD8 : 
			System.out.println("Player is claiming UP"); 
			claim(Direction.Up);
			break;
			
		case KeyEvent.VK_NUMPAD9 : 
			System.out.println("Player is claiming UP-LEFT"); 
			claim(Direction.UpLeft); 
			break;
			
		case KeyEvent.VK_NUMPAD6 : 
			System.out.println("Player is claiming LEFT"); 
			claim(Direction.Left); 
			break;
			
		case KeyEvent.VK_NUMPAD3 : 
			System.out.println("Player is claiming DOWN-LEFT"); 
			claim(Direction.DownLeft);
			break;
				
			
		case KeyEvent.VK_NUMPAD2 : 
			System.out.println("Player is claiming DOWN"); 
			claim(Direction.Down);
			break;

		case KeyEvent.VK_NUMPAD1 : 
			System.out.println("Player is claiming DOWN-RIGHT"); 
			claim(Direction.DownRight);
			break;		
			
		case KeyEvent.VK_NUMPAD4 : 
			System.out.println("Player is claiming RIGHT"); 
			claim(Direction.Right);
			break;
			
		case KeyEvent.VK_NUMPAD7 : 
			System.out.println("Player is claiming UP-RIGHT"); 
			claim(Direction.UpRight);
			break;
		}
	}
	
	public void move(Direction direction)
	{
		if(!canMove(direction)) return;
		
		Player p = players.get(playerTurn);
		
		switch(direction) {
		default: break;
		case Up: p.y--; break;
		case Down: p.y++; break;
		case Left: p.x++; break;
		case Right: p.x--; break;
		}
		
		map[p.x][p.y] = p.color; 
		
		endPhase();
	}
	
	public void claim(Direction direction) {
		//Random rand = new Random();
		//if(rand.nextBoolean()) { endPhase(); return; }
		
		int dx = 0;
		int dy = 0;
		switch(direction) {
		case Up: dy--; break;
		case UpLeft: dy--; dx++; break;
		case Left: dx++; break;
		case DownLeft: dy++; dx++; break;
		case Down: dy++; break;
		case DownRight: dy++; dx--; break;
		case Right: dx--; break;
		case UpRight: dy--; dx--; break;
		}

		Player p = players.get(playerTurn);
		int x = p.x;
		int y = p.y;
		boolean valid = true;
		while(valid) {
			map[x][y] = p.color;
			x += dx;
			y += dy;	
			
			if(!inBounds(x, y)) valid = false;
			if((x == 0 && y == 0) || (x == 0 && y == height - 1) || (x == width - 1 && y == 0) || (x == width - 1 && y == height - 1)) valid = false;
			
			if(!valid) continue;
			for(Player p2 : players) {
				if(p2 == p) continue;
				if(p2.x == x && p2.y == y) { valid = false; break; }
			}
		}
		
		endPhase();
	}
	
	public boolean inBounds(int x, int y) 
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	public boolean canMove(Direction direction) {
		Player p = players.get(playerTurn);
		
		switch(direction) {
		default: return false;
		case Up: return inBounds(p.x, p.y-1) && (map[p.x][p.y - 1] == 0 || map[p.x][p.y - 1] == p.color );
		case Down: return inBounds(p.x, p.y+1) && (map[p.x][p.y + 1] == 0 || map[p.x][p.y + 1] == p.color );
		case Left: return inBounds(p.x+1, p.y) && (map[p.x+1][p.y] == 0 || map[p.x+1][p.y] == p.color );
		case Right: return inBounds(p.x-1, p.y) && (map[p.x-1][p.y] == 0 || map[p.x-1][p.y] == p.color );
		}
	}
}
