import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * класс получения и обработки ответов от сервера
 */
public class ReadNode implements Runnable {

    private BufferedReader bufferedReader;
    private Socket serverSocket;

    public ReadNode(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        //ждем сообщения от сервера
        try {

            while (true) {
                String word = bufferedReader.readLine();
                if (word == null) {
                    break;
                }
                System.out.println("Сервер ответил: " + word);
            }
        } catch (IOException e){
            if (!serverSocket.isClosed())
                System.out.println("Проблемы соединения с сервером!");
        }
    }
}
