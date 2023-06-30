package club.calong.calang.entry;

import club.calong.calang.util.Constants;
import club.calong.calang.util.FunctionsFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Block {

    private final boolean shortly;

    private final Map<String, Object> variables;

    private final List<Block> subBlocks;

    private List<String> lines;

    private List<Expression> expressions;

    public List<Block> getSubBlocks() {
        return subBlocks;
    }

    public Map<String, Object> getVariables() {
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
        // TODO: 创建虚拟机
        for (String line : lines) {
            // 解析表达式
            Expression expression = createExpression(line);
            expressions.add(expression);
            // TODO: 计算表达式
        }

        for (Expression expression : expressions) {
            System.out.println(expression);
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
            StringBuffer buffer = new StringBuffer();
            for (int i = 3; i < trim.length; i++) {
                buffer.append(trim[i]);
            }
            return new Expression(trim[1], buffer, getDataType(trim[0]), ExpressionType.VAR);
        } else if (expressionType == ExpressionType.FUNC) {
            String functionName = trim[0].substring(0, trim[0].indexOf('(')).trim();
            Function function = functionsFactory.getFunction(functionName);
            return new Expression(functionName, function, function.getRetType(), ExpressionType.FUNC);
        }
        else
            return Expression.UNKNOWN_EXPRESSION;
    }
}
