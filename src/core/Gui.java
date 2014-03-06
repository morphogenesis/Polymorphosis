package core;

import controlP5.*;
import graph.GraphEditor;
import graph.GraphObject;
import graph.xml.Node;
import org.philhosoft.p8g.svg.P8gGraphicsSVG;
import phys.PhysicsSystem;
import processing.core.PVector;

import static core.App.*;


public class Gui extends Controllers {

	public static Knob objSize;
	private static Knob colorSlider;
	public static Knob capacitySlider;
	private static Group toolGroup;
	private static Group graphGroup;
	private static Group vorGroup;
	private static Group fileGroup;
	private static Group physGroup;
	public static Textfield objName;
	private static Accordion leftAccordion;
	private static Accordion rightAccordion;
	public static final int outlinerX = App.WIDTH - 200;
	public static boolean isEditMode = true;
	public static float cloudBhvStr = -1;
	public static float cloudBhvScl = 100;
	public static float cloudVecWgt = .5f;
	public static float cloudMinScl = 100;
	public static float cloudMinStr = -1;
	private static Group metaGroup;
	 public static Slider meta_visc;

	public static void controlEvent(App app, ControlEvent theEvent) {

		if (!theEvent.isGroup()) {
			float theValue = theEvent.getController().getValue();
			System.out.println(theEvent.getController().getName() + "=>" + theValue);
			switch (theEvent.getController().getName()) {
				/** File */
				case "file_quit": System.out.println("[quit]"); app.exit(); break;
				case "file_open": GraphObject.rebuild(); break;
				case "file_save": GraphObject.build(); break;
				case "file_print": app.beginRecord(P8gGraphicsSVG.SVG, "./out/svg/print-###.svg"); App.RECORDING = true; break;
			}
		}
	}
	public static void init() {
		initCP5();
		project_settings();
		object_properties();
		leftAccordion = CP5.addAccordion("leftAccordion").setPosition(0, 0);
		rightAccordion = CP5.addAccordion("rightAccordion").setPosition(App.WIDTH - 220, 0);
		styleSliders();
		styleButtons();
		styleKnobs();
		styleTextfields();
		styleToggles();
		styleAccordions();
		leftAccordion.addItem(physGroup).addItem(metaGroup)/*.addItem(graphGroup)*/.addItem(vorGroup).open();
		rightAccordion.addItem(toolGroup);
		CP5.update();
	}

	private static void project_settings() {

		physGroup = CP5.addGroup("physics_group");
		CP5.begin(10, 12);
		CP5.addSlider("WORLD_SCALE").plugTo(PHYSICS).setGroup(physGroup).setValue(App.WORLD_SCALE).setRange(1, 20).setDecimalPrecision(0).linebreak();
		CP5.addSlider("phys_drag").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_drag).setRange(.1f, 1).setDecimalPrecision(2).linebreak();
		CP5.addSlider("phys_vec_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_vec_scale).setRange(.5f, 2).setDecimalPrecision(1).linebreak();
		CP5.addSlider("phys_vec_wght").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_vec_wght).setRange(.1f, 3).setDecimalPrecision(1).linebreak();
		CP5.addSlider("phys_spr_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_spr_scale).setRange(.5f, 2).setDecimalPrecision(1).linebreak();
		CP5.addSlider("phys_spr_strength").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_spr_strength).setRange(.001f, .05f).setDecimalPrecision(3).linebreak();
		CP5.addSlider("phys_bhavior_str").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_bhavior_str).setRange(0, 3).setDecimalPrecision(1).linebreak();
		CP5.addSlider("phys_bhavior_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_bhavior_scale).setRange(-3f, 0).setDecimalPrecision(2).linebreak();
		CP5.addSlider("phys_mindist_str").plugTo(PHYSICS).setGroup(physGroup).setValue(PhysicsSystem.phys_mindist_str).setRange(.001f, .05f).setDecimalPrecision(2).linebreak();
		CP5.end();
		styleGroup(physGroup, 9);

		metaGroup = CP5.addGroup("metaball_group");
		CP5.begin(10, 12);
		meta_visc = CP5.addSlider("meta_viscosity").plugTo(METABALL).setGroup(metaGroup).setRange(1, 3).setValue(2).setDecimalPrecision(1).linebreak();
		CP5.addSlider("meta_threshold").plugTo(METABALL).setGroup(metaGroup).setRange(0.0001f, 0.001f).setValue(0.0006f).setDecimalPrecision(4).linebreak();
		CP5.addSlider("meta_resolution").plugTo(METABALL).setGroup(metaGroup).setValue(40).setRange(5, 100).setDecimalPrecision(0).linebreak();
		CP5.addSlider("meta_maxSteps").plugTo(METABALL).setGroup(metaGroup).setValue(400).setRange(200, 800).setDecimalPrecision(0).linebreak();
		CP5.addSlider("draw_vor_offset").plugTo(VORONOI).setGroup(metaGroup).setValue(-2).setRange(-10, 10).setDecimalPrecision(0).linebreak();
		CP5.end();
		styleGroup(metaGroup, 4);
		styleSliders();

		vorGroup = CP5.addGroup("voronoi_group");
		CP5.addTextlabel("voronoi_label").setPosition(10,12).setText("Voronoi Settings").setGroup(vorGroup).linebreak();
		CP5.begin(10, 24);
		CP5.addToggle("update_voronoi").plugTo(VORONOI).setGroup(vorGroup).setValue(true).linebreak();
		CP5.addToggle("draw_voronoi").plugTo(VORONOI).setGroup(vorGroup).setValue(true).linebreak();
		CP5.addToggle("draw_vor_poly").plugTo(VORONOI).setGroup(vorGroup).setValue(true).linebreak();
		CP5.addToggle("draw_vor_bez").plugTo(VORONOI).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_vor_vec").plugTo(VORONOI).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_vor_info").plugTo(VORONOI).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_vor_metaball").plugTo(VORONOI).setGroup(vorGroup).setValue(true).linebreak();
		CP5.addToggle("draw_vor_offset").plugTo(VORONOI).setGroup(vorGroup).setValue(true).linebreak();
		CP5.end();
		CP5.addTextlabel("graph_label").setPosition(115,12).setText("Graph Settings").setGroup(vorGroup).linebreak();
		CP5.begin(125, 24);
		CP5.addToggle("update_graph").plugTo(GRAPH).setValue(true).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_graph").plugTo(GRAPH).setValue(true).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_graph_list").plugTo(GRAPH).setValue(true).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_graph_nodes").plugTo(GRAPH).setValue(true).setGroup(vorGroup).linebreak();
		CP5.addToggle("draw_graph_edges").plugTo(GRAPH).setValue(true).setGroup(vorGroup).linebreak();
		CP5.end();

		styleTextlabel();
		styleGroup(vorGroup, 9);
		styleToggles();

//		graphGroup = CP5.addGroup("graph_group");
//		CP5.begin(10, 12);

//		CP5.end();
//		styleGroup(graphGroup, 5);

		fileGroup = CP5.addGroup("fileGroup").setPosition(220, 22).setWidth(100).setAbsolutePosition(new PVector(0, 0, 0));
		CP5.begin(0, 0);
		CP5.addButton("file_quit").setCaptionLabel("Quit").setGroup(fileGroup).linebreak();
		CP5.addButton("file_open").setCaptionLabel("Open XML").setGroup(fileGroup).linebreak();
		CP5.addButton("file_save").setCaptionLabel("Save XML").setGroup(fileGroup).linebreak();
		CP5.addButton("file_print").setCaptionLabel("Print SVG").setGroup(fileGroup).linebreak();
		CP5.addButton("file_loadDef").setCaptionLabel("Load Defaults").setGroup(fileGroup).linebreak();
		CP5.addButton("file_saveDef").setCaptionLabel("Save Defaults").setGroup(fileGroup).linebreak();
		CP5.end();
		styleGroup(fileGroup, 6);
		styleButtons();
	}

	private static void object_properties() {
		objName = CP5.addTextfield("setName").setCaptionLabel("Name").addListener(new nameTextfieldListener()).setPosition(0, 0).setStringValue("untitled").hide();
		toolGroup = CP5.addGroup("tool_group").setBackgroundHeight(140).setPosition(0, 0);
		CP5.begin(0, 0);
		objSize = CP5.addKnob("setSize").setCaptionLabel("Size").addListener(new sizeSliderListener()).setRange(0, 500).setValue(50).setPosition(10, 30).setDecimalPrecision(1).setGroup(toolGroup).hide();
		colorSlider = CP5.addKnob("setType").setCaptionLabel("Type").addListener(new colorSliderListener()).setPosition(80, 30).setRange(0, 6).setValue(3).setDecimalPrecision(0).setGroup(toolGroup).hide();
		capacitySlider = CP5.addKnob("setCapacity").setCaptionLabel("Capacity").addListener(new capacitySliderListener()).setPosition(150, 30).setRange(1, 200).setValue(1).setDecimalPrecision(0).setGroup(toolGroup).hide();
		CP5.end();
	}

	static void toggleObjProperties() {
		if (App.GRAPH.hasActiveNode()) {
			objSize.setValue(GraphEditor.activeNode.size);
			colorSlider.setValue(GraphEditor.activeNode.getType());
			capacitySlider.setValue(GraphEditor.activeNode.getOccupancy());
			objName.setValue(GraphEditor.activeNode.getName());
			objSize.show();
			colorSlider.show();
			capacitySlider.show();
			objName.show();
			objName.setPosition(outlinerX, 50 + GraphEditor.activeNode.getId() * 14);
		}
		else {
			objSize.hide();
			colorSlider.hide();
			capacitySlider.hide();
			objName.hide();
		}
	}

	static class nameTextfieldListener implements ControlListener {

		String name;
		public void controlEvent(ControlEvent e) { name = e.getController().getStringValue(); if (App.GRAPH.hasActiveNode()) GraphEditor.activeNode.setName(name); }
	}

	static class colorSliderListener implements ControlListener {

		float color;
		public void controlEvent(ControlEvent e) {
			color = e.getController().getValue();
			if (App.CP5.isMouseOver()) { if (App.GRAPH.hasActiveNode()) GraphEditor.activeNode.setType((int) color); for (Node n : App.GRAPH.selectedNodes) {n.setType((int) color);} }
		}
	}

	static class capacitySliderListener implements ControlListener {

		float capacity;
		public void controlEvent(ControlEvent e) {
//			capacity = e.getController().getValue(); if (App.CP5.isMouseOver()) { for (Node g : Editor.selectedNodes) {g.setOccupancy((int) capacity);} }
		}
	}

	static class sizeSliderListener implements ControlListener {

		float size;
		public void controlEvent(ControlEvent e) {
			size = e.getController().getValue();
			if (App.CP5.isMouseOver()) {App.GRAPH.editSize(size); }
		}
	}
}
