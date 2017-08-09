import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


class Board extends JPanel
{ 
	
	public static final int BLOCK_SIZE = 24;
	public static final int BOTTOM = 510;
	public static final int LEFT_BORDER = 100;
	public static final int RIGHT_BORDER = 340;
	public static final int MIDDLE = 220;
	public static final int TOP = 30;
	public static final int HOLD_X = 424;
	public static final int HOLD_Y = 64;
	public static final int UP_NEXT_X = 424;
	public static final int UP_NEXT_Y = 224;
	private static final int FONT_SIZE = 20;
	private static final int SCORE_X = 410;
	private static final int SCORE_Y = 423;
	
	
	
	private BufferedImage tiles;
	private BufferedImage background;
	private int[][] rotation;
	private int[][] board;
	private int[][] colors;
	private Tetris t;
	private Tetris holdBlock;
	private int row; 
	private int col;  
	private int numRotations;
	private boolean firstHold;
	private boolean printHold;
	private boolean collision;
	private ScoreTracker score;
	
	private Queue<Tetris> blockQueue = new LinkedList<Tetris>();

	
	
	// constructor
	public Board() 
	{ 
		numRotations = 0;
		board = new int[20][10];
		colors = new int[20][10];
		firstHold = true;
		score = new ScoreTracker();
		
		new MainActivity(this);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
	
		newShape();
		setImage();
		
		
	} 

	public void setImage() 
	{   
		tiles = null;
		background = null;
		// reads the image file of the blocks
		try {
			tiles = ImageIO.read(Board.class.getResource("/tiles.png"));
			background = ImageIO.read(Board.class.getResource("/bg.png"));
		} catch (IOException e) {
			
		} 
	} 

	// motion control methods
	public void rotate(){
		
		rotation = t.getState(numRotations%t.states.length);
		numRotations++;
	
		if(col + rotation[0].length * BLOCK_SIZE > RIGHT_BORDER){
			col = col - BLOCK_SIZE;
		}
		
		if(row + rotation.length * BLOCK_SIZE > BOTTOM){
			row = row - BLOCK_SIZE;
		}

	}
	
	public void speedDown(){
		MainActivity.ns = 1000000000.0/1000.0;
		
	}
	public void stopSpeed(){
		MainActivity.ns = 1000000000.0/60.0;
	}
	
	public void drop(){
		row = heightOfDropped(col);
	}
	
	private int heightOfDropped(int x) {
		int height = 0;
		int width = 0;
		
		for(int i = 0; i < rotation.length; i++){
			for(int j = 0; j < rotation[i][j]; j++){
				width = j;
			}
		}
		
		for(int i = 0; i < board.length; i++){
			for(int j = x; j < rotation[width].length; j++){
					if(board[i][j] == 1){
						height = i;
						System.out.println(height);
						return height;
					}
			}
		}
		return height;
	}

	public void moveRight(){
		// check to make sure the block doesn't go past the right border
		if(!(col + 1 + rotation[0].length * BLOCK_SIZE > RIGHT_BORDER))
			col = col + 24;
		
	}
	public void moveLeft() {
		// check to make sure the block doesn't go past the left border
		if(!(col - 12 < LEFT_BORDER))
				col = col - 24;
	}
	
	public void hold(){
		
		if(firstHold){
			firstHold = false;
			holdBlock = t;
			t = blockQueue.remove();
			rotation = t.getShape();
		}
		
		else{
			Tetris tempBlock;
			
			tempBlock = holdBlock;
			holdBlock = t;
			t = tempBlock;
			rotation = t.getShape();
		}
		
		printHold = true;
		repaint();
	}
	
	
	private void lineClear(){
		int height = board.length - 1;
		
		for(int i = height; i > 0; i--){
			int count = 0; 
			for(int j = 0; j < board[0].length; j++){
				if(board[i][j] != 0)
					count++;
				
				board[height][j] = board[i][j];
			}
			if(count < board[0].length)
				height--;
				
		}
	}
	
	// resets the y position of the block so it stays on the screen
	public void updatePosition(){
		
		if(!(row + rotation.length * BLOCK_SIZE > BOTTOM) && isEmptyDown()){
			row++;
		}
		else{
			collision = true;
		}
		
		if(collision){
			
			score.updateScore();
			
			for(int y = 0; y < rotation.length; y++)
				for(int x = 0; x < rotation[y].length; x++)
					if(rotation[y][x] != 0){
						board[(row - TOP)/BLOCK_SIZE + y][(col - LEFT_BORDER)/BLOCK_SIZE + x] = 1;
						colors[(row - TOP)/BLOCK_SIZE + y][(col - LEFT_BORDER)/BLOCK_SIZE + x] = t.getColor();
					}
			
			lineClear();
			newShape();
		}

	}
	
	
	
	// gets a random block shape, color, and coordinates
	public void newShape(){
		
		while(blockQueue.size() < 3){
			blockQueue.add(Tetris.randomOne());
		}
		
		row = TOP;
		col = MIDDLE;
		t = blockQueue.remove();
		rotation = t.getShape();
		numRotations = 0;
		collision = false;
	}
	
	
	
	// Printing methods
	// paints the board and renders the block shapes at the same time
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.drawImage(background,0,0,null);
		printShape(col, row, g, t.getColor(), rotation);
		printShape(UP_NEXT_X, UP_NEXT_Y, g, blockQueue.peek().getColor(), blockQueue.peek().getShape());
		
		paintDroppedBlocks(g);
			
		if(printHold){
			printShape(HOLD_X, HOLD_Y, g, holdBlock.getColor(), holdBlock.getShape());
		}
		
		g.setFont(new Font ("courierNew", Font.BOLD, FONT_SIZE));
		g.drawString(String.valueOf(score.getScore()), SCORE_X, SCORE_Y);
		
	}

	
	// runs through a 10X20 2D array, everywhere there is a 1, a tile is printed
	public void paintDroppedBlocks(Graphics g){
		int color;
		
		for(int y = 0; y < board.length; y++)
			for(int x = 0; x < board[y].length; x++)
				if(isOccupied(y,x)){
					color = colors[y][x];
					g.drawImage(tiles.getSubimage(color,0,BLOCK_SIZE,BLOCK_SIZE), x * BLOCK_SIZE + LEFT_BORDER,
						    y * BLOCK_SIZE + TOP, null);
				}

	}
	
	
	
	
	// prints the tetris blocks based on 2D array coordinates
	private void printShape(int x, int y, Graphics g, int color, int[][] coords){
		
		//crops the image to a single 24X24 block
		BufferedImage block = tiles.getSubimage(color, 0, BLOCK_SIZE, BLOCK_SIZE);
		
		//prints the block in the shape of the tetromino
		for(int row = 0; row < coords.length; row++)
			for(int col = 0; col < coords[row].length; col++)
				if(coords[row][col] == 1){
					g.drawImage(block, col * BLOCK_SIZE + x, row * BLOCK_SIZE + y, null);
				}
		
	}
	
	

	// Collision Detection methods
	//You can for example use a 2D array with Boolean or integer values that represent presence of a square on a location: int[][] squares = new int[20][10];

	//The following method finds the collision
	public boolean isValid()
	{
	       //Ensure the piece is in a valid row.
		//if(!(row + rotation.length * BLOCK_SIZE > BOTTOM))
		//    return true;
        //
	    return false;
	}
	
	public boolean isEmptyLeft(){
		
		for(int y = 0; y < rotation.length; y++)
			for(int x = 0; x < rotation[y].length; x++)
				if(rotation[y][x] != 0)
					if((col - LEFT_BORDER)/BLOCK_SIZE + x < 10 && 
						board[(row - TOP)/BLOCK_SIZE + y][(col - LEFT_BORDER)/BLOCK_SIZE + x + 1] != 0){
						return false;
					}
		
	    return true;
			
	}
	
public boolean isEmptyRight(){
		
		for(int y = 0; y < rotation.length; y++)
			for(int x = 0; x < rotation[y].length; x++)
				if(rotation[y][x] != 0)
					if((col - LEFT_BORDER)/BLOCK_SIZE + x > LEFT_BORDER/BLOCK_SIZE + 1 && 
						board[(row - TOP)/BLOCK_SIZE + y][(col - LEFT_BORDER)/BLOCK_SIZE + x - 1] != 0){
						return false;
					}
		
	    return true;
			
	}
	
	public boolean isEmptyDown(){

	         /*
	         * Loop through every tile in the piece and see if it conflicts
	         * with an existing tile.
	         * Note: It's fine to do this even though it allows for wrapping * because we've already
	         * checked to make sure the piece is in a valid location.*/
		for(int y = 0; y < rotation.length; y++)
			for(int x = 0; x < rotation[y].length; x++)
				if(rotation[y][x] != 0)
					if((row - TOP)/BLOCK_SIZE + y + 1 < 20 && 
						board[(row - TOP)/BLOCK_SIZE + y + 1][(col - LEFT_BORDER)/BLOCK_SIZE + x] != 0){
						return false;
					}
	        return true;
	        
	}
	
	private boolean isOccupied(int y, int x) {
		if(board[y][x] != 0)
			return true;
		else
			return false;
	}

	
	// for testing only
	private void printBoard(){
		for(int y = 0; y < board.length; y++){
			System.out.print("\n");
			for(int x = 0; x < board[y].length; x++){
				System.out.print(board[y][x] + " ");
			}
		}
		
		System.out.println("\n\n\n\n");
				
	}

}
