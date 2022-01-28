package view.actions.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ConsoleActionsUtils {
    static <T> T chooseFromList(List<T> variants, Scanner scanner, String errorMessage) {
        for (int i = 0; i < variants.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, variants.get(i));
        }
        T chosen;
        while (true) {
            try {
                final var indexString = scanner.nextLine();
                final var index = Integer.parseInt(indexString);
                chosen = variants.get(index - 1);
                break;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println(errorMessage);
            }
        }
        return chosen;
    }

    static <T> List<T> chooseMultipleFromList(List<T> variants, Scanner scanner, String errorMessage) {
        for (int i = 0; i < variants.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, variants.get(i));
        }
        List<T> chosen = new ArrayList<>();
        while (true) {
            try {
                final var indexesString = scanner.nextLine();
                if (indexesString.isBlank()) {
                    break;
                }
                final String[] indexesStringSplitted = indexesString.split("\\s*,\\s*");
                for (String indexString : indexesStringSplitted) {
                    final var indexes = Integer.parseInt(indexString);
                    chosen.add(variants.get(indexes - 1));
                }
                break;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println(errorMessage);
            }
        }
        return chosen;
    }
}
