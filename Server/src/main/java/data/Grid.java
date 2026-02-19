package data;

enum Color{
	red,
	yellow
}//null is empty

public class Grid {

	private Color grid[][];
	
	public Grid() {
		this.grid = new Color[6][7];
		
	}
	
	public synchronized void insert(Color color,int column) throws Exception {
		if (!isColumnFull(column)) {
			for(int i = 0; i < 6; i++) {
				if(grid[i][column] == null) {
					grid[i][column] = color;
					return;
				}
			}
		}
		
		throw new Exception("the grid is full");
	}
	
	public synchronized int getLastElement(int column) throws Exception {
		
		if(isColumnEmpty(column))
			throw new Exception("The column is empty");
		
		for(int i = 0; i < 6; i++) {
			if(grid[i][column] == null) {
				return i-1;
			}
		}
		
		return 6;
		
	}
	
	public boolean isFinished(Color color,int column) throws Exception {
		
		int row = getLastElement(column);
		
		if(checkRow(color, row))
			return true;
		
		if(checkColumn(color,column))
			return true;
		
		if(checkMainDiagonal(color,row,column))
			return true;
		
		if(checkSecondaryDiagonal(color,row,column))
			return true;
	
		return false;
	}
	
	private synchronized boolean checkRow(Color color,int row) {
		
		int counter = 0;
		
		for(int i = 0; i < 7; i++) {
			if(grid[row][i] == color) {
				counter++;
			}
			else {
				counter = 0;
			}
			
			if(counter == 4)
				return true;
		}
		
		return false;
	}
	
	private synchronized boolean checkColumn(Color color,int column) {
		
		int counter = 0;
		
		for(int i = 0; i < 6; i++) {
			if(grid[i][column] == color) {
				counter++;
			}
			else {
				counter = 0;
			}
			
			if(counter == 4)
				return true;
		}
		
		return false;
	}
	
	private synchronized boolean checkMainDiagonal(Color color, int row,  int column) {
		
		//Trovo il punto di partenza della diagonale
		//Sottraggo ai due indici il più basso
		
		int lower = column < row? column: row;
		int counter = 0;
		
		column -= lower;
		row -= lower;
		
		for(int i = 0; column < 7 && row < 6; column++, row++) {
			if(grid[row][column] == color) {
				counter++;
			}
			
			else {
				counter = 0;
			}
			
			if(counter == 4)
				return true;
		}
		
		return false;
	}
	
	private synchronized boolean checkSecondaryDiagonal(Color color, int row,  int column) {
		//Trovo il punto di partenza della diagonale
		//Trovo la distanza della colonna attuale rispetto alle dimensioni della griglia
		int columnOffset = 6 - column;
			
		//Trovo il valore medio
		int lower = columnOffset < row? columnOffset: row;
		int counter = 0;
			
		//Sottraggo al primo il valore minimo e lo sommo al secondo
		row -= lower;
		column += lower;
				
		for(int i = 0; column > 0 && row < 6; row++, column--) {
			if(grid[row][column] == color) {
				counter++;
			}
					
			else {
				counter = 0;
			}
					
			if(counter == 4)
				return true;
		}
				
		return false;
	}
	
	public synchronized boolean isColumnFull(int column) {
		return grid[6][column] != null? true : false ;
	}
	
	public synchronized boolean isColumnEmpty(int column) {
		return grid[0][column] == null? true: false;
	}
}

