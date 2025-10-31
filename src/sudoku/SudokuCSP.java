/**
 * 
 */
package sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import busqCSP.CSP;
import busqCSP.ArcoRB;
import busqCSP.ResBin;
import busqCSP.ResBinDistintos;

/**
 * @author Ines
 * @version 2021.11.*
 *
 */
public class SudokuCSP extends CSP<Integer>{
	int[][] tablero;
	
	/** constructor del sudoku a partir de un fichero
	 * @param sudokuFile, el nombre del fichero
	 */
	public SudokuCSP( String sudokuFile ){
		// leemos el sudoku del fichero sudokuFile e inicializamos los atributos heredados
		try {
			tablero = SudokuUtil.readInBoard(sudokuFile, 9);
			iniciaDominios();
			iniciaRestricciones();
		} catch (Exception e) {
			System.out.println("La lectura del sudoku " + sudokuFile + " ha fallado " + e);
		}
	}
	
	/** constructor del sudoku a partir de un tablero
	 * @param sudokuTab, el array doble de enteros que conforma el tablero
	 */
	public SudokuCSP( int[][] sudokuTab ){
		// leemos el sudoku del fichero sudokuFile e inicializamos los atributos heredados
		tablero = sudokuTab;
		iniciaDominios();
		iniciaRestricciones();
	}
	
	// METODOS PRIVADOS
	

	private void iniciaDominios() {
		// recorremos el tablero y para cada casilla tablero[i][j]:
		// - creamos la variable Xij
		// - creamos el dominio igual a {1,2,...,9} si tablero[i,j]=0 e igual a {tablero[i][j]} en otro caso
		Map<String,Set<Integer>> doms = new HashMap<String, Set<Integer>>(81,1); // mapa variable-dominio
		for( int i=0; i<9; i++ ) {
			for( int j=0; j<9; j++ ) {
				String var = SudokuUtil.coord2Var(i,j); // variable Xi,j
				HashSet<Integer> domVar = new HashSet<Integer>();
				if( tablero[i][j]== 0 ) {// celda "en blanco", no tiene valor asignado
					for( int k=1; k<10; k++ ) { // creamos un dominio con todos los valores posibles
						domVar.add(k);
					}
				}
				else { // celda con un valor
					domVar.add(tablero[i][j]); // el dominio esta formado por ese valor
				}
				doms.put(var,domVar); // aniadimos la pareja variable--dominio al mapa de dominios
			}
		}
		setDominios(doms); // modificamos el atributo de la clase padre
	}

	private void iniciaRestricciones() {
		// recorremos las variables y
		// para cada variable creamos las restricciones *binarias* incidentes en dicha variable
		ResBin<Integer> rDist = new ResBinDistintos<Integer>();
		HashMap<String,List<ArcoRB<Integer>>> rest = new HashMap<String,List<ArcoRB<Integer>>>();
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				// creamos lista restricciones para cada vble [i,j] (arcos con destino [i,j])
				LinkedList<ArcoRB<Integer>> restVar = new LinkedList<ArcoRB<Integer>>();
				restVar.addAll( this.restFila(i,j,rDist) ); // restricciones con vbles en misma fila
				restVar.addAll( this.restColumna(i,j,rDist)); // restricciones con vbles en misma columna
				restVar.addAll( this.restBloque(i,j,rDist) ); // restricciones con vbles en mismo bloque
				// aniadimos lista de restricciones a la tabla
				rest.put(SudokuUtil.coord2Var(i, j), restVar);
			}
		} 
		 setRestricciones(rest);
	}

	
	/**
	 * Metodo para obtener las restricciones de fila de una casilla dada
	 * @param fila, el numero de fila de una casilla
	 * @param columna, el numero de columna de una casilla
	 * @param rDist, un objeto que representa una restriccion binaria "distintas"
	 * @return la lista de arcos de restriccion (otra, casilla) con otra una casilla de la misma fila 
	 */
	private LinkedList<ArcoRB<Integer>> restFila( int fila, int columna, ResBin<Integer> rDist ){
		LinkedList<ArcoRB<Integer>> rf = new LinkedList<ArcoRB<Integer>>();
		for( int j=0; j<9; j++ ){
			if( j!=columna )
				rf.add(new ArcoRB<Integer>(SudokuUtil.coord2Var(fila,j),SudokuUtil.coord2Var(fila,columna), rDist));
		}
		return rf;
	}
	
	/**
	 * Metodo para obtener las restricciones de columna de una casilla dada
	 * @param fila, el numero de fila de una casilla
	 * @param columna, el numero de columna de una casilla
	 * @param rDist, un objeto que representa una restriccion binaria "distintas"
	 * @return la lista de arcos de restriccion de columna de dicha casilla (otro, casilla)
	 */
	private LinkedList<ArcoRB<Integer>> restColumna( int fila, int columna, ResBin<Integer> rDist ){
		LinkedList<ArcoRB<Integer>> rc = new LinkedList<ArcoRB<Integer>>();
		for( int i=0; i<9; i++ ){
			if( i!=fila )
				rc.add(new ArcoRB<Integer>( SudokuUtil.coord2Var(i,columna),SudokuUtil.coord2Var(fila,columna),rDist));
		}
		return rc;
	}
	
	/**
	 * Metodo para obtener las restricciones "de bloque" de una casilla dada
	 * @param fila, el numero de fila de una casilla
	 * @param columna, el numero de columna de una casilla
	 * @param rDist, un objeto que representa una restriccion binaria "distintas"
	 * @return la lista de restricciones de cuadrado de dicha casilla (quitando las de fila y columna)
	 */
	private LinkedList<ArcoRB<Integer>> restBloque( int fila, int columna, ResBin<Integer> rDist ){
		LinkedList<ArcoRB<Integer>> rb = new LinkedList<ArcoRB<Integer>>();
		// la casilla esta en el bloque (bloqueF,bloqueC) y
		// dentro de ese bloque, en la pos (filaBloque,colBloque)
		int bloqueF = fila/3; int filaBloque = fila%3;
		int bloqueC = columna/3; int colBloque = columna%3;
		// recorremos el bloque
		for( int i=0; i<3; i++ ){
			for( int j=0; j<3; j++){
				if( i!=filaBloque && j!=colBloque )
					rb.add(new ArcoRB<Integer>(SudokuUtil.coord2Var(bloqueF*3+i,bloqueC*3+j), SudokuUtil.coord2Var(fila,columna), rDist));
			}
		}
		return rb;
	}





	

}
