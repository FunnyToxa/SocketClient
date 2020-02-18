import java.io.IOException;
import java.net.Socket;

public class MyClient {
    private Socket server;

    /**
     * Метод создания подключения к серверу
     * Сразу создает поток для прослушивания ответов от сервера - с выводом в консоль
     * @param host
     * @param port
     * @throws IOException
     */
    public void connect(String host, int port) throws IOException {
        //Подключение
        server = new Socket(host, port);
        System.out.println("Установлено соединение с сервером");
        ReadNode readNode = new ReadNode(server);
        //создали поток на прослушивание ответов от сервера
        Thread readThead = new Thread(readNode, "readThead");
        readThead.start();
    }

    /**
     * запуск отправки сообщений от клиента серверу
     * @param message
     */
    public void startSendingMessages(String message){
//        try {
//            Thread sendThead = new Thread(new WriteNode(serverSocket, message));
//            sendThead.start();
//        } catch (IOException e) {
//            System.out.println("Не удалось отправить сообщение!");
//        }

        //отправку сообщений запускаем в главном потоке
        try {
            WriteNode wn = new WriteNode(server, message);
            wn.send();
        } catch (IOException e) {
            System.out.println("Не удалось отправить сообщение!");
        }

    }

    /**
     * закрытие соединения
     */
    public void closeConnection(){
        try {
            server.close();
        } catch (IOException e) {

        }
    }
}
