package lai.christine.hw4;

import android.graphics.RectF;

public class Thing {
	private RectF bounds = new RectF();
	private Type type;
	public static enum Type {
		OneByOne, OneByTwo, TwoByOne, TwoByTwo;
	}
	public Thing(Type type) {
		this.type = type;
	}
	public RectF getBounds() {
		return bounds;
	}
	public Type getType() {
		return type;
	}
}
