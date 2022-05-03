package unam.ciencias.computoconcurrente;

import unam.ciencias.computoconcurrente.user.*;

/**
 * Clase que implementa una solución al problema Readers Writers
 * El programa debe recibir como argumento por la línea de comandos dos enteros positivos
 * El primer argumento se toma como la cantidad de lectores
 * El segundo argumento se toma como la cantidad de escritores
 */

public class App {

    /**
     * Inicia la ejecución del programa
     * @param args son los argumentos de la línea de comandos
     */

    public static void main (String [] args) {

        int readers = 0;
        int writers = 0;
        try {
            readers = Integer.parseInt (args [0]);
            writers = Integer.parseInt (args [1]);
            if (readers <= 0 || writers <= 0) throw new Exception ();
        } catch (Exception e) {
            System.out.println ("El programa debe recibir como primeros dos argumentos un par de enteros positivos.");
            System.out.println ("El primer argumento es la cantidad de lectores y el segundo es la cantidad de escritores.");
            System.exit (0);
        }

        // Se crean los lectores y escritores
        Thread [] users = new Thread [readers + writers];
        int index = 0;
        while (index < users.length) {
            if (index < readers) users [index] = new Thread (new Reader (index + 1));
            else users [index] = new Thread (new Writer (index - readers + 1));
            index ++;
        }

        // Inicia la ejecución de los hilos
        for (Thread thread : users) thread.start ();
        
    }
}
