package club.calong.calang.entry;

public class Expression {

    private ExpressionType type;

    private String name;

    private Integer index;

    private Object value;

    private DataType dataType;

    private ValueType valueType;

    public static Expression UNKNOWN_EXPRESSION = new Expression();

    public Expression() {

    }

    public Expression(String name, Integer index, Object value, DataType dataType, ValueType valueType, ExpressionType type) {
        this.name = name;
        this.index = index;
        this.value = value;
        this.dataType = dataType;
        this.valueType = valueType;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public ExpressionType getType() {
        return type;
    }

    public void setType(ExpressionType type) {
        this.type = type;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", index=" + index +
                ", value=" + value +
                ", dataType=" + dataType +
                ", valueType=" + valueType +
                '}';
    }
}
