package club.calong.calang.entry;

import java.util.Arrays;

public class Function {

    private String name;

    private Integer nArgs;

    private DataType[] argTypes;

    private DataType retType;

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

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", nArgs=" + nArgs +
                ", argTypes=" + Arrays.toString(argTypes) +
                ", retType=" + retType +
                '}';
    }
}
