package dag;

import utils.TopologicalSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 有向无环图
 */
public class Dag {
    public Dag() {
        this.nodeToId = new HashMap<>();
        this.IdToNode = new HashMap<>();
        this.dag = new HashMap<>();
        this.nodesNum = 0;
    }

    public void addNode(DagNode node) {
        if (!nodeToId.containsKey(node)) {
            nodeToId.put(node, nodesNum);
            IdToNode.put(nodesNum, node);
            nodesNum++;
        }
    }

    public void addEdge(DagNode from, DagNode to) {
        addNode(from);
        addNode(to);
        int fromId = nodeToId.get(from);
        int toId = nodeToId.get(to);
        HashSet<Integer> set;
        if (!dag.containsKey(fromId)) {
            set = new HashSet<>();
        } else {
            set = dag.get(fromId);
        }
        set.add(toId);
        dag.put(fromId, set);
    }

    /**
     * 获取有向无环图的所有拓扑排序序列。
     *
     * @return 一个二维数组。一维元素为一个拓朴排序序列。
     */
    public List<List<DagNode>> getAllTopologicalSortResults() {
        TopologicalSort sort = new TopologicalSort(nodesNum);
        for (Integer from : dag.keySet()) {
            HashSet<Integer> edges = dag.get(from);
            for (Integer to : edges) {
                sort.addEdge(from, to);
            }
        }

        List<List<Integer>> results = sort.getResults();

        List<List<DagNode>> ret = new ArrayList<>();
        for (List<Integer> result : results) {
            List<DagNode> r = new ArrayList<>();
            for (Integer node : result) {
                r.add(IdToNode.get(node));
            }
            ret.add(r);
        }

        return ret;
    }

    private final HashMap<Integer, HashSet<Integer>> dag;
    private final HashMap<DagNode, Integer> nodeToId;
    private final HashMap<Integer, DagNode> IdToNode;
    private int nodesNum;
}


