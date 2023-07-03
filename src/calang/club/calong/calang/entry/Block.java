package club.calong.calang.entry;

import club.calong.calang.CalangRunner;
import club.calong.calang.CalangVm;
import club.calong.calang.util.Constants;
import club.calong.calang.util.FunctionsFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Block {

    private final boolean shortly;

    private final Map<String, Variable> variables;

    private final List<Block> subBlocks;

    private List<String> lines;

    private List<Expression> expressions;

    public List<Block> getSubBlocks() {
        return subBlocks;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public boolean isShortly() {
        return shortly;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public Block(boolean shortly) {
        this.shortly = shortly;
        variables = new HashMap<>();
        subBlocks = new LinkedList<>();
    }

    public void parse() {

        if (lines == null || lines.isEmpty())
            return;
        expressions = new LinkedList<>();
        // 创建虚拟机
        CalangVm vm = new CalangVm(1024);
        // 创建解释器
        CalangRunner runner = new CalangRunner(vm, variables);

        for (String line : lines) {
            // 解析表达式
            Expression expression = createExpression(line);
            expressions.add(expression);
            // 翻译表达式
            runner.translate(expression);
        }

        for (Expression exp : expressions) {
            System.out.println(exp);
        }
    }

    public DataType getDataType(String type) {

        switch (type) {
            case "int":
            case "INT":
                return DataType.INT;
            case "string":
            case "STRING":
                return DataType.STRING;
            case "float":
            case "FLOAT":
                return DataType.FLOAT;
            default:
                return DataType.UNKNOWN;
        }
    }

    public ValueType getValueType(DataType dataType, String value) {

        switch (dataType) {
            case INT: {
                try {
                    Integer.parseInt(value);
                    return ValueType.VALUE;
                } catch (NumberFormatException e) {
                    return ValueType.EXPRESSION;
                }
            }
            case STRING: {
                return Constants.STRING_PATTERN.matcher(value).matches() ?
                        ValueType.VALUE : ValueType.EXPRESSION;
            }
            case FLOAT: {
                try {
                    Float.parseFloat(value);
                    return ValueType.VALUE;
                } catch (NumberFormatException e) {
                    return ValueType.EXPRESSION;
                }
            }
            default:
                return ValueType.UNKNOWN;
        }
    }

    public ExpressionType getExpressionType(String line) {

        String[] trim = line.trim().split("\\s+");
        if (getDataType(trim[0]) != DataType.UNKNOWN)
            return ExpressionType.VAR;
        else if (isFunctionType(line))
            return ExpressionType.FUNC;
        else
            return ExpressionType.UNKNOWN;
    }

    private boolean isFunctionType(String line) {

        return Constants.FUNCTION_PATTERN.matcher(line).matches();
    }

    public Expression createExpression(String line) {

        FunctionsFactory functionsFactory = FunctionsFactory.getInstance();
        String[] trim = line.trim().split("\\s+");
        ExpressionType expressionType = getExpressionType(line);
        if (expressionType == ExpressionType.VAR){
            StringBuffer value = new StringBuffer();
            for (int i = 3; i < trim.length; i++) {
                value.append(trim[i]).append("#");
            }
            int length = value.length();
            value.delete(length - 1, length);
            Variable variable = new Variable(trim[1], variables.size(), value);
            variables.put(trim[1], variable);
            DataType dataType = getDataType(trim[0]);
            return new Expression(variable.getName(), variable.getIndex(), value,
                    dataType, getValueType(dataType, value.toString()), ExpressionType.VAR);
        } else if (expressionType == ExpressionType.FUNC) {
            String functionName = trim[0].substring(0, trim[0].indexOf('(')).trim();
            Function function = functionsFactory.getFunction(functionName);
            return new Expression(functionName, -1, function,
                    function.getRetType(), ValueType.VALUE, ExpressionType.FUNC);
        }
        else
            return Expression.UNKNOWN_EXPRESSION;
    }
}
