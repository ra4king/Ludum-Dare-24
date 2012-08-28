package com.ra4king.ld24;

import com.ra4king.gameutils.Game;

public class LudumDare24 extends Game {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		LudumDare24 game = new LudumDare24();
		game.setupFrame("Ludum Dare 24", true);
		game.start();
	}
	
	public LudumDare24() {
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
