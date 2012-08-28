package com.ra4king.ld24;

import com.ra4king.gameutils.Game;

public class CellWar extends Game {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		CellWar game = new CellWar();
		game.setupFrame("Ludum Dare 24", true);
		game.start();
	}
	
	public CellWar() {
		super(1024, 768);
	}
	
	@Override
	public void initGame() {
		try {
			getArt().add("images/amoeba.png");
			getArt().add("images/amoeba-splits.png");
			
			getSound().add("music/background.ogg");
		}
		catch(Exception exc) {
			exc.printStackTrace();
			System.exit(0);
		}
		
		getSound().loop("background");
		
		setScreen("Arena", new Arena());
	}
}
