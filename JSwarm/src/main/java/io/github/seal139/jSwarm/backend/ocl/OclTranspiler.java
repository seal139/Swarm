package io.github.seal139.jSwarm.backend.ocl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.seal139.jSwarm.backend.Transpiler;
import io.github.seal139.jSwarm.runtime.TranspileException;
import io.github.seal139.jSwarm.transpiler.JParser.AdditiveExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.AndExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ArrayAccessContext;
import io.github.seal139.jSwarm.transpiler.JParser.ArrayCreationExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.AssignmentContext;
import io.github.seal139.jSwarm.transpiler.JParser.AssignmentExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.BlockStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.BlockStatementsContext;
import io.github.seal139.jSwarm.transpiler.JParser.CastExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ConditionalAndExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ConditionalExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ConditionalOrExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.DimExprContext;
import io.github.seal139.jSwarm.transpiler.JParser.DimExprsContext;
import io.github.seal139.jSwarm.transpiler.JParser.DoStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.EqualityExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ExclusiveOrExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.FormalParameterContext;
import io.github.seal139.jSwarm.transpiler.JParser.IfThenElseStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.IfThenElseStatementNoShortIfContext;
import io.github.seal139.jSwarm.transpiler.JParser.IfThenStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.InclusiveOrExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.LocalVariableDeclarationStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.MethodDeclarationContext;
import io.github.seal139.jSwarm.transpiler.JParser.MethodDeclaratorContext;
import io.github.seal139.jSwarm.transpiler.JParser.MethodInvocationContext;
import io.github.seal139.jSwarm.transpiler.JParser.MultiplicativeExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PNNAContext;
import io.github.seal139.jSwarm.transpiler.JParser.PfEContext;
import io.github.seal139.jSwarm.transpiler.JParser.PostDecrementExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PostIncrementExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PostfixExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PreDecrementExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PreIncrementExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.PrimaryNoNewArrayContext;
import io.github.seal139.jSwarm.transpiler.JParser.RelationalExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.ShiftExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.StatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.StatementExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.StatementNoShortIfContext;
import io.github.seal139.jSwarm.transpiler.JParser.StatementWithoutTrailingSubstatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.UnaryExpressionContext;
import io.github.seal139.jSwarm.transpiler.JParser.UnaryExpressionNotPlusMinusContext;
import io.github.seal139.jSwarm.transpiler.JParser.VariableDeclaratorContext;
import io.github.seal139.jSwarm.transpiler.JParser.WhileStatementContext;
import io.github.seal139.jSwarm.transpiler.JParser.WhileStatementNoShortIfContext;

public final class OclTranspiler extends Transpiler {

    private TranspileException e = null;

    @Override
    public String getTranspiledSource() { return this.sb.toString(); }

    @Override
    public TranspileException getTranspileException() { return this.e; }

    private final StringBuilder sb = new StringBuilder();

    private static final Map<String, String> replacement = new HashMap<>();

    private static final Set<String> specialMapper = new HashSet<>();

    static {
        specialMapper.add("globalRangeX");
        specialMapper.add("globalRangeY");
        specialMapper.add("globalRangeZ");

        specialMapper.add("localRangeX");
        specialMapper.add("localRangeY");
        specialMapper.add("localRangeZ");

        specialMapper.add("totalRangeX");
        specialMapper.add("totalRangeY");
        specialMapper.add("totalRangeZ");

        specialMapper.add("currentGlobalRangeX");
        specialMapper.add("currentGlobalRangeY");
        specialMapper.add("currentGlobalRangeZ");

        specialMapper.add("currentLocalRangeX");
        specialMapper.add("currentLocalRangeY");
        specialMapper.add("currentLocalRangeZ");

        specialMapper.add("currentRangeX");
        specialMapper.add("currentRangeY");
        specialMapper.add("currentRangeZ");

        specialMapper.add("synchronize");

        // Garbage
        replacement.put("valueOf", "");
        replacement.put("floatValue", "");
        replacement.put("doubleValue", "");
        replacement.put("shortValue", "");
        replacement.put("intValue", "");
        replacement.put("longValue", "");

        // TODO Thread indexer
        replacement.put("globalRangeX", "get_num_groups(0)");
        replacement.put("globalRangeY", "get_num_groups(1)");
        replacement.put("globalRangeZ", "get_num_groups(2)");

        replacement.put("localRangeX", "get_local_size(0)");
        replacement.put("localRangeY", "get_local_size(1)");
        replacement.put("localRangeZ", "get_local_size(2)");

        replacement.put("totalRangeX", "get_global_size(0)");
        replacement.put("totalRangeY", "get_global_size(1)");
        replacement.put("totalRangeZ", "get_global_size(2)");

        replacement.put("currentGlobalRangeX", "get_group_id(0)");
        replacement.put("currentGlobalRangeY", "get_group_id(1)");
        replacement.put("currentGlobalRangeZ", "get_group_id(2)");

        replacement.put("currentLocalRangeX", "get_local_id(0)");
        replacement.put("currentLocalRangeY", "get_local_id(1)");
        replacement.put("currentLocalRangeZ", "get_local_id(2)");

        replacement.put("currentRangeX", "get_global_id(0)");
        replacement.put("currentRangeY", "get_global_id(1)");
        replacement.put("currentRangeZ", "get_global_id(2)");

        replacement.put("true", "1");
        replacement.put("false", "0");

        // TODO Synchronizer
        replacement.put("synchronize", "barrier(CLK_LOCAL_MEM_FENCE)");

        // TODO Integral
        replacement.put("max", "max");
        replacement.put("min", "min");
        replacement.put("abs", "abs");

        // TODO FP
        replacement.put("isinf", "isinf");
        replacement.put("isfinite", "isfinite");
        replacement.put("isnan", "isnan");

        // TODO FP32
        replacement.put("acosfp32", "acos");
        replacement.put("acoshfp32", "acosh");
        replacement.put("asinfp32", "asin");
        replacement.put("asinhfp32", "asinh");
        replacement.put("atanfp32", "atan");
        replacement.put("atan2fp32", "atan2");
        replacement.put("atanhfp32", "atanh");
        replacement.put("cbrtfp32", "cbrt");
        replacement.put("ceilfp32", "ceil");
        replacement.put("copysignfp32", "copysign");
        replacement.put("cosfp32", "cos");
        replacement.put("coshfp32", "cosh");
        replacement.put("cospifp32", "cospi");
        replacement.put("erfcfp32", "erfc");
        replacement.put("erffp32", "erf");
        replacement.put("expfp32", "exp");
        replacement.put("exp2fp32", "exp2");
        replacement.put("exp10fp32", "exp10");
        replacement.put("expm1fp32", "expm1");
        replacement.put("fabsfp32", "fabs");
        replacement.put("fdimfp32", "fdim");
        replacement.put("floorfp32", "floor");
        replacement.put("fmafp32", "fma");
        replacement.put("fmaxfp32", "fmax");
        replacement.put("fminfp32", "fmin");
        replacement.put("fmodfp32", "fmod");
        replacement.put("frexpfp32", "frexp");
        replacement.put("hypotfp32", "hypot");
        replacement.put("ilogbfp32", "ilogb");
        replacement.put("ldexpfp32", "ldexp");
        replacement.put("lgammafp32", "lgamma");
        replacement.put("logfp32", "log");
        replacement.put("log2fp32", "log2");
        replacement.put("log10fp32", "log10");
        replacement.put("log1pfp32", "log1p");
        replacement.put("logbfp32", "logb");
        replacement.put("nextafterfp32", "nextafter");
        replacement.put("powfp32", "pow");
        replacement.put("remainderfp32", "remainder");
        replacement.put("remquofp32", "remquo");
        replacement.put("rintfp32", "rint");
        replacement.put("roundfp32", "round");
        replacement.put("rsqrtfp32", "rsqrt");
        replacement.put("sinfp32", "sin");
        replacement.put("sinhfp32", "sinh");
        replacement.put("sinpifp32", "sinpi");
        replacement.put("sqrtfp32", "sqrt");
        replacement.put("tanfp32", "tan");
        replacement.put("tanhfp32", "tanh");
        replacement.put("tgammafp32", "tgamma");
        replacement.put("truncfp32", "trunc");
        replacement.put("scalbfp32", "ldexp");

        // TODO FP64
        replacement.put("acosfp64", "acos");
        replacement.put("acoshfp64", "acosh");
        replacement.put("asinfp64", "asin");
        replacement.put("asinhfp64", "asinh");
        replacement.put("atanfp64", "atan");
        replacement.put("atan2fp64", "atan2");
        replacement.put("atanhfp64", "atanh");
        replacement.put("cbrtfp64", "cbrt");
        replacement.put("ceilfp64", "ceil");
        replacement.put("copysignfp64", "copysign");
        replacement.put("cosfp64", "cos");
        replacement.put("coshfp64", "cosh");
        replacement.put("cospifp64", "cospi");
        replacement.put("erfcfp64", "erfc");
        replacement.put("erffp64", "erf");
        replacement.put("expfp64", "exp");
        replacement.put("exp2fp64", "exp2");
        replacement.put("exp10fp64", "exp10");
        replacement.put("expm1fp64", "expm1");
        replacement.put("fabsfp64", "fabs");
        replacement.put("fdimfp64", "fdim");
        replacement.put("floorfp64", "floor");
        replacement.put("fmafp64", "fma");
        replacement.put("fmaxfp64", "fmax");
        replacement.put("fminfp64", "fmin");
        replacement.put("fmodfp64", "fmod");
        replacement.put("frexpfp64", "frexp");
        replacement.put("hypotfp64", "hypot");
        replacement.put("ilogbfp64", "ilogb");
        replacement.put("ldexpfp64", "ldexp");
        replacement.put("lgammafp64", "lgamma");
        replacement.put("logfp64", "log");
        replacement.put("log2fp64", "log2");
        replacement.put("log10fp64", "log10");
        replacement.put("log1pfp64", "log1p");
        replacement.put("logbfp64", "logb");
        replacement.put("nextafterfp64", "nextafter");
        replacement.put("powfp64", "pow");
        replacement.put("remainderfp64", "remainder");
        replacement.put("remquofp64", "remquo");
        replacement.put("rintfp64", "rint");
        replacement.put("roundfp64", "round");
        replacement.put("rsqrtfp64", "rsqrt");
        replacement.put("sinfp64", "sin");
        replacement.put("sinhfp64", "sinh");
        replacement.put("sinpifp64", "sinpi");
        replacement.put("sqrtfp64", "sqrt");
        replacement.put("tanfp64", "tan");
        replacement.put("tanhfp64", "tanh");
        replacement.put("tgammafp64", "tgamma");
        replacement.put("truncfp64", "trunc");
        replacement.put("scalbfp64", "ldexp");

        replacement.put("ShortVector", "__global short*");
        replacement.put("IntVector", "__global int*");
        replacement.put("LongVector", "__global long long*");
        replacement.put("DoubleVector", "__global double*");
        replacement.put("FloatVector", "__global float*");

        replacement.put("Short", "short");
        replacement.put("Integer", "int");
        replacement.put("Long", "long long");
        replacement.put("long", "long long");
        replacement.put("Float", "float");
        replacement.put("Double", "double");
        replacement.put("Boolean", "int");
        replacement.put("boolean", "int");

    }

    /**
     * Entry Point. Dispatch by method
     *
     * @throws TranspileException
     */
    @Override
    public void enterMethodDeclaration(MethodDeclarationContext ctx) {
        try {
            // Method modifier
            if (ctx.methodModifier().stream().anyMatch(v -> "public".equals(v.getText()))) {
                this.sb.append("\n").append("__kernel");
            }
            else {
                this.sb.append("\n").append("inline");
            }

            // Header
            this.sb.append(" ");

            // Return Type
            String rawRet = ctx.methodHeader().result().getText();
            this.sb.append(replace(rawRet)).append(" ");

            final MethodDeclaratorContext header = ctx.methodHeader().methodDeclarator();

            // Name
            this.sb.append(header.identifier().getText());

            // Parameter
            this.sb.append("(");
            if (header.formalParameterList() != null) {

                boolean first = true;
                for (FormalParameterContext param : header.formalParameterList().formalParameter()) {
                    final String paramType = param.unannType().getText();
                    final String paramName = param.variableDeclaratorId().getText();

                    if (!first) {
                        this.sb.append(", ");
                    }

                    first = false;

                    this.sb.append(replace(paramType));
                    this.sb.append(" ");
                    this.sb.append(paramName);
                }
            }
            this.sb.append(")");

            // Method Body

            if ((ctx.methodBody().block() != null) && (ctx.methodBody().block().blockStatements() != null)) {
                visitBlock(ctx.methodBody().block().blockStatements());
            }
        }
        catch (TranspileException e) {
            this.e = e;
        }
    }

    /**
     * Block Statement group
     *
     * @param bscs
     * @throws TranspileException
     */
    protected void visitBlock(BlockStatementsContext blocks) throws TranspileException {
        this.sb.append("{");
        for (BlockStatementContext block : blocks.blockStatement()) {
            if (block.statement() != null) {
                this.sb.append("\n");
                visitStatement(block.statement());
            }

            else if (block.localVariableDeclarationStatement() != null) {
                visitLocalVariableDeclarationStatement(block.localVariableDeclarationStatement());
            }
        }

        this.sb.append("\n}");
    }

    protected void visitLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx) throws TranspileException {
        final String dataType = ctx.localVariableDeclaration().localVariableType().getText();

        // Array declaration must be final
        boolean isArray = dataType.contains("[]");

        for (VariableDeclaratorContext v : ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator()) {
            final String variableName = v.variableDeclaratorId().getText();

            this.sb.append("\n");
            this.sb.append(replace(dataType.replace("[]", "")));
            this.sb.append(" ").append(variableName);

            if (v.variableInitializer() != null) {
                if (!isArray) {
                    this.sb.append(" = ");
                }

                visitExpression(v.variableInitializer().expression());
            }

            this.sb.append(";");
        }
    }

    protected void visitExpression(ExpressionContext ctx) throws TranspileException {
        if (ctx.groupedExpression() != null) {
            this.sb.append("(");
            visitExpression(ctx.groupedExpression().expression()); // Unbox and do recursively
            this.sb.append(")");
        }

        else if (ctx.arrayCreationExpression() != null) {
            visitArrayCreationExpression(ctx.arrayCreationExpression());
        }

        else if (ctx.assignmentExpression() != null) {
            visitAssignmentExpression(ctx.assignmentExpression());
        }

        else if (ctx.methodInvocation() != null) {
            visitMethodInvocation(ctx.methodInvocation());
        }
    }

    protected void visitArrayCreationExpression(ArrayCreationExpressionContext ctx) throws TranspileException {
        if (ctx.arrayCreationExpressionWithInitializer() != null) {
            // Not supported. Will be implemented later due to complexity
            throw new TranspileException("Array creation with initializer is not yet supported");
        }

        this.sb.append("[");

        DimExprsContext dimensions = ctx.arrayCreationExpressionWithoutInitializer().dimExprs();

        boolean first = true;
        for (DimExprContext dim : dimensions.dimExpr()) {
            if (!first) {
                this.sb.append(", ");
            }

            first = false;
            this.sb.append(dim.expression().getText());
        }
        this.sb.append("]");
    }

    protected void visitAssignmentExpression(AssignmentExpressionContext ctx) throws TranspileException {
        if (ctx.assignment() != null) {
            visitAssignment(ctx.assignment());
        }
        else {
            visitConditionalExpression(ctx.conditionalExpression());
        }
    }

    protected void visitAssignment(AssignmentContext ctx) throws TranspileException {
        if (ctx.leftHandSide().arrayAccess() != null) {
            ArrayAccessContext aac = ctx.leftHandSide().arrayAccess();
            if (aac.arrayCreationExpressionWithInitializer() != null) {
                // Not supported. Will be implemented later due to complexity
                throw new TranspileException("Array creation with initializer is not yet supported");
            }

            if (aac.expressionName() != null) {
                this.sb.append(aac.expressionName().identifier().getText());
            }
            else {
                visitPrimaryNoNewArray(aac.primaryNoNewArray());
            }

            this.sb.append("[");
            visitExpression(aac.expression());
            this.sb.append("]");
        }
        else if (ctx.leftHandSide().expressionName() != null) {
            this.sb.append(ctx.leftHandSide().expressionName().identifier().getText());
        }
        else {
            this.sb.append(ctx.leftHandSide().fieldAccess().identifier().getText());
        }

        // Assignment operator
        this.sb.append(" ");
        this.sb.append(ctx.assignmentOperator().getText());
        this.sb.append(" ");

        // Value expression
        visitExpression(ctx.expression());
    }

    protected void visitPrimaryNoNewArray(PrimaryNoNewArrayContext ctx) throws TranspileException {
        int getset = 0;

        if (ctx.identifier() != null) {
            this.sb.append(ctx.identifier().getText());
        }

        else if (ctx.expressionName() != null) {
            this.sb.append(ctx.expressionName().identifier().getText());
        }

        else if (ctx.methodName() != null) {
            if ("get".equals(ctx.methodName().getText())) {
                getset = 1;
            }
            else if ("set".equals(ctx.methodName().getText())) {
                getset = 2;
            }
            else {
                this.sb.append(replace(ctx.methodName().getText()));
            }
        }
        else if (ctx.literal() != null) {
            if ("get".equals(ctx.literal().getText())) {
                getset = 1;
            }
            else if ("set".equals(ctx.literal().getText())) {
                getset = 2;
            }
            else {
                this.sb.append(replace(ctx.literal().getText()));
            }
        }

        if (getset > 0) {
            visitExpression(ctx.argumentList().expression().get(0));
            this.sb.append("[");
            visitExpression(ctx.argumentList().expression().get(1));
            this.sb.append("]");

            if (getset == 2) {
                this.sb.append(" = ");
                visitExpression(ctx.argumentList().expression().get(2));
            }

            return;
        }

        if (ctx.LPAREN() != null) {
            this.sb.append("(");

            if (ctx.argumentList() != null) {
                boolean first = true;
                for (ExpressionContext ectx : ctx.argumentList().expression()) {
                    if (!first) {
                        this.sb.append(", ");
                    }

                    first = false;
                    visitExpression(ectx);
                }
            }
            else {
                visitExpression(ctx.expression());
            }

            this.sb.append(")");
        }

        if (ctx.LBRACK() != null) {
            this.sb.append("[");
            visitExpression(ctx.expression());
            this.sb.append("]");
        }

        if (ctx.pNNA() != null) {
            visitPNNA(ctx.pNNA());
        }
    }

    protected void visitMethodInvocation(MethodInvocationContext ctx) throws TranspileException {
        if (ctx.methodName() != null) {
            this.sb.append(replace(ctx.methodName().getText()));
        }

        int getset = 0;

        if ((ctx.primary() != null //
        ) && (ctx.primary().primaryNoNewArray().expressionName() == null //
        ) && (ctx.primary().primaryNoNewArray().expression() != null)) {
            visitExpression(ctx.primary().primaryNoNewArray().expression());
            return;
        }

        if (ctx.identifier() != null) {
            if ("get".equals(ctx.identifier().getText())) {
                getset = 1;
            }
            else if ("set".equals(ctx.identifier().getText())) {
                getset = 2;
            }
            else {
                String rep = replace(ctx.identifier().getText());

                this.sb.append(rep);

                if (specialMapper.contains(ctx.identifier().getText())) {
                    return;
                }
            }
        }

        if (getset > 0) {
            visitExpression(ctx.argumentList().expression().get(0));
            this.sb.append("[");
            visitExpression(ctx.argumentList().expression().get(1));
            this.sb.append("]");

            if (getset == 2) {
                this.sb.append(" = ");
                visitExpression(ctx.argumentList().expression().get(2));
            }

            return;
        }

        if (ctx.LPAREN() != null) {
            this.sb.append("(");
        }

        if (ctx.argumentList() != null) {
            boolean first = true;
            for (ExpressionContext ectx : ctx.argumentList().expression()) {
                if (!first) {
                    this.sb.append(", ");
                }

                first = false;
                visitExpression(ectx);
            }
        }

        if (ctx.RPAREN() != null) {
            this.sb.append(")");
        }
    }

    protected void visitPNNA(PNNAContext ctx) throws TranspileException {
        int getset = 0;
        if (ctx.LBRACK() != null) {
            this.sb.append("[");
            visitExpression(ctx.expression());
            this.sb.append("]");
        }
        else if (ctx.identifier() != null) {
            if ("get".equals(ctx.identifier().getText())) {
                getset = 1;
            }
            else if ("set".equals(ctx.identifier().getText())) {
                getset = 2;
            }
            else {
                this.sb.append(replace(ctx.identifier().getText()));
            }
        }

        if (ctx.argumentList() != null) {
            if (getset > 0) {
                visitExpression(ctx.argumentList().expression().get(0));
                this.sb.append("[");
                visitExpression(ctx.argumentList().expression().get(1));
                this.sb.append("]");

                if (getset == 2) {
                    this.sb.append(" = ");
                    visitExpression(ctx.argumentList().expression().get(2));
                }

                return;
            }

            this.sb.append("(");

            if (ctx.argumentList() != null) {
                boolean first = true;
                for (ExpressionContext ectx : ctx.argumentList().expression()) {
                    if (!first) {
                        this.sb.append(", ");
                    }

                    first = false;
                    visitExpression(ectx);
                }
            }
            else {
                visitExpression(ctx.expression());
            }

            this.sb.append(")");
        }

        if (ctx.pNNA() != null) {
            visitPNNA(ctx.pNNA());
        }
    }

    protected void visitStatement(StatementContext ctx) throws TranspileException {

        // Just an another statement
        if (ctx.statementWithoutTrailingSubstatement() != null) {
            visitStatementWithoutTrailingSubstatement(ctx.statementWithoutTrailingSubstatement());
        }

        // Single branch if
        else if (ctx.ifThenStatement() != null) {
            visitIfThenStatement(ctx.ifThenStatement());
        }

        // Multiple branches if - else .. if*
        else if (ctx.ifThenElseStatement() != null) {
            visitIfThenElseStatement(ctx.ifThenElseStatement());
        }

        // While statement
        else if (ctx.whileStatement() != null) {
            visitWhileStatement(ctx.whileStatement());
        }
    }

    protected void visitStatementWithoutTrailingSubstatement(StatementWithoutTrailingSubstatementContext ctx) throws TranspileException {
        if (ctx.block() != null) {
            visitBlock(ctx.block().blockStatements());
        }

        else if (ctx.doStatement() != null) {
            visitDoStatement(ctx.doStatement());
        }

        else if (ctx.expressionStatement() != null) {
            visitStatementExpression(ctx.expressionStatement().statementExpression());
            this.sb.append(";");
        }

        else if (ctx.returnStatement() != null) {
            this.sb.append("\n");
            this.sb.append("return ");

            if (ctx.returnStatement().expression() != null) {
                visitExpression(ctx.returnStatement().expression());
            }
            this.sb.append(";\n");
        }

        else if (ctx.continueStatement() != null) {
            this.sb.append("\ncontinue;");
        }

        else if (ctx.breakStatement() != null) {
            this.sb.append("\nbreak;");
        }
    }

    protected void visitStatementExpression(StatementExpressionContext ctx) throws TranspileException {
        if (ctx.assignment() != null) {
            visitAssignment(ctx.assignment());
        }

        else if (ctx.methodInvocation() != null) {
            visitMethodInvocation(ctx.methodInvocation());
        }

        else if (ctx.preDecrementExpression() != null) {
            visitPreDecrementExpression(ctx.preDecrementExpression());
        }

        else if (ctx.postDecrementExpression() != null) {
            visitPostDecrementExpression(ctx.postDecrementExpression());
        }

        else if (ctx.preIncrementExpression() != null) {
            visitPreIncrementExpression(ctx.preIncrementExpression());
        }

        else if (ctx.postIncrementExpression() != null) {
            visitPostIncrementExpression(ctx.postIncrementExpression());
        }
    }

    protected void visitConditionalExpression(ConditionalExpressionContext ctx) throws TranspileException {
        visitConditionalOrExpression(ctx.conditionalOrExpression());

        if (ctx.expression() != null) {
            if (ctx.QUESTION() != null) {
                this.sb.append(" ? ");
            }

            visitExpression(ctx.expression());

            if (ctx.COLON() != null) {
                this.sb.append(" : ");
            }

            visitConditionalExpression(ctx.conditionalExpression());
        }
    }

    protected void visitConditionalOrExpression(ConditionalOrExpressionContext ctx) throws TranspileException {
        if (ctx.conditionalOrExpression() != null) {
            visitConditionalOrExpression(ctx.conditionalOrExpression());
            this.sb.append(" || ");
        }

        visitConditionalAndExpression(ctx.conditionalAndExpression());
    }

    protected void visitConditionalAndExpression(ConditionalAndExpressionContext ctx) throws TranspileException {
        if (ctx.conditionalAndExpression() != null) {
            visitConditionalAndExpression(ctx.conditionalAndExpression());
            this.sb.append(" && ");
        }

        visitInclusiveOrExpression(ctx.inclusiveOrExpression());
    }

    protected void visitInclusiveOrExpression(InclusiveOrExpressionContext ctx) throws TranspileException {
        if (ctx.inclusiveOrExpression() != null) {
            visitInclusiveOrExpression(ctx.inclusiveOrExpression());
            this.sb.append("|");
        }

        visitExclusiveOrExpression(ctx.exclusiveOrExpression());
    }

    protected void visitExclusiveOrExpression(ExclusiveOrExpressionContext ctx) throws TranspileException {
        if (ctx.exclusiveOrExpression() != null) {
            visitExclusiveOrExpression(ctx.exclusiveOrExpression());
            this.sb.append(" ^ ");
        }

        visitAndExpression(ctx.andExpression());
    }

    protected void visitAndExpression(AndExpressionContext ctx) throws TranspileException {
        if (ctx.andExpression() != null) {
            visitAndExpression(ctx.andExpression());
            this.sb.append(" & ");
        }

        visitEqualityExpression(ctx.equalityExpression());
    }

    protected void visitEqualityExpression(EqualityExpressionContext ctx) throws TranspileException {
        if (ctx.equalityExpression() != null) {
            visitEqualityExpression(ctx.equalityExpression());

            this.sb.append((ctx.EQUAL() != null) ? " == " : " != ");
        }

        visitRelationalExpression(ctx.relationalExpression());
    }

    protected void visitRelationalExpression(RelationalExpressionContext ctx) throws TranspileException {
        if (ctx.relationalExpression() != null) {
            visitRelationalExpression(ctx.relationalExpression());

            if (ctx.LT() != null) {
                this.sb.append(" < ");
            }
            else if (ctx.GT() != null) {
                this.sb.append(" > ");
            }
            else if (ctx.LE() != null) {
                this.sb.append(" <= ");
            }
            else {
                this.sb.append(" >= ");
            }
        }

        visitShiftExpression(ctx.shiftExpression());
    }

    protected void visitShiftExpression(ShiftExpressionContext ctx) throws TranspileException {
        if (ctx.shiftExpression() != null) {
            visitShiftExpression(ctx.shiftExpression());
            this.sb.append((ctx.GT(0) != null) ? " >> " : " << ");
        }

        visitAdditiveExpression(ctx.additiveExpression());
    }

    protected void visitAdditiveExpression(AdditiveExpressionContext ctx) throws TranspileException {
        if (ctx.additiveExpression() != null) {
            visitAdditiveExpression(ctx.additiveExpression());
            this.sb.append(ctx.ADD() != null ? " + " : " - ");
        }

        visitMultiplicativeExpression(ctx.multiplicativeExpression());
    }

    protected void visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) throws TranspileException {
        if (ctx.multiplicativeExpression() != null) {
            visitMultiplicativeExpression(ctx.multiplicativeExpression());
            if (ctx.MUL() != null) {
                this.sb.append(" * ");
            }
            else {
                this.sb.append((ctx.DIV() != null) ? " / " : " % ");
            }
        }

        visitUnaryExpression(ctx.unaryExpression());
    }

    protected void visitUnaryExpression(UnaryExpressionContext ctx) throws TranspileException {
        if (ctx.preIncrementExpression() != null) {
            visitPreIncrementExpression(ctx.preIncrementExpression());
        }

        else if (ctx.preDecrementExpression() != null) {
            visitPreDecrementExpression(ctx.preDecrementExpression());
        }

        else if (ctx.unaryExpressionNotPlusMinus() != null) {
            visitUnaryExpressionNotPlusMinus(ctx.unaryExpressionNotPlusMinus());
        }
        else {
            this.sb.append((ctx.ADD() != null) ? " + " : " - ");

            visitUnaryExpression(ctx.unaryExpression());
        }
    }

    protected void visitPreIncrementExpression(PreIncrementExpressionContext ctx) throws TranspileException {
        this.sb.append(" ++");
        visitUnaryExpression(ctx.unaryExpression());
    }

    protected void visitPostIncrementExpression(PostIncrementExpressionContext ctx) throws TranspileException {
        visitPostfixExpression(ctx.postfixExpression());
        this.sb.append("++ ");
    }

    protected void visitPostDecrementExpression(PostDecrementExpressionContext ctx) throws TranspileException {
        visitPostfixExpression(ctx.postfixExpression());
        this.sb.append("-- ");
    }

    protected void visitPreDecrementExpression(PreDecrementExpressionContext ctx) throws TranspileException {
        this.sb.append(" ++");
        visitUnaryExpression(ctx.unaryExpression());
    }

    protected void visitUnaryExpressionNotPlusMinus(UnaryExpressionNotPlusMinusContext ctx) throws TranspileException {
        if (ctx.switchExpression() != null) {
            throw new TranspileException("Switch expression is not supported");
        }

        if (ctx.postfixExpression() != null) {
            visitPostfixExpression(ctx.postfixExpression());
        }

        else if (ctx.castExpression() != null) {
            visitCastExpression(ctx.castExpression());
        }
        else {
            this.sb.append((ctx.TILDE() != null) ? " ~ " : "!");
            visitUnaryExpression(ctx.unaryExpression());
        }
    }

    protected void visitPostfixExpression(PostfixExpressionContext ctx) throws TranspileException {
        if (ctx.expressionName() != null) {
            this.sb.append(ctx.expressionName().identifier().getText());
        }
        else {

            if (ctx.primary().primaryNoNewArray() != null) {
                visitPrimaryNoNewArray(ctx.primary().primaryNoNewArray());
            }
            else {
                visitArrayCreationExpression(ctx.primary().arrayCreationExpression());
            }
        }

        if (ctx.pfE() != null) {
            visitPfE(ctx.pfE());
        }
    }

    protected void visitPfE(PfEContext ctx) {
        this.sb.append((ctx.INC() != null) ? " ++ " : " -- ");
        if (ctx.pfE() != null) {
            visitPfE(ctx.pfE());
        }
    }

    protected void visitCastExpression(CastExpressionContext ctx) throws TranspileException {
        if (ctx.primitiveType() != null) {
            this.sb.append("(").append(ctx.primitiveType().getText()).append(")");
        }

        if (ctx.unaryExpression() != null) {
            visitUnaryExpression(ctx.unaryExpression());
        }
        else if (ctx.unaryExpressionNotPlusMinus() != null) {
            visitUnaryExpressionNotPlusMinus(ctx.unaryExpressionNotPlusMinus());
        }
        else {
            throw new TranspileException("Lambda expression is not supported");
        }
    }

    protected void visitIfThenStatement(IfThenStatementContext ctx) throws TranspileException {
        this.sb.append("if (");
        visitExpression(ctx.expression());
        this.sb.append(")");
        visitStatement(ctx.statement());
    }

    protected void visitIfThenElseStatement(IfThenElseStatementContext ctx) throws TranspileException {
        this.sb.append("if (");
        visitExpression(ctx.expression());
        this.sb.append(")");
        visitStatementNoShortIf(ctx.statementNoShortIf());
        this.sb.append("\nelse ");
        visitStatement(ctx.statement());

    }

    protected void visitIfThenElseStatementNoShortIf(IfThenElseStatementNoShortIfContext ctx) throws TranspileException {
        this.sb.append("if (");
        visitExpression(ctx.expression());
        this.sb.append(")");
        visitStatementNoShortIf(ctx.statementNoShortIf(0));
        this.sb.append("\nelse ");
        visitStatementNoShortIf(ctx.statementNoShortIf(1));
    }

    protected void visitStatementNoShortIf(StatementNoShortIfContext ctx) throws TranspileException {
        if (ctx.statementWithoutTrailingSubstatement() != null) {
            visitStatementWithoutTrailingSubstatement(ctx.statementWithoutTrailingSubstatement());
        }

        else if (ctx.ifThenElseStatementNoShortIf() != null) {
            visitIfThenElseStatementNoShortIf(ctx.ifThenElseStatementNoShortIf());
        }

        else if (ctx.whileStatementNoShortIf() != null) {
            visitWhileStatementNoShortIf(ctx.whileStatementNoShortIf());
        }

        else if (ctx.forStatementNoShortIf() != null) {

        }
    }

    protected void visitWhileStatementNoShortIf(WhileStatementNoShortIfContext ctx) throws TranspileException {
        this.sb.append("while (");
        visitExpression(ctx.expression());
        this.sb.append(")");

        visitStatementNoShortIf(ctx.statementNoShortIf());
    }

    protected void visitWhileStatement(WhileStatementContext ctx) throws TranspileException {
        this.sb.append("while (");
        visitExpression(ctx.expression());
        this.sb.append(")");

        visitStatement(ctx.statement());
    }

    protected void visitDoStatement(DoStatementContext ctx) throws TranspileException {
        this.sb.append("do ");
        visitStatement(ctx.statement());
        this.sb.append("\nwhile (");
        visitExpression(ctx.expression());
        this.sb.append(");\n");
    }

    protected String replace(String method) throws TranspileException {
        String ret = replacement.getOrDefault(method, method);

        if (ret == null) {
            // throw new TranspileException(method + " is not a valid kernel function");
        }

        return ret;
    }
}
