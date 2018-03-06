package lai.christine.hw5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class MazeView extends View {
	
	private int shapeSize = (int) getResources().getDimension(R.dimen.shape_size);
	private int gridSize = (int) getResources().getDimension(R.dimen.grid_size);
	private int strokeSize = (int) getResources().getDimension(R.dimen.stroke_size);
	private int textSize = (int) getResources().getDimension(R.dimen.text_size);
	private Drawable circle = getResources().getDrawable(R.drawable.circle);
	private Drawable coin = getResources().getDrawable(R.drawable.coin);
	private int width;
	private int height;
	private Rect bounds = new Rect();
	private RectF boundsF = new RectF();
	private Paint paint;
	private Maze maze;
	private float x, y, vx, vy, ax, ay;
	
	private Handler handler = new Handler();
	private Runnable invalidator = new Runnable() {
		@Override public void run() {
			invalidate();
		}
	};
	private AnimationThread animationThread;

	public MazeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MazeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MazeView(Context context) {
		super(context);
	}
	
	private void init() {
		width = getWidth();
		height = getHeight();
		paint = new Paint();
		maze = new Maze(getContext(), width, height, shapeSize, gridSize);
		x = (float)(width/2-gridSize*(5-maze.getStartX())+(gridSize-shapeSize)/2); 
		y = (float)(height/2-gridSize*(5-maze.getStartY())+(gridSize-shapeSize)/2);
	}
	
	public void start() {
		stop();
		animationThread = new AnimationThread();
		animationThread.start();
	}
	public void stop() {
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}
	}
	public void accelerate(float ax, float ay) {
		this.ax = ax;
		this.ay = ay;
	}
	public void rotate(float degree) {
		if (maze != null) {
			maze.setDegree(degree);
		}
	}
	private class AnimationThread extends Thread {
		@Override public void run() {
			while (!isInterrupted()) {

				if (maze != null) {
					
					// move
					float[] newXY = maze.move(x, y, vx, vy);
					// decelerate at bounds
					if (Math.abs(x-newXY[0]) < vx) {
						ax = 0;
						ay = 0;
						vx = -vx/4;
						vy = vy/2;
					} else {
						vx += ax;
					}
					if (Math.abs(y-newXY[1]) < vy) {
						ax = 0;
						ay = 0;
						vx = vx/2;
						vy = -vy/4;
					} else {
						vy += ay;
					}
					
					x = newXY[0];
					y = newXY[1];
				}
				
				handler.post(invalidator);
				try {
					sleep(35);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (paint == null) {
			init();
		}
		canvas.drawColor(Color.BLACK);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(strokeSize/2);
		boolean[][] gridX = maze.getGridX();
		boolean[][] gridY = maze.getGridY();
		for (int i = 0; i < gridX.length; i++) {
			for (int j = 0; j < gridX[i].length; j++) {
				if (gridX[i][j]) {
					canvas.drawLine((float)(width/2-gridSize*(4-j)), (float)(height/2-gridSize*(5-i)), 
									(float)(width/2-gridSize*(4-j)), (float)(height/2-gridSize*(4-i)), paint);
				}
			}
		}
		for (int i = 0; i < gridY.length; i++) {
			for (int j = 0; j < gridY[i].length; j++) {
				if (gridY[i][j]) {
					canvas.drawLine((float)(width/2-gridSize*(5-j)), (float)(height/2-gridSize*(4-i)), 
									(float)(width/2-gridSize*(4-j)), (float)(height/2-gridSize*(4-i)), paint);
				}
			}
		}
		paint.setStrokeWidth(strokeSize*4);
		paint.setColor(Color.RED);
		boolean[][] traps = maze.getTraps();
		for (int i = 0; i < traps.length; i++) {
			for (int j = 0; j < traps[i].length; j++) {
				if (traps[i][j] == true) {
					drawEmptyRect(canvas, paint, 
							(float)(width/2-gridSize*(4.8-j)), (float)(height/2-gridSize*(4.8-i)),
							(float)(width/2-gridSize*(4.2-j)), (float)(height/2-gridSize*(4.2-i)));
				}
			}
		}
		paint.setColor(Color.YELLOW);
		boolean[][] coins = maze.getCoins();
		for (int i = 0; i < coins.length; i++) {
			for (int j = 0; j < coins[i].length; j++) {
				if (coins[i][j] == true) {
					boundsF.set((float)(width/2-gridSize*(4.8-j)), (float)(height/2-gridSize*(4.8-i)),
								(float)(width/2-gridSize*(4.2-j)), (float)(height/2-gridSize*(4.2-i)));
					boundsF.round(bounds);
					coin.setBounds(bounds);
					coin.draw(canvas);
				}
			}
		}
		drawEmptyRect(canvas, paint, 
				  	  (float)(width/2-gridSize*(4.8-maze.getExitX())), (float)(height/2-gridSize*(4.8-maze.getExitY())),
				  	  (float)(width/2-gridSize*(4.2-maze.getExitX())), (float)(height/2-gridSize*(4.2-maze.getExitY())));
		paint.setStrokeWidth(strokeSize);
		paint.setColor(Color.WHITE);
		drawEmptyRect(canvas, paint, 
					  (float)(width/2-gridSize*5), (float)(height/2-gridSize*5),
					  (float)(width/2+gridSize*5), (float)(height/2+gridSize*5));
		paint.setTextSize(textSize);
		canvas.drawText("Coins: " + maze.getNumCoins(), 
						(float)(width/2-gridSize*1.5), (float)(height/2-gridSize*5.8), 
						paint);
		canvas.drawText("Times Trapped: " + maze.getNumTrapped(), 
						(float)(width/2-gridSize*3), (float)(height/2+gridSize*6.2), 
						paint);
		if (maze.getSolved()) {
			paint.setTextSize(textSize);
			paint.setColor(Color.GREEN);
			paint.setTextSize(height/10);
			canvas.drawText("ESCAPED!", width/4, height/2, paint);
		} else {
			boundsF.set(x, y, x+shapeSize, y+shapeSize);
			boundsF.round(bounds);
			circle.setBounds(bounds);
			circle.draw(canvas);
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