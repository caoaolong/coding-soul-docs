package club.calong.calang;

import club.calong.calang.entry.Expression;
import club.calong.calang.entry.Function;
import club.calong.calang.entry.Variable;
import club.calong.calang.util.Commands;
import club.calong.calang.util.Constants;
import club.calong.calang.util.FunctionsFactory;

import java.util.*;

public class CalangRunner {

    private final Map<Integer, Integer> constants;

    private final Map<String, Variable> variables;

    private final CalangVm vm;

    private final List<Integer> commands;

    public Map<Integer, Integer> getConstants() {
        return constants;
    }

    public List<Integer> getCommands() {
        return commands;
    }

    public boolean putCommands(Integer cmd) {
        return commands.add(cmd);
    }

    public CalangRunner(CalangVm vm, Map<String, Variable> variables) {
        this.vm = vm;
        constants = new HashMap<>();
        commands = new LinkedList<>();
        this.variables = variables;
        vm.setConstants(constants);
    }

    public void translate(Expression expression) {

        switch (expression.getValueType()) {
            case VALUE: {
                switch (expression.getType()) {
                    case VAR: {
                        constants.put(expression.getIndex(),
                                Integer.parseInt(expression.getValue().toString()));
                        vm.stackPush(getVariableValue(expression.getName()));
                        break;
                    }
                    case FUNC: {

                    }
                }
                break;
            }
            case EXPRESSION: {
                switch (expression.getType()) {
                    case VAR: {
                        parseExpression(expression.getValue().toString());
                        variables.get(expression.getName()).setValue(vm.register[Constants.EAX_IDX]);
                        constants.put(expression.getIndex(), vm.register[Constants.EAX_IDX]);
                        break;
                    }
                    case FUNC: {
                        parseFunction(expression);
                    }
                }
                break;
            }
        }
        // 运行指令
        vm.run(commands);
    }

    private void parseFunction(Expression expression) {

        Function function = (Function) expression.getValue();
        if (function.getValues().length >= function.getnArgs()) {
            String[] values = function.getValues();
            for (int i = 0; i < values.length; i++) {
                parseExpression(values[i]);
                values[i] = String.valueOf(vm.stackPop());
            }
        }
        FunctionsFactory functionsFactory = FunctionsFactory.getInstance();
        functionsFactory.run(function);
    }

    private void parseExpression(String value) {

        Stack<String> symbols = new Stack<>();

        String[] entries = value.split("#");
        for (String entry : entries) {
            switch (entry) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "(":
                case ")":
                    pushSymbol(symbols, entry);
                    break;
                default:
                    vm.stackPush(getVariableValue(entry));
                    break;
            }
        }
        while (!symbols.isEmpty()) {
            calc(symbols.pop());
        }
    }

    private static final Map<String, Integer> symbolOrder = new HashMap<String, Integer>(){{
        put("+", 1); put("-", 1);
        put("*", 2); put("/", 2); put("%", 2);
        put("(", 0); put(")", 3);
    }};

    private void pushSymbol(Stack<String> symbols, String entry) {
        // 左括号直接入栈
        // 栈为空则直接入栈
        if (symbols.isEmpty() || entry.equals("(")) {
            symbols.push(entry);
            return;
        }
        // 右括号直接弹到左括号
        if (entry.equals(")")) {
            String symbol;
            while (!(symbol = symbols.pop()).equals("("))
                calc(symbol);
            return;
        }
        // 新符号优先级高则可以入栈，否则出栈计算
        if (symbolOrder.get(entry) > symbolOrder.get(symbols.peek())) {
            symbols.push(entry);
        } else {
            String symbol = symbols.pop();
            calc(symbol);
            pushSymbol(symbols, entry);
        }
    }

    public void calc(String symbol) {

        commands.add(Commands.POP | Constants.EAX_IDX << 16 | 0xFF00FF00);
        commands.add(Commands.POP | Constants.EDX_IDX << 16 | 0xFF00FF00);
        vm.run(commands);
        switch (symbol) {
            case "+":
                commands.add(Commands.ADD | Constants.EAX_IDX << 16 | Constants.EDX_IDX << 24 | 0x0000FF00);
                break;
            case "-":
                commands.add(Commands.SUB | Constants.EAX_IDX << 16 | Constants.EDX_IDX | 0x0000FF00);
                break;
            case "*":
                commands.add(Commands.MUL | Constants.EAX_IDX << 16 | Constants.EDX_IDX << 24 | 0x0000FF00);
                break;
            case "/":
                commands.add(Commands.DIV | Constants.EAX_IDX << 16 | Constants.EDX_IDX << 24 | 0x0000FF00);
                break;
            case "%":
                commands.add(Commands.SUR | Constants.EAX_IDX << 16 | Constants.EDX_IDX << 16 | 0xFFFF0000);
                break;
        }
        commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF00FF00);
        vm.run(commands);
    }

    public Integer getVariableValue(String name) {

        if (Constants.VARIABLE_PATTERN.matcher(name).matches()) {
            Variable variable = variables.get(name);
            if (variable == null) {
                throw new RuntimeException("变量未定义");
            }
            return Integer.parseInt(variable.getValue().toString());
        } else
            return Integer.parseInt(name);
    }
}
