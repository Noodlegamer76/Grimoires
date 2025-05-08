package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.network.spell.SpellPayload;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.graph.Edge;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.NodeLoader;
import imgui.ImVec2;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SpellCodec implements StreamCodec<FriendlyByteBuf, SpellPayload> {

    @Override
    public void encode(FriendlyByteBuf buffer, SpellPayload payload) {
        buffer.writeInt(payload.hand());

        encodeSpell(buffer, payload.spell());
    }

    public static void encodeSpell(FriendlyByteBuf buffer, Spell spell) {
        buffer.writeUtf(spell.getName());
        buffer.writeVarInt(spell.getGraph().nextId());
        buffer.writeVarInt(spell.getGraph().getNodes().size());
        buffer.writeVarInt(spell.getGraph().getAllEdges().size());
        buffer.writeVarInt(spell.getNodePositions().size());

        spell.getGraph().getNodes().forEach(node -> encodeNode(buffer, node));
        spell.getGraph().getAllEdges().forEach(edge -> encodeEdge(buffer, edge));

        spell.getNodePositions().forEach((id, position) -> {
            buffer.writeVarInt(id);
            buffer.writeFloat(position.x);
            buffer.writeFloat(position.y);
        });
    }

    @Override
    public SpellPayload decode(FriendlyByteBuf buffer) {
        int hand = buffer.readInt();

        Spell spell = decodeSpell(buffer);

        return new SpellPayload(hand, spell);
    }

    public static Spell decodeSpell(FriendlyByteBuf buffer) {
        Spell spell = new Spell(buffer.readUtf());
        int nextId = buffer.readVarInt();
        int nodeCount = buffer.readVarInt();
        int edgeCount = buffer.readVarInt();
        int positionCount = buffer.readVarInt();

        spell.getGraph().setIdIncrement(nextId);

        for (int i = 0; i < nodeCount; i++) {
            Node node = decodeNode(buffer, spell);
            if (node != null) {
                spell.getGraph().addNode(node, false);
            }
        }

        for (int i = 0; i < edgeCount; i++) {
            Edge edge = decodeEdge(buffer, spell);
            if (edge != null) {
                spell.getGraph().addEdge(edge.from(), edge.to(), edge.id());
            }
        }

        for (int i = 0; i < positionCount; i++) {
            int id = buffer.readVarInt();
            float x = buffer.readFloat();
            float y = buffer.readFloat();

            spell.addPosition(id, new ImVec2(x, y));
        }

        return spell;
    }

    public static void encodeNode(FriendlyByteBuf buffer, Node node) {
        buffer.writeUtf(node.getName());
        buffer.writeVarInt(node.getId());
        buffer.writeVarInt(node.getPins().size());
        buffer.writeUtf(node.getClass().getSimpleName());

        node.getPins().forEach(pin -> encodeNodePin(buffer, pin));
    }

    public static Node decodeNode(FriendlyByteBuf buffer, Spell spell) {
        String name = buffer.readUtf();
        int id = buffer.readVarInt();
        int pinCount = buffer.readVarInt();
        String className = buffer.readUtf();

        Node node = NodeLoader.load(className);
        if (node != null) {
            node.setId(id);
            node.setName(name);
        }

        // Always read the pin data, even if node is null
        for (int i = 0; i < pinCount; i++) {
            NodePin<?> pin = decodeNodePin(buffer, spell);
            if (node != null && pin != null) {
                node.addPin(pin);
            }
        }

        return node;
    }

    public static void encodeNodePin(FriendlyByteBuf buffer, NodePin<?> pin) {
        buffer.writeUtf(pin.getName());
        buffer.writeVarInt(pin.getId());
        buffer.writeVarInt(pin.getShape());
        buffer.writeByte(pin.getIOType().ordinal());
        buffer.writeUtf(pin.getType().getName());
    }

    public static NodePin<?> decodeNodePin(FriendlyByteBuf buffer, Spell spell) {
        String name = buffer.readUtf();
        int id = buffer.readVarInt();
        int shape = buffer.readVarInt();
        byte ioTypeByte = buffer.readByte();
        NodePin.IOType ioType = NodePin.IOType.values()[ioTypeByte];
        Class<?> type;
        String className = buffer.readUtf();
        try {
            type = Class.forName(className);
            return new NodePin<>(ioType, id, type, shape, name);
        } catch (ClassNotFoundException e) {
            GrimoiresMod.LOGGER.error("Could not find class for pin: " + name +
                    ", This error is most likely caused by an update or modified spell." +
                    "\nThis error is thrown when the spell is sent to the server." +
                    "\nSpell Name: " + spell.getName() + " Pin Name: " + name);
        }
        return null;
    }

    public static void encodeEdge(FriendlyByteBuf buffer, Edge edge) {
        buffer.writeVarInt(edge.id());
        buffer.writeVarInt(edge.from().getId());
        buffer.writeVarInt(edge.to().getId());
    }

    public static Edge decodeEdge(FriendlyByteBuf buffer, Spell spell) {
        int id = buffer.readVarInt();
        int fromId = buffer.readVarInt();
        int toId = buffer.readVarInt();

        NodePin<?> from = spell.getGraph().getNodePinById(fromId);
        NodePin<?> to = spell.getGraph().getNodePinById(toId);

        if (from != null && to != null) {
            return new Edge(from, to, id);
        }

        return null;
    }
}
