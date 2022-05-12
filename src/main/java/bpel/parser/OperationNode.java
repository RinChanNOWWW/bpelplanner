package bpel.parser;

import bpel.BpelInfo;

import java.util.List;

/**
 * Operation 子节点类型，在 BPEL 分析树中为叶子节点，代表具体的操作
 */
public class OperationNode extends BpelParseNode {

    @Override
    public ParseNodeType getType() {
        return ParseNodeType.OPERATION;
    }

    @Override
    public List<BpelParseNode> getChildren() {
        return null;
    }

    @Override
    public void addChild(BpelParseNode child) {
    }

    @Override
    public BpelInfo getInfo() {
        return info;
    }

    public OperationNode(String name, String input, String output) {
        info = new BpelInfo(name, input, output);
    }

    private final BpelInfo info;
}
