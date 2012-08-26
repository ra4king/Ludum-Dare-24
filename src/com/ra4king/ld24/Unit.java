package com.ra4king.ld24;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.ra4king.gameutils.Entity;
import com.ra4king.gameutils.gameworld.GameComponent;
import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.gameutils.util.FastMath;

public class Unit extends GameComponent {
	public static enum UnitType {
		AMOEBA(30,30,20),
		DIATOM(40,40,40),
		KRILL(40,40,50),
		SLUG(50,50,60);
		
		private int width, height, initialHealth;
		
		UnitType(int width, int height, int initialHealth) {
			this.width = width;
			this.height = height;
			this.initialHealth = initialHealth;
		}
	}
	
	//private Animation animation;
	private UnitType type;
	private int team;
	private double rot;
	
	private Food target;
	private final double speed = 100;
	
	private int health;
	
	private boolean isAlive;
	private boolean isHighlighted;
	
	public Unit(UnitType type, int team) {
		this.type = type;
		
		this.team = team;
		isAlive = true;
		
		health = type.initialHealth;
		setSize(type.width, type.height);
	}
	
	@Override
	public void init(GameWorld world) {
		super.init(world);
		
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
		//animation = new Animation(true);
		//animation.addFrames(getParent().getGame().getArt().get(type.name().toLowerCase()), type.width, type.height, 50);
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
	
	@Override
	public void update(long deltaTime) {
		//animation.update(deltaTime);
		
		final double delta = deltaTime / 1e9;
		
		if(target != null && getParent().contains(target)) {
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
			}
		}
		else
			target = getClosestFood();
	}
	
	@Override
	public void draw(Graphics2D g) {
		//g.drawImage(animation.getFrame(), getIntX(), getIntY(), null);
		
		g.rotate(rot, getCenterX(), getCenterY());
		g.setColor(Color.black);
		g.fill(getBounds());
		
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
