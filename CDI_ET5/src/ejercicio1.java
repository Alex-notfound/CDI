
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {
        ClassA a = new ClassA();
        ArrayList<Thread> hilos = new ArrayList<Thread>();
        String[] colores = {"rojo", "negro"};
        Random rnd = new Random();

        //Creamos varios hilos, asignandoles diferentes colores, los iniciamos y los añadimos a un ArrayList
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ClassB(a, colores[rnd.nextInt(2)]));
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
            try {
                hilos.get(i).join();
            } catch (Exception e) {
            }
        }
        
        //Esta parte solo se ejecutara si todos los hilos han podido ejecutar el metodo EnterAndWait()
        Iterator<Long> coleccion = a.getThreadIds().iterator();
        System.out.println("-- FINAL -- Contador: " + a.counter);
        while (coleccion.hasNext()) {
            System.out.println("Hilo: " + coleccion.next());
        }
    }
}

class ClassA extends Thread {

    int counter;
    private Set<Long> threadIds;
    private String ultimoColor;

    public ClassA() {
        this.counter = 10;
        threadIds = new TreeSet<Long>();
    }

    public String getUltimoColor() {
        return ultimoColor;
    }

    public void setUltimoColor(String ultimoColor) {
        this.ultimoColor = ultimoColor;
    }

    public Set<Long> getThreadIds() {
        return threadIds;
    }

    void EnterAndWait() throws InterruptedException {
        this.counter--;
        threadIds.add(Thread.currentThread().getId());
        Thread.sleep(10);
    }

    boolean isFinished() {
        return this.counter == 0;
    }
}

class ClassB implements Runnable {

    private ClassA a;
    private String color;

    public ClassB(ClassA a, String color) {
        this.a = a;
        this.color = color;
    }

    @Override
    public void run() {
        try {
            //Añadimos synchronized para que solo un hilo pueda utilizar el objeto simultaneamente
            synchronized (a) {
                a.wait();
                while (!a.isFinished()) {
                    if (a.getUltimoColor() != color) {
                        System.out.println("Hilo " + Thread.currentThread().getId() + " de color " + color + " inicia EnterAndWait()");
                        a.EnterAndWait();
                        a.setUltimoColor(color);
                        break;
                    } else {
                        try {
                            a.wait();
                        } catch (Exception e) {
                        }
                    }
                }
                a.notifyAll();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ClassB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}