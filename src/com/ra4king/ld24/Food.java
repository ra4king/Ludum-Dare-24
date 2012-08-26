package com.ra4king.ld24;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ra4king.gameutils.gameworld.GameComponent;

public class Food extends GameComponent {
	public Food(double x, double y) {
		super(x, y, 3, 3);
	}
	
	@Override
	public void update(long deltaTime) {}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.yellow);
		g.fill(getBounds());
	}
}
