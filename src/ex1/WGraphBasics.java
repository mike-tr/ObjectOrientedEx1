package ex1;

import java.io.*;

public abstract class WGraphBasics implements weighted_graph, Serializable {

    // we need a super default constructor
    WGraphBasics(){ }

    WGraphBasics(weighted_graph graph){
        init();
        setAsCopy(graph);
    }

    // this method should populate with zero values stuff like arrays etc...
    protected abstract void init();

    // return a deep copy of the current graph.
    public abstract WGraphBasics getDeepCopy();

    // create a copy from any weighted_graph ANY.
    private void setAsCopy(weighted_graph graph){
        System.out.println("Setting new copy");
        for (node_info node: graph.getV()) {
            addNode(node.getKey());
        }

        for (node_info node: graph.getV()) {
            int key = node.getKey();
            for (node_info ni: graph.getV(key)) {
                connect(key, ni.getKey(), graph.getEdge(key, ni.getKey()));
            }
        }
    }

    public WGraphBasics getSerializedCopy(){
        // this method would return as a serialized copy of this graph,
        // its slower so i don't use it.
        System.out.println("Creating Serialized copy");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            //De-serialization of object
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            WGraphBasics copied = (WGraphBasics) in.readObject();

            return copied;
        }catch (Exception ex){
            System.out.println(ex.fillInStackTrace());
        }
        return null;
    }
}
