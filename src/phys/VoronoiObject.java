package phys;

import core.App;
import core.Gui;
import graph.GraphEditor;
import graph.GraphObject;
import graph.xml.Node;
import toxi.geom.ConvexPolygonClipper;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import java.util.ArrayList;
import java.util.List;


public class VoronoiObject extends GraphicsObject {

	/**
	 * GraphicsObject
	 */
	public boolean update_voronoi;
	public boolean draw_voronoi;
	public boolean draw_vor_poly;
	public boolean draw_vor_offset;
	public boolean draw_vor_bez;
	public boolean draw_vor_vec;
	public boolean draw_vor_info;
	public boolean draw_vor_metaball = true;
	public float vor_offset;
	private Voronoi voronoi = new Voronoi();
	private Polygon2D bounds = PhysicsObject.bounds.toPolygon2D();
	private ConvexPolygonClipper clipper = new ConvexPolygonClipper(bounds);
	private ArrayList<Polygon2D> interiorRegions = new ArrayList<>();

	public VoronoiObject(App $p5) {
		super($p5);
	}

	public void update() {
		if (draw_voronoi) {
			if (update_voronoi) {
				voronoi = new Voronoi();
				for (Node n : GraphEditor.nodes) { voronoi.addPoint(n.getParticle2D()); }
				if (draw_vor_metaball) { for (Vec2D p : MetaballObject.getPoints()) { voronoi.addPoint(p);} }
			} draw();
		}
	}

	private void draw() {
		interiorRegions = new ArrayList<>();
		List<Polygon2D> regions = voronoi.getRegions();
		for (int i = 0, size = regions.size(); i < size; i++) {
			Polygon2D poly = regions.get(i);
			poly = clipper.clipPolygon(poly);
			if (poly.vertices.size() < 3) return;
			if (!poly.isClockwise()) poly.flipVertexOrder();
			if (draw_vor_offset) poly.offsetShape(vor_offset);
			if (draw_vor_poly) drawPoly(poly, 0xff222222, -1);
			if (draw_vor_bez) drawPolyBezier(poly, 0xff666666, -1);
			if (draw_vor_vec) drawPolyVerts(poly, 0xffeca860, -1);
			if (draw_vor_info) { drawRegionInfo(poly, i, 0xff666666); }
			for (Node n : GraphEditor.nodes) {if (poly.containsPoint(n.particle2D)) { interiorRegions.add(poly);} }
		}
		if (draw_vor_info) for (Vec2D v : voronoi.getSites()) { drawSiteInfo(v, voronoi.getSites().indexOf(v), 0xff33ffff); }

		p5.fill(0xffffffff);
		p5.text("Nodes: " + totalNodeArea(), 400, 400);
		p5.text("Cells: " + totalRegionArea(), 400, 410);
		p5.noFill();
	}
	int totalRegionArea() {
		float totalRegionArea = 0;
		for (Polygon2D p : interiorRegions) { totalRegionArea += p.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE); }
		return (int) Math.abs(totalRegionArea);
	}
	int totalNodeArea() {
		float totalNodeArea = 0;
		for (Node n : GraphObject.nodes) { totalNodeArea += n.size; }
		return (int) Math.abs(totalNodeArea);
	}
	private float getAreaDifference() {
		float totalNodeArea = 0;
		float totalRegionArea = 0;
		float totalDif = 0;

		for (Node n : GraphObject.nodes) {
			totalNodeArea += n.size;
			for (Polygon2D p : voronoi.getRegions()) {
				if (p.containsPoint(n.particle2D)) {
					totalRegionArea += p.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE);
				}
			}
		}
		return Math.abs(totalNodeArea - totalRegionArea);
	}
	public void calibrateThreshold() {
		if (Math.abs(totalNodeArea() - totalRegionArea()) > 2) {
			if (totalNodeArea() < totalRegionArea()) {
				Gui.meta_visc.setValue(Gui.meta_visc.getValue() + 0.01f);
				System.out.print("+");
			}
			if (totalNodeArea() > totalRegionArea()) {
				Gui.meta_visc.setValue(Gui.meta_visc.getValue() - 0.01f);
				System.out.print("-");
			}
		}
	}

	public void calibrateMetaball() {
		if (Math.abs(totalNodeArea() - totalRegionArea()) > 10) {
			for (Polygon2D p : interiorRegions) {
				for (Node n : GraphObject.nodes) {
					if (p.containsPoint(n.particle2D)) {
						float pa = p.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE);
						float na = n.size;
						if (pa > na) {n.metaballScale--; }
						else if (pa < na) {n.metaballScale++;}
					}
				}
			}
		}
	}
	private ArrayList<Polygon2D> getInteriorRegions() {
		return interiorRegions;
	}
}
/*if (Math.abs(totalNodeArea() - totalRegionArea()) > 2) {
			if (totalNodeArea() < totalRegionArea()) {
				Gui.meta_visc.setValue(Gui.meta_visc.getValue() + 0.01f);
				System.out.print("+");
			}
			if (totalNodeArea() > totalRegionArea()) {
				Gui.meta_visc.setValue(Gui.meta_visc.getValue() - 0.01f);
				System.out.print("-");
			}
		}*/
	/*	public void calibrateMetaballOrig() {
			if (Math.abs(totalNodeArea() - totalRegionArea()) > 2) {
				if (totalNodeArea() < totalRegionArea()) {
					Node.metaballScale--;
				}
				else if (totalNodeArea() > totalRegionArea()) {
					Node.metaballScale++;
				}
			}
		}*/
