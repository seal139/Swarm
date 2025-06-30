package io.github.seal139.jSwarm.backend.jvm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.runtime.Program;

public class JvmModule implements Module {

    private final Map<String, JvmKernel> kernelMap = new HashMap<>();

    JvmModule(Class<? extends Program> program, Set<String> syncedMethod) throws JvmException {
        final Method[]         methods = program.getDeclaredMethods();
        final Constructor<?>[] constrs = program.getDeclaredConstructors();

        Constructor<?> constructor = null;

        for (Constructor<?> constr : constrs) {
            if (constr.getParameterCount() == 0) {
                constructor = constr;
                break;
            }
        }

        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            if (method.isSynthetic()) {
                continue;
            }

            if (method.isBridge()) {
                continue;
            }

            this.kernelMap.put(method.getName(), new JvmKernel(constructor, method, syncedMethod.contains(method.getName())));
        }
    }

    @Override
    public Deallocator getDeallocator() { return null; }

    @Override
    public boolean isClosed() { return false; }

    @Override
    public void close() throws Exception {
        // NoOp;
    }

    @Override
    public Kernel getKernel(String kernelName) throws BackendException, DeallocatedException {
        return this.kernelMap.get(kernelName);
    }

}
