//En este ejercicio utilizaremos synchronized evitando la colision de los valores del contador
import java.util.ArrayList;

public class ejercicio2 {

    public static void main(String[] args) throws InterruptedException {

        Counter2 contador = new Counter2();
        ArrayList<MyTask2> array = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask2 e = new MyTask2(contador);
            e.start();
            array.add(e);
        }
        for (int i = 0; i < array.size(); i++) {
            array.get(i).join();
        }
        System.out.println("Contador: " + contador.getContador());
    }
}

class Counter2 {

    private int contador = 0;

    public int getContador() {
        return contador;
    }

    public void increment() {
        contador++;
    }
}

class MyTask2 extends Thread {

    private Counter2 a;

    public MyTask2(Counter2 a) {
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
            System.out.println("Valor del Contador2 en hilo: " + a.getContador());
        }
    }

}
