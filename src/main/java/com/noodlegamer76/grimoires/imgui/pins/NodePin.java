package com.noodlegamer76.grimoires.imgui.pins;

import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;

public class NodePin<T> {
    private final IOType iOType;
    private int id;
    private int shape;
    private String name = "Pin";
    private T data;
    private Class<T> type;
    private Node parentNode;

    public NodePin(IOType iOType, int id, Class<T> type, int shape, String name) {
        this.iOType = iOType;
        this.id = id;
        this.shape = shape;
        this.name = name;
        this.type = type;
    }

    public NodePin(IOType iOType, int id, Class<T> type, int shape) {
        this.iOType = iOType;
        this.id = id;
        this.shape = shape;
        this.type = type;
    }

    public NodePin(IOType iOType, Class<T> type, int shape, String name) {
        this.iOType = iOType;
        this.shape = shape;
        this.name = name;
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public IOType getIOType() {
        return iOType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getDataType() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public JsonObject save() {
        JsonObject pin = new JsonObject();
        pin.addProperty("s", getShape());
        pin.addProperty("t", type.getName());
        pin.addProperty("i", getIOType().toString());
        pin.addProperty("id", getId());
        pin.addProperty("n", getName());
        return pin;
    }

    public static void load(JsonObject jsonObject, Node node) {
        int pinId = jsonObject.get("id").getAsInt();
        int shape = jsonObject.get("s").getAsInt();
        String typeName = jsonObject.get("t").getAsString();
        String io = jsonObject.get("i").getAsString();
        String name = jsonObject.get("n").getAsString();

        try {
            Class<?> clazz = Class.forName(typeName);
            IOType ioType = IOType.valueOf(io);

            NodePin<?> pin = new NodePin<>(ioType, pinId, clazz, shape);
            pin.setName(name);
            node.addPin(pin);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public enum IOType {
        INPUT,
        OUTPUT
    }
}
