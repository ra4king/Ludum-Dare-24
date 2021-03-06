package com.ra4king.ld24;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.ra4king.gameutils.Game;
import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.ld24.Unit.UnitType;

public class Arena extends GameWorld {
	private HUD hud;
	
	private long elapsedTime, totalElapsedTime;
	
	public Arena() {
		setBackground(Color.blue);
		
		hud = new HUD(this);
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		Unit u = (Unit)add(1,new Unit(UnitType.CELL, 0));
		u.setLocation(0, 0);
		
		u = (Unit)add(1,new Unit(UnitType.CELL, 1));
		u.setLocation(getWidth() - u.getWidth(), getHeight() - u.getHeight());
		u.setRotation(-Math.PI);
		
		spawnFoods();
	}
	
	private void spawnFoods() {
		double foodCount = ((Math.sqrt((getWidth()/2) * (getHeight()/2))/3) * Math.random()) * Math.max(0, 1 - (totalElapsedTime / 120e9)) + 20;
		
		System.out.println("Spawning " + (int)foodCount + " food.");
		
		for(int a = 0; a < foodCount; a++)
			add(0,new Food(Math.random() * (getWidth() - 3), Math.random() * (getHeight() - 3)));
	}
	
	@Override
	public void update(long deltaTime) {
		super.update(deltaTime);
		
		elapsedTime += deltaTime;
		totalElapsedTime += deltaTime;
		
		if(elapsedTime >= 1e9) {
			elapsedTime -= 1e9;
			
			spawnFoods();
			
			System.out.println("food: " + getEntitiesAt(0).size() + "; units: " + getEntitiesAt(1).size() + "; total: " + getEntities().size());
		}
		
		hud.update(deltaTime);
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		hud.draw(g);
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_R)
			getGame().setScreen("Arena", new Arena());
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
