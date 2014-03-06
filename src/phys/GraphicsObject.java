package phys;

import core.App;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.List;

/**
 * Created on 3/3/14.
 */
public abstract class GraphicsObject {
	protected final App p5;
	public GraphicsObject(App $p5) {this.p5 = $p5;}
	public void drawAttractor(AttractionBehavior2D behavior, int stroke, int fill) {
		updateColor(stroke, fill);
		Vec2D vb = behavior.getAttractor();
		p5.ellipse(vb.x, vb.y, behavior.getRadius(), behavior.getRadius());
	}
	public void drawParticle(VerletParticle2D particle, float radius, int stroke, int fill) {
		updateColor(stroke, fill);
		p5.ellipse(particle.x, particle.y, radius, radius);
	}
	public void drawSprings(VerletSpring2D spring, int stroke, int fill, App app) {
		updateColor( stroke, fill);
		p5.line(spring.a.x, spring.a.y, spring.b.x, spring.b.y);
	}
	public void updateColor(int stroke, int fill) {
		if (fill == -1) { p5.noFill(); } else p5.fill(fill);
		if (stroke == -1) { p5.noStroke(); } else p5.stroke(stroke);
	}

	protected void drawPoly(Polygon2D poly, int stroke, int fill) {
		updateColor(stroke, fill);
		for (Line2D l : poly.getEdges()) { p5.line(l.a.x, l.a.y, l.b.x, l.b.y); }
	}

	protected void drawPolyBezier(Polygon2D poly, int stroke, int fill) {
		updateColor(stroke, fill);

		List<Vec2D> list = poly.vertices;
		Vec2D a = list.get(0);
		Vec2D b = list.get(list.size() - 1);
		Vec2D o = new Vec2D((b.x + a.x) / 2, (b.y + a.y) / 2);

		p5.beginShape();
		p5.vertex(o.x, o.y);
		for (int i = 0; i < list.size(); i++) {
			Vec2D c = list.get(i);
			Vec2D d = list.get((i + 1) % list.size());
			p5.bezierVertex(c.x, c.y, c.x, c.y, (d.x + c.x) / 2, (d.y + c.y) / 2);
		}
		p5.endShape(PApplet.CLOSE);
	}
	protected void drawPolyVerts(Polygon2D poly, int stroke, int fill) {
		updateColor(stroke, fill);
		for (Vec2D v : poly.vertices) {p5.ellipse(v.x, v.y, 4, 4);}
	}

	protected void drawShape(Polygon2D poly) {
		int id = 0;
		PShape pShape = p5.createShape();
		pShape.beginShape();
		pShape.fill(0xff444444);
		pShape.stroke(0xffffffff);
		for (Vec2D v : poly.vertices) {
			pShape.vertex(v.x, v.y);
			p5.fill(255);
			p5.text(id++, v.x, v.y);
		}
		pShape.endShape(PConstants.CLOSE);
		p5.shape(pShape);
	}

	protected void drawSiteInfo(Vec2D v, int index, int fill) {
		p5.fill(fill);
		p5.textAlign(PConstants.RIGHT);
		p5.text(index, v.x - 10, v.y);
		p5.noFill();
	}
	protected void drawRegionInfo(Polygon2D poly, int index, int fill) {
		float x = poly.getCentroid().x + 10;
		float y = poly.getCentroid().y;
		p5.fill(fill);
		p5.textAlign(PConstants.LEFT);
		p5.text("Index: " + index, x, y);
		p5.text("Area: " + (int) (poly.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE)), x, y + 10);
		p5.text("Circ: " + (int) poly.getCircumference() / App.WORLD_SCALE, x, y + 20);
		p5.text("Verts: " + poly.getNumVertices(), x, y + 30);
		p5.noFill();
	}
}
