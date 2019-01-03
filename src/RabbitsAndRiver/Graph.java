package RabbitsAndRiver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//Graph.java

public class Graph<V> {
    
    private final boolean debug = true;
    private Set<V> vertices;
    private Map<V, Set<V>> neighbors;
    
    public Graph() {
        vertices = new HashSet<V>();
        neighbors = new HashMap<V, Set<V>>();
    }
    
    public Set<V> get_neighbors(V v) {
        return neighbors.get(v);
    }
    
    public boolean add_vertex(V v) {
        if (vertices.contains(v)) return false;
        vertices.add(v);
        neighbors.put(v, new HashSet<V>());
        return true;
    }
    
    public boolean delete_vertex(V v) {
        if (!vertices.contains(v)) return false;
        vertices.remove(v);
        for (V u : neighbors.get(v)) {
            neighbors.get(u).remove(v);
        }
        neighbors.remove(v);
        return false;
    }
    
    public boolean add_edge(V v, V u) {
        if (!vertices.contains(v) || v.equals(u)) return false;
        if (neighbors.get(v).contains(u)) return false;
        neighbors.get(v).add(u);
        return true;
    }
    
    public int vertex_size() {
        return vertices.size();
    }
    
    public Set<V> get_vertices() {
        return vertices;
    }

}
