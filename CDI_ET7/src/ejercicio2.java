
import java.util.concurrent.ArrayBlockingQueue;

public class ejercicio2 {

    public static void main(String args[]) throws InterruptedException {
        ArrayBlockingQueue q = new ArrayBlockingQueue<Integer>(1000);
        Productor2[] prod = new Productor2[10];
        Consumidor2[] cons = new Consumidor2[10];
        
        //Crea e inicia varios hilos Productor2es y Consumidor2es
        for (int i = 0; i < prod.length; i++) {
            prod[i] = new Productor2(q);
            cons[i] = new Consumidor2(q);
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

    ArrayBlockingQueue buffer;
    int n;
    public Productor2(ArrayBlockingQueue buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep((long) (Math.random() * 5));
                n=(int) ((Math.random()*9));
                buffer.put(n);
                System.out.println("Añadido " + n);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}

class Consumidor2 extends Thread {

    ArrayBlockingQueue buffer;

    public Consumidor2(ArrayBlockingQueue buffer) {
        this.buffer = buffer;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep((long) (Math.random() * 5));
                System.out.println("Eliminado " + buffer.take());
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}