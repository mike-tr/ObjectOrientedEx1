package ex1;

public abstract class INode {
    private int index = -1;
    private Heap heap;

    // return parent heap, null if none.
    public Heap getHeap() {
        return heap;
    }

    // set the parent heap, honestly this should be an inner class but whatever.
    public void setHeap(Heap heap) {
        this.heap = heap;
    }

    // return the index of the node in the heap arr.
    public int getIndex() {
        return index;
    }

    // set the index of the node in the arr, again this should be only settable via the HEAP.
    public void setIndex(int index){
        this.index = index;
    }

    // return if node priority is bigger then mine.
    public boolean compareTo(INode node){
        return getPriority() < node.getPriority();
    }

    @Override
    public String toString() {
        return "" + getPriority();
    }

    // return/set priority, this variable can be whatever.
    public abstract double getPriority();
    public abstract void setPriority(double priority);
}
