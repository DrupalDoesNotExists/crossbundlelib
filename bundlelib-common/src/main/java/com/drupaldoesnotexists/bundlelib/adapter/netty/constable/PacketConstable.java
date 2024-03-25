package com.drupaldoesnotexists.bundlelib.adapter.netty.constable;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketConstable {

    private final ConcurrentLinkedQueue<PacketWindow> scheduledWindows = new ConcurrentLinkedQueue<>();
    private PacketWindow lastWindow;

    public PacketWindow getScheduledWindow() {
        return scheduledWindows.peek();
    }

    public void schedule(PacketWindow window) {
        if (window.kind() == PacketWindowKind.SINGLE && lastWindow.kind() == window.kind()) {
            // Stack singular windows as 1 object
            lastWindow.size().addAndGet(window.size().get());
        } else {
            // Otherwise, add a new object to the end
            scheduledWindows.add(window);
            lastWindow = window;
        }
    }

    public void closeWindow() {
        scheduledWindows.poll();
    }

}
