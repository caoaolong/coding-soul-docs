package club.calong.calang;

import club.calong.Variable;
import club.calong.calang.util.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CalangVm {

    private int eip;

    private int ebp, esp;

    /**
     * eax: 存储返回值
     * ebx, edx: 存储临时变量的索引
     */
    public int eax, ebx, ecx, edx;

    private int eflags;

    private final Stack<Integer> stack;

    private final ArrayList<Object> heap;

    public CalangVm(int heapSize) {

        eip = 0;
        eax = ebx = ecx = edx = 1;
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

        switch (command & 0x000000FF) {
            case Commands.PUSH:
                cmdPush(command);
                break;
            case Commands.MOV:
                cmdMov(command);
                break;
            case Commands.ADD:
                cmdAdd(command);
                break;
            case Commands.SUB:
                cmdSub(command);
                break;
            case Commands.MUL:
                cmdMul(command);
                break;
            case Commands.DIV:
                cmdDiv(command);
                break;
            case Commands.SUR:
                cmdSur(command);
                break;
        }
    }

    private void cmdPush(Integer command) {


    }

    private void cmdMov(Integer command) {


    }

    private void cmdAdd(Integer command) {


    }

    private void cmdSub(Integer command) {


    }

    private void cmdMul(Integer command) {


    }

    private void cmdDiv(Integer command) {


    }

    private void cmdSur(Integer command) {


    }
}
