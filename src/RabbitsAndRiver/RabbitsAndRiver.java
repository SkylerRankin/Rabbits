package RabbitsAndRiver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.graphstream.graph.implementations.*;

public class RabbitsAndRiver {

    public static void main(String[] args) {
        System.out.println("Rabbits Crossing River Puzzle Solver");
        org.graphstream.graph.Graph visual = new SingleGraph("Visual");
        visual.addAttribute("ui.stylesheet", "url(file:\\\\assets\\style.css)");
        Set<String> all = new HashSet<>();
        all.add("A1");
        all.add("A2");
        all.add("A3");
        all.add("C1");
        all.add("C2");
        all.add("C3");
        Configuration start = new Configuration(6, 0, all, new HashSet<>());
        Configuration end = new Configuration(6, 1, new HashSet<>(), all);
        Graph<Configuration> g = GraphGenerator.generate(3, 1, start, end, visual);
        for (Configuration c : g.get_vertices()) System.out.println(c+" - "+c.hashCode());
        List<Configuration> path = Operations.dfs(g, start, end);
        if (path.size() == 0) {
            System.out.println("No solution");
        } else {
            System.out.println("path");
            for (Configuration c : path) System.out.println(c);
        }
    }
}