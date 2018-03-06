package lai.christine.hw4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class KlotskiView extends View {
	
	private int shapeSize = (int) getResources().getDimension(R.dimen.shape_size_1);
	private int strokeSize = (int) getResources().getDimension(R.dimen.stroke_size);
	private int textSize = (int) getResources().getDimension(R.dimen.text_size);
	private Drawable shape1x1 = getResources().getDrawable(R.drawable.shape_1x1);
	private Drawable shape1x2 = getResources().getDrawable(R.drawable.shape_1x2);
	private Drawable shape2x1 = getResources().getDrawable(R.drawable.shape_2x1);
	private Drawable shape2x2 = getResources().getDrawable(R.drawable.shape_2x2);
	private int width;
	private int height;
	private Rect rect;
	private Paint paint;
	private KlotskiPuzzle puzzle;
	private int[] selected;
	

	public KlotskiView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public KlotskiView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KlotskiView(Context context) {
		super(context);
	}
	
	private void init() {
		width = getWidth();
		height = getHeight();
		rect = new Rect();
		paint = new Paint();
		puzzle = new KlotskiPuzzle(width, height, getResources().getDimension(R.dimen.shape_size_1),
								   shape1x1, shape1x2, shape2x1, shape2x2);
	}
	
//  Use for ScrollView
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int height = (int)Math.round(420*getResources().getDisplayMetrics().density);
//		setMeasuredDimension(widthMeasureSpec, height);
//	}
	
	@Override public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				selected = findThingLocation(event.getX(), event.getY());
				invalidate();
				break;
			
			case MotionEvent.ACTION_MOVE:
				if (selected != null) {
					selected = puzzle.moveThingTo(selected[0], selected[1], event.getX(), event.getY());
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				selected = null;
				invalidate();
				break;
			default:
				break;
		}
		return true;
	}
	
	private int[] findThingLocation(float x, float y) {
		Thing[][] things = puzzle.getThings();
		for(int i = 0; i < things.length; i++) {
			for (int j = 0; j < things[0].length; j++) {
				if (things[i][j] != null) {
					if (things[i][j].getBounds().contains(x, y)) {
						return new int[] {i, j};
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (paint == null) {
			init();
			paint.setColor(Color.WHITE);
		}
		canvas.drawColor(Color.BLACK);
		for(Thing[] thingRow : puzzle.getThings()) {
			for (Thing thing : thingRow) {
				if (thing != null) {
					switch(thing.getType()) {
						case OneByOne:
							thing.getBounds().round(rect);
							shape1x1.setBounds(rect);
							shape1x1.draw(canvas);
							break;
						case OneByTwo:
							thing.getBounds().round(rect);
							shape1x2.setBounds(rect);
							shape1x2.draw(canvas);
							break;
						case TwoByOne:
							thing.getBounds().round(rect);
							shape2x1.setBounds(rect);
							shape2x1.draw(canvas);
							break;
						case TwoByTwo:
							thing.getBounds().round(rect);
							shape2x2.setBounds(rect);
							shape2x2.draw(canvas);
							break;
						default:
							break;
					}
				}
			}
		}
		paint.setStrokeWidth(strokeSize/2);
		paint.setColor(Color.WHITE);
		drawEmptyRect(canvas, paint, 
					  width/2-shapeSize*2, (float)(height/2-shapeSize*2.5),
					  width/2+shapeSize*2, (float)(height/2+shapeSize*2.5));
		paint.setStrokeWidth(strokeSize*2);
		paint.setColor(Color.YELLOW);
		canvas.drawLine(width/2-shapeSize, (float)(height/2+shapeSize*2.5), 
						width/2+shapeSize, (float)(height/2+shapeSize*2.5), paint);
		paint.setTextSize(textSize);
		canvas.drawText("Moves: " + puzzle.getNumMoves(), width/2, height/2-(float)(shapeSize*2.6), paint);
		if (selected != null) {
			paint.setColor(Color.RED);
			RectF bounds = puzzle.getThing(selected[0], selected[1]).getBounds();
			drawEmptyRect(canvas, paint, bounds.left, bounds.top, bounds.right, bounds.bottom);
		}
		if (puzzle.getSolved()) {
			paint.setColor(Color.BLUE);
			paint.setTextSize(height/10);
			canvas.drawText("SOLVED!", width/4, height/2, paint);
		}
	}
	
	private void drawEmptyRect(Canvas canvas, Paint paint,
							   float left, float top, float right, float bottom) {
		paint.setStrokeWidth(strokeSize);
		canvas.drawLine(left, top, right, top, paint);
		canvas.drawLine(left, bottom, right, bottom, paint);
		canvas.drawLine(left, top, left, bottom, paint);
		canvas.drawLine(right, top, right, bottom, paint);
	}
}