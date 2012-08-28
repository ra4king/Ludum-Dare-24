package com.ra4king.ld24;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.ra4king.gameutils.Art;
import com.ra4king.gameutils.Entity;
import com.ra4king.gameutils.gameworld.GameComponent;
import com.ra4king.gameutils.gameworld.GameWorld;
import com.ra4king.gameutils.util.Animation;
import com.ra4king.gameutils.util.FastMath;

public class Unit extends GameComponent {
	public static enum UnitType {
		CELL(50,50,100,10);
		
		private BufferedImage[][] frames, split;
		private int width, height, initialHealth, foodToSplit;
		
		private UnitType(int width, int height, int initialHealth, int foodToSplit) {
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
	
	private static enum TeamColor {
		ONE(Color.red),
		TWO(Color.green);
		
		private Color color;
		
		private TeamColor(Color color) {
			this.color = color;
		}
	}
	
	private Animation normal, splitting;
	private UnitType type;
	private int team;
	private double rot;
	
	private Food target;
	private int foodEaten;
	
	private Point2D.Double destination;
	
	private final double speed = 100;
	
	private long elapsedTime;
	
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
	
	public Point2D.Double getDestination() {
		return destination;
	}
	
	public void setDestination(Point2D.Double dest) {
		destination = dest;
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
			
			double dist = (f.getCenterX()-getCenterX()) * (f.getCenterX()-getCenterX()) + (f.getCenterY()-getCenterY()) * (f.getCenterY()-getCenterY());
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
		if(isAlive) {
			elapsedTime += deltaTime;
			
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
			
			if(destination == null) {
				if(!isSplitting && target != null && getParent().contains(target)) {
					double r = FastMath.atan2(target.getY() - getCenterY(), target.getX() - getCenterX());
					
					while(r < 0)
						r += 2*Math.PI;
					
					if(Math.abs(r-rot) > Math.PI)
						r -= 2*Math.PI;
					
					rot += (r-rot) * (5*delta);
					
					double dx = FastMath.cos(rot) * speed * delta;
					double dy = FastMath.sin(rot) * speed * delta;
					
					setX(getX() + dx);
					setY(getY() + dy);
					
					if(target.intersects(this)) {
						getParent().remove(target);
						target = null;
						foodEaten++;
						health = Math.min(health+10, type.initialHealth);
						checkForSplit();
					}
				}
				else if(!isSplitting)
					target = getClosestFood();
			}
			else {
				rot = FastMath.atan2(destination.y - getCenterY(), destination.x - getCenterX());
				
				double dx = FastMath.cos(rot) * speed * delta;
				double dy = FastMath.sin(rot) * speed * delta;
				
				setX(getX() + dx);
				setY(getY() + dy);
				
				if(getBounds().contains(destination)) {
					destination = null;
					target = null;
				}
			}
			
			for(Entity e : getParent().getEntities()) {
				if(e instanceof Food && ((Food)e).intersects(this)) {
					getParent().remove(e);
					foodEaten++;
					health = Math.min(health+10, type.initialHealth);
					checkForSplit();
				}
				else if(e instanceof Unit && ((Unit)e).intersects(this)) {
					Unit u = (Unit)e;
					
					if(!u.isAlive) {
						getParent().remove(u);
						foodEaten++;
						health = Math.min(health+1, type.initialHealth);
						checkForSplit();
					}
					else if(u.team != team) {
						u.health--;
						rot = (rot + Math.PI) % (2*Math.PI);
					}
				}
			}
			
			if(elapsedTime >= 5e8) {
				elapsedTime -= 5e8;
				health -= 10;
			}
			
			if(health <= 0) {
				isAlive = false;
				setSize(getWidth()/2, getHeight()/2);
				getParent().add(0,new Food(getCenterX(), getCenterY()));
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.rotate(rot, getCenterX(), getCenterY());
		
		if(isAlive) {
			g.drawImage((isSplitting ? splitting : normal).getFrame(), getIntX(), getIntY(), null);
			
			if(isHighlighted) {
				Stroke s = g.getStroke();
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.green);
				g.draw(getBounds());
				g.setStroke(s);
			}
			
			g.setColor(Color.gray);
			g.fillRect(getIntX()-5, getIntY()-15, getIntWidth()+10, 10);
			g.setColor(TeamColor.values()[team].color);
			g.fillRect(getIntX()-5, getIntY()-15, (int)Math.round((getWidth() + 10) * (double)health/type.initialHealth), 10);
		}
		else
			g.drawImage(normal.getFrame(), getIntX(), getIntY(), getIntWidth(), getIntHeight(), Color.green, null);
	}
}
