package club.calong.calang.entry;

public class Expression {

    private ExpressionType type;

    private String name;

    private Object value;

    private DataType dataType;

    public static Expression UNKNOWN_EXPRESSION = new Expression();

    public Expression() {

    }

    public Expression(String name, Object value, DataType dataType, ExpressionType type) {
        this.name = name;
        this.value = value;
        this.dataType = dataType;
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

    public void setValue(String value) {
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

    @Override
    public String toString() {
        return "Expression{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", dataType=" + dataType +
                '}';
    }
}
