package club.calong.calang;

import java.util.ArrayList;
import java.util.Stack;

public class CalangVm {

    private int eip;

    private int ebp, esp;

    private int eax, ebx, ecx, edx;

    private int eflags;

    private final Stack<Integer> stack;

    private final ArrayList<Object> heap;

    public CalangVm(int heapSize) {

        stack = new Stack<>();
        heap = new ArrayList<>(heapSize);
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
}
