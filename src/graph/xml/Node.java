package graph.xml;

import core.App;
import core.Gui;
import graph.GraphEditor;
import graph.GraphObject;
import phys.PhysicsSystem;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;
import util.Ball;
import util.Vector2D;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {

	@XmlAttribute
	public int id;
	@XmlAttribute
	public String name;
	@XmlAttribute
	public float size;
	@XmlAttribute
	public int occupancy;
	@XmlAttribute
	public float x;
	@XmlAttribute
	public float y;
	@XmlAttribute
	public int type;
	@XmlTransient
	protected static int numberOfGNodes = 0;
	@XmlTransient
	public VerletParticle2D particle2D;
	@XmlTransient
	public AttractionBehavior2D behavior2D;
	@XmlTransient
	public Ball ball;
	@XmlTransient
	public NodeGraphic graphic;
	@XmlTransient
	public float metaballScale=1;

	public Node() {
		this.particle2D = new VerletParticle2D(this.x, this.y);
		this.behavior2D = new AttractionBehavior2D(this.particle2D, getRadius() * PhysicsSystem.phys_bhavior_str, -1);
		this.ball = new Ball(this.x, this.y, this.size);
		ball.strength(getRadius() / App.WORLD_SCALE);
		this.graphic = new NodeGraphic(this);
		++numberOfGNodes;
	}

	public Node(String name, float size, Vec2D pos) {
		this.id = ++numberOfGNodes;
		this.name = name;
		this.size = size;
		this.x = pos.x;
		this.y = pos.y;
		this.particle2D = new VerletParticle2D(pos);
		this.behavior2D = new AttractionBehavior2D(this.particle2D, getRadius() * PhysicsSystem.phys_bhavior_str, -1);
		this.ball = new Ball(pos.x, pos.y, size);
		ball.strength(getRadius() / App.WORLD_SCALE);
		this.graphic = new NodeGraphic(this);
	}

	public void update() {
		setX(particle2D.x);
		setY(particle2D.y);
		particle2D.setWeight(PhysicsSystem.phys_vec_wght);
		behavior2D.setRadius(getRadius() * PhysicsSystem.phys_bhavior_str);
		behavior2D.setStrength(PhysicsSystem.phys_bhavior_scale);
		ball.position(new Vector2D(x, y));
		ball.strength((getRadius() / App.WORLD_SCALE)+metaballScale);
	}
	public void draw(App p5) {
		graphic.draw(p5);
	}

	public float getX() { return x; }
	public float getY() { return y; }
	public int getId() { return id; }
	public void setId(int id) {this.id = id;}
	public String getName() { return name; }
	public void setName(String name) {this.name = name;}
	//	public float getSize() { return size; }
//	public void setSize(float size) {this.size = size;}
	public int getType() { return type; }
	public void setType(int type) { this.type = type; }
	public float getRadius() { return (float) ((Math.sqrt(this.size / Math.PI)) * PhysicsSystem.phys_vec_scale * App.WORLD_SCALE); }
	public int getOccupancy() { return occupancy; }
	public void setOccupancy(int occupancy) { this.occupancy = occupancy; }
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public AttractionBehavior2D getBehavior2D() { return behavior2D; }
	public Ball getBall() { return ball; }
	public static int getNumberOfGNodes() {return numberOfGNodes;}
	public VerletParticle2D getParticle2D() { return particle2D; }
	public void setBehavior2D(AttractionBehavior2D behavior2D) { this.behavior2D = behavior2D; }
	public void setBall(Ball ball) { this.ball = ball; }
	public static void setNumberOfGNodes(int numberOfGNodes) { numberOfGNodes = numberOfGNodes; }
	public void setParticle2D(VerletParticle2D particle2D) { this.particle2D = particle2D;}
	//	public void setX(float x) { this.x = x; }
	//	public void setY(float y) { this.y = y; }
	public class NodeGraphic {

		private final Node n;
		private int col;
		private float x;
		private float y;
		private float r;
		private App p5;
		private int nCol;
		private static final float oX = Gui.outlinerX;
		private final float oY;
		private float areaPercentage;

		public NodeGraphic(Node n) {
			this.n = n;
			this.oY = 50 + (n.getId() * 14);
			this.areaPercentage = (n.size / GraphObject.totalArea) * 100;
		}
		public void draw(App p5) {
			if (this.p5 == null) this.p5 = p5;
			x = n.x;
			y = n.y;
			r = n.getRadius();
			areaPercentage = (n.size / GraphObject.totalArea) * 100;
			nCol = (GraphObject.nodes.size() / 2 + (360 / (GraphObject.nodes.size()) * n.getId()));
			drawNode(0xffffff88, -1);
			drawInfo();
			if (App.GRAPH.selectedNodes.contains(n)) { drawNode(nCol, -1); drawNode(180, 40); }
			drawDatablock();
			drawWire();
		}
		private void drawNode(int stroke, int fill) {
			updateColors(stroke, fill);
			p5.ellipse(x, y, r, r);
			p5.noFill(); p5.noStroke();
		}

		private void updateColors(int stroke, int fill) {
			if (fill == -1) { p5.noFill(); }
			else if (fill == col) { p5.fill(fill, 100, 100, 50); }
			else { p5.fill(fill, 50); }
			if (stroke == -1) { p5.noStroke(); }
			else if (stroke == col) { p5.stroke(stroke, 100, 100, 100); }
			else { p5.stroke(stroke); }
		}
		private void drawInfo() {
			p5.fill(nCol);
			p5.textAlign(PApplet.CENTER);
			p5.text("[" + n.id + "] " + (int) size, n.x, n.y - 10);
			p5.textAlign(PApplet.LEFT);
			p5.noFill();
		}
		private void drawDatablock() {
			/** Dot */
			p5.fill(0xff333333);
			p5.stroke(nCol, 100, 100);
			p5.ellipse(oX - 8, oY + 7, 3, 3);

			/** Bar */
			if (n.id % 2 == 0) { p5.fill(0xff2b2b2b); }
			else { p5.fill(0xff333333); }
			p5.noStroke();
			p5.rect(oX, oY + 1, 200, 12);
			p5.stroke(0xff666666);
			p5.line(oX + 120, oY + 1, oX + 120, oY + 12);

			/** Text */
			p5.fill(0xffaeaeae);
			p5.textAlign(PApplet.LEFT);
			p5.text(name, oX, oY + 10);
			p5.text(App.DF1.format(size) + "sq.m", oX + 124, oY + 10);
			p5.textAlign(PApplet.RIGHT);
			p5.text(App.DF1.format(areaPercentage) + "%", oX + 190, oY + 10);
			p5.textAlign(PApplet.LEFT);
		}
		public void drawWire() {
			if ((App.GRAPH.selectedNodes.contains(n)) || (GraphEditor.activeNode == n)) {
				float h = 12;
				float w = 150;
				float xx = n.x + r;
				float yy = n.y - r;

				/** Wire */
				p5.stroke(0xff888888);
				p5.line(n.x, n.y, xx + h, yy - h);
				p5.line(xx + w + h, yy - h, oX - 100, yy - h);
				p5.line(oX - 100, yy - h, oX - 20, oY + 7);
				p5.line(oX - 20, oY + 7, oX - 8, oY + 7);

				/** Tag */
				p5.fill(0xff888888);
				p5.quad(
						       xx, yy,
						       xx + h, yy - h,
						       xx + 3 * w + h, yy - h,
						       xx + 3 * w, yy);
				p5.fill(0xffcccccc);
				p5.quad(xx, yy, xx + h, yy - h, xx + w + h, yy - h, xx + w, yy);
				p5.noStroke();

				/**bars */
				float str = n.getBall().strength();
				p5.fill(0xffffffff);

				p5.textAlign(PApplet.RIGHT); p5.text("node radius", 490, 50); p5.rect(500, 45, r, 6); p5.textAlign(PApplet.LEFT); p5.text(r, r + 510, 50);
				p5.textAlign(PApplet.RIGHT); p5.text("node size", 490, 70); p5.rect(500, 65, size, 6); p5.textAlign(PApplet.LEFT); p5.text(size, size + 510, 70);
				p5.textAlign(PApplet.RIGHT); p5.text("meta str", 490, 90); p5.rect(500, 85, str, 6); p5.textAlign(PApplet.LEFT); p5.text(str + " (" + Ball.MAX_STRENGTH + ")", str + 510, 90);

				/** Node */
				p5.fill(0xffffffff);
				p5.ellipse(n.x, n.y, 4, 4);

				/** Text */
				yy = yy - 3;
				p5.fill(0xff000000);
				p5.textAlign(PApplet.LEFT);
				p5.text(n.id, xx + 15, yy);
				p5.text(name, xx + 35, yy);
				p5.textAlign(PApplet.RIGHT);
				p5.text(App.DF1.format(size) + "sq.m", xx + w - 10, yy);
				p5.textAlign(PApplet.LEFT);
				p5.text("MBstr:" + App.DF1.format(n.getBall().strength()), xx + w + 20, yy);
				p5.text("Rad:" + App.DF1.format(n.getRadius()), xx + w + 120, yy);
				p5.noFill();
			}
		}
	}
}
