//En este ejercicio utilizamos el objeto AtomicInteger para tener conocimiento sobre su existencia
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ejercicio3 {

    public static void main(String[] args) throws InterruptedException {

        Counter3 contador = new Counter3();
        ArrayList<MyTask3> array = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask3 e = new MyTask3(contador);
            e.start();
            array.add(e);
        }
        for (int i = 0; i < array.size(); i++) {
            array.get(i).join();
        }
        System.out.println("Contador: " + contador.getContador());
    }
}

class Counter3 {

    private AtomicInteger contador = new AtomicInteger();

    public AtomicInteger getContador() {
        return contador;
    }

    public void increment() {
        contador.incrementAndGet();
    }
}

class MyTask3 extends Thread {

    private Counter3 a;

    public MyTask3(Counter3 a) {
        this.a = a;
    }

    public void run() {
        try {
            Thread.sleep((long) Math.random() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (a) {
            a.increment();
            System.out.println("Valor de Contador3 en hilo: " + a.getContador());
        }
    }
}
