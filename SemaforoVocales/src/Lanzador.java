import java.util.concurrent.Semaphore;

class TotalVocales { // PROTEGE EL ACUMULADOR DE VOCALES

	public static Integer acumuladorVocales = 0;// CADA VEZ QUE ENCUENTRE UNA VOCAL UNO DE LOS HILOS VA A INTENTAR ENTRAR AQUI

}

public class Lanzador { // LLAMA A 5 HILOS

	public static void main(String[] args) {

		Semaphore principal = new Semaphore(1); // LE INDICAMOS QUE SOLO 1 RECURSOS ACCEDA A LA SECCION CRITICA, PODRIAN SER 2, 3, ...
		char[] vocales = { 'a', 'e', 'i', 'o', 'u' };
		Hilo [] hilos = new Hilo [5];
		Integer i = 0;
		for (i = 0; i < vocales.length; i++) { // LENGHT DE LAS VOCALES ES 5, POR TANTO CREA 5 HILOS
			hilos[i] = new Hilo(vocales[i], principal, "El perro de san Roque no tiene rabo porque Ramon...");
			hilos[i].start();
		}
		
		for (i = 0; i < vocales.length; i++) { // LENGHT DE LAS VOCALES ES 5, POR TANTO CREA 5 HILOS
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("TOTAL DE VOCALES "+TotalVocales.acumuladorVocales);
	}
}

class Hilo extends Thread { // CLASE QUE VA A GESTIONAR LOS DIFERENTES HILOS
	//ATRIBUTOS
	private char vocal;
	private Semaphore semaforo = null; // PARA QUE CADA VEZ QUE SE INICIE UNA NUEVA INSTANCIA NO SE VUELVA LOCO
	private String cadenaBusqueda = "";
	private Integer acumuladorIndividual = 0;
	//CONSTRUCTOR
	Hilo(char voc, Semaphore principal, String texto) { // CONSTRUCTOR
		vocal = voc;
		semaforo = principal;
		cadenaBusqueda = texto;
	}

	private void contar() {
		for (Integer i = 0; i < cadenaBusqueda.length(); i++) {
			if (cadenaBusqueda.toLowerCase().charAt(i) == vocal) {
				TotalVocales.acumuladorVocales++;
				acumuladorIndividual++;
			}
		}
		System.out.println("HAY UN TOTAL DE " + acumuladorIndividual + " " + vocal);
	}

	public void run() {

		// ESTO ES PARA GENERAR UNA ESPERA PARA QUE LO PODAMOS VER CON LOS OJOS
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Integer i = 0;
		for (i = 0; i < 1000; i++) {
		//ESTO SOLO DEJA PASAR EL TIEMPO, PODRIA SER CON UN SLEEP
		}
		contar();
		semaforo.release();
		
	}
//JAVA ES EL ENCARGADO DE HACER UN ALGORITMO DE PLANIFICACIÓN DE LOS HILOS, EN LOS PROCESOS ES EL SISTEMA OPERATIVO EL QUE LO HACE (FIFO,LIFO,ETC).
}
