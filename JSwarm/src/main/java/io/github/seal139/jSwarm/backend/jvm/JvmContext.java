package io.github.seal139.jSwarm.backend.jvm;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Context;
import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.datatype.Vector;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.runtime.NdRange;
import io.github.seal139.jSwarm.runtime.Program;
import io.github.seal139.jSwarm.runtime.SyncDirection;
import io.github.seal139.jSwarm.transpiler.Decompiler;

public class JvmContext implements Context {

    private boolean activated = false;

    private final Set<Runnable> processingKernel = new HashSet<>();
    private final JvmDevice     device;

    JvmContext(JvmDevice device) {
        this.device = device;
    }

    @Override
    public Deallocator getDeallocator() { return null; }

    @Override
    public boolean isClosed() { return false; }

    @Override
    public void close() throws Exception {
        // NoOp
    }

    @Override
    public void activate() throws BackendException {
        this.activated = true;
    }

    @Override
    public Executor getDevice() { return this.device; }

    @Override
    public Module loadProgram(Class<? extends Program> program) throws BackendException, DeallocatedException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        JvmAnalyzer ja = new JvmAnalyzer();

        try {
            Decompiler.process(ja, program);
        }
        catch (IOException e) {
            throw new JvmException(e);
        }

        return new JvmModule(program, ja.getSyncedMethod());
    }

    @Override
    public int getParallelismLevel() { return Runtime.getRuntime().availableProcessors(); }

    @Override
    public void addParallelismLevel(int additionalNumber) throws BackendException, DeallocatedException {
        // NoOp
    }

    private void validateLaunch(NdRange ndRange, Number... arguments) throws BackendException {
        long[] maxLocalThread = this.device.getMaxLocalSize();

        if ((ndRange.getXLocal() > maxLocalThread[0]) //
            || (ndRange.getYLocal() > maxLocalThread[1])//
            || (ndRange.getZLocal() > maxLocalThread[2]) //

            || ((ndRange.getXLocal() * ndRange.getYLocal() * ndRange.getZLocal()) > this.device.getMaxLocalThread()) //
        ) {
            throw new JvmException("Local thread exceed maximum range");
        }

        for (Number num : arguments) {
            if (num instanceof Vector vector) {
                if (!this.hookedVector.contains(vector)) {
                    throw new JvmException("Vector is not hooked");
                }
            }
        }
    }

    @Override
    public void launch(Kernel kernel, NdRange ndRange, Number... arguments) throws BackendException, DeallocatedException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        validateLaunch(ndRange);

        JvmKernel jKernel = (JvmKernel) kernel;

        try {
            jKernel.run(ndRange, arguments).run();
        }
        catch (RuntimeException e) {
            if (e.getCause() instanceof JvmException ex) {
                throw ex;
            }

            throw new JvmException(e.getCause());
        }
    }

    @Override
    public void launchAsync(Kernel kernel, NdRange ndRange, Number... arguments) throws BackendException, DeallocatedException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        validateLaunch(ndRange);

        JvmKernel jKernel = (JvmKernel) kernel;

        this.processingKernel.add(jKernel.run(ndRange, arguments));
    }

    final Set<Vector<? extends Number>> hookedVector = new HashSet<>();

    @Override
    public void hook(Vector<? extends Number> vector) throws BackendException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        this.hookedVector.add(vector);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sync(SyncDirection direction, Vector<? extends Number>... dataCollection) throws BackendException, DeallocatedException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        for (Vector<? extends Number> vector : dataCollection) {
            if (!this.hookedVector.contains(vector)) {
                throw new JvmException("Vector is not hooked");
            }
        }
    }

    @Override
    public void unhook(Vector<? extends Number> vector) throws BackendException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        if (!this.hookedVector.contains(vector)) {
            throw new JvmException("Vector is not hooked");
        }

        this.hookedVector.remove(vector);
    }

    @Override
    public void reHook(Vector<? extends Number> vector) throws BackendException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        if (!this.hookedVector.contains(vector)) {
            throw new JvmException("Vector is not hooked");
        }
    }

    @Override
    public void waitOperation() throws BackendException, DeallocatedException {
        if (!this.activated) {
            throw new JvmException("Context is not active");
        }

        JvmException[] eex = {
                null };

        this.processingKernel.forEach(v -> {
            try {
                v.run();
            }
            catch (RuntimeException e) {
                if (e.getCause() instanceof JvmException ex) {
                    eex[0] = ex;
                }

                eex[0] = new JvmException(e.getCause());
            }
        });

        this.processingKernel.clear();

        if (eex[0] != null) {
            throw eex[0];
        }
    }

}
