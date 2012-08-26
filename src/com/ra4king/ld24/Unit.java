package com.ra4king.ld24;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import com.ra4king.gameutils.Art;
import com.ra4king.gameutils.Entity;
import com.ra4king.gameutils.gameworld.GameComponent;
import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.gameutils.util.Animation;
import com.ra4king.gameutils.util.FastMath;

public class Unit extends GameComponent {
	public static enum UnitType {
		AMOEBA(50,50,20,10);
//		DIATOM(40,40,40),
//		KRILL(40,40,50),
//		SLUG(50,50,60);
		
		private BufferedImage[][] frames, split;
		private int width, height, initialHealth, foodToSplit;
		
		UnitType(int width, int height, int initialHealth, int foodToSplit) {
			this.width = width;
			this.height = height;
			this.initialHealth = initialHealth;
			this.foodToSplit = foodToSplit;
		}
		
		public void init(Art art) {
			if(frames == null) {
				frames = Art.split(art.get(name().toLowerCase()), width, height);
				split = Art.split(art.get(name().toLowerCase() + "-splits"), width*2, height);
			}
		}
	}
	
	private Animation normal, splitting;
	private UnitType type;
	private int team;
	private double rot;
	
	private Food target;
	private int foodEaten;
	private final double speed = 100;
	
	private int health;
	
	private boolean isSplitting;
	private boolean isAlive;
	private boolean isHighlighted;
	
	public Unit(UnitType type, int team) {
		this.type = type;
		
		this.team = team;
		isAlive = true;
		
		health = type.initialHealth;
		setSize(type.width, type.height);
	}
	
	public Unit(Unit u) {
		type = u.type;
		
		team = u.team;
		isAlive = true;
		
		health = type.initialHealth;
		setBounds(u.getX() + u.getWidth(), u.getY(), type.width, type.height);
		
		rot = u.rot + Math.PI;
		
		isHighlighted = u.isHighlighted;
	}
	
	@Override
	public void init(GameWorld world) {
		super.init(world);
		
		type.init(getParent().getGame().getArt());
		target = getClosestFood();
	}
	
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public int getTeam() {
		return team;
	}
	
	public int getHealth() {
		return health;
	}
	
	@Override
	public void show() {
		normal = new Animation(true);
		for(int a = 8; a < 12; a++)
			normal.addFrame(type.frames[a/4][a%4], 80 * (long)1e6);
		
		splitting = new Animation(false);
		for(int a = 0; a < 8; a++)
			splitting.addFrame(type.split[a%4][a/4], 80 * (long)1e6);
	}
	
	private Food getClosestFood() {
		Food closest = null;
		double closestDist = Double.MAX_VALUE;
		
		for(Entity e : getParent().getEntitiesAt(0)) {
			Food f = (Food)e;
			
			double dist = (f.getX()-getX()) * (f.getX()-getX()) + (f.getY()-getY()) * (f.getY()-getY());
			if(dist < closestDist) {
				closest = f;
				closestDist = dist;
			}
		}
		
		return closest;
	}
	
	private void checkForSplit() {
		if(foodEaten >= type.foodToSplit) {
			foodEaten -= type.foodToSplit;
			isSplitting = true;
			splitting.restart();
		}
	}
	
	@Override
	public void update(long deltaTime) {
		if(isSplitting) {
			if(splitting.isDone()) {
				getParent().add(1, new Unit(this));
				isSplitting = false;
			}
			else
				splitting.update(deltaTime);
		}
		else
			normal.update(deltaTime);
		
		final double delta = deltaTime / 1e9;
		
		if(!isSplitting && target != null && getParent().contains(target)) {
			double r = FastMath.atan2(target.getY() - getCenterY(), target.getX() - getCenterX());
			
			if(Math.abs(r - rot) > Math.PI)
				r = r-(2*Math.PI);
			
			rot += (r-rot) * (5*delta);
			
			double dx = FastMath.cos(rot) * speed * delta;
			double dy = FastMath.sin(rot) * speed * delta;
			
			setX(getX() + dx);
			setY(getY() + dy);
			
			if(this.intersects(target)) {
				getParent().remove(target);
				target = null;
				foodEaten++;
				
				checkForSplit();
			}
		}
		else
			target = getClosestFood();
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.rotate(rot, getCenterX(), getCenterY());
		g.drawImage((isSplitting ? splitting : normal).getFrame(), getIntX(), getIntY(), null);
		
//		g.setColor(Color.black);
//		g.fill(getBounds());
		
		if(isHighlighted) {
			Stroke s = g.getStroke();
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.green);
			g.draw(getBounds());
			g.setStroke(s);
		}
		
		g.setColor(Color.red);
		g.fillRect(getIntX()-5, getIntY()-15, (int)Math.round((getWidth() + 10) * (double)health/type.initialHealth), 10);
	}
}
