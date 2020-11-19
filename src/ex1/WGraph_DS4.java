package ex1;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS4 extends WGraphBasics {
    private class Node implements node_info, Serializable{
        private int key;
        private transient String info = "";
        private transient double tag = 0;

        private HashMap<Integer, Double> edges = new HashMap<>();
        public Node(int key){
            this.key = key;
        }

        @Override
        public String toString() {
            return "" + getKey();
        }

        @Override
        public int hashCode() {
            return getKey();
        }

        @Override
        public boolean equals(Object other){
            if (other == this) {
                return true;
            }

            if(other instanceof Integer){
                return this.key == (Integer) other;
            }

            if (other instanceof node_info) {
                return key == other.hashCode();
            }
            return false;
    }

        private void addEdge(Node target, double weight){
            if(target == null){
                return;
            }

            int tKey = target.getKey();
            if(tKey == getKey()){
                return;
            }
            // if the target is valid, we add action,
            // either we just update the weight, or we create a new edge.
            actionMade++;
            if(edges.containsKey(tKey)){
                edges.put(tKey, weight);
                target.edges.put(key, weight);
                return;
            }
            edges.put(tKey, weight);
            target.edges.put(key, weight);
            edgeNum++;
        }

        private void removeEdge(int target){
            if(edges.containsKey(target)){
                edges.remove(target);
                nodes.get(target).edges.remove(key);
                actionMade++;
                edgeNum--;
            }
        }

        public Collection<node_info> getV(){
            // we store neighbours as "Keys" in the set of edges,
            // so we use those to create an neighbour collection
            // with is faster then having one before hand ( because it takes twice as many insertions to populate 2 ).
            Collection<node_info> neighbours = new ArrayList<>();
            Collection<Integer> list = edges.keySet();
            for (int id : list) {
                neighbours.add(getNode(id));
            }
            return neighbours;
        }

        public boolean hasEdge(int neighbour){
            return edges.containsKey(neighbour);
        }

        public double getEdge(int neighbour){
            if(hasEdge(neighbour)){
                return edges.get(neighbour);
            }
            return -1;
        }

        private void deleteNode(){
            // we delete the node,
            // we go over all the neighbours and delete the edge one by one,
            // we don't use RemoveEdge as it would take twice as many actions.
            // so we delete it by hand.
            Collection<Integer> keys = new HashSet<>(edges.keySet());
            for (int ni: keys) {
                nodes.get(ni).edges.remove(key);
                actionMade++;
                edgeNum--;
            }
            edges = new HashMap<>();
            nodes.remove(key);
            actionMade++;
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            info = s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }

    // this is the fastest implementation i've made, the Node is responsible,
    // for handling edges, with means the graph only saves the nodes.

    private HashMap<Integer,Node> nodes;
    private int actionMade;
    private int edgeNum;

    public WGraph_DS4(){
        init();
    }

    public WGraph_DS4(weighted_graph graph){
        super(graph);
        // the copy graph is implemented in an abstract class.
    }

    @Override
    protected void init() {
        nodes = new HashMap<>();
        actionMade = 0;
        edgeNum = 0;
    }

    @Override
    public WGraphBasics getDeepCopy() {
        return new WGraph_DS4(this);
    }

    @Override
    public String toString() {
        return "nodes : " + nodeSize() + " || edges : " + edgeSize() + " || MC : " + getMC();
    }

    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        Node first = nodes.get(node1);
        if(first != null){
            return first.hasEdge(node2);
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        Node first = nodes.get(node1);
        if(first != null){
            return first.getEdge(node2);
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!nodes.containsKey(key)){
            nodes.put(key, new Node(key));
            actionMade++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        Node first = nodes.get(node1);
        if(first != null){
            Node second = nodes.get(node2);
            if(second != null){
                first.addEdge(second, w);
            }
        }
    }

    @Override
    public Collection<node_info> getV() {
        // b.t.w this casting operation is O(1) already tested.
        // its safe because i know that all the objects in nodes are indeed node_info.
        return (Collection<node_info>) (Collection<? extends node_info>) nodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        Node node = nodes.get(node_id);
        return node != null ? node.getV() : null;
    }

    @Override
    public node_info removeNode(int key) {
        Node node = nodes.get(key);
        if(node != null){
            node.deleteNode();
        }
        return node;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        Node node = nodes.get(node1);
        if(node != null) {
            node.removeEdge(node2);
        }
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeNum;
    }

    @Override
    public int getMC() {
        return actionMade;
    }
}
