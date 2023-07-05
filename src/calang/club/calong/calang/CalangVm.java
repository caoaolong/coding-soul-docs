package club.calong.calang;

import club.calong.calang.entry.Variable;
import club.calong.calang.util.Commands;

import java.util.*;

public class CalangVm {

    private int eip;

    private int ebp, esp;

    /**
     * register[0]: eax: 存储返回值
     * register[1, 3]: ebx, edx: 存储临时变量的索引
     * register[2]: ecx
     */
    public int[] register = {0, 0, 0, 0};

    private int eflags;

    private final Stack<Integer> stack;

    private final ArrayList<Object> heap;

    private Map<Integer, Integer> constants;

    public Map<Integer, Integer> getConstants() {
        return constants;
    }

    public void setConstants(Map<Integer, Integer> constants) {
        this.constants = constants;
    }

    public CalangVm(int heapSize) {

        eip = 0;
        Arrays.fill(register, 1);
        stack = new Stack<>();
        heap = new ArrayList<>(heapSize);
    }

    public Stack<Integer> getStack() {
        return stack;
    }

    public synchronized void stackPush(int value) {

        stack.push(value);
        esp++;
    }

    public synchronized int stackPop() {

        esp--;
        return stack.pop();
    }

    public Object getHeapObject(int index) {

        return heap.get(index);
    }

    public int setHeapObject(Object value) {

        heap.add(value);
        return heap.size() - 1;
    }

    public void run(List<Integer> commands) {

        for (int i = eip; i < commands.size(); i++) {
            // 逐行读取指令
            Integer command = commands.get(i);
            // 执行指令
            exec(command);
            // 检测异常
            interrupt(command);
            // 更新EIP
            eip ++;
        }
    }

    private void interrupt(Integer command) {


    }

    private void exec(Integer command) {

        int index = (command & 0x0000FF00) >> 8;
        int reg1 = (command & 0x00FF0000) >> 16;
        int reg2 = command >> 24;
        switch (command & 0x000000FF) {
            case Commands.PUSH:
                cmdPush(index, reg1, reg2);
                break;
            case Commands.POP:
                cmdPop(index, reg1, reg2);
                break;
            case Commands.MOV:
                cmdMov(index, reg1, reg2);
                break;
            case Commands.ADD:
                cmdAdd(index, reg1, reg2);
                break;
            case Commands.SUB:
                cmdSub(index, reg1, reg2);
                break;
            case Commands.MUL:
                cmdMul(index, reg1, reg2);
                break;
            case Commands.DIV:
                cmdDiv(index, reg1, reg2);
                break;
            case Commands.SUR:
                cmdSur(index, reg1, reg2);
                break;
        }
    }

    private void cmdPush(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF) {
            stack.push(constants.get(index));
            return;
        }

        if (reg1 != 0xFF) {
            stack.push(register[reg1]);
        }
    }

    private void cmdPop(Integer index, Integer reg1, Integer reg2) {

        if (reg1 != 0xFF) {
            register[reg1] = stack.pop();
        }
    }

    private void cmdMov(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] = constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg2] = constants.get(register[reg1]);
        }
    }

    private void cmdAdd(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] += constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg1] += register[reg2];
        }
    }

    private void cmdSub(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] -= constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg1] -= register[reg2];
        }
    }

    private void cmdMul(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] *= constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg1] *= register[reg2];
        }
    }

    private void cmdDiv(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] /= constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg1] /= register[reg2];
        }
    }

    private void cmdSur(Integer index, Integer reg1, Integer reg2) {

        if (index != 0xFF && reg1 != 0xFF) {
            register[reg1] %= constants.get(index);
            return;
        }

        if (reg1 != 0xFF && reg2 != 0xFF) {
            register[reg1] %= register[reg2];
        }
    }

    public void cleanVariables(Map<String, Variable> variables) {

        variables.clear();
        constants.clear();
    }
}
