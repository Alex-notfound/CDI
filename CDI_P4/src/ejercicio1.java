//En este ejercicio vemos como chocan los valores del Contador en los diferentes hilos
import java.util.ArrayList;

public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {

        Counter contador = new Counter();
        ArrayList<MyTask> array = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
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

    private int contador = 0;

    public int getContador() {
        return contador;
    }

    public void increment() {
        contador++;
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
        a.increment();
        System.out.println("Valor de contador en hilo: " + a.getContador());
    }

}
