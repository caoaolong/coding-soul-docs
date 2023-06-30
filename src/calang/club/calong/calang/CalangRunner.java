package club.calong.calang;

import java.util.List;

public class CalangRunner {

    private List<Integer> commands;

    public List<Integer> getCommands() {
        return commands;
    }

    public boolean putCommands(Integer cmd) {
        return commands.add(cmd);
    }
}
