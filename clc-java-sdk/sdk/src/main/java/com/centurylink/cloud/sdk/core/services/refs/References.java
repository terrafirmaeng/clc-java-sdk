package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;

import java.util.function.Supplier;

/**
 * @author Ilya Drabenia
 */
public class References {

    public static <T> T exceptionIfNotFound(T value) {
        if (value != null) {
            return value;
        } else {
            // TODO: it would be great to resolve type of resources from annotations
            throw new ResourceNotFoundException("Resource not resolved");
        }
    }

}
