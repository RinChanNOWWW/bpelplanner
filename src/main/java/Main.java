import bpel.BpelParser;
import dag.Dag;
import dag.DagNode;
import org.dom4j.DocumentException;

import java.util.List;

public class Main {
    public static void main(String[] args) throws DocumentException {
        BpelParser parser = new BpelParser("examples/purchaseOrderProcess.xml");
        Dag dag = parser.parseToDag();
        List<List<DagNode>> result = dag.getAllTopologicalSortResults();
        printResults(result);
    }

    public static void printResults(List<List<DagNode>> result) {
        for (int i = 0; i < result.size(); i++) {
            List<DagNode> oneResult = result.get(i);
            System.out.printf("Plan %02d: ", i + 1);
            for (int j = 0; j < oneResult.size() - 1; j++) {
                System.out.printf("%s -> ", oneResult.get(j));
            }
            System.out.printf("%s\n", oneResult.get(oneResult.size()- 1));
        }
    }

    public static void dagTest() {
        Dag dag = new Dag();
        DagNode node1 = new DagNode("Receive Purchase Order");
        DagNode node2 = new DagNode("Initiate Price Calculation");
        DagNode node3 = new DagNode("Complete Price Calculation");
        DagNode node4 = new DagNode("Decide On Shipper");
        DagNode node5 = new DagNode("Arrange Logistics");
        DagNode node6 = new DagNode("Initiate Production Scheduling");
        DagNode node7 = new DagNode("Complete Production Scheduling");
        DagNode node8 = new DagNode("Invoice Processing");

        dag.addEdge(node1, node2);
        dag.addEdge(node1, node4);
        dag.addEdge(node1, node6);

        dag.addEdge(node2, node3);
        dag.addEdge(node4, node5);
        dag.addEdge(node4, node3);
        dag.addEdge(node5, node7);
        dag.addEdge(node6, node7);

        dag.addEdge(node3, node8);
        dag.addEdge(node5, node8);
        dag.addEdge(node7, node8);

        List<List<DagNode>> result = dag.getAllTopologicalSortResults();
        printResults(result);
    }
}
