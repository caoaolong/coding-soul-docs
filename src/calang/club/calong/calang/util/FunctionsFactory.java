package club.calong.calang.util;

import club.calong.calang.entry.DataType;
import club.calong.calang.entry.Function;

import java.util.HashMap;
import java.util.Map;

public class FunctionsFactory {

    private static final FunctionsFactory factory = new FunctionsFactory();

    private FunctionsFactory() {}

    public static FunctionsFactory getInstance() {

        return factory;
    }

    private static final Function print =
            new Function(
                    "print", 1, new DataType[]{DataType.STRING}, DataType.VOID);

    private static final Map<String, Function> functions = new HashMap<>();

    static {
        functions.put("print", print);
    }

    public Function getFunction(String name) {

        return functions.getOrDefault(name, null);
    }
}
