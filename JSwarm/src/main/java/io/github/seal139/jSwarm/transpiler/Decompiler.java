/*
* Swarm - An Extensible and Modular GPGPU framework
* Copyright (C) 2025  Septian Pramana / Mercu Buana University
*
* This file is part of Swarm.
*
* Swarm is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2.
*
* Swarm is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; If not, see <http://www.gnu.org/licenses/>.
*/

package io.github.seal139.jSwarm.transpiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.RuleNode;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Transpiler;
import io.github.seal139.jSwarm.runtime.Program;
import io.github.seal139.jSwarm.runtime.TranspileException;

/**
 * Decompile Java byte code into generic source
 */
public final class Decompiler {

    // TODO: Integrate CRF and AstBuilder into single implementation to reduce
    // overhead

    // Rules:
    // - Expression is prioritize the deepest nested instruction
    // - Statement is based on top-down

    // Due to nature of GPGPU programming and how GPU works,
    // - Lambda is not supported
    // - Loop is based on while (Evaluate first, then execute) and do-while (execute
    // first, then evaluate)
    // - Numeric data type only
    // - Inheritance to other than BaseKernel is not supported
    // - Method is bit unique. Actually it was a statement. But can be treated as an
    // expression

    /**
     * Deecompile Java bytecode using CRF into java-based source code
     */
    private static final class JDecompiler implements OutputSinkFactory {

        private static final JDecompiler instance = new JDecompiler();

        public static JDecompiler getInstance() { return instance; }

        private StringBuilder ret;

        private final Sink<?> JavaSink = t -> {
            this.ret = new StringBuilder();
            this.ret.append(t.toString());
        };

        @Override
        @SuppressWarnings("unchecked")
        public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
            // Handle only JAVA output
            return sinkType == SinkType.JAVA ? (Sink<T>) this.JavaSink : null;
        }

        @Override
        public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> available) {
            if ((sinkType == SinkType.JAVA) && available.contains(SinkClass.STRING)) {
                return Collections.singletonList(SinkClass.STRING);
            }

            return Collections.emptyList();
        }

        public StringBuilder getResult() { return this.ret; }
    }

    private static final CfrDriver decompilerDriver = new CfrDriver.Builder().withOutputSink(JDecompiler.getInstance()).build();

    public static String process(Transpiler transpiler, Class<? extends Program> cls) throws BackendException, IOException {
        final StringBuilder sb = new StringBuilder();

        decompile(sb, cls);

        return buildAst(sb.toString(), transpiler);
    }

    @SuppressWarnings("unchecked")
    public static String process(Transpiler transpiler, Class<? extends Program>... clss) throws BackendException, IOException {
        final StringBuilder sb = new StringBuilder("");

        for (Class<? extends Program> cls : clss) {
            decompile(sb, cls);
        }

        return buildAst(sb.toString(), transpiler);
    }

    public static String process(Transpiler transpiler, Collection<Class<? extends Program>> clss) throws BackendException, IOException {
        final StringBuilder sb = new StringBuilder("");

        for (Class<? extends Program> cls : clss) {
            decompile(sb, cls);
        }

        return buildAst(sb.toString(), transpiler);
    }

    protected static void decompile(StringBuilder sb, Class<? extends Program> cls) throws BackendException, IOException {
        File f = null;
        try {
            f = File.createTempFile("swarm-", ".kernel");
            f.deleteOnExit();

            try ( //
                    InputStream input = cls.getResourceAsStream("/" + cls.getName().replace('.', '/') + ".class");
                    OutputStream output = new FileOutputStream(f); //
            ) {
                output.write(input.readAllBytes());
            }

            // Run decompilation
            decompilerDriver.analyse(Collections.singletonList(Path.of(f.getAbsolutePath()).toString()));

            // Retrieve output
            sb.append(JDecompiler.getInstance().getResult());

        }
        finally {
            if (f != null) {
                f.delete();
            }
        }
    }

    protected static String buildAst(String source, Transpiler transpiler) throws TranspileException {
        JLexer java20Lexer = new JLexer(CharStreams.fromString(source));

        CommonTokenStream tokens = new CommonTokenStream(java20Lexer);
        JParser           parser = new JParser(tokens);
        RuleNode          tree   = parser.compilationUnit();

        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(transpiler, tree);

        if (transpiler.getTranspileException() != null) {
            throw transpiler.getTranspileException();
        }

        return transpiler.getTranspiledSource();
    }
}
