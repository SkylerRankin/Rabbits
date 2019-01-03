package RabbitsAndRiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Operations {
    
    public static <V> List<V> dfs(Graph<V> g, V start, V end) {
        Map<V, V> parent = new HashMap<>();
        List<V> path = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        parent.put(start, null);
        dfs_recursive(g, start, end, visited, parent);
        V curr = end;
        for (Entry<V, V> e : parent.entrySet()) {
            System.out.println(e.getKey()+":"+e.getValue());
        }
        while(curr != null) {
            path.add(curr);
            curr = parent.get(curr);
        }
        return path;
    }
    
    private static <V> void dfs_recursive(Graph<V> g, V curr, V target, Set<V> visited, Map<V, V> parent) {
        System.out.println("curr="+curr);
        visited.add(curr);
        if (curr.equals(target)) {
            return;
        }
        Iterator<V> i = g.get_neighbors(curr).iterator();
        while (i.hasNext()) {
            V n = i.next();
            if (!visited.contains(n)) {
                parent.put(n, curr);
                dfs_recursive(g, n, target, visited, parent);
            }
        }
        System.out.println("end resursion for "+curr);
    }
}