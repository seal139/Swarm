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
