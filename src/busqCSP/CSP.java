package busqCSP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Clase para representar un CSP: (variables,dominios,restricciones)
 * @author ines
 * @param <V> el tipo de los valores de las variables
 * @version 2022.10.*
 */
public class CSP<V> {
	// ATRIBUTOS
	// Las variables X={X1,...,Xn} son de tipo String, los nombres de las variables
	// Dominios D={D1,...,Dn}, los valores (de tipo V) que puede tomar cada variables
	// Lo guardamos "todo junto'' en un mapa
	private Map<String,Set<V>> dominios; // mapa por parejas: variable-dominio
	// Restricciones binarias R, representadas como arcos dirigidos
	private Map<String,List<ArcoRB<V>>> restricciones; // tabla por parejas: variable-arcos que llegan a dicha variable

	
	// CONSTRUCTORES
	/**
	 * Constructor por defecto: lo inicializa todo a vacio
	 */
	public CSP( ){
		dominios = new HashMap<String, Set<V>>();
		restricciones = new HashMap<String,List<ArcoRB<V>>>();
	}
	

	/**
	 * Constructor copia; copia DURA de los atributos (no refs.)
	 * excepto las restricciones, de manera que si se modifican los dominios de la copia
	 * no afecta al original
	 * @param otro CSP
	 */
	public CSP( CSP<V> otro ){
		setDominiosCopia(otro.getDominios());
		setRestricciones( otro.getRestricciones() ); // copiamos (por ref.) las restricciones
	}
	

	// METODOS NECESARIOS PARA RESOLVER EL CSP	
	
	// OBSERVADORES Y MODIFICADORES
	// estan los obvios y alguno mas "sofisticado" para hacer copias duras
	
	// ATRIBUTO variables
	/**
	 * observador de las variables del csp
	 * @return the variables
	 */
	public Set<String> getVariables() {
		return dominios.keySet();
	}

	/**
	 * observador para saber el numero de variables
	 * @return the nVars
	 */
	public int getnVars() {
		return dominios.size();
	}

	// ATRIBUTO dominios
	/**
	 * observador de TODOS los dominios de TODAS las variables del csp
	 * @return the dominios, un mapa cuyas claves son las variables 
	 * 		   y los valores, el conjunto de valores que se le puede asignar a la variable/clave correspondiente
	 */
	public Map<String, Set<V>> getDominios() {
		return dominios;
	}
	
	/**
	 * modificador simple de dominios
	 * Metodo para copiar la ref. a un objeto Dominios
	 * @param dominios, el objeto a copiar (copia blanda)
	 */
	public void setDominios(Map<String, Set<V>> dominios) {
		this.dominios = dominios; // copia blanda
	}
	
	/**
	 * modificador duro de dominios
	 * Metodo para copiar otra tabla de dominios (copia dura) en el atributo
	 * IMPORTANTE si queremos que al modificar la copia NO se modifique el original
	 * @param otro, la tabla var-dominio para copiar
	 */
	public void setDominiosCopia(Map<String,Set<V>> otro) {
		this.dominios = new HashMap<String, Set<V>>();
		Set<String> vars= otro.keySet();
		for( String var : vars ){
			Set<V> dom = new HashSet<V>(otro.get(var));
			aniadeDomA(var, dom); // copiamos dominio para la variable var
		}
	}
	
	/**
	 * metodo privado auxiliar para aniadir la lista de valores al dominio de la variable, ignorando repetidos
	 * se usa en setDominiosCopia
	 * @param variable
	 * @param valores
	 */
	private void aniadeDomA(String variable, Set<V> valores) {
		Iterator<V> iter = valores.iterator();
		while(iter.hasNext()){
			aniadeValorA(variable,iter.next());
		}
	}
	/**
	 * metodo privado para aniadir un valor al dominio de una variable si no esta ya
	 * se usa en setDominiosCopia
	 * @param variable
	 * @param valor
	 */
	private void aniadeValorA(String variable, V valor) {
		Set<V> dominioV = dominios.get(variable);
		if (dominioV!=null){
				dominioV.add(valor);
		}
		else{// dominioV=null;
			dominioV=new HashSet<V>();
			dominioV.add(valor);
			dominios.put(variable, dominioV);
		}
	}
	
	/**
	 * observador del dominio de una sola variable var (devuelve una ref)
	 * @param la variable de la que queremos saber el dominio
	 * @return el dominio de var
	 */
	public Set<V> getDominioDe( String var ){
		return dominios.get(var);
	}
	
	/**
	 * modificador del dominio de una sola variable var
	 * @param var, la variable para la que queremos cambiar el dominio
	 * @param dom, el nuevo dominio 
	 */
	public void setDominioDe( String var, Set<V> dom ){
		this.dominios.put(var, dom );
	}
	
	
	/**
	 * modificador del dominio de una sola variable var mediante borrado:
	 * borra un valor del dominio de una variable
	 * @param var, una variable del csp
	 * @param valor, el valor que queremos borrar de su dominio
	 */
	public void borraValorDeDom( String var,V valor ){
		Set<V> dominio = dominios.get(var);
		if( dominio != null) {
			dominio.remove(valor);
			setDominioDe(var, dominio );
		}
	}
	
	/**
	 * Observador para saber si un valor esta en el dominio de una variable
	 * @param var la variable
	 * @param valor el valor
	 * @return true si valor esta en el dominio de var
	 */
	public boolean valorEnDominio(String var, V valor) {
		return getDominioDe(var).contains(valor);
	}


	
	// ATRIBUTO RESTRICCIONES
	
	/**
	 * observador de restricciones
	 * @return las restricciones, como tabla variable-lista de restricciones (arcos) que inciden en la variable
	 */
	public Map<String,List<ArcoRB<V>>> getRestricciones() {
		return restricciones;
	}

	/**
	 * modificador simple de restricciones (copia referencias)
	 * @param restricciones the restricciones to set
	 */
	public void setRestricciones(Map<String,List<ArcoRB<V>>> restricciones) {
		this.restricciones = restricciones;
	}

	/**
	 * restTodas(): observador de restricciones a modo de lista "gorda"
	 * @return una lista con todos los arcos de restriccion del problema
	 */
	public Queue<ArcoRB<V>> listaArcosRest() {
		Queue<ArcoRB<V>> restTodas = new LinkedList<ArcoRB<V>>();
		Set<String> vars = restricciones.keySet();
		for( String x: vars ){
			restTodas.addAll(restricciones.get(x));
		}		
		return restTodas;
	}
	
	/**
	 * observador de arcos de restriccion incidentes en una variable dada
	 * @param var, una variable
	 * @return lista de arcos de todas las relaciones binarias del CSP en las que el destino es var
	 */
	public Queue<ArcoRB<V>> arcosIncidentesEn( String var ){
		return new LinkedList<ArcoRB<V>>(restricciones.get( var ));
	}
	

}
