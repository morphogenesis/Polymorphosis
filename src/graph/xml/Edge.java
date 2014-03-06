package graph.xml;

import core.App;
import phys.PhysicsSystem;
import toxi.physics2d.VerletSpring2D;

import javax.xml.bind.annotation.*;

import static util.Color.SELECTED;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "rel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Edge {

	@XmlAttribute
	private int from;
	@XmlAttribute
	private int to;
	@XmlTransient
	private Node a;
	@XmlTransient
	private Node b;
	@XmlTransient
	private VerletSpring2D spring2D;
	@XmlTransient
	public EdgeGraphic graphic;

	public Edge() {graphic = new EdgeGraphic(this); }
	public Edge(Node a, Node b) {
		this.a = a;
		this.b = b;
		this.from = (a.getId());
		this.to = (b.getId());
		this.spring2D = new VerletSpring2D(a.getParticle2D(), b.getParticle2D(), getLength() * PhysicsSystem.phys_spr_scale, PhysicsSystem.phys_spr_strength);
		graphic = new EdgeGraphic(this);
	}

	public void update() {
		spring2D.setStrength(PhysicsSystem.phys_spr_strength);
		spring2D.setRestLength(getLength() * PhysicsSystem.phys_spr_scale);
	}
	public void draw(App p5) {graphic.draw(p5);}
	public VerletSpring2D getSpring2D() { return spring2D; }
	public void setSpring2D(VerletSpring2D s) {
		spring2D = s;
		//		this.spring2D = spring2D;
	}
	public float getLength() { return a.getRadius() + b.getRadius();}
	public Node getB() { return b; }
	public void setB(Node nB) { this.b = nB;}
	public Node getA() { return a;}
	public void setA(Node nA) { this.a = nA; }
	public int getTo() { return to; }
	public int getFrom() { return from; }
	//	public void setTo(int to) {this.to = to;}
	//	public void setFrom(int from) {this.from = from;}

	/**
	 * Created on 2/26/14.
	 */
	public class EdgeGraphic {
		private final Edge e;
		private float ax;
		private float ay;
		private float bx;
		private float by;

		public EdgeGraphic(Edge e) {
			this.e = e;
		}
		public void draw(App p5) {
			ax = getA().getX();
			ay = getA().getY();
			bx = getB().getX();
			by = getB().getY();

			p5.noFill();
			p5.stroke(0xff666666);
			p5.line(ax, ay, bx, by);
		}
		public void drawActive(App p5) {
			p5.noFill();
			p5.stroke(SELECTED);
			p5.line(ax, ay, bx, by);
		}
	}
}
