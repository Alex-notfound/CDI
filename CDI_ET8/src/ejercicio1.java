
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ejercicio1 {

    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println(Runtime.getRuntime().availableProcessors());
        miHilo[] hilos = new miHilo[40];
        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new miHilo((int) Math.floor(Math.random() * 10));
            executor.execute(hilos[i]);
        }
        executor.shutdown();
        System.out.println("Ejecutor cerrado");
        while (!executor.isTerminated()) {
        }
    }
}

class miHilo implements Runnable {

    int num;

    public miHilo(int num) {
        this.num = num;
    }

    public void run() {
        int result = 1;
        if (num > 1) {
            for (int i = 2; i <= num; i++) {
                result *= i;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
        System.out.println("El factorial de " + num + " es: " + result);
    }
}
