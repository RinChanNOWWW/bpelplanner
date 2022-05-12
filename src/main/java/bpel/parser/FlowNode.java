package bpel.parser;

import bpel.BpelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Flow 节点类型，其子节点之间为并行运行
 */
public class FlowNode extends BpelParseNode {

    @Override
    public ParseNodeType getType() {
        return ParseNodeType.FLOW;
    }

    @Override
    public List<BpelParseNode> getChildren() {
        return children;
    }

    @Override
    public void addChild(BpelParseNode child) {
        children.add(child);
    }

    @Override
    public BpelInfo getInfo() {
        return null;
    }

    public FlowNode() {
        this.children = new ArrayList<>();
    }

    private final ArrayList<BpelParseNode> children;
}
