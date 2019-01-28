import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class Log {
    void writeToLog(String s) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt", true), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(writer).append(s).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(writer).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}