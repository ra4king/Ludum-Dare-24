package com.ra4king.ld24;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.ld24.Unit.UnitType;

public class Arena extends GameWorld {
	private HUD hud;
	
	public Arena() {
		hud = new HUD(this);
		
		add(new Unit(UnitType.AMOEBA));
	}
	
	@Override
	public void update(long deltaTime) {
		super.update(deltaTime);
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		hud.draw(g);
	}
	
	@Override
	public void mousePressed(MouseEvent me) {
		hud.mousePressed(me);
	}
	
	@Override
	public void mouseReleased(MouseEvent me) {
		hud.mouseReleased(me);
	}
	
	@Override
	public void mouseDragged(MouseEvent me) {
		hud.mouseDragged(me);
	}
	
	@Override
	public void mouseMoved(MouseEvent me) {
		hud.mouseMoved(me);
	}
}
