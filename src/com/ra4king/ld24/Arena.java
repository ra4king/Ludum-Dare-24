package com.ra4king.ld24;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import com.ra4king.gameutils.Game;
import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.ld24.Unit.UnitType;

public class Arena extends GameWorld {
	private HUD hud;
	
	private long elapsedTime;
	
	public Arena() {
		setBackground(Color.blue);
		
		hud = new HUD(this);
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		spawnUnit();
	}
	
	private void spawnUnit() {
		Unit u = (Unit)add(new Unit(UnitType.AMOEBA));
		u.setLocation(Math.random() * (getWidth()-u.getWidth()), Math.random() * (getHeight()-u.getHeight()));
	}
	
	@Override
	public void update(long deltaTime) {
		super.update(deltaTime);
		
		elapsedTime += deltaTime;
		
		if(elapsedTime >= 1e9) {
			elapsedTime -= 1e9;
			
			spawnUnit();
		}
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
