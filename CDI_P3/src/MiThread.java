public class MiThread extends Thread {

    public void run() {
        int random = (int) (Math.random() * 1000);
        try {
            Thread.sleep((long) Math.random() * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished" + " " + this.getId() + " " + random);
    }

    public static void main(String[] args) throws InterruptedException {
        MiThread thread1 = new MiThread();
        MiThread thread2 = new MiThread();

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

}
