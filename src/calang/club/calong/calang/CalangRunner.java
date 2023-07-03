package club.calong.calang;

import club.calong.calang.entry.Expression;
import club.calong.calang.entry.Variable;
import club.calong.calang.util.Commands;
import club.calong.calang.util.Constants;

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
    }

    public void translate(Expression expression) {

        switch (expression.getValueType()) {
            case VALUE: {
                switch (expression.getType()) {
                    case VAR: {
                        constants.put(expression.getIndex(),
                                Integer.parseInt(expression.getValue().toString()));
                        commands.add(Commands.PUSH | expression.getIndex() << 8 | 0xFFFF0000);
                        break;
                    }
                    case FUNC: {
                        // TODO: 执行函数
                    }
                }
                break;
            }
            case EXPRESSION: {
                switch (expression.getType()) {
                    case VAR: {
                        parseExpression(expression.getValue().toString());
                        constants.put(expression.getIndex(), vm.eax);
                        commands.add(Commands.PUSH | expression.getIndex() << 8 | 0xFFFF0000);
                        break;
                    }
                    case FUNC: {
                        // TODO: 执行函数
                    }
                }
                break;
            }
        }
        // 运行指令
        vm.run(commands);
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
                    vm.stackPush(getVariableIndex(entry));
                    break;
            }
        }
        while (!symbols.isEmpty()) {
            calc(symbols.pop());
        }
        System.out.println(vm.getStack());
        System.out.println(symbols);
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

        commands.add(Commands.POP | Constants.EBX_IDX << 16 | 0xFF00FF00);
        commands.add(Commands.POP | Constants.EBX_IDX << 16 | 0xFF00FF00);
        switch (symbol) {
            case "+":
                commands.add(Commands.MOV | vm.edx << 8 | Constants.EAX_IDX << 16 | 0xFF000000);
                commands.add(Commands.ADD | vm.ebx << 8 | 0xFFFF0000);
                commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF00FF00);
                break;
            case "-":
                commands.add(Commands.MOV | vm.edx << 8 | Constants.EAX_IDX << 16 | 0xFF000000);
                commands.add(Commands.SUB | vm.ebx << 8 | 0xFFFF0000);
                commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF000000);
                break;
            case "*":
                commands.add(Commands.MOV | vm.edx << 8 | Constants.EAX_IDX << 16 | 0xFF000000);
                commands.add(Commands.MUL | vm.ebx << 8 | 0xFFFF0000);
                commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF000000);
                break;
            case "/":
                commands.add(Commands.MOV | vm.edx << 8 | Constants.EAX_IDX << 16 | 0xFF000000);
                commands.add(Commands.DIV | vm.ebx << 8 | 0xFFFF0000);
                commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF000000);
                break;
            case "%":
                commands.add(Commands.MOV | vm.edx << 8 | Constants.EAX_IDX << 16 | 0xFF000000);
                commands.add(Commands.SUR | vm.ebx << 8 | 0xFFFF0000);
                commands.add(Commands.PUSH | Constants.EAX_IDX << 16 | 0xFF000000);
                break;
        }
    }

    public Integer getVariableIndex(String name) {

        Variable variable = variables.get(name);
        if (variable == null) {
            throw new RuntimeException("变量未定义");
        }
        return variable.getIndex();
    }
}
