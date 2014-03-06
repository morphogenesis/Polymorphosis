package core;

import processing.core.PConstants;

/**
 * Created on 3/5/14.
 */
public class UserInterface{

	private final App p5;
	private float fps = 0;

	public UserInterface(App p5){
		this.p5 = p5;
	}
	public void draw(){
		drawStatusBar();

	}
	private void drawStatusBar() {
		float x = 20;
		float y = App.HEIGHT - 6;
		if (p5.frameCount % 60 == 0) {fps = p5.frameRate;}

		p5.fill(0xff1d1d1d);
		p5.rect(0, y - 12, App.WIDTH, 20);
		p5.fill(0xffffffff);
		p5.noStroke();
		p5.textAlign(PConstants.LEFT);
		p5.text("FPS: " + (int) fps, x, y);
		p5.textAlign(PConstants.CENTER);

		if (Gui.isEditMode) { p5.text("EDIT MODE", x + 100, y); }
		if (App.isShiftDown) {p5.text("SHIFT", x + 200, y);}
	}
}
