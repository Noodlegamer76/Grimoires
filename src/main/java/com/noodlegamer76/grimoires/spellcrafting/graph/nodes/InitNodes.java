package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import imgui.extension.imnodes.flag.ImNodesPinShape;

import java.util.List;

public class InitNodes {

    public static final NodeHolder<TestNode> TEST_NODE =
            NodeRegistryUtils.register(new NodeHolder<>(TestNode.class, TestNode::new, 0, false, new PinRegistry()
                    .addPin(0, List.of(
                            () -> new NodePin<>(NodePin.IOType.INPUT, Integer.class, ImNodesPinShape.Triangle, "Int Input"),
                            () -> new NodePin<>(NodePin.IOType.INPUT, Boolean.class, ImNodesPinShape.Quad, "Boolean Input"),
                            () -> new NodePin<>(NodePin.IOType.OUTPUT, Integer.class, ImNodesPinShape.TriangleFilled, "Integer Output 1"),
                            () -> new NodePin<>(NodePin.IOType.OUTPUT, Integer.class, ImNodesPinShape.TriangleFilled, "Integer Output 2"),
                            () -> new NodePin<>(NodePin.IOType.OUTPUT, Boolean.class, ImNodesPinShape.QuadFilled, "Boolean Output")
                    ))
            ));

    public static final NodeHolder<InputNode> INPUT_NODE =
            NodeRegistryUtils.register(new NodeHolder<>(InputNode.class, InputNode::new, 1, true, new PinRegistry()
                    .addPin(0, List.of(
                            () -> new NodePin<>(NodePin.IOType.OUTPUT, Integer.class, ImNodesPinShape.TriangleFilled, "Input")
                    ))
            ));

    //just so I can load the static fields
    public static void init() {
    }
}
