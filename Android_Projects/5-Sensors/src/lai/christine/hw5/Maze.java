package lai.christine.hw5;

import android.content.Context;
import android.os.Vibrator;
//import android.util.Log;

public class Maze {
	
	private Vibrator vibrator;
	private static final int GRID_WIDTH = 10;
	private static final int GRID_HEIGHT = 10;
	private static final int START_I = 0;
	private static final int START_J = 0;
	private static final int EXIT_I = 9;
	private static final int EXIT_J = 9;
	private boolean solved;
	private boolean trapped;
	private int numCoins;
	private int numTrapped;
	private float width;
	private float height;
	private float shapeSize;
	private float gridSize;
	private boolean[][] gridX, gridY;
	private boolean[][] coins;
	private boolean[][] traps;
	private float degree;
	private float trappedDegrees;
	private int trappedI, trappedJ;
	
	public Maze(Context context, float width, float height, float shapeSize, float gridSize) {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		solved = false;
		trapped = false;
		numCoins = 0;
		numTrapped = 0;
		this.width = width;
		this.height = height;
		this.shapeSize = shapeSize;
		this.gridSize = gridSize;
		gridX = new boolean[GRID_HEIGHT][GRID_WIDTH];
		gridY = new boolean[GRID_HEIGHT][GRID_WIDTH];
		coins = new boolean[GRID_HEIGHT][GRID_WIDTH];
		traps = new boolean[GRID_HEIGHT][GRID_WIDTH];
		degree = 0f;
		trappedDegrees = 0f;
		for (int i = 0; i < GRID_HEIGHT; i++) {
			if (i != 0 && i != 1 && i != 2 &&  i != 3 && i != 5 && i != 6 && i != 8 && i != 9) { gridX[i][0] = true; }
			if (i != 0 && i != 1 && i != 2 && i != 3 && i != 5 && i != 6 && i != 9) { gridX[i][1] = true; }
			if (i != 0 && i != 2 && i != 3 && i != 8) { gridX[i][2] = true; }
			if (i != 0 && i != 1 && i != 3 && i != 4 && i != 6 && i != 7 && i != 8) { gridX[i][3] = true; }
			if (i != 0 && i != 1 && i != 2 && i != 4 && i != 6 && i != 7 && i != 8 && i != 9) { gridX[i][4] = true; }
			if (i != 1 && i != 3 && i != 4 && i != 8 && i != 9) { gridX[i][5] = true; }
			if (i != 2 && i != 3 && i != 5 && i != 7 && i != 8 && i != 9) {	gridX[i][6] = true; }
			if (i != 0 && i != 1 && i != 7 && i != 9) {	gridX[i][7] = true; }
			if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4 && i != 8 && i != 9) { gridX[i][8] = true; }
		}
		for (int j = 0; j < GRID_WIDTH; j++) {
			if (j != 2 && j != 6 && j != 7 && j != 9) { gridY[0][j] = true; }
			if (j != 0 && j != 1 && j != 3 && j != 4 && j != 5 && j != 6 && j != 7 && j != 8) { gridY[1][j] = true; }
			if (j != 8) { gridY[2][j] = true; }
			if (j != 0 && j != 1 && j != 2 && j != 3 && j != 6 && j != 7 && j != 8) { gridY[3][j] = true; }
			if (j != 0 && j != 1 && j != 2 && j != 3 && j != 5 && j != 7 && j != 8 && j != 9) {	gridY[4][j] = true; }
			if (j != 0 && j != 1 && j != 2 && j != 3 && j != 5 && j != 6 && j != 8 && j != 9) {	gridY[5][j] = true; }
			if (j != 0 && j != 1 && j != 2 && j != 3 && j != 5 && j != 6 && j != 7 && j != 9) {	gridY[6][j] = true; }
			if (j != 1 && j != 8 && j != 9) { gridY[7][j] = true; }
			if (j != 0 && j != 1 && j != 2 && j != 3 && j != 4 && j != 7) { gridY[8][j] = true; }
		}
		coins[0][3] = true;
		coins[0][4] = true;
		coins[1][0] = true;
		coins[2][5] = true;
		coins[0][7] = true;
		coins[0][9] = true;
		coins[3][9] = true;
		coins[5][8] = true;
		coins[7][3] = true;
		coins[7][4] = true;
		coins[3][4] = true;
		coins[3][0] = true;
		coins[4][0] = true;
		coins[6][2] = true;
		coins[6][0] = true;
		coins[7][0] = true;
		coins[9][0] = true;
		coins[9][3] = true;
		coins[9][5] = true;
		coins[9][6] = true;
		
		traps[0][5] = true;
		traps[0][6] = true;
		traps[0][8] = true;
		traps[2][0] = true;
		traps[2][4] = true;
		traps[2][9] = true;
		traps[6][8] = true;
		traps[6][7] = true;
		traps[3][5] = true;
		traps[7][5] = true;
		traps[5][0] = true;
		traps[4][1] = true;
		traps[7][2] = true;
		traps[8][0] = true;
		traps[9][4] = true;
	}
	
	public float[] move(float x, float y, float vx, float vy) {
		if (solved == true || trapped == true) {
			return new float[]{x, y};
		}
		float newX = x + vx;
		float newY = y + vy;
		// original coordinates
		int i = (int) Math.floor((y+shapeSize/2+gridSize*5-height/2)/gridSize);
		int j = (int) Math.floor((x+shapeSize/2+gridSize*5-width/2)/gridSize);
//		Log.d("grid", i + "," + j);
		// move along x-axis
		if (newX < x) {
			if (newX < width/2-gridSize*5) {
				newX = width/2-gridSize*5;
			} else if (j > 0 && gridX[i][j-1] && newX < width/2-gridSize*(5-j)) {
				newX = width/2-gridSize*(5-j);
			}
		} else if (newX > x) {
			if (newX > width/2+gridSize*5-shapeSize) {
				newX = width/2+gridSize*5-shapeSize;
			} else if (gridX[i][j] && newX > width/2-gridSize*(4-j)-shapeSize) {
				newX = width/2-gridSize*(4-j)-shapeSize;
			}
		}
		// move along y-axis
		if (newY < y) {
			if (newY < height/2-gridSize*5) {
				newY = height/2-gridSize*5;
			} else if (i > 0 && gridY[i-1][j] && newY < height/2-gridSize*(5-i)) {
				newY = height/2-gridSize*(5-i);
			}
		} else if (newY > y){
			if (newY > height/2+gridSize*5-shapeSize) {
				newY = height/2+gridSize*5-shapeSize;
			} else if (gridY[i][j] && newY > height/2-gridSize*(4-i)-shapeSize) {
				newY = height/2-gridSize*(4-i)-shapeSize;
			}
		}
		// new coordinates
		int newI = (int) Math.floor((newY+shapeSize/2+gridSize*5-height/2)/gridSize);
		int newJ = (int) Math.floor((newX+shapeSize/2+gridSize*5-width/2)/gridSize);
		if (coins[newI][newJ]) { // collect coins
			numCoins++;
			coins[newI][newJ] = false;
//			vibrator.vibrate(50); // causes sensor readings to go nuts
		} else if (traps[newI][newJ]) { // encounter trap
			numTrapped++;
			trapped = true;
			trappedDegrees = 0;
			trappedI = newI;
			trappedJ = newJ;
//			vibrator.vibrate(250); // causes sensor readings to go nuts
			return new float[]{(float)(width/2.0-gridSize*(5.0-newJ)+(gridSize-shapeSize)/2), 
					   (float)(height/2.0-gridSize*(5.0-newI)+(gridSize-shapeSize)/2)};
		} else if (newI == EXIT_I && newJ == EXIT_J) { // exit
			solved = true;
			vibrator.vibrate(500);
			return new float[]{(float)(width/2.0-gridSize*(5.0-EXIT_J)+(gridSize-shapeSize)/2), 
					   (float)(height/2.0-gridSize*(5.0-EXIT_I)+(gridSize-shapeSize)/2)};
		}
		return new float[]{newX, newY};
	}

	public boolean[][] getGridX() {
		return gridX;
	}
	
	public boolean[][] getGridY() {
		return gridY;
	}
	
	public boolean[][] getCoins() {
		return coins;
	}
	
	public boolean[][] getTraps() {
		return traps;
	}
	
	public boolean isTrapped() {
		return trapped;
	}
	
	public int getStartX() {
		return START_J;
	}
	
	public int getStartY() {
		return START_I;
	}
	
	public int getExitX() {
		return EXIT_J;
	}
	
	public int getExitY() {
		return EXIT_I;
	}
	
	public boolean getSolved() {
		return solved;
	}
	
	public int getNumCoins() {
		return numCoins;
	}
	
	public int getNumTrapped() {
		return numTrapped;
	}
	
	public void setDegree(float degree) {
		this.degree = degree%360;
		trappedDegrees += Math.abs(this.degree);
		if (trapped && trappedDegrees > 50000) {
			trapped = false;
			traps[trappedI][trappedJ] = false;
		}
	}
}
