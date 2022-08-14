import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;

/**
 * Programa que cifra y descifra un texto utilizando el disco de Alberti.
 */

/**
 * @author Darío
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Declaramos un escáner para poder introducir datos por pantalla.
		Scanner sc = new Scanner(System.in);
		// Variable booleana que dará salida al programa cuando el usuario lo desee.
		boolean flag = true;
		// Variable que almacenará la opción elegida por el usuario.
		String option;
		// Variable que almacenará el mensaje sobre el que el usuario quiere hacer una operación.
		String message;
		// Variable que almacenará, por cada caracter impreso en la codificación, el color con el que cada uno va a imprimirse.
		int value;
		// StringBuilder que nos servirá para ir almacenando código cifrado al colorearlo y poder mostrarlo luego por pantalla.
		StringBuilder coloredMessage;
		
		// Inicializamos el disco de Alberti.
		AlbertiDisk Cypher = new AlbertiDisk();
		
		// El programa se ejecutará hasta que el usuario seleccione salir, lo que cambiará el valor del flag y terminará el bucle.
		while(flag) {
			// Mostramos el texto del menú por pantalla.
			System.out.println("Bienvenido al cifrador de Alberti\n"
					+ "¿Qué desea hacer?\n"
					+ "1. Cifrar un mensaje\n"
					+ "2. Descifrar un mensaje\n"
					+ "3. Salir");
			
			// Pedimos al usuario que introduzca la opción que desee por pantalla.
			System.out.println("Seleccione una opción: ");
			option = sc.nextLine().trim();
			
			// Hacemos un switch que compruebe la opción elegida por el usuario para que el programa actúe en consecuencia.
			switch(option) {
			// El programa cifrará un mensaje introducido por pantalla.
			case "1":
				// Inicializamos el StringBuilder con parte del mensaje que devolveremos por pantalla.
				coloredMessage = new StringBuilder("El código cifrado es: ");
				// Comenzamos siempre devolviendo el disco a su posición inicial, ya que se puede no ser la primera vez que lo usemos.
				Cypher.restartDiscState();
				// Pedimos el mensaje a cifrar por pantalla.
				System.out.println("Introduce el mensaje a cifrar: ");
				message = sc.nextLine();
				// Si el usuario ha introducido algún caracter que no existe en el disco, se producirá un error.
				// Controlamos que ese error no interrumpa el programa; sólo devolvemos el mensaje del error.
				try {
					// Almacenamos el código cifrado para poder colorearlo al mostrarlo por pantalla.
					message = Cypher.cypher(message);
					// Recorremos cada uno de los caracteres del mensaje para poder colorearlos individualmente.
					for (int i = 0; i < message.length(); i++) {
						// Almacenamos el índice del color que corresponde al índice de caracter actual.
						value = restraintColorValue(i);
						// Coloreamos el caracter usando el índice de color anterior para el fondo y llamando a un método para que seleccione el
						// color que tendrá el caracter en función del color del fondo. El método reset() evitará que el color se mantenga en el texto posterior.
						coloredMessage.append(ansi().fg(niceFGValue(value)).bg(value).a(message.charAt(i)).reset());
					}
					// Añadimos un cambio de línea para que el texto siguiente no aparezca junto y quede más bonito.
					coloredMessage.append("\n");
					// Mostramos el código cifrado por pantalla.
					System.out.println(coloredMessage.toString());
				}
				// Si se produce un error al introducir un caracter que no exista en el disco, devolvemos un mensaje.
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			// El programa descifrará un mensaje introducido por pantalla.
			case "2":
				// Comenzamos siempre devolviendo el disco a su posición inicial, ya que se puede no ser la primera vez que lo usemos.
				Cypher.restartDiscState();
				// Pedimos el código a descifrar por pantalla.
				System.out.println("Introduce el código a descifrar: ");
				message = sc.nextLine();
				// Si el usuario ha introducido algún caracter que no existe en el disco, se producirá un error.
				// Controlamos que ese error no interrumpa el programa; sólo devolvemos el mensaje del error.
				try {
					// Mostramos el mensaje descifrado por pantalla con un fondo amarillo y los caracteres en negro.
					// El método reset() evitará que el color se mantenga en el texto posterior.
					System.out.println("El mensaje descifrado es: " + ansi().fg(0).bg(3).a(Cypher.decypher(message)).reset() + "\n");
				}
				// Si se produce un error al introducir un caracter que no exista en el disco, devolvemos un mensaje.
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			// El programa terminará.
			case "3":
				// Cerramos el escáner porque no van a introducirse más datos.
				sc.close();
				// Cambiamos el valor del flag para salir del bucle y terminar del programa.
				flag = false;
				break;
			// El programa informará de que se ha introducido una opción no válida.
			default:
				System.out.println("Seleccione una opción válida \n");
				break;
			}
		}

	}
	
	/**
	 * Método recursivo que se asegurará de que el valor del color que vamos a usar se mantenga
	 * dentro de unos límites establecidos teniendo en cuenta el índice del caracter que estamos
	 * coloreando.
	 * 
	 * @param value El índice del caracter cuyo fondo se va a colorear.
	 * @return El índice del color con el que se va a colorear.
	 */
	public static int restraintColorValue(int value) {
		// Constante que almacena el máximo de colores que vamos a usar al colorear.
		final int MAXCOLORS = 9;
		// Comprobamos que el índice del caracter no se pase del índice del último color que consideramos válido.
		if (value > MAXCOLORS - 1) {
			// Si se pasara, lo corregimos restándole el máximo de colores.
			value = value - MAXCOLORS;
			// Llamamos al método de nuevo para comprobar si el índice se sigue pasando.
			value = restraintColorValue(value);			
		}
		// Devolvemos el índice del color.
		return value;	
	}

	/**
	 * Método que se asegura de que el color del caracter sea agradable a la vista teniendo
	 * en cuenta el índice del color que está pintando el fondo.
	 * 
	 * El valor de los colores es el siguiente:
	 * BLACK --> 0
	 * RED --> 1
	 * GREEN --> 2
	 * YELLOW --> 3
	 * BLUE --> 4
	 * MAGENTA --> 5
	 * CYAN --> 6
	 * WHITE --> 7
	 * DEFAULT --> 8
	 * 
	 * @param value El índice de color que tiene el fondo.
	 * @return El índice de color que tendrá el caracter.
	 */
	public static int niceFGValue(int value) {
		// Variable que almacenará el índice del color con el que se pintará el caracter.
		int bgvalue = 0;
		// Usamos un switch para controlar todos los casos.
		switch (value) {
		// Si el fondo es negro, el caracter será verde.
		case 0:
			bgvalue = 2;
			break;
		// Si el fondo es rojo, el caracter será amarillo.
		case 1:
			bgvalue = 3;
			break;
		// Si el fondo es verde, el caracter será azul.
		case 2:
			bgvalue = 4;
			break;
		// Si el fondo es amarillo, el caracter será  magenta.
		case 3:
			bgvalue = 5;
			break;
		// Si el fondo es azul, el caracter será cian.
		case 4:
			bgvalue = 6;
			break;
		// Si el fondo es magenta, el caracter será gris.
		case 5:
			bgvalue = 8;
			break;
		// Si el fondo es cian, el caracter será blanco.
		case 6:
			bgvalue = 7;
			break;
		// Si el fondo es blanco, el caracter será negro.
		case 7:
			bgvalue = 0;
			break;
		// Si el fondo es gris, el caracter será rojo.
		case 8:
			bgvalue = 1;
			break;
		}
		// Devolvemos el índice del color que tendrá el caracter.
		return bgvalue;
	}
	
}
