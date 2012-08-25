package com.ra4king.ld24;

import java.awt.Graphics2D;

import com.ra4king.gameutils.gameworld.GameComponent;

public class Unit extends GameComponent {
	public static enum UnitType {
		AMOEBA(30,30),
		DIATOM(40,40),
		KRILL(40,40),
		SLUG(50,50);
		
		private int width, height;
		
		UnitType(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
	
	//private Animation animation;
	//private UnitType type;
	private double rot;
	
	public Unit(UnitType type) {
		//this.type = type;
		
		setSize(type.width, type.height);
	}
	
	@Override
	public void show() {
		//animation = new Animation(true);
		//animation.addFrames(getParent().getGame().getArt().get(type.name().toLowerCase()), type.width, type.height, 50);
	}
	
	@Override
	public void update(long deltaTime) {
		//animation.update(deltaTime);
	}
	
	@Override
	public void draw(Graphics2D g) {
		//g.drawImage(animation.getFrame(), getIntX(), getIntY(), null);
		
		g.rotate(rot, getCenterX(), getCenterY());
		g.fill(getBounds());
	}
}
