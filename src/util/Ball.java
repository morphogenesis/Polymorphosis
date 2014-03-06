package util;

public class Ball {

	public static int MIN_STRENGTH = 1;
	public static float MAX_STRENGTH = 200;
	private Vector2D _position;
	private float _strength;
	public boolean tracking;
	public Vector2D edge;
	public Vector2D direction;

	public Ball(Vector2D position, float strength) {
		_position = position.clone();
		_strength = strength;
		tracking = false;
		edge = position.clone();
		direction = new Vector2D((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
	}
	public Ball(float x, float y, float strength) {
		this(new Vector2D(x, y), strength);
	}
	public Vector2D position() {
		return _position;
	}
	public void position(Vector2D value) {
		_position.copy(value);
	}
	public float strengthAt(Vector2D v, float c) {
		float div = (float) Math.pow(Vector2D.subtract(_position, v).getLengthSq(), c * 0.5f);
		return (div != 0) ? (_strength / div) : 10000;
	}
	public float strength() {
		return _strength;
	}
	public void strength(float value) {
		_strength = MathUtil.clamp(value, MIN_STRENGTH, MAX_STRENGTH);
	}
	public void overrideStrength(float increment){
		this._strength+=increment;
	}
	public String toString() {
		return "[object core.Ball][position=" + position() + "][size=" + strength() + "]";
	}
}
