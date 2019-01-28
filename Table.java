import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class Table {
    private String[] headers;
    private List<String[]> rows = new ArrayList<>();

    void headers(String... headers) {
        this.headers = headers;
    }

    void addRow(String... cells) {
        rows.add(cells);
    }

    void printTable() {
        int[] maxWidths = headers != null ? Arrays.stream(headers).mapToInt(String::length).toArray() : null;

        for (String[] cells : rows) {
            if (maxWidths == null) {
                maxWidths = new int[cells.length];
            }
            if (cells.length != maxWidths.length) {
                throw new IllegalArgumentException("Number of row-cells and headers must be the same.");
            }
            for (int i = 0; i < cells.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
            }
        }

        if (headers != null) {
            printLine(maxWidths);
            printRow(headers, maxWidths);
            printLine(maxWidths);
        }
        for (String[] cells : rows) {
            printRow(cells, maxWidths);
        }
        if (headers != null) {
            printLine(maxWidths);
        }
    }

    private void printLine(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String line = String.join("", Collections.nCopies(columnWidths[i] + "|".length() + 1, "-"));
            System.out.print("+" + line + (i == columnWidths.length - 1 ? "+" : ""));
        }
        System.out.println();
    }

    private void printRow(String[] cells, int[] maxWidths) {
        for (int i = 0; i < cells.length; i++) {
            String s = cells[i];
            String verStrTemp = i == cells.length - 1 ? "|" : "";

            System.out.printf("%s %-" + maxWidths[i] + "s %s", "|", s, verStrTemp);
        }
        System.out.println();
    }
}