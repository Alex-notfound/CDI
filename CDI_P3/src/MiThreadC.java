import java.util.Map;

public class MiThreadC extends Thread {

    private ThreadLocal<Integer> valor = new ThreadLocal<Integer>();
    private Map<Thread, Integer> mapa;

    public void run() {
        try {
            Thread.sleep((long) Math.random() * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int random = (int) (Math.random() * 1000);
        System.out.println("Hilo " + Thread.currentThread().getId()
                + " " + random);
        this.valor.set(new Integer(random));
    }

    public ThreadLocal<Integer> getValue() {
        return valor;
    }

    public static void main(String[] args) throws InterruptedException {
        MiThreadC thread1 = new MiThreadC();
        MiThreadC thread2 = new MiThreadC();

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
