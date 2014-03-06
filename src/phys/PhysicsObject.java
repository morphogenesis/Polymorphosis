package phys;

import core.App;
import toxi.geom.Rect;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 3/5/14.
 */
public class PhysicsObject extends GraphicsObject {

	public static final VerletPhysics2D physics = new VerletPhysics2D();
	public static Rect bounds;
	public static ArrayList<AttractionBehavior2D> attractors = new ArrayList<>();
	public static ArrayList<VerletParticle2D> attractorParticles = new ArrayList<>();
	public static ArrayList<VerletSpring2D> attractorMin = new ArrayList<>();
	protected final List<VerletSpring2D> springs = new ArrayList<>();
	protected final List<VerletSpring2D> mindists = new ArrayList<>();
	protected final List<AttractionBehavior2D> behaviors = new ArrayList<>();
	protected final ArrayList<VerletParticle2D> particles = new ArrayList<>();
	public PhysicsObject(App $p5) {super($p5);}
}
