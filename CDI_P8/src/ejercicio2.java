//En este ejercicio utilizamos la clase semaforo de Java, muy similar al ejercicio anterior
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ejercicio2 {

    public static void main(String args[]) throws InterruptedException {

        Buffer2 buffer = new Buffer2();
        Productor2[] prod = new Productor2[10];
        Consumidor2[] cons = new Consumidor2[10];

        //Crea e inicia varios hilos Productores y Consumidores
        for (int i = 0; i < prod.length; i++) {
            prod[i] = new Productor2(buffer);
            cons[i] = new Consumidor2(buffer);
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

class Productor2 extends Thread {

    Buffer2 buffer;

    public Productor2(Buffer2 buffer) {
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

class Consumidor2 extends Thread {

    Buffer2 buffer;

    public Consumidor2(Buffer2 buffer) {
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

class Buffer2 {

    ArrayList lista;
    int capacidad;
    Semaphore notEmpty, notFull;

    public Buffer2() throws InterruptedException {
        capacidad = 1000;
        lista = new ArrayList<>(capacidad);
        notEmpty = new Semaphore(10, false);
        notFull = new Semaphore(10, true);
    }

    void write() throws InterruptedException {
        notEmpty.acquire();
        try {
            if (lista.size() < capacidad) {
                int e = (int) Math.floor(Math.random() * 9);
                try {
                    lista.add(e);
                    System.out.println("Añadido " + e);
                } catch (Exception ex) {
                }
            }
        } finally {
            notFull.release();
        }
    }

    void read() throws InterruptedException {
        notFull.acquire();
        try {
            if (lista.size() > 0) {
                try {
                    System.out.println("Eliminado " + lista.remove(0));
                } catch (Exception ex) {
                }
            }
        } finally {
            notEmpty.release();
        }
    }
}