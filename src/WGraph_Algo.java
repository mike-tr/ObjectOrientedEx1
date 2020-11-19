package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    public WGraph_Algo(){
        graph = new WGraph_DS();
    }
    weighted_graph graph;
    public WGraph_Algo(weighted_graph graph){
        init(graph);
    }
    @Override
    public void init(weighted_graph g) {
        graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return graph;
    }

    @Override
    public weighted_graph copy() {
        if(graph instanceof WGraphBasics){
            System.out.println("Making WGraph copy");
            return ((WGraphBasics)graph).getDeepCopy();
        }
        return new WGraph_DS(graph);
    }

    @Override
    public boolean isConnected() {
        Collection<node_info> nodes = graph.getV();
        if(nodes.size() < 2){
            return true;
        }

        int numNodes = nodes.size() - 1;
        int edges = graph.edgeSize();
        if(numNodes > edges){
            // if the graph has less then n-1 edges its not connected
            return false;
        }

        for (node_info node: nodes) {
            node.setTag(-1);
        }

        ArrayDeque<node_info> open = new ArrayDeque<>();
        node_info current = nodes.iterator().next();
        current.setTag(0);
        open.add(current);
        while (!open.isEmpty()){
            current = open.poll();
            if(numNodes == 0){
                // if the number of nodes counted is n, we exit.
                return true;
            }

            Collection<node_info> nis = graph.getV(current.getKey());
            if(nis.size() == 0){
                // maybe the node has no neighbours so we just exit.
                return false;
            }
            for (node_info node : nis) {
                // if not marked then we add it to the open list
                if(node.getTag() == -1){
                    node.setTag(0);
                    open.add(node);
                    numNodes--;
                }
            }
        }

        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        // the priority is the distance so we return it.
        WPathNode path = calculateShortestPath(src, dest);
        return path != null ? path.getPriority() : -1;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        // given the PathNode we can retrace back from dest to src.
        WPathNode pathN = calculateShortestPath(src, dest);
        if(pathN != null){
            List<node_info> path = new ArrayList<>();
            while (pathN != null){
                path.add(0, pathN.getNode());
                if(pathN.getKey() == src){
                    break;
                }
                pathN = pathN.getParent();
            }
            return path;
        }
        return null;
    }

    private static final int CALCULATED = -2;
    private static final int NEWNODE = -1;
    public WPathNode calculateShortestPath(int src, int dest) {
        // create an open list that would be a heap.
        Heap<WPathNode> open = new Heap();
        // if any of the src or dest are not in the graph we exist there is no possible path.
        node_info source = graph.getNode(src);
        if(source == null){
            return null;
        }

        if(graph.getNode(dest) == null){
            return null;
        }

        // mark every node as new node.
        for (node_info node: graph.getV()) {
            node.setTag(NEWNODE);
        }


        double tag;
        double distance;
        int key;
        open.add(new WPathNode(source), 0);
        while (open.size() > 0){
            WPathNode current = open.poll();
            key = current.getKey();
            if(key == dest){
                return current;
            }
            // tag -2 means we already visited this node so we skip
            current.getNode().setTag(CALCULATED);

            for (node_info node: graph.getV(key)) {
                tag = node.getTag();
                if(tag == CALCULATED){
                    continue;
                }

                distance = graph.getEdge(key, node.getKey()) + current.getPriority();
                if(tag == NEWNODE){
                    WPathNode pathNode = new WPathNode(node, current);
                    open.add(pathNode, distance);
                    continue;
                }

                // here happens the optimization, my WPathNode, sets the tag of the node
                // to be the index in the heap array, with means we hae O(1), find method,
                // with means updating the item cost us only log(n), as it should.
                // if we would needed to "search" for it that would make it much worse
                // a.k.a in the PriorityQueue one must remove and insert the item,
                // with means we search for the item with is log(n) or worse, and then remove it log(n)
                // and then add it log(n), hence 3log(n)
                WPathNode pn = open.getAt((int)tag);
                if(open.tryUpgrade(pn, distance) == 1){
                    pn.setParent(current);
                }
            }
        }
        return null;
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream myFile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(myFile);
            oos.writeObject(graph);

            oos.close();
            myFile.close();
            return true;
        }catch (Exception ex){
            System.out.println(ex.fillInStackTrace());
        }
        return false;
    }

    @Override
    public boolean load(String file) {
        try{
            FileInputStream myFile = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(myFile);
            weighted_graph graph = (weighted_graph)ois.readObject();

            ois.close();
            myFile.close();

            if(graph != null) {
                this.graph = graph;
                return true;
            }
        }
        catch (Exception error){
            error.printStackTrace();
        }
        return false;
    }

    // implemintation without my heap, using priority queue, on the node_info itself
    // it is very ineficient, as we need to always remove and insert the same element in order to update it.
    // hence slow af, or doesn't work

//    @Override
//    public double shortestPathDist(int src, int dest) {
//        // given the WPathNode we have the distance, so return it.
//        node_info path = calculateShortestPath(src, dest);
//        return path != null ? path.getTag() : -1;
//    }
//
//    @Override
//    public List<node_info> shortestPath(int src, int dest) {
//        //given WPathNode we traverse back and get the original path.
//        node_info pathN = calculateShortestPath(src, dest);
//        if(pathN != null){
//            List<node_info> path = new ArrayList<>();
//            while (pathN != null){
//                path.add(0, pathN);
//                if(pathN.getKey() == src){
//                    break;
//                }
//                pathN = graph.getNode(Integer.parseInt(pathN.getInfo()));
//            }
//            return path;
//        }
//        return null;
//    }
//
//    public node_info calculateShortestPath(int src, int dest) {
//        PriorityQueue<node_info> open = new PriorityQueue<>(
//                (node1, node2) -> node1.getTag() > node2.getTag() ? 1 : -1);
//        HashSet<Integer> closed = new HashSet<>();
//
//        node_info source = graph.getNode(src);
//        if(source == null){
//            return null;
//        }
//
//        if(graph.getNode(dest) == null){
//            return null;
//        }
//
//        for (node_info node: graph.getV()) {
//            node.setTag(-1);
//        }
//
//        node_info current;
//        double distance;
//        double distance_ni;
//        String parent;
//        open.add(source);
//        source.setTag(0);
//        while (open.size() > 0){
//            System.out.println(open);
//            current = open.poll();
//            System.out.println(current.getTag());
//            System.out.println(open);
//            System.out.println();
//
//            if(current.getKey() == dest){
//                return current;
//            }
//
//            int key = current.getKey();
//            closed.add(key);
//            parent = Integer.toString(key);
//            distance = current.getTag();
//            //current.setTag(0);
//
//            for (node_info node: graph.getV(key)) {
//                if(closed.contains(node.getKey())){
//                    continue;
//                }
//
//                distance_ni = graph.getEdge(key, node.getKey()) + distance;
//
//                if(node.getTag() == -1){
//                    node.setInfo(parent);
//                    node.setTag(distance_ni);
//                    open.add(node);
//                }else if(node.getTag() > distance_ni){
//                    node.setInfo(parent);
//                    node.setTag(distance_ni);
//                    //open.add(node);
//                }
//            }
//        }
//        return null;
//    }
}
