package club.calong.calang.entry;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Function {

    private String name;

    private Integer nArgs;

    private DataType[] argTypes;

    private String[] values;

    private DataType retType;

    private Method method;

    private boolean variable;

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }

    public boolean isVariable() {
        return variable;
    }

    public Function(String name, Integer nArgs, DataType[] argTypes, DataType retType) {
        this.name = name;
        this.nArgs = nArgs;
        this.argTypes = argTypes;
        this.retType = retType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getnArgs() {
        return nArgs;
    }

    public void setnArgs(Integer nArgs) {
        this.nArgs = nArgs;
    }

    public DataType[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(DataType[] argTypes) {
        this.argTypes = argTypes;
    }

    public DataType getRetType() {
        return retType;
    }

    public void setRetType(DataType retType) {
        this.retType = retType;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", nArgs=" + nArgs +
                ", argTypes=" + Arrays.toString(argTypes) +
                ", values=" + Arrays.toString(values) +
                ", retType=" + retType +
                '}';
    }
}
