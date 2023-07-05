package club.calong.calang.entry;

public class Variable {

    private String name;

    private Integer index;

    private Object value;

    public Variable(String name, Integer index, Object value) {
        this.name = name;
        this.index = index;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public Object getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
