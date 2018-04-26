package guru.bonacci.oogway.app;

import java.util.Random;

public class Randomizer {

    public static String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

}
