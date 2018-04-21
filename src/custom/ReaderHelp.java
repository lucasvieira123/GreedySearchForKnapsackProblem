package custom;

import java.io.*;

public class ReaderHelp {

    public static BufferedReader tryGetBufferedReader(File selectedFile) {
        try {
            return new BufferedReader(new FileReader(selectedFile));
        } catch (FileNotFoundException e) {
            //print msg de erro
            e.printStackTrace();
        }

        return null;
    }

    public static String tryReadLine(BufferedReader br) {

        try {
            return br.readLine();
        } catch (IOException e) {
            //print msg de erro
            e.printStackTrace();
        }
        return null;
    }
}
