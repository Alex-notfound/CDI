//Ejercicio que utiliza un objeto ClassA para ver como lo ejecutan varios hilos secuencialmente gracias a synchronized

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {
        ClassA a = new ClassA();
        ArrayList<Thread> hilos = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ClassB(a));
            thread.start();
            hilos.add(thread);
        }
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).join();
        }
    }
}

class ClassA extends Thread {

    void EnterAndWait() throws InterruptedException {
        System.out.println("Inicia ejecutando hilo " + Thread.currentThread().getId());
        Thread.sleep(100);
        System.out.println("Acaba ejecutando hilo " + Thread.currentThread().getId());
    }
}

class ClassB implements Runnable {

    private ClassA a;

    public ClassB(ClassA a) {
        this.a = a;
    }

    @Override
    public void run() {
        try {
            //Añadimos synchronized para que solo un hilo pueda utilizar el objeto simultaneamente
            synchronized (a) {
                a.EnterAndWait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ClassB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
