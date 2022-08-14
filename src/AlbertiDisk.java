/**
 * Clase que simula el disco de Alberti.
 */

/**
 * @author Darío
 *
 */
public class AlbertiDisk {

	// Los discos externo e interno, respectivamente.
	private final static char[] STDISC = {'A','B','C','D','E','F','G','I','L','M','N','O','P','Q','R','S','T','V','X','Z','1','2','3','4'};
	private final static char[] MVDISC = {'g','k','l','n','p','r','t','v','z','&','x','y','s','o','m','q','i','h','f','d','b','a','c','e'};
	
	// El número de caracteres traducidos antes de que gire el disco.
	private final static int MAXCHAR = 10;
	// El número de veces que girará el disco al alcanzar el número anterior de caracteres traducidos.
	private final static int TIMESMOVED = 2;
	
	/*
	 * Variable que almacenará la posición del caracter que se encuentra actualmente alineado con la A.
	 * Restaremos a ese valor para girar en sentido horario y sumaremos a ese valor para girar en sentido
	 * antihorario.
	 * 
	 * Lo anterior es debido a que, si giramos el disco en sentido horario (hacia la derecha), la
	 * posición que ocupaba un caracter particular es ocupada actualmente por uno de los caracteres
	 * anteriores a ella (un caracter con una posición negativa respecto al caracter original).
	 * Si el caracter estuviera en la posición 0, el nuevo caracter que ocuparía la posición al girar
	 * en sentido horario sería el caracter de la posición 22 (si el array tiene 24 caracteres).
	 */
	private int aCharMoved;
	// Variable que almacenará la posición que tenía el disco cuando se creó.
	private int ogPosition;
	
	/**
	 *  Getter que devuelve el contenido del disco externo.
	 *  
	 * @return Un array con los caracteres pertenecientes al disco externo.
	 */
	public static char[] getStdisc() {
		return STDISC;
	}
	
	/**
	 * Getter que devuelve el contenido del disco interno.
	 * 
	 * @return Un array con los caracteres pertenecientes al disco interno.
	 */
	public static char[] getMvdisc() {
		return MVDISC;
	}

	/**
	 * Getter que devuelve el número de posiciones que se ha girado el disco interno.
	 * 
	 * @return El número de posiciones que se ha girado el disco interno.
	 */
	public int getACharMoved() {
		return aCharMoved;
	}

	/**
	 * Setter que modifica el número de posiciones que se ha girado el disco interno.
	 * 
	 * @param aCharMoved El nuevo número de posiciones que se ha girado el disco interno.
	 */
	public void setACharMoved(int aCharMoved) {
		this.aCharMoved = aCharMoved;
	}

	/**
	 * Constructor del disco de Alberti sin parámetros.
	 */
	public AlbertiDisk() {
		// Damos estado a las variables de instancia.
		aCharMoved = 0;
		ogPosition = aCharMoved;
	}
	
	/**
	 * Método que hace que el disco vuelva a su posición original.
	 */
	public void restartDiscState() {
		setACharMoved(ogPosition);
	}
	
	/**
	 * Método que recibe un número de giros del disco y lo corrige para que nunca sea mayor
	 * que la longitud del array (o menor que su mínimo). Esto lo hace restando el número de 
	 * caracteres del disco al valor si este se pasa (al girar en sentido antihorario) o sumando 
	 * el número de caracteres del disco (al girar en sentido horario).
	 * 
	 * EJEMPLO EN VALOR ABSOLUTO (Sin tener en cuenta en qué dirección ha girado)
	 * Si nuestro disco tiene 24 posiciones, y ha girado 26 veces, es como si se volviera a encontrar en el
	 * caracter resultante de girar el disco 2 veces (ya que ha dado la vuelta completa). Al restarle el número
	 * de posiciones del disco al de giros (26 - 24 = 2) obtendremos que se han dado dos giros desde la posición
	 * inicial.
	 * 
	 * @param newPos El nuevo número de posiciones que se encuentra girado el disco.
	 */
	private int correctMoved(int newPos) {
		// Comprobamos si el número de giros se ha pasado en sentido antihorario.
		if (newPos >= getMvdisc().length) {
			newPos = newPos - getMvdisc().length;
		}
		// Comprobamos si el número de giros se ha pasado en sentido horario.
		if (newPos < 0) {
			newPos = newPos + getMvdisc().length;
		}
		// Después de realizar las comprobaciones, podemos modificar el valor.
		return newPos;
	}
	
	/**
	 * Método que comprueba si un caracter se encuentra en el disco introducido.
	 * 
	 * @param c El caracter a buscar.
	 * @param disk El disco en el que se buscará.
	 * @return Valor booleano que informa si el caracter se encuentra en el disco o no.
	 */
	private boolean isCharInDisk(char c, char[] disk) {
		// Valor booleano que informa si el caracter se encuentra en el disco o no.
		// Lo inicializamos en falso porque aún no sabemos si el caracter está.
		boolean IsIt = false;
		// Recorremos el array hasta que encontramos alguna coincidencia.
		for (int i = 0; i < disk.length && IsIt == false; i++) {
			// Si encontramos una coincidencia, cambiamos el valor booleano a true.
			if (disk[i] == c) {
				IsIt = true;
			}
		}
		// Devolvemos el valor booleano.
		return IsIt;
	}
	
	/**
	 * Método que devuelve la posición del disco en la que se encuentra un caracter.
	 * Es obligatorio saber previamente si el caracter se encuentra en el disco para que este método funcione
	 * correctamente. Usar el método isCharInDisk.
	 * 
	 * @param c El caracter del que se quiere conocer la posición.
	 * @param disk El disco a partir del cual se va a obtener la posición del caracter introducido.
	 * @return La posición del caracter introducido en el disco.
	 */
	private int characterPosition(char c, char[] disk) {
		// Variable que contendrá la posición del caracter en el disco.
		int position = 0;
		// Variable que usaremos para comprobar si la posición ya ha sido encontrada.
		boolean found = false;
		// Recorreremos el array hasta que encontremos la posición.
		for (int i = 0; i < disk.length && found == false; i++) {
			// Cuando encontremos la posición, la almacenamos en la variable position
			// y cambiamos la variable found a true para abandonar el bucle.
			if (disk[i] == c) {
				found = true;
				position = i;
			}
		}
		// Devolvemos la posición.
		return position;
	}
	
	/**
	 * Método que cifra el mensaje introducido y devuelve ese mismo cifrado en forma de cadena.
	 * 
	 * @param message El mensaje a cifrar.
	 * @return Cadena que contiene el código cifrado.
	 */
	public String cypher(String message) {
		// Variable que almacenará el número de caracteres traducidos desde el último giro.
		int numLetters = 0;
		// StringBuilder que irá almacenando el cifrado del mensaje.
		StringBuilder code = new StringBuilder(message.length());
		// String que almacenará el mensaje que devolverá el error.
		String excError;
		
		// Convertimos la cadena de texto a mayúsculas para trabajar con ella (ya que el disco externo sólo contiene mayúsculas).
		message = message.toUpperCase();
		
		// Recorremos todas las posiciones de la cadena introducida y vamos obteniendo el caracter que se encuentra en cada una.
		for (int i = 0; i < message.length(); i++) {
			// Por lo que a mí respecta, permitiremos y respetaremos los espacios.
			if (message.charAt(i) == ' ') {
				// Añadimos el espacio al código.
				code.append(' ');
				// Sumamos 1 al número de caracteres traducidos.
				numLetters++;
			} 
			// Si el caracter no es un espacio, comprobaremos si es un caracter válido (se encuentra en el disco externo).
			else if (isCharInDisk(message.charAt(i), getStdisc())) {
				/*
				 * De ser así, añadimos al código el caracter correspondiente. Para ello, cogemos la posición del caracter
				 * en el disco externo y la comprobamos con el caracter correspondiente en el disco interno.
				 * 
				 * Para calcular el caracter correspondiente en el disco interno, cogemos la posición original del caracter del disco
				 * interno que está alineado con la A y le sumamos la posición en el disco externo del caracter que queremos traducir.
				 */
				code.append(getMvdisc()[correctMoved(characterPosition(message.charAt(i), getStdisc()) + getACharMoved())]);
				// Sumamos 1 al número de caracteres traducidos.
				numLetters++;
			}
			// Si el caracter no se encuentra en el disco externo se producirá un error.
			else {
				excError = String.format("Lo siento, no puedo cifrar ese mensaje. No sé cómo cifrar la letra %c", message.charAt(i));
				throw new IllegalArgumentException(excError);
			}
			// Comprobamos, en cada repetición del bucle si hemos alcanzado el número de caracteres traducidos
			// con el que debemos girar el disco.
			if (numLetters == MAXCHAR) {
				// De haber llegado a ese número de caracteres, devolvemos el valor del contador a 0.
				numLetters = 0;
				// También, por supuesto, giramos el disco. En este caso, giramos en sentido horario, así que restamos
				// el número de posiciones correspondiente al giro.
				setACharMoved(correctMoved(getACharMoved() - TIMESMOVED));
			}
		}
		
		// Devolvemos el código cifrado.
		return code.toString();
		
	}
	
	/**
	 * Método que descifra el código introducido y devuelve ese mismo descifrado en forma de cadena.
	 * 
	 * @param code El código a descifrar.
	 * @return Cadena que contiene el mensaje descifrado.
	 */
	public String decypher(String code) {
		// Variable que almacenará el número de caracteres traducidos desde el último giro.
		int numLetters = 0;
		// StringBuilder que irá almacenando el descifrado del código.
		StringBuilder message = new StringBuilder(code.length());
		// String que almacenará el mensaje que devolverá el error.
		String excError;
				
		// Convertimos la cadena de texto a minúsculas para trabajar con ella (ya que el disco interno sólo contiene minúsculas).
		code = code.toLowerCase();
		
		// Recorremos todas las posiciones de la cadena introducida y vamos obteniendo el caracter que se encuentra en cada una.
		for (int i = 0; i < code.length(); i++) {
			// Por lo que a mí respecta, permitiremos y respetaremos los espacios.
			if (code.charAt(i) == ' ') {
				// Añadimos el espacio al mensaje.
				message.append(' ');
				// Sumamos 1 al número de caracteres traducidos.
				numLetters++;
			} 
			// Si el caracter no es un espacio, comprobaremos si es un caracter válido (se encuentra en el disco interno).
			else if (isCharInDisk(code.charAt(i), getMvdisc())) {
				/*
				 * De ser así, añadimos al mensaje el caracter correspondiente. Para ello, cogemos la posición del caracter
				 * en el disco interno y la comprobamos con el caracter correspondiente en el disco externo.
				 * 
				 * Para calcular el caracter correspondiente en el disco externo, cogemos la posición original del caracter que queremos traducir
				 * en el disco interno y le sumamos la diferencia entre el total de caracteres y la posición original del caracter que está actualmente
				 * alineado con la A.
				 */
				message.append(getStdisc()[correctMoved(characterPosition(code.charAt(i), getMvdisc()) + (getMvdisc().length - getACharMoved()))]);
				// Sumamos 1 al número de caracteres traducidos.
				numLetters++;
			}
			// Si el caracter no se encuentra en el disco interno se producirá un error.
			else {
				excError = String.format("Lo siento, no puedo descifrar ese mensaje. No sé cómo descifrar la letra %c", code.toUpperCase().charAt(i));
				throw new IllegalArgumentException(excError);
			}
			// Comprobamos, en cada repetición del bucle si hemos alcanzado el número de caracteres traducidos
			// con el que debemos girar el disco.
			if (numLetters == MAXCHAR) {
				// De haber llegado a ese número de caracteres, devolvemos el valor del contador a 0.
				numLetters = 0;
				// También, por supuesto, giramos el disco. En este caso, giramos en sentido horario, así que restamos
				// el número de posiciones correspondiente al giro.
				setACharMoved(correctMoved(getACharMoved() - TIMESMOVED));
			}
		}
		
		// Devolvemos el código cifrado.
		return message.toString();
		
	}
	
}
