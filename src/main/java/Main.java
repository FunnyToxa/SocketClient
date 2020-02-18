import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyClient client = new MyClient();
        try {
            client.connect("localhost", 10000);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите сообщение:");
            String line = scanner.nextLine();
            if (line.equals("exit"))
                break;
            client.startSendingMessages(line);
        }

        client.closeConnection();
    }
}
