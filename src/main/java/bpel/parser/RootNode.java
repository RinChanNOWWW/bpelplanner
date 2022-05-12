package bpel.parser;

import bpel.BpelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 根节点类型
 */
public class RootNode extends BpelParseNode {

    @Override
    public ParseNodeType getType() {
        return ParseNodeType.ROOT;
    }

    @Override
    public List<BpelParseNode> getChildren() {
        ArrayList<BpelParseNode> ret = new ArrayList<>();
        ret.add(next);
        return ret;
    }

    @Override
    public void addChild(BpelParseNode child) {
        next = child;
    }

    @Override
    public BpelInfo getInfo() {
        return null;
    }

    private BpelParseNode next;
}
