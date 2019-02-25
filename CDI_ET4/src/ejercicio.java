//En este ejercicio utilizamos el objeto Lock para descubrir una nueva forma de bloquear recursos
//Es mas versatil, pero tras realizar pruebas, demora mas tiempo que si se utilizara un bloque synchronized
//Esto lo he comprobado llevando el contador hasta 10000, tardando el bloque synchronized 9 segundos y usando lock 10

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ejercicio {

    public static void main(String[] args) throws InterruptedException {

        Counter contador = new Counter();
        ArrayList<MyTask> array = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            MyTask e = new MyTask(contador);
            e.start();
            array.add(e);
        }
        for (int i = 0; i < array.size(); i++) {
            array.get(i).join();
        }
        System.out.println("Contador: " + contador.getContador());
    }
}

class Counter {

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

class MyTask extends Thread {

    private Counter a;

    public MyTask(Counter a) {
        this.a = a;
    }

    public void run() {
        try {
            Thread.sleep((long) Math.random() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Reemplazamos el bloque synchronized por la llamada al metodo lock seguida de un try para bloquear el recurso
        a.lock.lock();
        try {
            a.increment();
            System.out.println("Valor de Contador en hilo: " + a.getContador());
        } 
        //Finalmente llamamos al metodo unlock para liberar el recurso
        finally {
            a.lock.unlock();
        }
    }
}
