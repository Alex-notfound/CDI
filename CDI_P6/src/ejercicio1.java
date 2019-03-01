
public class ejercicio1 {

    public static void main(String[] args) throws InterruptedException {
        ClassA a = new ClassA();
        ClassB b1 = new ClassB(a);
        ClassB b2 = new ClassB(a);
        
        //Creacion de hilos utilizando el objeto ClassB
        Thread thread1 = new Thread(b1);
        Thread thread2 = new Thread(b2);
        
        //Refencio en cada hilo al otro hilo
        b1.setThread(thread2);
        b2.setThread(thread1);

        //Inicio los hilos
        thread1.start();
        thread2.start();

        //Espero a que los hilos se ejecuten y notifico a uno para que pueda continuar ejecutandose
        Thread.sleep(100);
        synchronized (thread1) {
            thread1.notify();
            System.out.println("Inicia");
        }

        //Espero un tiempo para interrumper los hilos
        Thread.sleep(2000);
        thread1.interrupt();
        thread2.interrupt();
        
        System.exit(0);
    }
}

class ClassA extends Thread {

    void EnterAndWait() throws InterruptedException {
        System.out.println("Inicia ejecutando hilo " + Thread.currentThread().getId());
        Thread.sleep(100);
        System.out.println("Acaba ejecutando hilo " + Thread.currentThread().getId());
    }
}

class ClassB implements Runnable {

    private ClassA a;
    private Thread t;

    public ClassB(ClassA a) {
        this.a = a;
    }
    
    //Metodo que referencia a otro hilo
    public void setThread(Thread t) {
        this.t = t;
    }

    @Override
    public void run() {
        
        //Bucle indefinido hasta que el hilo es interrumpido
        while (!Thread.currentThread().isInterrupted()) {
            //Sincronizamos el hilo actual
            synchronized (Thread.currentThread()) {
                try {
                    //Esperamos a que este hilo sea notificado para ejecutar EnterAndWait()
                    Thread.currentThread().wait();
                    a.EnterAndWait();
                } catch (Exception e) {
                    //Salimos del bucle
                    break;
                }
                //Sincronizamos el otro hilo
                synchronized (t) {
                    //Notifica al otro hilo
                    t.notify();
                }
            }
        }
    }
}
