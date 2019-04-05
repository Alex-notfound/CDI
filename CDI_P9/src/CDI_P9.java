
import java.net.*;
import java.io.*;

class MySockets {

    public static void main(String args[]) {
        new Server().start();
        new Client().start();
    }
}

class Server extends Thread {

    Socket socket = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    GrayImage img = new GrayImage(".\\src\\imag.jpg");
    Matriz matriz = new Matriz(img.getWidth());
    int segmentoActual;

    public void run() {
        try {
            ServerSocket server = new ServerSocket(4444);
            while (true) {
                socket = server.accept();
                ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println("Server Received: " + message);
                //Envia tarea al cliente:
                segmentoActual = matriz.getSegmentNumber();
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(segmentoActual);
                System.out.println("Enviado");
                matriz.setSegmentNumber(++segmentoActual);

                //img.writeImage(".\\src\\imag.jpg", rdata[][]);
                ois.close();
                oos.close();
                socket.close();
            }
        } catch (Exception e) {
        }
    }
}

class Client extends Thread {

    InetAddress host = null;
    Socket socket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Object o;
    GrayImage img;

    public void run() {
        try {
            for (int x = 0; x < 5; x++) {
                host = InetAddress.getLocalHost();
                socket = new Socket(host.getHostName(), 4444);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("Client Message " + x);
                //Recibe respuesta del Server:
                ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();
//                img = (GrayImage) ois.readObject();
//                System.out.println(ois.readObject());
                System.out.println("Client Received: " + o);
                ois.close();
                oos.close();
                socket.close();
            }
        } catch (Exception e) {
        }
    }
}
