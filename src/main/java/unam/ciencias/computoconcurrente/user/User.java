package unam.ciencias.computoconcurrente.user;

import java.util.Random;
import unam.ciencias.computoconcurrente.DatabaseMonitor;

/**
 * Clase que modela un usuario de la base de datos que se ejecuta en un hilo
 */

public abstract class User implements Runnable {
    
    protected static Random rng = new Random ();                        // Generador de números aleatorios
    protected static long maxTime = 0;                                  // Tiempo máximo que pasará un usuario inactivo (fuera de la base datos) o
                                                                        // activo (leyendo o escribiendo la base de datos)
    protected static DatabaseMonitor database = new DatabaseMonitor (); // El monitor de la base de datos compartida

    protected int id;                                                   // El identificador de un usuario de la base de datos

    /**
     * Inicialización de un usuario de la base de datos
     * Actualiza el tiempo máximo de lectura, escritura e inactividad de los usuarios
     * En todo momento, el tiempo máximo (milisegundos) es 400 veces la cantidad de usuarios totales de la base de datos
     * @param id es el identificador (entero positivo) del lector
     */

    protected User (int id) {
        if (id <= 0) throw new IllegalArgumentException ();
        this.id = id;
        maxTime += 400;
    }

    /**
     * El hilo del usuario se duerme por una cantidad aleatoria de tiempo, acotada por maxTime
     */

    protected void sleepRandomTime () throws InterruptedException {
        long time = rng.nextLong () % maxTime;
        if (time < 0) time += maxTime;
        Thread.sleep (time);
    }
}
