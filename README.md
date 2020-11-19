# OOJavaA

I. File list
--------------------------------------------------------------------------------------------
EX1

node_info, weighted_graph, weighted_graph_algorithms, WGraph_AlgoTest, WGraph_DSTest
are all given files.


Heap
 - Heap implementation made because java PriorityQueue is not good enough
---------
INode
 - an interface that add extra spice to the Heap optimization.
---------
WGraph_Algo
 - implements weighted_graph_algorithms, ( save, load, isConnected, PathFinding)
---------
WGraph_DS
 - extends WGraph_DS4, or WGraph_DS3, both work great, but for testing its convenient to extend them. (instead of refactoring)
--------
WGraph_DS3
 - implements weighted_graph it used HashMap<HashMap<>> to store edges.
---------
WGraph_DS4
 - implements weighted_graph it used Hash of edges inside the nodes.
---------
WGraphBasics
 - an abstract class, it just implements the "deep copy", function for both DS3, and DS4.
 ---------
WPathNode
 - node class that impl
 ---------


How to use? like any other java program.
Use WGraph_DS to build a graph, add nodes etc...
Use WGraph_Algo to do some calculations


II. Design
--------------------------------------------------------------------------------------------
A. WGraph_DS

1. Uses WGraph_DS4, with just stores nodes, each node stores all the weight to any other nodes.
that's essentially it, also uses Serializeble, so we can save it.
--------------------------------------------------------------------------------------------
B. Heap

1. The basic array implementation of a heap quite easy to make, works great,
added some features that makes it easy to "find" an item that is already in the list.

2. WPathNode, holds the node_info, the distance and the parent to retrace the path.

--------------------------------------------------------------------------------------------
C. WGraph_Algo

1. IsConnected, compute if the graph is fully connected. using BFS

2. PathFinding ( distance and path )
Using the Dijkstra's Algorithm, we compute a path, then we retrace or just return the distance of the last node.
we use the Heap, because it is hand crafted to give us the fastest update item, and get first item methods.

3. Save Load
just save and load.

4. Copy - generate a deep copy of the graph.
--------------------------------------------------------------------------------------------



