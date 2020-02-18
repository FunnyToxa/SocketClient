import java.io.*;
import java.net.Socket;

/**
 * класс обаротки и отправки сообщений серверу
 */
public class WriteNode implements Runnable {

    private Socket serverSocket;
    private BufferedWriter bufferedWriter;
    private String message;

    public WriteNode(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
    }

    public WriteNode(Socket serverSocket, String message) throws IOException {
        this(serverSocket);
        this.message = message;
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
        send();
    }

    /**
     * обработка отправки сообщения
     */
    public void send(){

        //отправляем сообщение
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Сообщение не отправлено!");
        }

        if (message.startsWith("file:")) {
            String filePath = message.replace("file:", "");
            try {
                sendFile(filePath);
            } catch (IOException e) {
                System.out.println("Не удалось отправить файл");
            }
        }
    }

    /**
     * отправка файла
     * @param filePath
     * @throws IOException
     */
    private void sendFile(String filePath) throws IOException {
        System.out.println("Отправка файла: " + filePath);
        InputStream in = null;
        DataOutputStream out = null;
        File outFile = null;
        try {
            outFile = new File(filePath);
            out =  new DataOutputStream(serverSocket.getOutputStream());
            in = new FileInputStream(outFile);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
            return;
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
            in.close();
            return;
        }

        //сначала отправляем размер файла
        out.writeLong(outFile.length());

        //массив байт, какими частями будем передавать файл
        byte[] bytes = new byte[8*1024];

        int count;
        while ((count = in.read(bytes)) != -1) {
//            System.out.println(count);
            out.write(bytes, 0, count);
        }

        System.out.println("Файл отправлен!");
//        out.close();
        in.close();
    }
}
