package phys;

import processing.core.PApplet;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import util.Ball;
import util.Vector2D;

import java.util.ArrayList;


/**
 * Created on 3/5/14.
 */
public class MetaballObject {

	public static float meta_viscosity = 2;
	public static float meta_threshold = 0.0006f;
	public static float meta_resolution = 40;
	public static int meta_maxSteps = 400;
	public static ArrayList<Ball> _balls;
	public static ArrayList<Vec2D> points = new ArrayList<>();
	public static ArrayList<Polygon2D> _polygons;
	protected static boolean lock;
	protected static float minStrength;
	private static MetaballObject instance = null;
	protected final PApplet p5;
	Polygon2D _outline = new Polygon2D();

	public MetaballObject(PApplet p5) {
		this.p5 = p5;
		if (lock) { throw new Error("Error: Instantiation failed. Use getInstance() instead of new."); }
		else { _balls = new ArrayList<>(); minStrength = Ball.MIN_STRENGTH; }
	}
	public void draw() {
		if (_balls.size() > 0) {
			run();
			p5.noFill();
			int numVerts = 0;
			for (Polygon2D p : _polygons) {
				p5.stroke(0xff2b2b2b);
				for (Vec2D v :points) {
					numVerts++; p5.text(numVerts, v.x, v.y); p5.ellipse(v.x, v.y, 3, 3); }
				p5.stroke(0xffff3333);
				for (Line2D l : p.getEdges()) { p5.line(l.a.x, l.a.y, l.b.x, l.b.y); }
				p5.noStroke();
			}
			p5.fill(0xffffffff);
			p5.text("Ball", 20, p5.height - 200);
			p5.text("Polys: " + _polygons.size(), 20, p5.height - 180);
			p5.text("Verts: " + numVerts, 20, p5.height - 160);
		}
	}
	public void addMetaball(Vector2D pos) {
		Ball m = new Ball(pos, p5.random(1, 4));
		addMetaball(m);
	}
	public void addMetaball(Ball ball) {
		minStrength = Math.min(ball.strength(), minStrength);
		_balls.add(ball);
		meta_maxSteps = _balls.size() * 400;
	}
	public void clearMetaballs() { _balls.clear(); }
	public static MetaballObject getInstance(PApplet p5) { if (instance == null) { instance = new MetaballObject(p5); lock = true; } return instance; }
	public static ArrayList<Vec2D> getPoints() { return points; }
	public int getSize() { return _balls.size(); }
	public void removeMetaball(Ball ball) { _balls.remove(ball); }

	public void run() {
		_polygons = new ArrayList<>();
		points = new ArrayList<>();
		_outline = new Polygon2D();
		Vector2D seeker = new Vector2D();
		int i;

		for (Ball ball : _balls) {
			ball.tracking = false;
			seeker.copy(ball.position());
			i = 0;
			//	while ((stepToEdge(seeker.add(new Vector2D(1, 1))) > meta_threshold) && (++i < 50)) { }
			while ((stepToEdge(seeker) > meta_threshold) && (++i < 50)) { }
			ball.edge.copy(seeker);
		}

		int edgeSteps = 0;
		Ball current = untrackedMetaball();
		seeker.copy(current.edge);
		_outline.add(seeker.x, seeker.y);
		points.add(new Vec2D(seeker.x, seeker.y));
		while (current != null && edgeSteps < meta_maxSteps) {
			rk2(seeker, meta_resolution);
			_outline.add(seeker.x, seeker.y);
			for (Ball ball : _balls) {
				if (seeker.dist(ball.edge) < (meta_resolution * 0.9f)) {
					seeker.copy(ball.edge);
					_outline.add(seeker.x, seeker.y);
					current.tracking = true;
					if (ball.tracking) {
						current = untrackedMetaball();
						if (current != null) {
							seeker.copy(current.edge);
							addOutline(_outline);
							_outline = new Polygon2D();
							_outline.add(seeker.x, seeker.y);
						}
					}
					else { current = ball; } break;
				}
			} ++edgeSteps;
		} addOutline(_outline);
	}

	private Ball untrackedMetaball() {
		for (Ball ball : _balls) {
			if (!ball.tracking) { return ball; }
		} return null;
	}
	private float calc_force(Vector2D v) {
		float force = 0.0f;
		for (Ball ball : _balls) {
			force += ball.strengthAt(v, meta_viscosity);
		} return force;
	}
	private float stepToEdge(Vector2D seeker) {
		float force = calc_force(seeker);
		float stepsize;
		stepsize = (float) Math.pow(minStrength / meta_threshold, 1 / meta_viscosity) - (float) Math.pow(minStrength / force, 1 / meta_viscosity) + 0.01f;
		seeker.add(calc_normal(seeker).multiply(stepsize));
		return force;
	}
	private Vector2D calc_normal(Vector2D v) {
		Vector2D force = new Vector2D();
		Vector2D radius;
		for (Ball ball : _balls) {
			radius = Vector2D.subtract(ball.position(), v);
			if (radius.getLengthSq() == 0) { continue; }
			radius.multiply(-meta_viscosity * ball.strength() * (1 / (float) Math.pow(radius.getLengthSq(), (2 + meta_viscosity) * 0.5f)));
			force.add(radius);
		} return force.norm();
	}
	private void rk2(Vector2D v, float h) {
		Vector2D t1 = calc_normal(v).getPerpLeft();
		t1.multiply(h * 0.5f);
		Vector2D t2 = calc_normal(Vector2D.add(v, t1)).getPerpLeft();
		t2.multiply(h);
		v.add(t2);
	}
	private void addOutline(Polygon2D outline) {
		_polygons.add(outline);
		//		outline.removeDuplicates(meta_resolution);
		points.addAll(outline.removeDuplicates(meta_resolution).vertices);
	}
}
