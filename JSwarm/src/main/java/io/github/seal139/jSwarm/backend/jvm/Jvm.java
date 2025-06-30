package io.github.seal139.jSwarm.backend.jvm;

import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.backend.Platform;

public class Jvm implements Platform {

    private static Jvm obj;

    public static Jvm getInstance() {
        if (obj == null) {
            obj = new Jvm();
        }

        return obj;
    }

    // ===================================

    @Override
    public String getName() { return "JVM"; }

    @Override
    public String getFullName() { return "Java Virtual Machine"; }

    @Override
    public boolean isPrimary() { return false; }

    @Override
    public String getVersion() { return "1.0"; }

    private final JvmDevice device = new JvmDevice();

    @Override
    public Executor[] getDevices() {
        return new Executor[] {
                this.device };
    }

}
