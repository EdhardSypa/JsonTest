import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

    static final int M = 26;        // Stała M, która jest równa 26 (alfabet angielski)

    static String encryptMessage(char[] massage, int key1, int key2) {      // Metoda dla szyfrowania tekstu według podanych kluczy.
        String cipher = "";
        for (int i = 0; i < massage.length; i++) {
            if (massage[i] != ' ') {
                cipher = cipher + (char) ((((key1 * (massage[i] - 'A')) + key2) % M) + 'A'); // Szyfrowanie danych używając formuły (a*x + b) mod m
            } else {
                cipher += massage[i];       // Tworzenie spacji między słowami
            }
        }
        return cipher;
    }

    static String decryptCipher(String cipher, int key1, int key2) {        // Metoda do odszywrowywania podanego tekstu
        String massage = "";
        int a_inverse = 0;
        int h = 0;

        for (int i = 0; i < M; i++) {
            h = (key1 * i) % M;

            if (h == 1) {
                a_inverse = i;      // Znalezienie liczby a^-1, żeby póżniej podstawić do formuły
            }
        }
        for (int i = 0; i < cipher.length(); i++) {
            if (cipher.charAt(i) != ' ') {
                massage = massage + (char) (((a_inverse * ((cipher.charAt(i) + 'A' - key2)) % M)) + 'A');    // Odszyfrowania danych za pomocą formuły a^-1*(x-b) mod m
            }
            else {
                massage += cipher.charAt(i);        // Spacja pomiedzy słowami
            }
        }

        return massage;
    }

    static String wayToFile(String operation, String plainText) {     // Utworzenie ścieżki do pliku z wynikiem
        operation = operation.substring(0, 1).toUpperCase() + operation.substring(1);
        plainText = plainText.substring(0, 1).toUpperCase() + plainText.substring(1);

        String result = "./src/test/java/" + operation + plainText + "Output.json";
        return result;
    }

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("./src/test/java/Json.json"));    // Odczyt dannych z pliku json

            JSONObject jsonObject = (JSONObject) obj;

            String a = (String) jsonObject.get("a");
            String b = (String) jsonObject.get("b");
            int key1 = Integer.parseInt(a);         // Zmiana typu dannych z String na int dla zmiennych a, b
            int key2 = Integer.parseInt(b);

            String operation = (String) jsonObject.get("operation");
            String plainText = (String) jsonObject.get("plainText");

            JSONObject js = new JSONObject();       // Zapisywanie wyniku
            js.put("a", a);
            js.put("b", b);
            js.put("operation", operation);
            js.put("plainText", plainText);

            if(operation.equals("encrypt")) {
                String encryptCipher = encryptMessage(plainText.toCharArray(), key1, key2);
                js.put("cryptogram", encryptCipher);
            } else {
                String decryptCipher = decryptCipher(plainText, key1, key2);
                js.put("cryptogram", decryptCipher);
            }

            FileWriter file = new FileWriter(wayToFile(operation, plainText));      // Tworzenie pliku z wynikiem
            file.write(js.toJSONString());
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
