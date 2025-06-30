package io.github.seal139.jSwarm.backend.jvm;

import io.github.seal139.jSwarm.backend.BackendException;

public class JvmException extends BackendException {

    private static final long serialVersionUID = 7348770568796625467L;

    JvmException(Throwable e) {
        super(e.getMessage());

        this.setStackTrace(e.getStackTrace());
        this.initCause(e.getCause());
    }

    JvmException(String e) {
        super(e);
    }

}
