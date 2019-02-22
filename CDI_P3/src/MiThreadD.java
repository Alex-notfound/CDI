public class MiThreadD extends Thread {

    private boolean negative;
    private float pi;
    private boolean detener = false;

    public void run() {

        while (!detener) {
            for (int i = 3; i < 100000; i += 2) {
                if (negative) {
                    pi -= (1.0 / i);
                } else {
                    pi += (1.0 / i);
                }
                negative = !negative;
            }
            pi += 1.0;
            pi *= 4.0;
        }
        System.out.println(this.getId() + " " + pi );
    }

    public static void main(String[] args) throws InterruptedException {
        MiThreadD thread1 = new MiThreadD();
        MiThreadD thread2 = new MiThreadD();

        thread1.start();
        thread2.start();
        try {
            Thread.sleep((long) Math.random() * 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.detener = true;
        Thread.sleep((long) Math.random() * 1000);
        thread2.detener = true;

    }
}
