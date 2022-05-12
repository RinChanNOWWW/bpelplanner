package bpel;

/**
 * BPEL 节点所携带的额外信息，包括名称，输入变量与输出变量，用于构造有向无环图 (DAG)
 */
public class BpelInfo {
    public BpelInfo(String name, String inputVariable, String outputVariable) {
        this.name = name;
        this.inputVariable = inputVariable;
        this.outputVariable = outputVariable;
    }

    public String getName() {
        return name;
    }

    public String getInputVariable() {
        return inputVariable;
    }

    public String getOutputVariable() {
        return outputVariable;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            return name.equals(((BpelInfo) obj).getName());
        }
        return false;
    }

    private final String name;
    private final String inputVariable;
    private final String outputVariable;
}
