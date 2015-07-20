package IOUtils;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by seal on 7/20/15.
 */
public class InputReader {
    private BufferedReader reader;
    public StringTokenizer tokenizer;

    InputReader(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        tokenizer = null;
    }

    InputReader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
    }

    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (Exception e) {

            }
        }
        return tokenizer.nextToken();
    }

    public String nextLine() {
        String str = "";
        try {
            str = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return str;
    }

    public boolean hasMoreTokens() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            String str = null;
            try {
                str = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException();
            }
            if (str == null) return false;
            tokenizer = new StringTokenizer(str);
        }
        return true;
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }
}

