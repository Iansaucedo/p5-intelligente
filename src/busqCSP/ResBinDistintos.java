/**
 * 
 */
package busqCSP;

/**
 * Relacion binaria "distintos valores", es decir:
 * R=<(X1,X2), X1!=X2>
 * @author ines
 * @version 2022.10
 *
 */
public class ResBinDistintos<V> extends ResBin<V> {

	public ResBinDistintos(){
		nombre = "distintas";
	}
	
	/** sonConsistentes
	 * metodo para comprobar que una pareja de valores cumple la restriccion de ser distintos
	 * @param val1 el valor de X1
	 * @param val2 el valor de X2
	 * @return cierto si val1 y val2 son dos valores distintos
	 */
	public boolean sonConsistentes( V val1, V val2){
		return !val1.equals(val2);
	}
	

	/**
	 * comparacion de igualdad
	 * @param otro objeto
	 * @return cierto si el otro objeto tambien es una restriccion de ser distintos, falso en otro caso
	 */
	public boolean equals( Object otro) {
		if( otro instanceof ResBinDistintos<?>) {
			return true;
		}
		return false;
	}


}
