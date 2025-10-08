
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        System.out.println("What's Your Name?");
        String name = reader.readLine();
        System.out.println("Hello World" + " -" + name);

    }
}
