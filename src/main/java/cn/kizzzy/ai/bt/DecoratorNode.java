package cn.kizzzy.ai.bt;

public class DecoratorNode extends Node {
    
    public DecoratorNode(int id, String name, Node child) {
        super(id, name != null ? name : "Decorator", child);
    }
}
