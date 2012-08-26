package com.ra4king.ld24;

import com.ra4king.gameutils.Game;

public class LudumDare24 extends Game{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		LudumDare24 game = new LudumDare24();
		game.setupFrame("Ludum Dare 24", true);
		game.start();
	}
	
	public LudumDare24() {
		super(800, 600);
	}
	
	@Override
	public void initGame() {
		setScreen("Arena", new Arena());
	}
}
