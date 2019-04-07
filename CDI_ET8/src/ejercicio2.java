
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ejercicio2 {

    public static void main(String args[]) throws InterruptedException, ExecutionException {
        List<Future<Integer>> resultList = new ArrayList<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            FactorialTask calculator = new FactorialTask(random.nextInt(10));
            Future<Integer> result = executor.submit(calculator);
            resultList.add(result);
        }

        while (executor.getCompletedTaskCount() < resultList.size());
        
        System.out.println("Numero de tareas completadas: " + executor.getCompletedTaskCount());
        executor.shutdown();
        for (int i = 0; i < resultList.size(); i++) {
            Future<Integer> result=resultList.get(i);
            System.out.printf("Main: Task %d: %s\n", i, result.isDone());
            System.out.println("Core: Task " + i + ": " + result.get());
        }
    }
}

class FactorialTask implements Callable<Integer> {

    int num;

    //Constructor de la tarea2, a la cual se le pasa como parametro un numero para calcular su factorial
    public FactorialTask(int num) {
        this.num = num;
    }

    public Integer call() throws Exception {
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
        return result;
    }
}
