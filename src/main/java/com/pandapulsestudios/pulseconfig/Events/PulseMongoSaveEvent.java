package com.pandapulsestudios.pulseconfig.Events;

import com.pandapulsestudios.pulseconfig.Interface.PulseMongo;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PulseMongoSaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final PulseMongo pulseMongo;

    public PulseMongoSaveEvent(PulseMongo pulseMongo){
        this.pulseMongo = pulseMongo;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public PulseMongo getMongo(){return pulseMongo;}
}