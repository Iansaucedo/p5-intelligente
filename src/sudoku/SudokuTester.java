package sudoku;

import java.io.IOException;
import java.util.Map;

import busqCSP.CSPSolver;

/**
 * Tester para probar el codigo de la practica
 * Inspirado en el tester de http:// modelai.gettysburg.edu/2010/csp/index.htm.
 *
 * @author Ines Gonzalez (adaptacion a ISI, Unican)
 * 
 * @version 2022.10.*
 */
public class SudokuTester {

	/**
	 * @param argv not used.
	 * 
	 * @throws java.io.IOException
	 */
	public static void main(String[] argv) throws java.io.IOException {
		CSPSolver<Integer> acSolver = new CSPSolver<Integer>();
		System.out.println(acSolver.autores() + "\n");

		/** Sudokus faciles */
		// Este sudoku ya esta resuelto, asi que obviamente puede "resolverse" solo con AC-3
		test(acSolver, "solved", true, true);
		// Este Sudoku puede "resolverse" solo con AC-3
		test(acSolver, "easy", true, true);
		// DEJAR COMENTADOS LOS QUE SIGUEN HASTA COMPROBAR EL CORRECTO FUNCIONAMIENTO DE AC-3
		// DESCOMENTAR UNA VEZ IMPLEMENTADO MAC PARA COMPROBAR QUE LA BUSQUEDA TAMBIEN FUNCIONA
//
//		
//		// Este sudoku no puede resolverse unicamente con propagacion de restricciones, hace falta busqueda
//		test(acSolver, "hard", true, true);
//		// sudoku dificil de from www.websudoku.com (dificil para los humanos, facil para MAC)
//		test(acSolver, "evil", true, false);
//		// sudoku de un entusiasta de este rompecabezas (http://www.flickr.com/photos/npcomplete/2384354604/), necesita MAC
//		test(acSolver, "starBurstLeo", true, false);
//
//		/*
//		 * Sudokus algo mas complejos que se supone que son un reto para los humanos.
//		 * Todos necesitan propagacion de restricciones combinada con busqueda (MAC)
//		 * Tomados de http://en.wikipedia.org/wiki/Algorithmics_of_sudoku
//		 * Aqui ya puede pasar que veamos que MAC tarda algo en resolverlos (pero muy poquito)
//		 */
//		test(acSolver, "easterMonster", true, false);
//		test(acSolver, "tarek071223170000-052", true, false);
//		test(acSolver, "goldenNugget", true, false);
//
//		/**
//		 * Dos sudokus con solo 17 entradas ya fijadas, que es el menor numero de entradas conocido que de lugar a sudokus validos
//		 * MAC tarda un rato (entre 1 y 6 minutos) en resolverlos.
//		 * Los resolvedores ya especificos lo hacen inmediatamente.
//		 */
//		test(acSolver, "minimum1", true, false); 
//		test(acSolver, "minimum50", true, false); 
//
//		/**
//		 * Un sudoku que en principio es muy dificil... sin embargo, a MAC le cuesta menos que los anteriores (!!!)
//		 * Fuente: http://en.wikipedia.org/wiki/Algorithmics_of_sudoku
//		 */
//		test(acSolver, "nearWorstCase", true, false);
		
		System.out.println("FIN. Si has conseguido resolverlos todos, !enhorabuena!");
	}

	/**
	 * @param acSolver Sudoku solver;
	 * @param nombreTablero name of the Sudoku board;
	 * @param tieneSolucion if true, then the board has a solution and it will be compared to one obtained by the solver;
	 * @param verbose if true, problem board and the solution board will be printed on the screen.
	 * 
	 * @throws IOException
	 */
	private static void test(CSPSolver<Integer> acSolver, String nombreTablero, boolean tieneSolucion, boolean verbose) throws IOException {
		long time = System.currentTimeMillis();
		try {
			System.out.println("Tablero '" + nombreTablero + "': ");
			int[][] tablero = SudokuUtil.readInBoard(nombreTablero + ".sud", 9); // leemos el tablero
			SudokuCSP problema = new SudokuCSP( tablero ); // construimos el CSP correspondiente al sudoku
			if (verbose)
				System.out.println(SudokuUtil.formatBoard(tablero));
			System.out.print("Solucion: ");
			
			Map<String,Integer> solucion = acSolver.resuelve(problema);

			int[][] solution = SudokuUtil.asig2Tablero(solucion);
			if (verbose)
				System.out.println("\n" + SudokuUtil.formatBoard(solution));
			if (tieneSolucion)
				System.out.println(match(solution, SudokuUtil.readInBoard(nombreTablero + "Solution.sud", 9)) ? "CORRECTO" : "INCORRECTO");
			else
				System.out.println("ERROR: el tablero '" + nombreTablero + "' no es un sudoku valido.");
		} catch (Exception e) {
			System.out.println("Board '" + nombreTablero + "': crashed " + e);
		}
		System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + " seconds\n");
	}

	/**
	 * @param board1 Sudoku board;
	 * @param board2 Sudoku board.
	 * 
	 * @return true if given boards are identical, false otherwise.
	 */
	private static boolean match(int[][] board1, int[][] board2) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board1[i][j] != board2[i][j])
					return false;
		return true;
	}
}