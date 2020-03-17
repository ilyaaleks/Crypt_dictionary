import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.DoubleStream;

public class Main {
    static double binaryEntropy;
    static double cyrillicEntropy;
    static double latinEntropy;

    public static void main(String[] args) throws IOException {
        System.out.println("Cyrillic Entropy");
        System.out.println(cyrillicEntropy = GetEntropyOfTheAlphabet("абвгдђежзиjклљмнњопрстћуфхцчџш", "D:\\6sem\\crypt\\Lab2\\serb.txt",false));
        System.out.println("Binary Entropy");
        System.out.println(binaryEntropy = GetEntropyOfTheAlphabet("01", "D:\\6sem\\crypt\\Lab2\\serb.txt",true));
        System.out.println("Latin entropy");
        System.out.println(latinEntropy = GetEntropyOfTheAlphabet("abcdefghijklmnopqrstuvwxyz", "D:\\6sem\\crypt\\Lab2\\germ.txt",false));

        System.out.println(getAmountOfInformation("Алексеев Илиа Алекандровицх", cyrillicEntropy));
        System.out.println("Alekseev Ilya Aleksandrovich".getBytes("US-ASCII").length * binaryEntropy);
        System.out.println("With errors");
        System.out.println(getAmountOfInformationWithErrors("Alekseev Ilya Aleksandrovich".getBytes().length * 8, 0.1));
        System.out.println(getAmountOfInformationWithErrors("Alekseev Ilya Aleksandrovich".getBytes().length * 8, 0.5));
        System.out.println(getAmountOfInformationWithErrors("Alekseev Ilya Aleksandrovich".getBytes().length * 8, 1));
    }

    public static double GetEntropyOfTheAlphabet(String alphabet, String path, boolean isBinary) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        content = content.chars().filter((t) -> (t != ' ' && t != ',' && t != '.' && t != '"' && t != ':')).collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append).toString();
        if(isBinary==true){
            content=toBinaryFormat(content);
        }
        double[] p = new double[alphabet.length()];
        for (int i = 0; i < alphabet.length(); i++) {
            char character = alphabet.charAt(i);
            long numberOfCharacters = content.toLowerCase().chars().filter(s -> s == character).count();
            double length = content.length();
            p[i] = numberOfCharacters / length;
            System.out.println(numberOfCharacters);
            System.out.println("P[" + character + "](" + p[i] + ")\n");
        }
        return findEntropyOfShenon(p);
    }

    public static double findEntropyOfShenon(double[] probability) {
        return -DoubleStream.of(probability).map(p -> p * customLog(2, p)).sum();
    }

    private static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }

    public static double getAmountOfInformation(String information, double entropy) {
        return entropy * information.length();
    }

    public static double getAmountOfInformationWithErrors(int informationLength, double errorCoefficient) {

        return (1 - (-errorCoefficient * customLog(2, errorCoefficient) - (1 - errorCoefficient) * customLog(2, (1 - errorCoefficient)))) * informationLength;
    }
    public static String toBinaryFormat(String s)
    {
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        System.out.println("'" + s + "' to binary: " + binary);
        return binary.toString();
    }
}
