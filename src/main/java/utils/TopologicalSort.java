package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拓扑排序类。使用回溯法进行深度优先搜索
 */
public class TopologicalSort {
    public TopologicalSort(int n) {
        nodesNum = n;
        edges = new boolean[n][n];
        inDegrees = new int[n];
        visited = new boolean[n];
    }

    public void addEdge(int from, int to) {
        edges[from][to] = true;
        inDegrees[to]++;
    }

    public List<List<Integer>> getResults() {
        List<List<Integer>> results = new ArrayList<>();
        int[] tempPath = new int[nodesNum];
        dfs(0, tempPath, results);
        return results;
    }

    private void dfs(int step, int[] tempPath, List<List<Integer>> results) {
        if (step == nodesNum) {
            List<Integer> result = Arrays.stream(tempPath).boxed().collect(Collectors.toList());
            results.add(result);
        } else {
            for (int i = 0; i < nodesNum; i++) {
                if (canPick(i)) {
                    tempPath[step] = i;
                    visit(i);
                    dfs(step + 1, tempPath, results);
                    // 回溯
                    undoVisit(i);
                }
            }
        }
    }

    private void visit(int i) {
        visited[i] = true;
        for (int to = 0; to < nodesNum; to++) {
            if (edges[i][to]) {
                inDegrees[to]--;
            }
        }
    }

    private void undoVisit(int i) {
        visited[i] = false;
        for (int to = 0; to < nodesNum; to++) {
            if (edges[i][to]) {
                inDegrees[to]++;
            }
        }
    }

    private boolean canPick(int i) {
        return !visited[i] && inDegrees[i] == 0;
    }

    private final int nodesNum;
    private final boolean[][] edges;
    private final int[] inDegrees;
    private final boolean[] visited;
}