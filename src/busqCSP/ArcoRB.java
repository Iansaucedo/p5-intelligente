/**
 * Clase para representar un arco dirigido entre 2 vbles de una restriccion binaria cualquiera
 */
package busqCSP;

/**
 * @author ines
 * @version 2022.10.*
 *
 */
public class ArcoRB<V> {
	// ATRIBUTOS
	private String origen;
	private String destino;
	private ResBin<V> relacion;

	/**
	 * Constructor
	 * @param origen, la variable de origen (un String)
	 * @param destino, la variable de destino (un String)
	 * @param relacion, una relacion binaria (de tipo ResBin)
	 */
	public ArcoRB(String origen, String destino, ResBin<V> relacion ) {
		setOrigen(origen);
		setDestino(destino);
		setResBin(relacion);
	}
	
	/**
	 * consistentes
	 * @return true si la asignacion de valores origen=vori, destino=vdest cumple la restriccion
	 */
	boolean consistentes(V vori, V vdest){
		return relacion.sonConsistentes(vori, vdest);
	}
	
	
	// METODOS HEREDADOS DE Object
	@Override
	public int hashCode() {
		return toString().hashCode(); // usamos el hashCode del String que representa al arco
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArcoRB)) {
			return false;
		}
		// otro objeto de clase ArcoRB
		@SuppressWarnings("unchecked")
		ArcoRB<V> otroArco = (ArcoRB<V>) obj;
		return getOrigen().equals(otroArco.getOrigen()) 
				&& getDestino().equals(otroArco.getDestino())
				&& getResBin().equals(otroArco.getResBin());
}
	
	@Override
	public String toString() {
		return relacion.getNombre() + "( " + origen + ", " + destino + ")";
	}
	


	// OBSERVADORES Y MODIFICADORES
	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	private void setResBin(ResBin<V> relacion) {
		this.relacion = relacion;
		
	}
	
	public ResBin<V> getResBin() {
		return relacion;
	}


}
