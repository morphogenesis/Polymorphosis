package core;

import controlP5.*;
import processing.core.PVector;
import util.Color;

import static core.App.CP5;


/**
 * Created on 3/5/14.
 */
public abstract class Controllers {

	protected static void initCP5() {
		CP5.enableShortcuts();
		CP5.setAutoDraw(false);
		CP5.setFont(App.pfont, 8);
		CP5.setAutoSpacing(4, 4);
		CP5.setColorBackground(Color.CP5_BG).setColorForeground(Color.CP5_FG).setColorActive(Color.CP5_ACT);
		CP5.setColorCaptionLabel(Color.CP5_CAP).setColorValueLabel(Color.CP5_VAL);
	}
	protected static void styleTextlabel() {
		for (Textlabel t : CP5.getAll(Textlabel.class)) {
			t.setSize(115, 26);
			t.setLineHeight(22);
			t.setFont(App.bfont);
		}
	}

	protected static void styleButtons() {
		for (Button b : CP5.getAll(Button.class)) {
			b.setSize(100, 24);
			b.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setFont(App.pfont);
			b.setColorCaptionLabel(0xffffffff);
		}
	}
	protected static void styleSliders() {
		for (Slider s : CP5.getAll(Slider.class)) {
			s.setSize(200, 10);
			s.showTickMarks(false);
//			s.setHandleSize(20);
			s.setSliderMode(Slider.FLEXIBLE);
			s.getCaptionLabel().setLineHeight(10);
			s.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
			s.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER);
//			s.getCaptionLabel().getStyle().setPaddingRight(4);
//			s.getCaptionLabel().getStyle().setPaddingTop(2);
			s.getCaptionLabel().disableColorBackground();
//			s.setSliderMode()
		}
	}
	protected static void styleKnobs() {
		for (Knob k : CP5.getAll(Knob.class)) {
			k.setRadius(30);
			k.setDragDirection(Knob.HORIZONTAL);
		}
	}
	protected static void styleGroup(Group g, int numItems) {
		g.setBackgroundColor(Color.CP5_GRP);
		g.setBarHeight(22);
		g.setBackgroundHeight(groupHeight(numItems));
		g.getCaptionLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
	}
	protected static void styleTextfields() {
		for (Textfield t : CP5.getAll(Textfield.class)) {
			t.setSize(120, 12);
			t.setAutoClear(false);
			t.getCaptionLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).getStyle().setPaddingTop(4);
			t.setColor(0xff000000);
			t.setColorBackground(0xffffffff);
			t.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
			t.setColorForeground(0xffffffff);
			t.getCaptionLabel().hide();
		}
	}
	protected static void styleAccordions() {
		for (Accordion a : CP5.getAll(Accordion.class)) {
			a.setWidth(220).setCollapseMode(Accordion.MULTI);
		}
	}
	protected static int groupHeight(int numControllers) {
		int cHeight = 20;
		return (numControllers * cHeight) + cHeight;
	}
	protected static void styleToggles() {
		int i = 0;
		for (Toggle t : CP5.getAll(Toggle.class)) {
			i++;
			t.setMode(ControlP5.DEFAULT);
			t.setHeight(10); t.setWidth(10); t.setAbsolutePosition(new PVector(0, 0, 0));
//			t.getCaptionLabel().setHeight(11);
			Toggle.autoWidth = 115;/*Toggle..autoHeight=5;*/
			t.setColorCaptionLabel(0xffcccccc);
			t.setColorActive(Color.CP5_FG); t.setColorForeground(0xff444444); t.setColorBackground(0xff666666);
			t.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setFont(App.pfont).getStyle().setPaddingLeft(4);
			t.updateSize();
			if (i % 2 == 0) {
				t.linebreak();
			}
		}
	}
}
/*
	protected static void styleBangs() {
		for (Bang b : CP5.getAll(Bang.class)) {
			b.setSize(100, 14);
			b.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setFont(App.pfont);
		}
	}
	protected static void styleDropdown(DropdownList dl) {
//		dl.setBackgroundColor(color(190));
		dl.setItemHeight(22);
		dl.setBarHeight(28);
		dl.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setFont(App.pfont);
//		dl.actAsPulldownMenu(true);
		dl.setScrollbarVisible(false);
		dl.setPosition(220, 28).setWidth(120);
	}
*/
