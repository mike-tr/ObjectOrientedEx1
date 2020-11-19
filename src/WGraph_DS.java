package ex1.src;

public class WGraph_DS extends WGraph_DS4 {
    // nothing in here, this is just a fast way to switch between GRAPHS.
    // currently implements DS4
    public WGraph_DS(){

    }

    public WGraph_DS(weighted_graph graph){
        super(graph);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(obj instanceof weighted_graph){
            weighted_graph gs = (weighted_graph) obj;
            return gs.edgeSize() == edgeSize() && gs.nodeSize() == nodeSize();
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
