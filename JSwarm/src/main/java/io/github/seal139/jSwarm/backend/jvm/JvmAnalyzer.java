package io.github.seal139.jSwarm.backend.jvm;

import java.util.HashSet;
import java.util.Set;

import io.github.seal139.jSwarm.backend.Transpiler;
import io.github.seal139.jSwarm.runtime.TranspileException;
import io.github.seal139.jSwarm.transpiler.JParser.MethodDeclarationContext;
import io.github.seal139.jSwarm.transpiler.JParser.MethodInvocationContext;

public final class JvmAnalyzer extends Transpiler {

    private TranspileException e = null;

    @Override
    public String getTranspiledSource() { return null; }

    @Override
    public TranspileException getTranspileException() { return this.e; }

    public Set<String> getSyncedMethod() { return this.syncedMethod; }

    private final Set<String> syncedMethod      = new HashSet<>();
    private String            currentMethodName = null;

    @Override
    public void enterMethodDeclaration(MethodDeclarationContext ctx) {
        this.currentMethodName = ctx.methodHeader().methodDeclarator().identifier().getText();
    }

    @Override
    public void enterMethodInvocation(MethodInvocationContext ctx) {
        boolean synced = "synchronize".equals(ctx.identifier().getText());

        if (synced) {
            this.syncedMethod.add(this.currentMethodName);
        }
    }
}
