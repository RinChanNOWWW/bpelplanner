package bpel;

import bpel.parser.*;
import dag.Dag;
import dag.DagNode;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * BPEL XML 分析器
 */
public class BpelParser {
    public BpelParser(String file) throws DocumentException {
        SAXReader reader = new SAXReader();
        this.document = reader.read(new File(file));
        this.operationInfos = new HashSet<>();
    }

    /**
     * 从 BPEL XML 分析得到有向无环图
     *
     * @return 一个有向无环图
     */
    public Dag parseToDag() {
        Dag dag = new Dag();
        BpelParseNode root = parse();
        List<BpelParseNode> children = root.getChildren();
        // 按照顺序关系添加边到 DAG
        for (BpelParseNode child : children) {
            addToDag(dag, child);
        }
        // 通过 input 与 output 添加额外的边到 DAG
        for (BpelInfo from : operationInfos) {
            for (BpelInfo to : operationInfos) {
                if (from.getOutputVariable() != null && to.getInputVariable() != null &&
                        from.getOutputVariable().equals(to.getInputVariable())) {
                    dag.addEdge(new DagNode(from.getName()), new DagNode(to.getName()));
                }
            }
        }
        return dag;
    }

    /**
     * 用于 addToDag 系列函数的返回值，表示路径的开始节点和结束节点
     */
    static class StartAndEnd {
        StartAndEnd(List<DagNode> start, List<DagNode> end) {
            this.start = start;
            this.end = end;
        }
        List<DagNode> start;
        List<DagNode> end;
    }

    /**
     * 分析语法树节点，想有向无环图中加边。采用了类似于线段树的思想。
     *
     * @param dag 需要添加边的有向无环图
     * @param node 当前分析节点
     * @return 此分析节点对应的入点列表与出点列表
     */
    private StartAndEnd addToDag(Dag dag, BpelParseNode node) {
        StartAndEnd ret = null;
        // 根据节点类型分配实际访问器
        switch (node.getType()) {
            case SEQUENCE -> ret = addSequenceToDag(dag, (SequenceNode) node);
            case FLOW -> ret = addFlowToDag(dag, (FlowNode) node);
            case OPERATION -> ret = addOperationToDag(dag, (OperationNode) node);
        }
        return ret;
    }

    private StartAndEnd addSequenceToDag(Dag dag, SequenceNode node) {
        List<BpelParseNode> children = node.getChildren();
        if (children.size() == 1) {
            BpelParseNode curChild = children.get(0);
            return addToDag(dag, curChild);
        }
        List<DagNode> start = new ArrayList<>();
        List<DagNode> end = new ArrayList<>();
        // 主要思想：children 是顺序执行
        // 将 i 的出点集中的节点全部指向 i + 1 的入点集
        // 返回 i = 0 的入点集与 i = n - 1 的出点集
        for (int i = 0; i < children.size() - 1; i++) {
            BpelParseNode curChild = children.get(i);
            BpelParseNode nextChild = children.get(i + 1);
            StartAndEnd curNodes = addToDag(dag, curChild);
            if (i == 0) {
                start.addAll(curNodes.start);
            }
            StartAndEnd nextNodes = addToDag(dag, nextChild);
            if (i + 1 == children.size() - 1) {
                end.addAll(nextNodes.end);
            }
            for (DagNode from : curNodes.end) {
                for (DagNode to : nextNodes.start) {
                    dag.addEdge(from, to);
                }
            }
        }
        return new StartAndEnd(start, end);
    }

    private StartAndEnd addFlowToDag(Dag dag, FlowNode node) {
        List<BpelParseNode> children = node.getChildren();
        List<DagNode> start = new ArrayList<>();
        List<DagNode> end = new ArrayList<>();
        // 主要思想：children 是并行执行
        // 入点集为每一个 child 的入点总合
        // 出点集为每一个 child 的出点总合
        for (BpelParseNode child : children) {
            StartAndEnd se = addToDag(dag, child);
            start.addAll(se.start);
            end.addAll(se.end);
        }
        return new StartAndEnd(start, end);
    }

    private StartAndEnd addOperationToDag(Dag dag, OperationNode node) {
        List<DagNode> nodes = new ArrayList<>();
        DagNode dagNode = new DagNode(node.getInfo().getName());
        // 分析树的叶子节点，入点合出点都是自己
        dag.addNode(dagNode);
        nodes.add(dagNode);
        return new StartAndEnd(nodes, nodes);
    }

    /*
    以下是生成分析树的过程。采用自底向上递归建立整个分析树。
     */

    /**
     * 分析 XML，形成语法树
     *
     * @return 语法树根节点
     */
    private BpelParseNode parse() {
        BpelParseNode ret = new RootNode();
        Element root = document.getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) {
            Element elem = it.next();
            if (elem.getName().equals("sequence")) {
                ret.addChild(parseSequence(elem));
            } else if (elem.getName().equals("flow")) {
                ret.addChild(parseFlow(elem));
            }
        }
        return ret;
    }

    private SequenceNode parseSequence(Element elem) {
        SequenceNode ret = new SequenceNode();
        Iterator<Element> it = elem.elementIterator();
        while (it.hasNext()) {
            Element e = it.next();
            String elemName = e.getName();
            switch (elemName) {
                case "flow" -> ret.addChild(parseFlow(e));
                case "invoke", "receive", "reply" -> {
                    OperationNode operation = parseOperation(e);
                    if (operation != null) {
                        ret.addChild(operation);
                    }
                }
                default -> {
                }
            }
        }
        return ret;
    }

    private FlowNode parseFlow(Element elem) {
        FlowNode ret = new FlowNode();
        Iterator<Element> it = elem.elementIterator();
        while (it.hasNext()) {
            Element e = it.next();
            String elemName = e.getName();
            switch (elemName) {
                case "sequence" -> ret.addChild(parseSequence(e));
                case "invoke", "receive", "reply" -> {
                    OperationNode operation = parseOperation(e);
                    if (operation != null) {
                        ret.addChild(operation);
                    }
                }
                default -> {
                }
            }
        }
        return ret;
    }

    private OperationNode parseOperation(Element elem) {
        String name = getDocumentName(elem);
        if (name == null) {
            return null;
        }
        name = name.trim();
        operationInfos.add(new BpelInfo(name, getInputVariable(elem), getOutputVariable(elem)));
        return new OperationNode(
                name,
                getInputVariable(elem),
                getOutputVariable(elem));
    }

    private String getInputVariable(Element elem) {
        Attribute attr = elem.attribute("inputVariable");
        if (attr != null) {
            return attr.getValue();
        }
        return null;
    }

    private String getOutputVariable(Element elem) {
        Attribute attr = elem.attribute("outputVariable");
        if (attr != null) {
            return attr.getValue();
        }
        attr = elem.attribute("variable");
        if (attr != null) {
            return attr.getValue();
        }
        return null;
    }

    private String getDocumentName(Element elem) {
        Element child = elem.element("documentation");
        if (child != null) {
            return child.getStringValue();
        }
        return null;
    }

    private final Document document;
    private final HashSet<BpelInfo> operationInfos;
}