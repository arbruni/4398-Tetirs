
public abstract class Tetris
{
	//four blocks 
	protected Cell[] cell = new Cell[4];
	protected State[] state; //rotate state
	protected int index = 100000;

	//rotate
	protected class State
	{
		int row0, col0, row1, col1, row2, col2, row3, col3; //rotate one circle 
		
		public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3)
		{
			super();
			this.row0 = row0;
			this.col0 = col0;
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = rol3;
	
		}
	}
	
}


//static for four blocks       
public static Tetris randomOne() 
{            
	// blocks created         
	Random random = new Random();           
	int type = random.nextInt(7);           
	switch(type) 
	{            
		case 0 : return new T();           
		case 1 : return new I();           
		case 2 : return new J();           
		case 3 : return new L();           
		case 4 : return new S();           
		case 5 : return new Z();           
		case 6 : return new O();           
	}               
	
	return null;      
}



class T extends Tetris 
{      
	public T() 
	{            
		cells[0] = new Cell(0,4,Tetris.T);
		cells[1] = new Cell(0,3,Tetris.T);
		cells[2] = new Cell(0,5,Tetris.T);
		cells[3] = new Cell(1,4,Tetris.T);
		states = new State[4];            
		states[0] = new State(0,0, 0,-1, 0,1, 1,0);           
		states[1] = new State(0,0, -1,0, 1,0, 0,-1);           
		states[2] = new State(0,0, 0,1, 0,-1, -1,0);           
		states[3] = new State(0,0, 1,0, -1,0, 0,1);      
		} 
}  

class I extends Tetris
{      
	public I() 
	{            
		cells[0] = new Cell(0,4,Tetris.I);           
		cells[1] = new Cell(0,3,Tetris.I);           
		cells[2] = new Cell(0,5,Tetris.I);           
		cells[3] = new Cell(0,6,Tetris.I);        
		states = new State[] 
		{
			new State(0,0, 0,1, 0,-1, 0,-2),                                      
			new State(0,0, -1,0, 1,0, 2,0)};
			} 
}

class J extends Tetris 
{      
	public J() 
	{            
		cells[0] = new Cell(0,4,Tetris.J);           
		cells[1] = new Cell(0,3,Tetris.J);           
		cells[2] = new Cell(0,5,Tetris.J);           
		cells[3] = new Cell(1,5,Tetris.J);           
		states = new State[]
		{             
			new State(0,0, 0,-1, 0,1, 1,1),            
			new State(0,0, -1,0, 1,0, 1,-1), 
           new State(0,0, 0,1, 0,-1, -1,-1),            
		   new State(0,0, 1,0, -1,0, -1,1)
		};      
	} 
}

class L extends Tetris 
{      
	public L() 
	{            
		cells[0] = new Cell(0,4,Tetris.L);           
		cells[1] = new Cell(0,3,Tetris.L);           
		cells[2] = new Cell(0,5,Tetris.L);           
		cells[3] = new Cell(1,3,Tetris.L);           
		states = new State[]
		{                           
			new State(0,0, 0,-1, 0,1, 1,-1),
			new State(0,0, -1,0, 1,0, -1,-1),
			new State(0,0, 0,1, 0,-1, -1,1), 
			new State(0,0, 1,0, -1,0, 1,1)
		};      
	} 
}  

class S extends Tetris 
{      
	public S() 
	{            
		cells[0] = new Cell(0,4,Tetris.S);
		cells[1] = new Cell(0,5,Tetris.S);
		cells[2] = new Cell(1,3,Tetris.S); 
		cells[3] = new Cell(1,4,Tetris.S);   
        states = new State[]
		{    
			new State(0,0, 0,1, 1,-1, 1,0),
			new State(0,0, -1,0, 1,1, 0,1)
		};      
	} 
}


class Z extends Tetris 
{      
	public Z() 
	{  
		cells[0] = new Cell(1,4,Tetris.Z);
		cells[1] = new Cell(0,3,Tetris.Z); 
		cells[2] = new Cell(0,4,Tetris.Z);  
		cells[3] = new Cell(1,5,Tetris.Z);  
		states = new State[]
		{ 
			new State(0,0, -1,-1, -1,0, 0,1), 
			new State(0,0, -1,1, 0,1, 1,0)
		};

	} 
}


class O extends Tetris 
{ 
		public O() 
		{ 
			cells[0] = new Cell(0,4,Tetris.O);
			cells[1] = new Cell(0,5,Tetris.O);  
			cells[2] = new Cell(1,4,Tetris.O); 
			cells[3] = new Cell(1,5,Tetris.O);
			states = new State[]{
					new State(0,0,0,1,1,0,1,1),
					new State(0,0,0,1,1,0,1,1);
					
			}
}
