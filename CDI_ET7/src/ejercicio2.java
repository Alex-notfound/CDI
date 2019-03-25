//Ejercicio tipico del Productor/Consumidor para ver como se produce una situacion deadlock

import java.util.ArrayList;

public class ejercicio2 {

    public static void main(String args[]) throws InterruptedException {
        Buffer buffer = new Buffer();
        Productor[] prod = new Productor[10];
        Consumidor[] cons = new Consumidor[10];
        for (int i = 0; i < prod.length; i++) {
            prod[i]=new Productor(buffer);
            cons[i]=new Consumidor(buffer);
            prod[i].start();
            cons[i].start();
        }
    }

}

class Productor2 extends Thread {

    Buffer buffer;

    public Productor2(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (buffer) {
                if (buffer.notFull) {
                    buffer.write();
                    buffer.notify();
                } else {
                    try {
                        buffer.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }
    }
}

class Consumidor2 extends Thread {

    Buffer buffer;

    public Consumidor2(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (buffer) {
                if (buffer.notEmpty) {
                    buffer.read();
                    buffer.notify();
                } else {
                    try {
                        buffer.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }
    }
}

class Buffer2 {

    ArrayList lista;
    int capacidad;
    public boolean notFull, notEmpty;

    public Buffer2() {
        lista = new ArrayList<Integer>();
        capacidad = 1000;
        notFull = true;
        notEmpty = false;
    }

    void write() {
        int e = (int) Math.floor(Math.random() * 9);
        lista.add(e);
        System.out.println("Añadido " + e);
        
        //Estas lineas son criticas para producir el deadlock
        notEmpty = true;
        notFull = lista.size() >= capacidad;
    }

    void read() {
        System.out.println("Eliminado " + lista.remove(0));
        
        //Estas lineas son criticas para producir el deadlock
        notFull = true;
        notEmpty = !lista.isEmpty();
    }
}
