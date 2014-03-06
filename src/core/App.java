package core;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import graph.GraphEditor;
import phys.MetaballObject;
import phys.PhysicsSystem;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;
import util.Color;
import phys.VoronoiObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class App extends PApplet {

	public static final String staticFilepath = "./data/graphtest.xml";
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 1000;
	public static final DecimalFormat DF3 = new DecimalFormat("#.###");
	public static final DecimalFormat DF2 = new DecimalFormat("#.##");
	public static final DecimalFormat DF1 = new DecimalFormat("#.#");
	private static final String timestamp = new SimpleDateFormat("yyyy-MM-dd'v'HH").format(new Date());
	private static final String filename = "thesis_" + timestamp + ".xml";
	public static final String filepath = "./data/" + filename;
	public static boolean RECORDING = false;
	public static boolean isShiftDown;
	public static PFont pfont, bfont;
	public static ControlP5 CP5;
	public static PhysicsSystem PHYSICS;
	public static GraphEditor GRAPH;
	public static VoronoiObject VORONOI;
	public static MetaballObject METABALL;
	public static float WORLD_SCALE = 10;

	public void setup() {
		pfont = createFont("SourceCodePro", 8);
		bfont = createFont("SourceCodePro", 10);
		CP5 = new ControlP5(this);
		PHYSICS = new PhysicsSystem(this);
		GRAPH = new GraphEditor(this);
		METABALL = MetaballObject.getInstance(this);
		VORONOI = new VoronoiObject(this);
		Gui.init();
		size(WIDTH, HEIGHT, P2D);
		frameRate(60);
		smooth(16);
		colorMode(HSB, 360, 100, 100, 100);
		background(Color.BG);
		ellipseMode(RADIUS);
		textAlign(LEFT);
		textFont(pfont, 10);
		strokeWeight(1);
		noStroke();
		noFill();
	}
	public void draw() {
		background(Color.BG);
		noFill(); noStroke();
		VORONOI.update();
		PHYSICS.draw();
		GRAPH.draw();
		METABALL.draw();
		if (RECORDING) { RECORDING = false; endRecord(); System.out.println("SVG EXPORTED SUCCESSFULLY"); }
		CP5.draw();
	}
	public static void __rebelReload() {
		System.out.println("********************  rebelReload  ********************");
		System.out.println("Current File: " + filepath);
		Gui.init();
		Controllers.styleToggles();
		CP5.update();
	}
	public void controlEvent(ControlEvent theEvent) { Gui.controlEvent(this, theEvent); }
	public void keyPressed() {
		if (key == CODED) { switch (keyCode) { case SHIFT: isShiftDown = true; break; } }
		if (key == TAB) { Gui.isEditMode = !Gui.isEditMode; }
		if (Gui.isEditMode) {
			switch (key) {
				case '4': PhysicsSystem.drawPhysInfo = !PhysicsSystem.drawPhysInfo; break;
				case 'a': GRAPH.createNewNode(Gui.objName.getStringValue(), Gui.objSize.getValue(), mousePos()); Gui.toggleObjProperties(); break;
				case 'f': GRAPH.createNewEdge(); break;
				case 'x': GRAPH.deleteNode(GraphEditor.activeNode); break;
				case 'v': GRAPH.deleteEdges(); break;
				case 'q': GRAPH.createNewBranch(Gui.capacitySlider.getValue(), true); break;
				case 'w': GRAPH.createNewBranch(Gui.capacitySlider.getValue(), false); break;
				case 'l': GRAPH.lockNode(); break;
				case 'z': METABALL.clearMetaballs(); break;
				case 'o': VORONOI.calibrateThreshold();break;
				case 'p': VORONOI.calibrateMetaball();break;
			}
		}
	}
	private Vec2D mousePos() {return new Vec2D(mouseX, mouseY);}
	public void keyReleased() {
		if (key == CODED) { if (keyCode == SHIFT) { isShiftDown = false; } }
	}
	public static void main(String[] args) {
		PApplet.main(new String[]{("core.App")});
		System.out.println("Current File  " + filepath);
	}
	public void mouseDragged() {
		if (mouseButton == RIGHT) GRAPH.mouseDragged(mousePos());
	}
	public void mouseMoved() {
		GRAPH.mouseMoved(mousePos());
	}
	public void mousePressed() {
//		if (mouseButton == LEFT) { }
		if (mouseButton == RIGHT) { GRAPH.mousePressed(mousePos()); Gui.toggleObjProperties(); }
	}
	public void mouseReleased() {
		if (mouseButton == RIGHT) GRAPH.mouseReleased();
	}
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
		GRAPH.mouseWheel(e);
	}
}


