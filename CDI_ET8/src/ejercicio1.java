/* 
En este ejercicio utilizamos la clase Executor de Java, de manera que ejecuta tantos hilos como numero de
procesadores disponibles para calcular el factorial de varios numeros aleatorios
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ejercicio1 {

    public static void main(String args[]) {
        //Creacion del executor utilizando un metodo que obtiene el numero de procesadores disponibles
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //Imprime el numero de procesadores disponibles
        System.out.println("Numero de procesadores disponibles: " + Runtime.getRuntime().availableProcessors());
        //Crea un array de tareas
        tarea[] tareas = new tarea[40];
        //Instancia y el executor ejecuta todas las tareas de manera ordenada
        for (int i = 0; i < tareas.length; i++) {
            tareas[i] = new tarea((int) Math.floor(Math.random() * 10));
            executor.execute(tareas[i]);
        }
        //Cierra el executor
        executor.shutdown();
        //Avisa de que el executor se ha cerrado
        System.out.println("Ejecutor cerrado");
        //EL programa continua su ejecucion mientras el executor no termine
        while (!executor.isTerminated()) {
        }
    }
}

class tarea implements Runnable {

    int num;

    //Constructor de la tarea, a la cual se le pasa como parametro un numero para calcular su factorial
    public tarea(int num) {
        this.num = num;
    }

    //Calcula el factorial de un numero
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
        //Imprime el resultado
        System.out.println("El factorial de " + num + " es: " + result);
    }
}
