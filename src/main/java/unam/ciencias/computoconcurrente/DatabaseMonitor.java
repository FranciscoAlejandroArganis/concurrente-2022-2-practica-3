package unam.ciencias.computoconcurrente;

import java.util.ArrayList;

/**
 * Monitor para sincronizar el acceso a los recursos de una base de datos compartida
 */

public class DatabaseMonitor {

    private boolean currentlyWriting;         // Indica si hay un escritor escribiendo en la base de datos
    private int currentlyReading;             // Indica la cantidad de lectores leyendo en la base de datos
    private int accessGranted;                // Indica el permiso actual para que acceda un usuario a la base datos (0 significa que no se ha otorgado ningún permiso)
    private ArrayList <Integer> waitingUsers; // Cola en la que se forman los usuarios para acceder a la base de datos
                                              // Los lectores se forman en la cola con su identificador
                                              // Los escritores se forman en la cola con la negación de su identificador

    /**
     * Construye un nuevo monitor
     */

    public DatabaseMonitor () {
        waitingUsers = new ArrayList <> ();
    }

    /**
     * Permite que un lector adquiera los recursos de la base de datos
     * Se permite que múltiples lectores lean la base de datos al mismo tiempo
     * El lector debe esperar para acceder a la base de datos si ya un escritor usándola
     * @param id es el identificador (entero positivo) del lector
     * @throws InterruptedException
     */

    public synchronized void startReading (int id) throws InterruptedException {
        waitingUsers.add (id);               // El lector se forma en la cola de espera
        requestAccess ();                    // Se solicita permiso para acceder a la base de datos
        while (accessGranted != id) wait (); // Se espera a que el permiso otrogado coincida con el identificador del lector
        waitingUsers.remove (0);             // El lector deja la cola de espera y procede a leer la base de datos
        currentlyReading ++;                 // Se incrementa el contador de lectores activos en la base de datos
        System.out.println ("Lector " + id + " empieza a leer la base de datos");
    }

    /**
     * Permite que un lector libere los recursos de la base de datos
     * No afecta a otros lectores que estén usando la base de datos al mismo tiempo
     * @param id es el identificador (entero positivo) del lector
     */

    public synchronized void stopReading (int id) {
        currentlyReading --; // El lector sale de la base de datos
        requestAccess ();    // Se revisa si es posible otorgar otro permiso de acceso
        System.out.println ("Lector " + id + " termina de leer la base de datos");
    }

    /**
     * Permite que un escritor adquiera los recursos de la base de datos
     * El escritor debe esperar para acceder a la base de datos si ya otro usuario (lector o escritor) usándola
     * @param id es el identificador (entero positivo) del escritor
     * @throws InterruptedException
     */

    public synchronized void startWriting (int id) throws InterruptedException {
        waitingUsers.add (- id);               // El escritor se forma en la cola de espera
        requestAccess ();                      // Se solicita permiso para acceder a la base de datos
        while (accessGranted != - id) wait (); // Se espera a que el permiso otrogado coincida con el identificador del escritor
        waitingUsers.remove (0);               // El escritor deja la cola de espera y procede a escrinir la base de datos
        currentlyWriting = true;               // Se hace true la bandera de escritor activo en la base de datos
        System.out.println ("Escritor " + id + " empieza a escribir la base de datos");
    }

    /**
     * Permite que un escritor libere los recursos de la base de datos
     * Al terminar de escribir, ningún otro usuario estará usando la base de datos
     * @param id es el identificador (entero positivo) del escritor
     */

    public synchronized void stopWriting (int id) {
        currentlyWriting = false; // El escritor sale de la base de datos
        requestAccess ();         // Se revisa si es posible otorgar otro permiso de acceso
        System.out.println ("Escritor " + id + " termina de escribir la base de datos");
    }

    /**
     * Intenta otorgar un permiso de acceso para el usuario al frente de la cola de espera
     * Si el usuario es un lector y no hay un escritor usándola en este momento, se permite el acceso
     * Si el usuario es un escritor y no hay ningún usuario (escritor o lector) usándola en este momento, se permite el acceso
     * Si alguna de las condiciones anteriores no se cumplen, no se otorga ningún permiso y el usuario deberá seguir esperando
     */

    private void requestAccess () {
        if (waitingUsers.isEmpty ()) return;                                                     // Si no hay usuarios esperando, no se hace nada
        int user = waitingUsers.get (0);                                                         // Se considera el usuario al frente de la cola de espera
        boolean denied = user > 0 ? currentlyWriting : currentlyWriting || currentlyReading > 0; // Se verifica si el usuario puede acceder a la base de datos sin conflictos
        if (denied) accessGranted = 0;                                                           // Si el usuario no puede acceder en este momento, no se otorga ningún permiso y deberá seguir esperando
        else {
            accessGranted = user;                                                                // Si no hay conflictos, se otorga un permiso al usuario para acceder a la base de datos
            notifyAll ();                                                                        // Se avisa a los usuarios en la cola de espera que se ha otorgado un permiso de acceso
        }
    }

}
