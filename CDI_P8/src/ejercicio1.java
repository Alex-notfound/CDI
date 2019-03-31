
import java.util.ArrayList;

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

    ArrayList lista;
    int capacidad;
    MySemaphore notEmpty, notFull;

    public Buffer() throws InterruptedException {
        lista = new ArrayList<Integer>();
        capacidad = 1000;
        notEmpty = new MySemaphore(1);
        notFull = new MySemaphore();
        notEmpty.down();
        notFull.up();
    }

    void write() throws InterruptedException {
        notEmpty.up();
        try {
            if (lista.size() < capacidad) {
                int e = (int) Math.floor(Math.random() * 9);
                lista.add(e);
                System.out.println("Añadido " + e);
            }
        } finally {
            notFull.down();
        }
    }

    void read() throws InterruptedException {
        notFull.up();
        try {
            if (lista.size() > 0) {
                System.out.println("Eliminado " + lista.remove(0));
            }
        } finally {
            notEmpty.down();
        }
    }
}

class MySemaphore {

    int valor;

    public MySemaphore(int initialValue) {
        valor = initialValue;
    }

    public MySemaphore() {
        this(0);
    }

    public synchronized void down() throws InterruptedException {
        if (valor <= 0) {
            this.wait();
        }
        valor--;
    }

    public synchronized void up() {
        valor++;
        this.notifyAll();
    }
}
