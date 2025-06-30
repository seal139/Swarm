package io.github.seal139.jSwarm.backend;

import io.github.seal139.jSwarm.runtime.TranspileException;
import io.github.seal139.jSwarm.transpiler.JParserBaseListener;

public abstract class Transpiler extends JParserBaseListener {
    public abstract String getTranspiledSource();

    public abstract TranspileException getTranspileException();
}
