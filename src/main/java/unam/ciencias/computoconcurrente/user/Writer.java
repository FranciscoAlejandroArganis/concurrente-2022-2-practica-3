package unam.ciencias.computoconcurrente.user;

/**
 * Clase que modela un escritor que se ejecuta en un hilo
 */

public class Writer extends User {

    /**
     * Construye un nuevo escritor
     * @param id es el identificador (entero positivo) del escritor
     */

    public Writer (int id) {
        super (id);
    }

    /**
     * Inicia la ejecuación del proceso del escritor
     */

    public void run () {
        try {
            while (true) {
                sleepRandomTime ();         // El escritor produce datos para escribirlos
                database.startWriting (id); // El escritor entra a la base de datos
                sleepRandomTime ();         // El escritor escribe sus datos
                database.stopWriting (id);  // El escritor sale de la base de datos
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

}
