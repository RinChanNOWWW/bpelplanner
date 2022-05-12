package bpel.parser;

import bpel.BpelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequence 节点类型，其子节点之间为顺序执行
 */
public class SequenceNode extends BpelParseNode {

    @Override
    public ParseNodeType getType() {
        return ParseNodeType.SEQUENCE;
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

    public SequenceNode() {
        this.children = new ArrayList<>();
    }

    private final ArrayList<BpelParseNode> children;
}
