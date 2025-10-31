package busqCSP;

/**
 * clase abstracta para una relacion binaria con ambito (X1,X2)
 * el metodo sonConsistentes define implicitamente la relacion que ha de cumplirse
 * @author ines
 * @version 2022.10.*
 *
 */
public abstract class ResBin<V> {	
	
	protected String nombre; // le damos un nombre al tipo de relacion
	
	/** 
	 * @param val1 un valor para la variable X1
	 * @param val2 un valor para la variable X2
	 * @return cierto si val1 y val2 son dos valores consistentes para esta restriccion binaria
	 */
	public abstract boolean sonConsistentes( V val1, V val2);
	
	/**
	 * @return el nombre
	 */
	public String getNombre() {
		return nombre;
	}
}
