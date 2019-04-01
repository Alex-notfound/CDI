/* 
En este ejercicio utilizamos la clase Executor de Java, de manera que ejecuta tantos hilos como numero de
procesadores disponibles para calcular el factorial de varios numeros aleatorios
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ejercicio1 {

    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println(Runtime.getRuntime().availableProcessors());
        tarea[] tareas = new tarea[40];
        for (int i = 0; i < tareas.length; i++) {
            tareas[i] = new tarea((int) Math.floor(Math.random() * 10));
            executor.execute(tareas[i]);
        }
        executor.shutdown();
        System.out.println("Ejecutor cerrado");
        while (!executor.isTerminated()) {
        }
    }
}

class tarea implements Runnable {

    int num;

    public tarea(int num) {
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
