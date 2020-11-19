package ex1;

public class WPathNode extends INode {
    // this is a node object, it implements INode with is an interface
    // that lets me set the "parent heap" set priority, and Compare 2 node objects.
    // the priority would be used as the Distance.
    private node_info node;
    private WPathNode parent;
    private double distance;

    WPathNode(node_info node){
        this.node = node;
    }

    WPathNode(node_info node, WPathNode root){
        this.node = node;
        this.parent = root;
    }

    public WPathNode getParent(){
        // get the parent, a.k.a the node we came.
        return parent;
    }

    public node_info getNode(){
        return node;
    }

    public int getKey(){
        return node.getKey();
    }

    public void setParent(WPathNode root){
        // set the parent of this Node, for traversal.
        this.parent = root;
    }

    @Override
    public double getPriority() {
        // get the distance / priority
        return distance;
    }

    @Override
    public void setPriority(double distance) {
        // set the distance in traversal with is the "priority" for the min heap.
        this.distance = distance;
    }

    @Override
    public void setIndex(int index) {
        // set the index of the Node inside the heap.
        super.setIndex(index);
        node.setTag(index);
    }
}
