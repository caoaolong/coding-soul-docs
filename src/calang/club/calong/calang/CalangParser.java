package club.calong.calang;

import club.calong.calang.entry.Block;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CalangParser {

    private final Block root = new Block(true);

    private List<String> lines;

    public Block getRoot() {
        return root;
    }

    public CalangParser (String filepath) {

        try (FileReader reader = new FileReader(filepath)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            lines = new LinkedList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        root.setLines(lines);
        root.parse();
    }
}
