package ex1;

import java.util.Arrays;

public class Heap<T extends INode> {
    // this is my own implementation for a heap,
    // i can assure you i made it my self.
    // this should be a proper heap, but mostly its fine tuned for the path finding algorithm.

    // Java does not have a heap implementation that support heapify up and down,
    // and i also need a way to quickly "take out" items from the heap,
    // in this case i save the heap index, as the tag inside the node_info, with lets me
    // a very quick way of updating an existing item in the heap
    // this is the main reason why this heap is much much better, then the PriorityQueue
    private T[] items;
    int boundUp = 0;
    int used = 0;
    int arrSize = 1000;
    public Heap() {
        items = (T[])new INode[arrSize];
    }

    public Heap(int size){
        arrSize = size;
        items = (T[])new INode[size];
    }

    public T lookFirst(){
        return items[0];
    }

    @Override
    public String toString() {
        // print heap content
        if(size() > 0) {
            String content = "[";
            for (int i = 0; i < size() - 1; i++) {
                content += items[i] + " ,";
            }
            content += items[size()-1] + "]";
            return content;
        }
        return "[]";
    }

    public int size(){
        return used;
    }

    public T getAt(int index){
        return items[index];
        //return items.get(index);
    }

    public void add(T item, double priority){
        // for now no protection at all
        if(item.getHeap() != null){
            return;
        }
        // mark the item to be part of the heap.
        item.setHeap(this);
        item.setPriority(priority);
        item.setIndex(used);

        items[size()] = item;
        used++;

        // if the heap Arr is to small we would expand it by 5.
        // this takes O(n) time.
        if(used >= arrSize){
            arrSize *= 5;
            items = Arrays.copyOf(items, arrSize);
        }

        //items.add(item);
        boundUp = (used - 1) / 2 - 1;

        heapifyUp(item);
    }

    public T poll(){
        // get the first element, this takes O(logn);
        swap(getAt(0), getAt(used - 1));
        T first = getAt(used - 1);
        used--;
        if(size() > 0) {
            heapifyDown(getAt(0));
        }
        // we mark the node to be out of heap.
        first.setHeap(null);
        boundUp = (used - 1) / 2 - 1;
        return first;
    }

    public void addOrUpgrade(T target, double priority){
        if(tryUpgrade(target, priority) == -1){
            add(target, priority);
        }
    }

    public int tryUpgrade(T target, double priority){
        // this is mainly for the PathFinding,
        // if the node is in the heap.
        // we try to update the priority, if its a "better" one,
        // we update it.
        // return 1 if successful.
        // return 0, if failed
        // return -1, if node not in heap.
        if(target.getHeap() != this){
            return -1;
        }else if(target.getPriority() > priority){
            target.setPriority(priority);
            heapifyUp(target);
            return 1;
        }
        return 0;
    }

    private void swap(T node1, T node2){
        int index = node1.getIndex();
        int index2 = node2.getIndex();

        node1.setIndex(index2);
        node2.setIndex(index);

        items[index] = node2;
        items[index2] = node1;
    }

    private void heapifyUp(T target){
        // update item, look upwards.
        // this takes O(logn);
        int index = target.getIndex();
        if(index == 0){
            return;
        }

        T parent = getAt((target.getIndex() - 1) / 2);
        if(target.compareTo(parent)){
            swap(target, parent);
            heapifyUp(target);
        }
    }

    private void heapifyDown(T target){
        // update item, look downwards.
        // this takes O(logn);
        int index = target.getIndex();
        if(index > boundUp){
            return;
        }


        T left = getAt(index * 2 + 1);
        if(index *2 + 3 > used){
            if(left.compareTo(target)){
                swap(target, left);
            }
            return;
        }
        T right = getAt(index * 2 + 2);
        if(left.compareTo(target)){
            if(left.compareTo(right)){
                swap(target, left);
                heapifyDown(target);
            }else{
                swap(target, right);
                heapifyDown(target);
            }
        }else if(right.compareTo(target)){
            swap(target, right);
            heapifyDown(target);
        }
    }
}
