package unam.ciencias.computoconcurrente.user;

/**
 * Clase que modela un lector que se ejecuta en un hilo
 */

public class Reader extends User {

    /**
     * Construye un nuevo lector
     * @param id es el identificador (entero positivo) del lector
     */

    public Reader (int id) {
        super (id);
    }

    /**
     * Inicia la ejecuación del proceso del escritor
     */

    public void run () {
        try {
            while (true) {
                database.startReading (id); // El lector entra a la base de datos
                sleepRandomTime ();         // El lector lee datos
                database.stopReading (id);  // El lector sale de la base de datos
                sleepRandomTime ();         // El lector procesa los datos que leyó
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

}
