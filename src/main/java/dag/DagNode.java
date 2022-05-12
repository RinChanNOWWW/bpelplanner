package dag;

/**
 * 有向无环图节点
 */
public class DagNode {
    public DagNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            return name.equals(obj.toString());
        }
        return false;
    }

    private final String name;
}