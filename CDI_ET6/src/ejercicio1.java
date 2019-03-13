//Ejercicio en el que se utilizara wait() y notify()/notifyAll() para que los hilos esperen a que el objeto ClassC 
//sea liberado para utilizarlo y cuando acaben de utilizarlo notifiquen de que han terminado
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {
        ClassC a = new ClassC();
        ArrayList<Thread> hilos = new ArrayList<Thread>();

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new ClassD(a));
            thread.start();
            hilos.add(thread);
        }
        //Dormimos por un tiempo para que le de tiempo a los hilos a llegar al wait()
        //Es una manera cutre, proximamente se explicara una manera mas profesional
        Thread.sleep(100);
        synchronized (a) {
            a.notifyAll();
        }
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).join();
        }
        Iterator<Long> coleccion = a.getThreadIds().iterator();
        System.out.println("-- FINAL -- Contador: " + a.counter);
        while (coleccion.hasNext()) {
            System.out.println("Hilo: " + coleccion.next());
        }
    }
}

class ClassC extends Thread {

    int counter;
    private Set<Long> threadIds;

    public ClassC() {
        this.counter = 10;
        threadIds = new TreeSet<Long>();
    }

    public Set<Long> getThreadIds() {
        return threadIds;
    }

    void EnterAndWait() throws InterruptedException {
        this.counter--;
        threadIds.add(Thread.currentThread().getId());
        System.out.println("Inicia hilo " + Thread.currentThread().getId());
        Thread.sleep(10);
        System.out.println("Acaba hilo " + Thread.currentThread().getId());
    }

    boolean isFinished() {
        return this.counter == 0;
    }
}

class ClassD implements Runnable {

    private ClassC a;

    public ClassD(ClassC a) {
        this.a = a;
    }

    @Override
    public void run() {
        try {
            //Añadimos synchronized para que solo un hilo pueda utilizar el objeto simultaneamente
            synchronized (a) {
                a.wait();
                if (!a.isFinished()) {
                    a.EnterAndWait();
                }
                a.notifyAll();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ClassD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
