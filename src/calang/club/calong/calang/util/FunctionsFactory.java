package club.calong.calang.util;

import club.calong.calang.entry.DataType;
import club.calong.calang.entry.Function;

import java.lang.reflect.InvocationTargetException;
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
        Class<FunctionsFactory> aClass = FunctionsFactory.class;
        try {
            print.setVariable(true);
            print.setMethod(aClass.getDeclaredMethod("print", String.class, String[].class));
        } catch (NoSuchMethodException ignored) {}
        functions.put("print", print);
    }

    public Function getFunction(String name) {

        return functions.getOrDefault(name, null);
    }

    public void run(Function function) {

        try {
            Function func = functions.get(function.getName());
            int paramCount = func.getMethod().getParameterCount();
            if (func.isVariable() && func.getnArgs() == paramCount - 1) {
                Object[] values = new Object[paramCount];
                for (int i = 0; i < paramCount - 1; i++) {
                    values[i] = func.getValues()[i];
                }
                values[paramCount - 1] = new String[]{};
                func.getMethod().invoke(this, values);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void print(String fmt, String ...values) {

        if (values.length == 0) {
            System.out.println(fmt);
        }
    }
}
