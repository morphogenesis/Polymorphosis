package phys;

import core.App;
import core.Gui;
import graph.xml.Edge;
import graph.xml.Node;
import graph.xml.XmlGraph;
import processing.core.PApplet;
import toxi.geom.Rect;
import toxi.physics2d.VerletMinDistanceSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.HashMap;

import static util.Color.BLACK;
import static util.Color.GREY_DK;


public class PhysicsSystem extends PhysicsObject {

	public static boolean updatePhysics = true;
	public static boolean drawPhysSpr = true;
	public static boolean drawPhysVec = true;
	public static boolean drawPhysInfo;
	public static boolean drawPhysMin;
	public static boolean drawPhysBhv;
	public static boolean drawPhysWgt;

	public static float phys_drag = 0.3f;
	public static float phys_vec_scale = 1;
	public static float phys_vec_wght = 1f;
	public static float phys_spr_scale = 1;
	public static float phys_spr_strength = 0.01f;
	public static float phys_bhavior_str = 2f;
	public static float phys_bhavior_scale = -1f;
	public static float phys_mindist_str = 0.01f;

	protected final HashMap<String, String> info = new HashMap<>();

	public PhysicsSystem(App $p5) {
		super($p5);
		bounds = new Rect(10, 10, p5.width - 20, p5.height - 20);
		physics.setWorldBounds(bounds);
		physics.setDrag(phys_drag);
	}
	public void draw() {
		p5.noFill(); p5.noStroke();
		if (updatePhysics) update();
		if (drawPhysInfo) drawInfo();
		if (drawPhysSpr) for (VerletSpring2D s : springs) drawSprings(s, 0xff333333, -1, p5);
		if (drawPhysMin) for (VerletSpring2D s : mindists) drawSprings(s, 0xff333333, -1, p5);
		if (drawPhysVec) for (VerletParticle2D a : particles) drawParticle(a, 3, BLACK, GREY_DK);
		if (drawPhysWgt) for (VerletParticle2D a : particles) drawParticle(a, a.getWeight(), BLACK, GREY_DK);
		if (drawPhysBhv) {
			for (AttractionBehavior2D a : behaviors) {drawAttractor(a, 0xff343434, -1); }
			for (AttractionBehavior2D a : attractors) {drawAttractor(a, 0xff222222, -1); }
			for (VerletParticle2D a : attractorParticles) {drawParticle(a, 10, BLACK, GREY_DK); }
		}
	}
	void update() {
		physics.update();
		physics.setDrag(phys_drag);
		for (VerletParticle2D p : particles) { p.setWeight(phys_vec_wght); }
		for (AttractionBehavior2D b : behaviors) {b.setStrength(phys_bhavior_scale);}
		for (AttractionBehavior2D b : behaviors) {b.setStrength(phys_bhavior_scale);}
		for (VerletSpring2D s : springs) { s.setStrength(phys_spr_strength); }
		for (VerletSpring2D s : mindists) {s.setStrength(phys_mindist_str);}
		for (VerletSpring2D s : attractorMin) {s.setStrength(Gui.cloudMinStr); s.setRestLength((Gui.cloudBhvScl * 2) * Gui.cloudMinScl);}
		for (VerletParticle2D p : attractorParticles) { p.setWeight(Gui.cloudVecWgt); }
		for (AttractionBehavior2D b : attractors) { b.setRadius(Gui.cloudBhvScl); b.setStrength(Gui.cloudBhvStr); }
	}
	void drawInfo() {
		info.put("PSYS.springs : ", String.valueOf(physics.springs.size()));
		info.put("PSYS.particles phys: ", String.valueOf(physics.particles.size()));
		info.put("PSYS.behaviors phys: ", String.valueOf(physics.behaviors.size()));
		info.put("phys.springs : ", String.valueOf(springs.size()));
		info.put("phys.particles : ", String.valueOf(particles.size()));
		info.put("phys.behaviors : ", String.valueOf(behaviors.size()));
		info.put("w.iter : ", App.DF3.format(physics.getNumIterations()));
		info.put("w.drag : ", App.DF3.format(physics.getDrag()));
		info.put("x.getB scale : ", App.DF3.format(phys_bhavior_str));
		info.put("x.p scale : ", App.DF3.format(phys_vec_scale));
		info.put("x.s scale : ", App.DF3.format(phys_spr_scale));

		p5.fill(0xff666666); p5.noStroke();
		p5.pushMatrix(); p5.translate(300, 30);
		for (String key : info.keySet()) {
			p5.translate(0, 10); p5.textAlign(PApplet.LEFT); p5.text(key, -50, 0);
			p5.textAlign(PApplet.RIGHT); p5.text(String.valueOf(info.get(key)), 80, 0);
		} p5.popMatrix();
	}
	public void addMinDist() {
		clearMinDist();
		for (Node na : XmlGraph.nodes) {
			VerletParticle2D va = na.getParticle2D();
			for (Node nb : XmlGraph.nodes) {
				VerletParticle2D vb = nb.getParticle2D();
				if ((na != nb) && (physics.getSpring(na.getParticle2D(), nb.getParticle2D()) == null)) {
					float len = (na.getRadius() + nb.getRadius());
					VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, len, .01f);
					mindists.add(s);
					physics.addSpring(s);
				}
			}
		}
	}
	public void clearMinDist() {
		for (VerletSpring2D s : attractorMin) {physics.springs.remove(s);}
		for (VerletSpring2D s : mindists) { physics.springs.remove(s); }
		attractorMin.clear();
		mindists.clear();
	}
	public void addParticle(Node n) {
		particles.add(n.getParticle2D());
		physics.addParticle(n.getParticle2D());
		behaviors.add(n.getBehavior2D());
		physics.addBehavior(n.getBehavior2D());
	}
	public void addSpring(Edge e) {
		springs.add(e.getSpring2D());
		physics.addSpring(e.getSpring2D());
	}
	public void removeParticle(Node n) {
		particles.remove(n.getParticle2D());
		physics.removeParticle(n.getParticle2D());
		behaviors.remove(n.getBehavior2D());
		physics.removeBehavior(n.getBehavior2D());
	}
	public void removeSpring(Edge e) {
		springs.remove(e.getSpring2D());
		physics.removeSpring(e.getSpring2D());
	}
	public void reset() {springs.clear(); mindists.clear(); particles.clear(); behaviors.clear(); physics.clear();}
}
