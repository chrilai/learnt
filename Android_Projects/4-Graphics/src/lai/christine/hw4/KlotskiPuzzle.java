package lai.christine.hw4;

import lai.christine.hw4.Thing.Type;
import android.graphics.drawable.Drawable;

public class KlotskiPuzzle {
	
	private static final int GRID_WIDTH = 4;
	private static final int GRID_HEIGHT = 5;
	private static final int EXIT_I = 3;
	private static final int EXIT_J = 1;
	private boolean solved;
	private int numMoves;
	private float shapeSize;
	private Thing[][] grid;
	public static enum Direction {
		Left, Up, Right, Down;
	}
	
	public KlotskiPuzzle(float width, float height, float shapeSize, 
						 Drawable shape1x1, Drawable shape1x2, Drawable shape2x1, Drawable shape2x2) {
		solved = false;
		numMoves = 0;
		grid = new Thing[GRID_HEIGHT][GRID_WIDTH];
		this.shapeSize = shapeSize;
		grid[0][0] = new Thing(Type.OneByTwo);
		grid[0][0].getBounds().set(0, 0, shape1x2.getIntrinsicWidth(), shape1x2.getIntrinsicHeight());
		grid[0][0].getBounds().offsetTo(width/2-shapeSize*2, (float) (height/2-shapeSize*2.5));
		grid[0][1] = new Thing(Type.TwoByTwo);
		grid[0][1].getBounds().set(0, 0, shape2x2.getIntrinsicWidth(), shape2x2.getIntrinsicHeight());
		grid[0][1].getBounds().offsetTo(width/2-shapeSize, (float) (height/2-shapeSize*2.5));
		grid[0][3] = new Thing(Type.OneByTwo);
		grid[0][3].getBounds().set(0, 0, shape1x2.getIntrinsicWidth(), shape1x2.getIntrinsicHeight());
		grid[0][3].getBounds().offsetTo(width/2+shapeSize, (float) (height/2-shapeSize*2.5));
		grid[2][0] = new Thing(Type.OneByTwo);
		grid[2][0].getBounds().set(0, 0, shape1x2.getIntrinsicWidth(), shape1x2.getIntrinsicHeight());
		grid[2][0].getBounds().offsetTo(width/2-shapeSize*2, (float) (height/2-shapeSize*0.5));
		grid[2][1] = new Thing(Type.TwoByOne);
		grid[2][1].getBounds().set(0, 0, shape2x1.getIntrinsicWidth(), shape2x1.getIntrinsicHeight());
		grid[2][1].getBounds().offsetTo(width/2-shapeSize, (float) (height/2-shapeSize*0.5));
		grid[2][3] = new Thing(Type.OneByTwo);
		grid[2][3].getBounds().set(0, 0, shape1x2.getIntrinsicWidth(), shape1x2.getIntrinsicHeight());
		grid[2][3].getBounds().offsetTo(width/2+shapeSize, (float) (height/2-shapeSize*0.5));
		grid[3][1] = new Thing(Type.OneByOne);
		grid[3][1].getBounds().set(0, 0, shape1x1.getIntrinsicWidth(), shape1x1.getIntrinsicHeight());
		grid[3][1].getBounds().offsetTo(width/2-shapeSize, (float) (height/2+shapeSize*0.5));
		grid[3][2] = new Thing(Type.OneByOne);
		grid[3][2].getBounds().set(0, 0, shape1x1.getIntrinsicWidth(), shape1x1.getIntrinsicHeight());
		grid[3][2].getBounds().offsetTo(width/2, (float) (height/2+shapeSize*0.5));
		grid[4][0] = new Thing(Type.OneByOne);
		grid[4][0].getBounds().set(0, 0, shape1x1.getIntrinsicWidth(), shape1x1.getIntrinsicHeight());
		grid[4][0].getBounds().offsetTo(width/2-shapeSize*2, (float) (height/2+shapeSize*1.5));
		grid[4][3] = new Thing(Type.OneByOne);
		grid[4][3].getBounds().set(0, 0, shape1x1.getIntrinsicWidth(), shape1x1.getIntrinsicHeight());
		grid[4][3].getBounds().offsetTo(width/2+shapeSize, (float) (height/2+shapeSize*1.5));
	}
	
	public int[] moveThingTo(int i, int j, float x, float y) {
		Thing thing = getThing(i, j);
		if (thing != null) {
			Type type = thing.getType();
			if (x < thing.getBounds().left && j > 0) {
				switch(type) {
					case OneByOne:
					case TwoByOne:
						if (isEmpty(i,j-1)) {
							return moveThing(i, j, thing, Direction.Left);
						}
						break;
					case OneByTwo:
					case TwoByTwo:
						if (isEmpty(i,j-1) && isEmpty(i+1,j-1)) {
							return moveThing(i, j, thing, Direction.Left);
						}
						break;
					default:
						break;
				}
			} else if (x > thing.getBounds().right && j < GRID_WIDTH-1) {
				switch(type) {
					case OneByOne:
						if (isEmpty(i,j+1)) {
							return moveThing(i, j, thing, Direction.Right);
						}
						break;
					case TwoByOne:
						if (j < GRID_WIDTH-2) {
							if (isEmpty(i,j+2)) {
								return moveThing(i, j, thing, Direction.Right);
							}
						}
						break;
					case OneByTwo:
						if (isEmpty(i,j+1) && isEmpty(i+1,j+1)) {
							return moveThing(i, j, thing, Direction.Right);
						}
						break;
					case TwoByTwo:
						if (j < GRID_WIDTH-2) {
							if (isEmpty(i,j+2) && isEmpty(i+1,j+2)) {
								return moveThing(i, j, thing, Direction.Right);
							}
						}
						break;
					default:
						break;
				}
			} else if (y < thing.getBounds().top && i > 0) {
				switch(type) {
					case OneByOne:
					case OneByTwo:
						if (isEmpty(i-1,j)) {
							return moveThing(i, j, thing, Direction.Up);
						}
						break;
					case TwoByOne:
					case TwoByTwo:
						if (isEmpty(i-1,j) && isEmpty(i-1,j+1)) {
							return moveThing(i, j, thing, Direction.Up);
						}
						break;
					default:
						break;
				}
			} else if (y > thing.getBounds().bottom && i < GRID_HEIGHT-1) {
				switch(type) {
					case OneByOne:
						if (isEmpty(i+1,j)) {
							return moveThing(i, j, thing, Direction.Down);
						}
						break;
					case OneByTwo:
						if (i < GRID_HEIGHT-2) {
							if (isEmpty(i+2,j)) {
								return moveThing(i, j, thing, Direction.Down);
							}
						}
						break;
					case TwoByOne:
						if (isEmpty(i+1,j) && isEmpty(i+1,j+1)) {
							return moveThing(i, j, thing, Direction.Down);
						}
						break;
					case TwoByTwo:
						if (i == EXIT_I && j == EXIT_J) {
							solved = true;
							return new int[]{i, j};
						} else if (i < GRID_HEIGHT-2) {
							if (isEmpty(i+2,j) && isEmpty(i+2,j+1)) {
								return moveThing(i, j, thing, Direction.Down);
							}
						}
						break;
				default:
					break;
				}
			}
		}
		return new int[]{i, j};
	}
	
	private int[] moveThing(int i, int j, Thing thing, Direction direction) {
		switch(direction) {
			case Left:
				thing.getBounds().offset(-shapeSize, 0);
				grid[i][j-1] = thing;
				grid[i][j] = null;
				numMoves++;
				return new int[]{i, j-1};
			case Right:
				thing.getBounds().offset(+shapeSize, 0);
				grid[i][j+1] = thing;
				grid[i][j] = null;
				numMoves++;
				return new int[]{i, j+1};
			case Up:
				thing.getBounds().offset(0, -shapeSize);
				grid[i-1][j] = thing;
				grid[i][j] = null;
				numMoves++;
				return new int[]{i-1, j};
			case Down:
				thing.getBounds().offset(0, +shapeSize);
				grid[i+1][j] = thing;
				grid[i][j] = null;
				numMoves++;
				return new int[]{i+1, j};
			default:
				return new int[]{i, j};
		}
	}
	
	private boolean isEmpty(int i, int j) {
		return !notEmpty()[i][j];
	}
	
	private boolean[][] notEmpty() {
		boolean[][] filled = new boolean[GRID_HEIGHT][GRID_WIDTH];
		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				Thing thing = getThing(i, j);
				if (thing != null) {
					Type type = thing.getType();
					switch(type) {
						case OneByOne:
							filled[i][j] = true;
							break;
						case OneByTwo:
							filled[i][j] = true;
							filled[i+1][j] = true;
							break;
						case TwoByOne:
							filled[i][j] = true;
							filled[i][j+1] = true;
							break;
						case TwoByTwo:
							filled[i][j] = true;
							filled[i+1][j] = true;
							filled[i][j+1] = true;
							filled[i+1][j+1] = true;
							break;
						default:
							break;
					}
				}
			}
		}
		return filled;
	}
	
	public boolean getSolved() {
		return solved;
	}
	public int getNumMoves() {
		return numMoves;
	}
	public Thing getThing(int i, int j) {
		return grid[i][j];
	}
	public Thing[][] getThings() {
		return grid;
	}

}
