package com.dig.www.character;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.dig.www.blocks.Block.Blocks;
import com.dig.www.start.Board;
import com.dig.www.util.Sprite;
import com.dig.www.util.Statics;

public abstract class GameCharacter extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}
	
	public enum Types {
		CLUB {
			public String toString() {
				return "club";
			}
		}, HEART {
			public String toString() {
				return "heart";
			}
		}, SPADE {
			public String toString() {
				return "shovel";
			}
		}, DIAMOND {
			public String toString() {
				return "diamond";
			}
		};
	}

	private int deltaX = 0;
	private int deltaY = 0;
	protected Direction direction = Direction.RIGHT;
	protected Types type;

	private boolean moveX = false;
	private boolean moveY = false;

	private boolean wallBound = false;
//	private int wallX = 0;
//	private int wallY = 0;

	protected boolean acting = false;
	protected int actTimer = 0;

	private final int SPEED = 10;

	private int counter = 0;
	private int animationTimer = 7;

	private static final int ANIMAX = 7;
	private static final int MAX = 4;
	private String charName = "reyzu";

	private static final int HP_MAX = 5;
	private static final int HP_TIMER_MAX = 50;
	private static final int HITSTUN_MAX = 10;
	private int health = HP_MAX;
	private int hpTimer = 0;
	private int hitstunTimer = 0;

	public GameCharacter(int x, int y, String name, Board owner, Types type, String charName) {
		super(x, y, "n", owner);
		
		this.charName = charName;
		this.type = type;
	}

	@Override
	public void animate() {
		// TODO Auto-generated method stub

		if (hitstunTimer > 0) {
			hitstunTimer--;
			flicker();

			if (hitstunTimer == 0) {
				visible = true;
			}
		}

		if (hpTimer > 0) {
			hpTimer--;

			if (hpTimer <= 0 && health < HP_MAX) {
				health++;
				hpTimer = HP_TIMER_MAX;
			}
		}

		if (!wallBound) {

			if (animationTimer >= ANIMAX) {

				if (moveX || moveY) {
					animationTimer = 0;
					image = newImage("w" + counter);

					counter++;

					if (counter == MAX)
						counter = 0;
				} else
					image = newImage("n");
			} else
				animationTimer++;

			owner.setScrollX(deltaX);
			owner.setScrollY(deltaY);
		} else {

//			int tempX = 0;
//			int tempY = 0;
//
//			if (wallX - 10 > getMidX())
//				tempX = SPEED;
//			else if (wallX + 10 < getMidX())
//				tempX = -SPEED;
//			else
//				tempX = 0;
//
//			if (wallY - 10 > getMidY())
//				tempY = SPEED;
//			else if (wallY + 10 < getMidY())
//				tempY = -SPEED;
//			else
//				tempY = 0;

			owner.setScrollX(-owner.getScrollX());
			owner.setScrollY(-owner.getScrollY());

			owner.reAnimate();

			owner.setScrollX(0);
			owner.setScrollY(0);

			wallBound = false;
		}
	}

	public void keyPressed(int keyCode) {

		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:

			deltaX = SPEED;
			direction = Direction.LEFT;
			moveX = true;

			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:

			deltaX = -SPEED;
			direction = Direction.RIGHT;
			moveX = true;

			break;

		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:

			deltaY = SPEED;
			direction = Direction.UP;
			moveY = true;

			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:

			deltaY = -SPEED;
			direction = Direction.DOWN;
			moveY = true;

			break;

		case KeyEvent.VK_SPACE:
			acting = true;
			actTimer = 10;
			break;
		}
	}

	public void keyReleased(int keyCode) {

		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:

			if (direction == Direction.LEFT || direction == Direction.RIGHT) {
				if (deltaY > 0)
					direction = Direction.UP;
				else if (deltaY < 0)
					direction = Direction.DOWN;
			}

			deltaX = 0;
			moveX = false;

			break;

		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			// deltaY = 0;
			if (direction == Direction.UP || direction == Direction.DOWN) {
				if (deltaX > 0)
					direction = Direction.LEFT;
				else if (deltaX < 0)
					direction = Direction.RIGHT;
			}

			deltaY = 0;
			moveY = false;

			break;
		}
	}

	public void collision(int midX, int midY) {
		// TODO Auto-generated method stub
		wallBound = true;
//		wallX = midX;
//		wallY = midY;
	}

	public Rectangle getActBounds() {

		switch (direction) {
		case UP:
			return new Rectangle(x + 47, y - 30, 6, 6);
		case DOWN:
			return new Rectangle(x + 47, y + Statics.BLOCK_HEIGHT + 40, 6, 6);
		case RIGHT:
			return new Rectangle(x + Statics.BLOCK_HEIGHT + 15, y + Statics.BLOCK_HEIGHT - 6, 6, 6);
		case LEFT:
		default:
			return new Rectangle(x - 40, y + Statics.BLOCK_HEIGHT - 6, 6, 6);
		}
	}

	private Font HUD = new Font("Broadway", Font.BOLD, 30);

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub

		if (visible)
			if (direction != Direction.LEFT)
				g2d.drawImage(image, x, y, owner);
			else
				g2d.drawImage(image, x + width, y, -width, height, owner);

		if (acting || actTimer > 0) {
			drawTool(g2d);
		}
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(10, 20, 180, 80);

		for (int i = 1; i <= HP_MAX; i++) {

			g2d.setColor(health >= i ? Color.RED : Color.DARK_GRAY);
			g2d.fillRect(i * 30, 70, 20, 20);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(i * 30, 70, 20, 20);
		}

		g2d.setColor(Color.RED);
		g2d.setFont(HUD);
		g2d.drawString("HEALTH:", 20, 50);
		
		drawCSHUD(g2d);
	}
	
	protected void drawTool(Graphics2D g2d) {
		int dX = 0;
		int dY = 0;

		switch (direction) {
		case UP:
			dX = x + 20;
			dY = y - Statics.BLOCK_HEIGHT + 70;
			break;

		case DOWN:
			dX = x - 10;
			dY = y + Statics.BLOCK_HEIGHT - 50;
			break;

		case RIGHT:
			dX = x + Statics.BLOCK_HEIGHT - 50;
			dY = y;
			break;

		case LEFT:
			dX = x - 100;
			dY = y;
			break;
		}

		g2d.drawImage(newImage(type.toString()), dX, dY, owner);

		if (direction == Direction.UP)
			g2d.drawImage(image, x, y, owner);

		actTimer--;
		
		if (actTimer == 0)
			acting = false;
	}
	
	protected abstract void drawCSHUD(Graphics2D g2d) ;
	
	// Works only for integers
	protected int numOfDigits(int num) {
		
		int x = 10;
		int i = 0;
		
//		if (num < 0)
//			num = -num;
		
		while (x <= num) {
			
			i++;
			x = x * 10;
		}
		
		return i;
	}

	public boolean isActing() {
		return acting;
	}

	public void endAction() {
		// TODO Auto-generated method stub
		acting = false;
	}

	public Image newImage(String name) {
		return super.newImage(getPath() + name + ".png");
	}

	private String getPath() {

		String dir;

		if (direction != null)
			switch (direction) {
			case UP:
				dir = "back";
				break;

			case DOWN:
				dir = "front";
				break;

			default:
				dir = "side";
				break;
			}
		else
			dir = "side";
		return "images/characters/" + (charName != null ? charName : "spade") + "/" + dir + "/";
	}

	public Rectangle getCollisionBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(x + 40, y + 40, width - 80, height - 40);
	}

	public void takeDamage() {

		if (hitstunTimer <= 0) {
			health--;
			hpTimer = 100;
			hitstunTimer = HITSTUN_MAX;

			if (health <= 0)
				owner.setState(Board.State.DEAD);
		}
	}
	
	public abstract boolean canAct() ;
	
	public abstract void getsActor() ;

	public Types getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public void stop() {
		// TODO Auto-generated method stub
		moveX = false;
		moveY = false;
		deltaX = 0;
		deltaY = 0;
	}
}