package com.drupaldoesnotexists.bundlelib.adapter.netty.constable;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketConstable {

    private final ConcurrentLinkedQueue<PacketWindow> scheduledWindows = new ConcurrentLinkedQueue<>();

    public PacketWindow getScheduledWindow() {
        return scheduledWindows.peek();
    }

    public void schedule(PacketWindow window) {
        PacketWindow end = getScheduledWindow();
        if (window.kind() == PacketWindowKind.SINGLE && end.kind() == window.kind()) {
            // Stack singular windows as 1 object
            window.size().addAndGet(window.size().get());
        } else {
            // Otherwise, add a new object to the end
            scheduledWindows.add(window);
        }
    }

    public void closeWindow() {
        scheduledWindows.poll();
    }

}
