package RabbitsAndRiver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {
    
    private int size;
    private int side;
    private Set<String> left;
    private Set<String> right;
    private List<String> sorted_left;
    private List<String> sorted_right;
    
    /**
     * Instantiate a configuration for a possible state of the puzzle
     * @param size         The total number of moving variables
     * @param side      0 for left, 1 for right
     */
    public Configuration(int size, int side) {
        this.size = size;
        this.side = side;
        left = new HashSet<String>();
        right = new HashSet<String>();
        sorted_left = new ArrayList<>();
        sorted_right = new ArrayList<>();
    }
    
    public Configuration(int size, int side, Set<String> left, Set<String> right) {
        if (size != left.size() + right.size()) throw new IllegalArgumentException("Sum total of variables must equal size: "+size+" != "+left.size()+" + "+right.size() );
        this.size = size;
        this.side = side;
        this.left = new HashSet<String>();
        this.right = new HashSet<String>();
        for (String k : left) this.left.add(k);
        for (String k : right) this.right.add(k);
        sorted_left = new ArrayList<>(left);
        sorted_right = new ArrayList<>(right);
        Collections.sort(sorted_left, new string_sort());
        Collections.sort(sorted_right, new string_sort());
    }
    
    public boolean add_right(String s) {
        sorted_right.add(s);
        Collections.sort(sorted_right, new string_sort());
        return right.add(s);
    }
    
    public boolean add_left(String s) {
        sorted_left.add(s);
        Collections.sort(sorted_left, new string_sort());
        return left.add(s);
    }
    
    public boolean remove_left(String s) {
        sorted_left.remove(s);
        return left.remove(s);
    }
    
    public boolean remove_right(String s) {
        sorted_right.remove(s);
        return right.remove(s);
    }
    
    public Set<String> get_left() {
        return left;
    }
    
    public Set<String> get_right() {
        return right;
    }
    
    public int variable_count() {
        return size;
    }
    
    public int get_side() {
        return side;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("["+side+"]");
        for (String s : left)
            sb.append(s);
        sb.append("#");
        for (String s : right)
            sb.append(s);
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Configuration) || ((Configuration)o).variable_count() != this.size) return false;
        Configuration c = (Configuration) o;
        if (c.get_left().size() != this.left.size() || c.get_right().size() != this.right.size() || c.get_side() != this.side) return false;
        for (String s : c.get_left()) {
            if (!this.left.contains(s)) return false;
        }
        for (String s : c.get_right()) {
            if (!this.right.contains(s)) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sorted_left.size(); i++) sb.append(sorted_left.get(i).substring(1));
        for (int i = 0; i < sorted_right.size(); i++) sb.append(sorted_right.get(i).substring(1));
        sb.append(side);
        sb.append(left.size());
        return Integer.parseInt(sb.toString());
    }
    
    private class string_sort implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            int alpha = a.substring(0, 1).compareTo(b.substring(0, 1));
            if (alpha != 0) return alpha;
            int x = Integer.parseInt(a.substring(1));
            int y = Integer.parseInt(b.substring(1));
            return x == y ? 0 : (x > y ? 1 : -1);
        }
        
    }

}
