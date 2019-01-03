package RabbitsAndRiver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// GraphGenerator.java

public class GraphGenerator {
    
    /**
    Generate a graph representing configuration paths between a given start and end
    @param pairs        the number of parent/child pairs to consider
    @param speacial     the number of children with the crossing ability
    */
    public static Graph<Configuration> generate(int pairs, int special, Configuration start, Configuration end, org.graphstream.graph.Graph visual) {
        Graph<Configuration> g = new Graph<Configuration>();
        
        //number of key vertices to start searching from; will be incremented later
        int keys = 2;
        
        //map of visited vertices, the integers represent starting points
        Map<Integer, Set<Configuration>> visited = new HashMap<>();
        
        //queue for search
        Map<Integer, Queue<Configuration>> queue_map = new HashMap<>();
        
        g.add_vertex(start);
            visual.addNode(start.toString());
            visual.getNode(start.toString()).setAttribute("ui.label", "start");
        g.add_vertex(end);
            visual.addNode(end.toString());
            visual.getNode(end.toString()).setAttribute("ui.label", "end");
        queue_map.put(0, new LinkedList<Configuration>());
        queue_map.get(0).add(start);
        queue_map.put(1, new LinkedList<Configuration>());
        queue_map.get(1).add(end);
        
        //alternate between start and end, adding valid vertices and edges to the graph
        //maintain sets of visited vertices from each
        //once a common vertex is found, stop
        boolean empty = false, loop = true;
        int count = 0, max = 10000000;
        while (loop && count < max) {
            count++;
            empty = false;
            for (int i = 0; i < keys; ++i) {
                Configuration curr = queue_map.get(i).remove();
                //get set of next reachable vertices
                Set<Configuration> s = next_configurations(curr, visited.get(i), pairs, special);
                queue_map.get(i).addAll(s);
                
                //TODO implement skipping for starting points that don't have anywhere to go but haven't reached the other starting points
                //loop = !s.isEmpty();
                
                //mark configurations as visited from starting vertex i
                if (visited.containsKey(i)) {
                    visited.get(i).addAll(s);
                } else {
                    visited.put(i, s);
                }
                
                //update the graph
                for (Configuration c : s) {
                    
                    //once a new vertex if found, mark as not empty
                    empty = !g.add_vertex(c);
                        try {
                            visual.addNode(c.toString());
                            visual.getNode(c.toString()).setAttribute("ui.label", c.toString());
                        } catch (Exception e) {}
                        
                    g.add_edge(curr, c);
                        try {
                            visual.addEdge(curr.toString()+"->"+c.toString(), curr.toString(), c.toString(), true);
                        } catch (Exception e) {}
                        
                    //if 1, check 0, if 0, check 1
                    if (visited.get(Math.abs(i-1)) != null && visited.get(Math.abs(i-1)).contains(c)) { 
                        System.out.println("COMMONALITY "+c);
                        loop = false;
                        break;
                    }
                }
                if (!loop || empty) break;
            }
        }
        
        //TODO systematically add more centralized points to expand from, "middle out"
        visual.display();
        return g;
    }
    
    /**
    Checks if a given configuration is a valid node in the graph
    TODO instead of checking the entire configuration, assume that the previous one was valid, and then only checked the changed variables
    @param c        The configuration to check
     */
    private static boolean valid(Configuration c, int special) {
        Set<String> adults = new HashSet<String>();
        Set<String> isolated = new HashSet<String>();
        for (String s : c.get_left()) {
            int n = Integer.parseInt(s.substring(1, s.length()));
            //if the child exists on left without matching parent
            if (s.charAt(0) == 'C' && !c.get_left().contains("A"+n)) {
                if (adults.size() > 0) return false;
                isolated.add(s);
            } else if (s.charAt(0) == 'A') {
                if (isolated.size() > 0 && !isolated.contains("C"+n)) return false;
                adults.add(s);
            }
        }
        isolated.clear();
        adults.clear();
        for (String s : c.get_right()) {
            int n = Integer.parseInt(s.substring(1, s.length()));
            //if the child exists on left without matching parent
            if (s.charAt(0) == 'C' && !c.get_right().contains("A"+n)) {
                if (adults.size() > 0) return false;
                isolated.add(s);
            } else if (s.charAt(0) == 'A') {
                if (isolated.size() > 0 && !isolated.contains("C"+n)) return false;
                adults.add(s);
            }
        }
        return true;
    }
    
    /**
    Generates a set of possible next steps from a particular configuration
    @param c            The current configuration
    @param visited      The previously visited configurations
    @param direction    0 if moving to the right, 1 if moving to the left
     */
    public static Set<Configuration> next_configurations(Configuration c, Set<Configuration> visited, int pairs, int special) {
        Set<Configuration> next = new HashSet<>();
        int new_dir = c.get_side() == 0 ? 1 : 0;
        //single movements
        for (String s : (c.get_side() == 0 ? c.get_left() : c.get_right())) {
            Configuration c2 = new Configuration(pairs*2, new_dir, c.get_left(), c.get_right());
            if (s.charAt(0) == 'A' || (s.charAt(0) == 'C' && Integer.parseInt(s.substring(1, s.length())) <= special)) {
                if (c.get_side() == 0) {
                    c2.add_right(s);
                    c2.remove_left(s);
                } else {
                    c2.add_left(s);
                    c2.remove_right(s);
                }
                if (valid(c2, special)) next.add(c2);
            }
        }
        
        //double movements
        Set<String> used = new HashSet<>();
        for (String s : (c.get_side() == 0 ? c.get_left() : c.get_right())) {
            for (String d : (c.get_side() == 0 ? c.get_left() : c.get_right())) {
                if (s.equals(d)) continue;
                //if both are children and both are not special, skip
                if (s.charAt(0) == 'C' && d.charAt(0) == 'C' && Integer.parseInt(s.substring(1, s.length())) > special && Integer.parseInt(d.substring(1, d.length())) > special) continue;
                //if adult and child, but not related, skip
                if (((s.charAt(0) == 'C' && d.charAt(0) == 'A') || (s.charAt(0) == 'A' && d.charAt(0) == 'C')) && !s.substring(1, s.length()).equals(d.substring(1, d.length()))) continue;
                Configuration c2 = new Configuration(pairs*2, new_dir, c.get_left(), c.get_right());
                if (c.get_side() == 0) {
                    c2.add_right(s);
                    c2.add_right(d);
                    c2.remove_left(s);
                    c2.remove_left(d);
                } else {
                    c2.add_left(s);
                    c2.add_left(d);
                    c2.remove_right(s);
                    c2.remove_right(d);
                }
                if (!used.contains(s+d) && valid(c2, special)) { 
                    next.add(c2);
                    used.add(s+d);
                    used.add(d+s);
                }
            }
        }
        return next;
    }
    
    public static void print_graph(Graph<Configuration> g) {
        System.out.println("---Graph, "+g.vertex_size()+" vertices---");
        for (Configuration c : g.get_vertices()) {
            System.out.println(c);
            for (Configuration d : g.get_neighbors(c)) {
                System.out.println("\t"+d);
            }
        }
        System.out.println("------");
    }
    
    public static void printset(Set<Configuration> s) {
        System.out.println("-------------"+s.size()+"-------------");
        for (Configuration c : s) {
            System.out.println(c);
        }
        System.out.println("---------------------------");
    }
}