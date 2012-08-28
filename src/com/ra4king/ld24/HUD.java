package com.ra4king.ld24;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.ra4king.gameutils.Entity;
import com.ra4king.gameutils.util.Bag;

public class HUD {
	private Arena arena;
	
	private Bag<Unit> selected; 
	private Point2D.Double origin;
	private Rectangle2D.Double highlight;
	
	public HUD(Arena arena) {
		this.arena = arena;
		
		selected = new Bag<Unit>();
	}
	
	public Bag<Unit> getSelectedUnits() {
		return selected;
	}
	
	public void update(long deltaTime) {
		boolean ctrl = arena.getGame().getInput().isKeyDown(KeyEvent.VK_CONTROL); 
		
		if(highlight != null) {
			if(!ctrl)
				selected.clear();
			
			for(Entity e : arena.getEntitiesAt(1)) {
				if(e instanceof Unit) {
					Unit u = (Unit)e;
					
					if(!ctrl)
						u.setHighlighted(false);
					
					if(!selected.contains(u) && highlight.intersects(u.getBounds())) {
						selected.add(u);
						u.setHighlighted(true);
					}
				}
			}
		}
	}
	
	public void draw(Graphics2D g) {
		if(highlight != null) {
			g.setColor(Color.green);
			g.draw(highlight);
		}
	}
	
	public void mousePressed(MouseEvent me) {
		origin = new Point2D.Double(me.getX(),me.getY());
		highlight = new Rectangle2D.Double(origin.x,origin.y,0,0);
		
		if(me.getButton() == MouseEvent.BUTTON3) {
			for(Unit u : selected)
				u.setDestination(new Point2D.Double(me.getPoint().x,me.getPoint().y));
		}
	}
	
	public void mouseReleased(MouseEvent me) {
		highlight = null;
		origin = null;
	}
	
	public void mouseDragged(MouseEvent me) {
		if(highlight != null) {
			double dx = me.getX() - origin.x;
			double dy = me.getY() - origin.y;
			
			if(dx < 0) {
				highlight.x = origin.x + dx;
				highlight.width = -dx;
			}
			else {
				highlight.x = origin.x;
				highlight.width = dx;
			}
			
			if(dy < 0) {
				highlight.y = origin.y + dy;
				highlight.height = -dy;
			}
			else {
				highlight.y = origin.y;
				highlight.height = dy;
			}
		}
	}
	
	public void mouseMoved(MouseEvent me) {
		
	}
}
