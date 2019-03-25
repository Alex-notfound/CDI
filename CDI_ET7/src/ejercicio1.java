
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ejercicio1 {

    public static void main(String args[]) throws InterruptedException {
        Buffer buffer = new Buffer();
        Productor[] prod = new Productor[10];
        Consumidor[] cons = new Consumidor[10];
        for (int i = 0; i < prod.length; i++) {
            prod[i] = new Productor(buffer);
            cons[i] = new Consumidor(buffer);
            prod[i].start();
            cons[i].start();
        }
        Thread.sleep(1000);
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

    private final ReentrantLock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    boolean anteriorProductor;
    ArrayList lista;
    int capacidad;

    public Buffer() {
        lista = new ArrayList<Integer>();
        capacidad = 1000;
        anteriorProductor = false;
    }

    void write() throws InterruptedException {
        lock.lock();
        try {
            while (lista.size() == capacidad || anteriorProductor) {
                notFull.await();
            }
            int e = (int) Math.floor(Math.random() * 9);
            lista.add(e);
            anteriorProductor = true;
            System.out.println("Añadido " + e);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    void read() throws InterruptedException {
        lock.lock();
        try {
            while (lista.size() == 0) {
                notEmpty.await();
            }
            anteriorProductor = false;
            System.out.println("Eliminado " + lista.remove(0));
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}
