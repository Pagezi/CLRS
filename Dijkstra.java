import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
//分为四个步骤
//1.是否还有节点没用处理
//2.获取离起点最近的顶点
//3.更新其邻居的开销，同时更新父节点
//4.标记该节点被处理过，转步骤1
class Vertex implements Comparable<Vertex>{
    public String name;
    public int dist;
    public boolean isMarked;
    public Vertex(String name)
    {
        this.name=name;
        this.dist=Integer.MAX_VALUE;
        this.isMarked=false;
    }
    public Vertex(String name,int dist)
    {
        this.name=name;
        this.dist=dist;
        this.isMarked=false;
    }
    @Override
    public int compareTo(Vertex o) {
        return o.dist>this.dist?-1:1;
    }
}

public class Dijkstra {
    private List<Vertex> vertexs;
    private int[][] edges;
    private Queue<Vertex> unVisited;
    public Dijkstra(List<Vertex> vertexs, int[][] edges) {
        this.vertexs = vertexs;
        this.edges = edges;
        initUnVisited();
    }
    private void initUnVisited() {
        unVisited = new PriorityQueue<Vertex>();
        for (Vertex v : vertexs) {
            unVisited.add(v);
        }
    }
    public void search()
    {
        while (!unVisited.isEmpty())
        {
            Vertex vertex = unVisited.element();
            vertex.isMarked=true;
            //获取所有"未访问"的邻居
            List<Vertex> neighbors = getNeighbors(vertex);
            //更新邻居的最短路径
            updatesDistance(vertex, neighbors);
            unVisited.poll();
        }
    }
    private void updatesDistance(Vertex vertex, List<Vertex> neighbors){
        for(Vertex neighbor: neighbors){
            updateDistance(vertex, neighbor);
        }
    }
    private void updateDistance(Vertex vertex, Vertex neighbor){
        int distance = edges[vertexs.indexOf(vertex)][vertexs.indexOf(neighbor)]+ vertex.dist;
        if(distance < neighbor.dist){
            neighbor.dist=distance;
        }
    }
    private List<Vertex> getNeighbors(Vertex v) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        int position = vertexs.indexOf(v);
        Vertex neighbor = null;
        int distance;
        for(int i=0;i<vertexs.size();i++)
        {
            if(i==position)
                continue;
            distance = edges[position][i];    //到所有顶点的距离
            if (distance < Integer.MAX_VALUE) {
                //是邻居(有路径可达)
                neighbor = vertexs.get(i);
                if (neighbor.isMarked==false) {
                    //如果邻居没有访问过，则加入list;
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }
    public void printGraph() {
        int verNums = vertexs.size();
        for (int row = 0; row < verNums; row++) {
            for (int col = 0; col < verNums; col++) {
                if(Integer.MAX_VALUE == edges[row][col]){
                    System.out.print("X");
                    System.out.print(" ");
                    continue;
                }
                System.out.print(edges[row][col]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        List<Vertex> vertexs = new ArrayList<Vertex>();
        Vertex a = new Vertex("A", 0);
        Vertex b = new Vertex("B");
        Vertex c = new Vertex("C");
        Vertex d = new Vertex("D");
        Vertex e = new Vertex("E");
        Vertex f = new Vertex("F");
        vertexs.add(a);
        vertexs.add(b);
        vertexs.add(c);
        vertexs.add(d);
        vertexs.add(e);
        vertexs.add(f);
        int[][] edges = {
                {Integer.MAX_VALUE, 6, 3, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {6, Integer.MAX_VALUE, 2, 5, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {3, 2, Integer.MAX_VALUE, 3, 4, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, 5, 3, Integer.MAX_VALUE, 5, 3},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 4, 5, Integer.MAX_VALUE, 5},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3, 5, Integer.MAX_VALUE}

        };
        Dijkstra graph = new Dijkstra(vertexs, edges);
        graph.printGraph();
        graph.search();
    }
}
