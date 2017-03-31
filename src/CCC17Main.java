public class CCC17Main {

    public static void main(String[] args) {
        for (String line : FileHelper.readAllLines("test.txt")) {
            System.out.println(line);
        }
    }
}
