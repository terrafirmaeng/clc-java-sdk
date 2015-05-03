package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

/**
 * {@inheritDoc}
 */
public abstract class Server implements Reference<ServerFilter> {

    public static ServerByIdRef refById(String id) {
        return new ServerByIdRef(id);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
