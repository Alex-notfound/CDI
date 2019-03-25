/* En este ejercicio se utiliza ReentrantLock y Conditions, junto a los metodos lock(), await() y signal()
para resolver el problema tipico del Productor/Consumidor.
A mayores se ha evitado que un productor añada 2 veces consecutivas */

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ejercicio1 {

    public static void main(String args[]) throws InterruptedException {
        
        Buffer buffer = new Buffer();
        Productor[] prod = new Productor[10];
        Consumidor[] cons = new Consumidor[10];
        
        //Crea e inicia varios hilos Productores y Consumidores
        for (int i = 0; i < prod.length; i++) {
            prod[i] = new Productor(buffer);
            cons[i] = new Consumidor(buffer);
            prod[i].start();
            cons[i].start();
        }
        
        Thread.sleep(1000);
        
        //Interrumpe todos los hilos ejecutados
        for (int i = 0; i < prod.length; i++) {
            prod[i].interrupt();
            cons[i].interrupt();
        }
    }
}

class Productor extends Thread {

    Buffer buffer;

    public Productor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep((long) (Math.random() * 5));
                buffer.write();
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}

class Consumidor extends Thread {

    Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep((long) (Math.random() * 5));
                buffer.read();
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}

class Buffer {
    //Se instancian los objetos a utilizar en este ejercicio para asegurar la coherencia
    private final ReentrantLock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    ArrayList lista;
    int capacidad;
    //Este booleano es el que determinara si el anterior hilo que utilizo el buffer era un productor
    boolean anteriorProductor;

    public Buffer() {
        lista = new ArrayList<Integer>();
        capacidad = 1000;
        anteriorProductor = false;
    }

    void write() throws InterruptedException {
        lock.lock();
        try {
            //Mientras el buffer esta lleno o ha sido utilizado por un productor la ultima vez, espera
            while (lista.size() == capacidad || anteriorProductor) {
                notFull.await();
            }
            int e = (int) Math.floor(Math.random() * 9);
            lista.add(e);
            anteriorProductor = true;
            System.out.println("Añadido " + e);
            //Avisa de que el buffer ya no esta vacio
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    void read() throws InterruptedException {
        lock.lock();
        try {
            //Mientras el buffer este vacio, espera
            while (lista.size() == 0) {
                notEmpty.await();
            }
            anteriorProductor = false;
            System.out.println("Eliminado " + lista.remove(0));
            //Avisa de que el buffer ya no esta lleno
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}
