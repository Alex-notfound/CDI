/* Ejercicio en el que se utilizara wait() y notifyAll() para que los hilos esperen a que el objeto ClassA 
sea liberado para utilizarlo y cuando acaben de utilizarlo notifiquen de que han terminado.
Para llevar un orden de los hilos en la ejecucion del metodo hemos añadido un atributo "puesto" en ClassB y un atributo
"turno" en ClassA, empleando un metodo de fuerza bruta como exige el enunciado. */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {
        ClassA a = new ClassA();
        ArrayList<Thread> hilos = new ArrayList<Thread>();

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new ClassB(a, i));
            thread.start();
            hilos.add(thread);
        }
        //Dormimos por un tiempo para que le de tiempo a los hilos a llegar al wait()
        //Es una manera cutre, proximamente se explicara una manera mas profesional
        Thread.sleep(100);
        synchronized (a) {
            a.notifyAll();
        }
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).join();
        }
        Iterator<Long> coleccion = a.getThreadIds().iterator();
        System.out.println("-- FINAL -- Contador: " + a.counter);
        while (coleccion.hasNext()) {
            System.out.println("Hilo: " + coleccion.next());
        }
    }
}

class ClassA extends Thread {

    int counter;
    private Set<Long> threadIds;
    int turno;

    public ClassA() {
        this.counter = 10;
        threadIds = new TreeSet<Long>();
        turno = 0;
    }

    public Set<Long> getThreadIds() {
        return threadIds;
    }

    void EnterAndWait() throws InterruptedException {
        this.counter--;
        threadIds.add(Thread.currentThread().getId());
        System.out.println("Inicia hilo " + Thread.currentThread().getId());
        Thread.sleep(10);
        System.out.println("Acaba hilo " + Thread.currentThread().getId());
        turno++;
    }

    boolean isFinished() {
        return this.counter == 0;
    }
}

class ClassB implements Runnable {

    private ClassA a;
    private int puesto;

    public ClassB(ClassA a, int puesto) {
        this.a = a;
        this.puesto = puesto;
    }

    @Override
    public void run() {
        try {
            //Añadimos synchronized para que solo un hilo pueda utilizar el objeto simultaneamente
            synchronized (a) {
                while (a.turno != this.puesto) {
                    a.wait();
                }
                if (!a.isFinished()) {
                    System.out.println("Entra hilo con puesto " + this.puesto);
                    a.EnterAndWait();
                }
                a.notifyAll();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ClassB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
