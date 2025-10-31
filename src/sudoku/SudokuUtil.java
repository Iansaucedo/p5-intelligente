package sudoku;

import java.io.*;
import java.util.Map;

/**
 * An utility class for creating a Sudoku solver Includes input and output functions for the game board as well as text formatting of the Sudoku board
 * 
 * @author Mark Crowley (original)
 * 
 * @author Jacek Kisynski (updates)
 * 
 * @author Ines Gonzalez (adaptado a ISI, Unican)
 * 
 * @version 2021.11.*
 */
public final class SudokuUtil {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private SudokuUtil() {
		// Empty
	}

	/**
	 * Read a Sudoku board from the file given in boardInFile as a comma delimited file with new lines to indicate rows. The Board must be bdSize x
	 * bdSize large Any data after bdSize lines will not be read
	 * 
	 * @param boardInFile The full file name and location for the Sudoku board;
	 *
	 * @param bdSize The number of rows and columns in the Sudoku board.
	 * 
	 * @return board The int[][] array representing the board in boardInFile.
	 * 
	 * @throws IOException
	 */
	public static int[][] readInBoard(String boardInFile, int bdSize) throws IOException {
		int[][] board = new int[bdSize][bdSize];
		BufferedReader in = new BufferedReader(new FileReader(boardInFile));
		String str;
		int row = 0;
		String[] rowStr;
		while ((str = in.readLine()) != null && row < bdSize) {
			rowStr = str.split(",\\s*");
			for (int i = 0; i < rowStr.length; i++) {
				board[row][i] = Integer.parseInt(rowStr[i]);
			}
			row++;
		}
		in.close();
		return board;
	}

	/**
	 * Read a Sudoku board from the file given in boardInFile as a a string of bdSize^2 characters: digits from 1 to 9 and dots. Dots stand for empty
	 * cells. This format is widely used on the Internet to share Sudokus.
	 * 
	 * @param boardInFile The full file name and location for the Sudoku board;
	 * @param bdSize The number of rows and columns in the Sudoku board.
	 * 
	 * @return board The int[][] array representing the board in boardInFile.
	 * 
	 * @throws IOException
	 */
	public static int[][] readInBoardDots(String boardInFile, int bdSize) throws IOException {
		int[][] board = new int[bdSize][bdSize];
		try (BufferedReader in = new BufferedReader(new FileReader(boardInFile))) {
			String str = in.readLine();
			if (str == null)
				throw new IOException("Invalid input file.");
			str = str.trim(); // Get rid of leading and trailing white spaces
			if (str.length() != bdSize * bdSize)
				throw new IOException("Invalid input file.");
			int index = 0;
			char[] characters = str.toCharArray();
			for (int row = 0; row < bdSize; row++)
				for (int column = 0; column < bdSize; column++) {
					if (characters[index] == '.')
						board[row][column] = 0;
					else {
						try {
							board[row][column] = Integer.valueOf(characters[index]) - 48;
						} catch (NumberFormatException e) {
							throw new IOException(e.getMessage());
						}
						if ((board[row][column] < 1) || (board[row][column] > 9))
							throw new IOException("Invalid input file");
					}
					index++;
				}
			in.close();
		}
		return board;
	}

	/**
	 * Output the Sudoku board to a text file in comma delimited format.
	 * 
	 * @param newBoard a 2d int array representing the Sudoku board to write out;
	 * @param boardOutFile the full file name and location for the board to be written to.
	 * 
	 * @throws IOException
	 */
	public static void writeOutBoard(int[][] newBoard, String boardOutFile) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(boardOutFile));
		out.write(formatBoardComma(newBoard));
		out.close();

		System.out.println("new board saved");
	}

	/**
	 * Produces a nicely formatted in text for the Sudoku board.
	 * 
	 * @param board the 2d int array representing the Sudoku board.
	 * 
	 * @return a formatted text view of the Sudoku board.
	 */
	public static String formatBoard(int[][] board) {
		String r = "";
		String hline = "";
		for (int s = 0; s < 24; s++) {
			hline += "-";
		}
		for (int i = 0; i < board.length; i++) {
			if (i > 0 && i % 3 == 0)
				r += hline + "\n";
			for (int j = 0; j < board[i].length; j++) {
				if (j > 0 && j % 3 == 0)
					r += " | ";
				r += board[i][j] + " ";
			}
			r += "\n";
		}
		return r;
	}

	/**
	 * Produces a formatted text out of the Sudoku board with numbers separated by commas only. This is the format used to read in and write out
	 * Sudoku boards.
	 * 
	 * @param board the 2d int array representing the Sudoku board.
	 * 
	 * @return a comma delimited text String of the Sudoku board.
	 */
	protected static String formatBoardComma(int[][] board) {
		String r = "";
		int i = 0, j = 0;
		for (i = 0; i < board.length; i++) {
			for (j = 0; j < board[i].length - 1; j++) {
				r += board[i][j] + ",";
			}
			r += board[i][j] + "\n";
		}
		return r;
	}
	
	/**
	 * pasa de coordenadas a id de variable
	 * @param fila, un entero entre 0 y 8 (coordenada de fila)
	 * @param col, un entero entre 0 y 8 (coordenada de columna)
	 * @return el String [fila+1,col+1]
	 */
	protected static String coord2Var( int fila, int col){
		int fila1 = fila+1; int col1=col+1;
		String varid = "["+fila1+","+col1+"]";
		return varid;
	}

	/**
	 * pasa de id vble a coordenada de fila
	 * PRECONDICION la var siempre es de la forma "[x,y]" con 1<=x,y<=9 
	 * @param la variable var (un String), id de la posicion [x,y]
	 * @return el numero de fila x-1
	 */

	protected static int var2Fila( String var ){
		return Character.getNumericValue(var.charAt(1))-1;
	}


	/**
	 * pasa de id vble a coordenada de fila
	 * PRECONDICION la var siempre es de la forma "[x,y]" con 1<=x,y<=9 
	 * @param la variable var (un String), id de la posicion [x,y]
	 * @return el numero de columna y-1
	 */

	protected static int var2Col( String var ){
		return Character.getNumericValue(var.charAt(3))-1;
	}
	
	/**
	 * asig2Tablero
	 * Metodo para pasar de una asignacion (mapa <variable,valor>) al tablero de sudoku correspondiente
	 * @param asignacion, una asignacion de valores a variables
	 * @return el array con el tablero del sukoku correspondiente a la asignacion
	 */
	protected static int[][] asig2Tablero( Map<String,Integer> asignacion ){
		int[][] tablero = new int[9][9];
		for (Map.Entry<String, Integer> par : asignacion.entrySet()) {
		     int i= var2Fila(par.getKey());
		     int j= var2Col(par.getKey());
		     int valor = par.getValue();
		     tablero[i][j]=valor;
		    }
		return tablero;
	}
}
