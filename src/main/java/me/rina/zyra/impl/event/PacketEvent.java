package me.rina.zyra.impl.event;

import event.bus.Event;
import net.minecraft.network.Packet;

/**
 * @author SrRina
 * @since 07/09/2021 at 16:04
 **/
public class PacketEvent extends Event {
    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
