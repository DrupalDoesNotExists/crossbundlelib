package com.drupaldoesnotexists.bundlelib.adapter.netty;

/**
 * Exception that is thrown when channel injector determines that
 * channel is already injected.
 */
public class ChannelAlreadyInjectedException extends IllegalStateException {

    public ChannelAlreadyInjectedException(String msg) {
        super(msg);
    }

}
