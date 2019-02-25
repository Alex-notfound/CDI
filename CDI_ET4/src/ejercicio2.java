//En este ejercicio se usan 2 contadores (p y q), los cuales siempre se incrementan al ejecutar un hilo
//y despues se sumara al contador q el contador p (q=q+p) en el propio hilo.
//De esta manera realizamos una operacion sencilla entre los contadores sin que los hilos se pisen

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ejercicio2 {

    public static void main(String[] args) throws InterruptedException {

        Counter2 contador = new Counter2();
        Counter2 p = new Counter2();
        ArrayList<MyTask2> array = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask2 e = new MyTask2(contador, p);
            e.start();
            array.add(e);
        }
        for (int i = 0; i < array.size(); i++) {
            array.get(i).join();
        }
        System.out.println("Contador al finalizar: " + contador.getContador());
    }
}

class Counter2 {

    private AtomicInteger contador = new AtomicInteger();
    //Creacion del objeto lock
    Lock lock = new ReentrantLock();

    public AtomicInteger getContador() {
        return contador;
    }

    public void increment() {
        contador.incrementAndGet();
    }

}

class MyTask2 extends Thread {

    private Counter2 q;
    private Counter2 p;

    public MyTask2(Counter2 q, Counter2 p) {
        this.q = q;
        this.p = p;
    }

    public void run() {
        try {
            Thread.sleep((long) Math.random() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        q.lock.lock();
        try {
            q.increment();
            p.increment();
            System.out.println("Valor de Contador q en hilo: " + q.getContador());
            System.out.println("Valor de Contador p en hilo: " + p.getContador());
            q.getContador().addAndGet(p.getContador().get());
            System.out.println("Valor de Contador q tras la adicion: " + q.getContador());
        } finally {
            q.lock.unlock();
        }
    }
}
