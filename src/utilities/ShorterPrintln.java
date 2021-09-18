package utilities;

import java.util.Arrays;

public class ShorterPrintln {

    public static void sout(Object... args) {
        Arrays.stream(args).forEach(System.out::println);
    }
}
