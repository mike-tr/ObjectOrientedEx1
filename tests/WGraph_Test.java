import ex1.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class represents a very simple (naive) JUnit test case
 * for Graph (Ex0)
 */
public class WGraph_Test {
    private static Random _rand;
    private static long _seed;
    public static void initSeed(long seed) {
        _seed = seed;
        _rand = new Random(_seed);
    }
    public static void initSeed() {
        initSeed(_seed);
    }
    @Test
    public void graphTest_0() {
        int v=0, e=0;
        weighted_graph g = graph_creator(v,e,1);
        if (g==null) {
            fail("The graph g "+g+" should not ne null");
        }
    }
    @Test
    public void graphTest_1() {
        int v=10, e=30;
        weighted_graph g = graph_creator(v,e,1);
        assertEquals(g.edgeSize(),e);
        assertEquals(g.nodeSize(), v);
    }

    /**
     * Runtime test - if the testing method is still working after 5 seconds (5000 mili sec)
     * An exception is being thrown.
     */
    @Test(timeout = 5000)
    public void graphTest_runtime() {
        int v=10000*30, e=v*5;
        weighted_graph g = graph_creator(v,e,1);
        // while(true) {;}
    }

    @Test
    public void graphLoadSaveTest(){
        int v=1000, e=v*10;
        String fileLoc = "graphT01";
        weighted_graph g = graph_creator(v,e,1);



        weighted_graph_algorithms algo = new WGraph_Algo(g);

        assertTrue(algo.save(fileLoc));
        assertTrue(algo.load(fileLoc));
        assertEquals(algo.getGraph(), g);

        weighted_graph_algorithms algoOrigin = new WGraph_Algo(g);
        assertEquals(algo.shortestPath(0, 10).toString(),
                algoOrigin.shortestPath(0, 10).toString());
    }

    @Test
    public void graphPathTest(){
        weighted_graph graph = new WGraph_DS();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);

        graph.connect(1, 2, 10);
        graph.connect(1, 3, 5);
        graph.connect(1,4,100);
        graph.connect(2,4,5);
        graph.connect(2,3,50);
        graph.connect(3,5,30);
        graph.connect(4,5,10);

        weighted_graph_algorithms algo = new WGraph_Algo(graph);

        double v1 = algo.shortestPathDist(1,5);
        double v2 = algo.shortestPathDist(5,1);
        assertEquals(v1, v2);
        assertEquals(algo.shortestPathDist(1,5), 25);
    }

    @Test
    public void graphConnectivity(){
        int v_size = 1000;
        int e_size = v_size * 2;
        int mid = v_size / 2;
        testIsConnected(mid, e_size, v_size, 1);
        testIsConnected(mid, e_size * 4, v_size , 15);
        testIsConnected(mid, e_size, v_size * 5, 32);
        testIsConnected(mid, e_size * 3, v_size * 15, 32);
}

    public void testIsConnected(int mid, int e_size, int v_size, int seed) throws RuntimeException {
        if(v_size < 50){
            v_size = 50;
        }
        if(mid < 1 || mid > v_size - 2){
            throw new RuntimeException("mid should be > 0 and less then n_size - 1");
        }
        weighted_graph g = graph_creator(v_size, 0,seed);
        int[] nodes = nodes(g);
        int middle = nodes[mid];

        int rootA = nodes[mid - 1];
        int rootB = nodes[mid];

//         create 2 "trees", with mid - 1 connected to all in one,
//         and mid connected to all in the other half.
        for (int i = 0; i < mid - 1; i++) {
            double w = nextRnd(1, 10.0);
            g.connect(nodes[i], rootA,w);
        }
        for (int i = mid+1; i < v_size; i++) {
            double w = nextRnd(1, 10.0);
            g.connect(nodes[i], rootB,w);
        }


        e_size = e_size / 2;
        while(g.edgeSize() < e_size) {
            int a = nextRnd(mid,v_size);
            int b = nextRnd(mid,v_size);
            double w = nextRnd(1, 10.0);
            g.connect(nodes[a],nodes[b],w);

            a = nextRnd(0,mid);
            b = nextRnd(0,mid);
            w = nextRnd(5, 10.0);
            g.connect(nodes[a],nodes[b],w);
        }

        weighted_graph_algorithms a = new WGraph_Algo(g);
        assertFalse(a.isConnected());
        assertEquals(a.shortestPathDist(nodes[0], nodes[v_size-1]), -1);

        g.connect(rootA , rootB, 1);
        assertTrue(a.isConnected());
        assertEquals(a.shortestPathDist(nodes[0], nodes[v_size-1]), a.shortestPathDist(nodes[v_size-1], nodes[0]));
        g.connect(nodes[0], nodes[5], 1);
        g.connect(nodes[5], rootA, 1);
        g.connect(nodes[v_size - 8], rootB, 1);
        g.connect(nodes[v_size - 8], nodes[v_size - 1], 1);
        assertEquals(a.shortestPathDist(nodes[0], nodes[v_size-1]), 5);
    }


    /////////////////////////////////////////////////
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rand.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        //System.out.println(V.size() + " ," + size);
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    private static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
       initSeed(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            double w = nextRnd(0, 10.0);
            int i = nodes[a];
            int j = nodes[b];
            g.connect(i,j,w);
        }
        return g;
    }
}
