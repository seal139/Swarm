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

// Generated from JParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JParser}.
 */
public interface JParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JParser#start_}.
	 * @param ctx the parse tree
	 */
	void enterStart_(JParser.Start_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#start_}.
	 * @param ctx the parse tree
	 */
	void exitStart_(JParser.Start_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(JParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(JParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(JParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(JParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unqualifiedMethodIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedMethodIdentifier(JParser.UnqualifiedMethodIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unqualifiedMethodIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedMethodIdentifier(JParser.UnqualifiedMethodIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#contextualKeyword}.
	 * @param ctx the parse tree
	 */
	void enterContextualKeyword(JParser.ContextualKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#contextualKeyword}.
	 * @param ctx the parse tree
	 */
	void exitContextualKeyword(JParser.ContextualKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#contextualKeywordMinusForTypeIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterContextualKeywordMinusForTypeIdentifier(JParser.ContextualKeywordMinusForTypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#contextualKeywordMinusForTypeIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitContextualKeywordMinusForTypeIdentifier(JParser.ContextualKeywordMinusForTypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#contextualKeywordMinusForUnqualifiedMethodIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterContextualKeywordMinusForUnqualifiedMethodIdentifier(JParser.ContextualKeywordMinusForUnqualifiedMethodIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#contextualKeywordMinusForUnqualifiedMethodIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitContextualKeywordMinusForUnqualifiedMethodIdentifier(JParser.ContextualKeywordMinusForUnqualifiedMethodIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(JParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(JParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(JParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(JParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#numericType}.
	 * @param ctx the parse tree
	 */
	void enterNumericType(JParser.NumericTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#numericType}.
	 * @param ctx the parse tree
	 */
	void exitNumericType(JParser.NumericTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#integralType}.
	 * @param ctx the parse tree
	 */
	void enterIntegralType(JParser.IntegralTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#integralType}.
	 * @param ctx the parse tree
	 */
	void exitIntegralType(JParser.IntegralTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#floatingPointType}.
	 * @param ctx the parse tree
	 */
	void enterFloatingPointType(JParser.FloatingPointTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#floatingPointType}.
	 * @param ctx the parse tree
	 */
	void exitFloatingPointType(JParser.FloatingPointTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#referenceType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceType(JParser.ReferenceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#referenceType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceType(JParser.ReferenceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#coit}.
	 * @param ctx the parse tree
	 */
	void enterCoit(JParser.CoitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#coit}.
	 * @param ctx the parse tree
	 */
	void exitCoit(JParser.CoitContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(JParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(JParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(JParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(JParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceType}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceType(JParser.InterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceType}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceType(JParser.InterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeVariable}.
	 * @param ctx the parse tree
	 */
	void enterTypeVariable(JParser.TypeVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeVariable}.
	 * @param ctx the parse tree
	 */
	void exitTypeVariable(JParser.TypeVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(JParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(JParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#dims}.
	 * @param ctx the parse tree
	 */
	void enterDims(JParser.DimsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#dims}.
	 * @param ctx the parse tree
	 */
	void exitDims(JParser.DimsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(JParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(JParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeParameterModifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameterModifier(JParser.TypeParameterModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeParameterModifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameterModifier(JParser.TypeParameterModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void enterTypeBound(JParser.TypeBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void exitTypeBound(JParser.TypeBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#additionalBound}.
	 * @param ctx the parse tree
	 */
	void enterAdditionalBound(JParser.AdditionalBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#additionalBound}.
	 * @param ctx the parse tree
	 */
	void exitAdditionalBound(JParser.AdditionalBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(JParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(JParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentList(JParser.TypeArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentList(JParser.TypeArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(JParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(JParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(JParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(JParser.WildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#wildcardBounds}.
	 * @param ctx the parse tree
	 */
	void enterWildcardBounds(JParser.WildcardBoundsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#wildcardBounds}.
	 * @param ctx the parse tree
	 */
	void exitWildcardBounds(JParser.WildcardBoundsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#moduleName}.
	 * @param ctx the parse tree
	 */
	void enterModuleName(JParser.ModuleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#moduleName}.
	 * @param ctx the parse tree
	 */
	void exitModuleName(JParser.ModuleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#packageName}.
	 * @param ctx the parse tree
	 */
	void enterPackageName(JParser.PackageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#packageName}.
	 * @param ctx the parse tree
	 */
	void exitPackageName(JParser.PackageNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(JParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(JParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#packageOrTypeName}.
	 * @param ctx the parse tree
	 */
	void enterPackageOrTypeName(JParser.PackageOrTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#packageOrTypeName}.
	 * @param ctx the parse tree
	 */
	void exitPackageOrTypeName(JParser.PackageOrTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#expressionName}.
	 * @param ctx the parse tree
	 */
	void enterExpressionName(JParser.ExpressionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#expressionName}.
	 * @param ctx the parse tree
	 */
	void exitExpressionName(JParser.ExpressionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(JParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(JParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#ambiguousName}.
	 * @param ctx the parse tree
	 */
	void enterAmbiguousName(JParser.AmbiguousNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#ambiguousName}.
	 * @param ctx the parse tree
	 */
	void exitAmbiguousName(JParser.AmbiguousNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(JParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(JParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#ordinaryCompilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterOrdinaryCompilationUnit(JParser.OrdinaryCompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#ordinaryCompilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitOrdinaryCompilationUnit(JParser.OrdinaryCompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#modularCompilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterModularCompilationUnit(JParser.ModularCompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#modularCompilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitModularCompilationUnit(JParser.ModularCompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(JParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(JParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#packageModifier}.
	 * @param ctx the parse tree
	 */
	void enterPackageModifier(JParser.PackageModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#packageModifier}.
	 * @param ctx the parse tree
	 */
	void exitPackageModifier(JParser.PackageModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(JParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(JParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#singleTypeImportDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleTypeImportDeclaration(JParser.SingleTypeImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#singleTypeImportDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleTypeImportDeclaration(JParser.SingleTypeImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeImportOnDemandDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeImportOnDemandDeclaration(JParser.TypeImportOnDemandDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeImportOnDemandDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeImportOnDemandDeclaration(JParser.TypeImportOnDemandDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#singleStaticImportDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleStaticImportDeclaration(JParser.SingleStaticImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#singleStaticImportDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleStaticImportDeclaration(JParser.SingleStaticImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#staticImportOnDemandDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStaticImportOnDemandDeclaration(JParser.StaticImportOnDemandDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#staticImportOnDemandDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStaticImportOnDemandDeclaration(JParser.StaticImportOnDemandDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#topLevelClassOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelClassOrInterfaceDeclaration(JParser.TopLevelClassOrInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#topLevelClassOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelClassOrInterfaceDeclaration(JParser.TopLevelClassOrInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#moduleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterModuleDeclaration(JParser.ModuleDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#moduleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitModuleDeclaration(JParser.ModuleDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#moduleDirective}.
	 * @param ctx the parse tree
	 */
	void enterModuleDirective(JParser.ModuleDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#moduleDirective}.
	 * @param ctx the parse tree
	 */
	void exitModuleDirective(JParser.ModuleDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#requiresModifier}.
	 * @param ctx the parse tree
	 */
	void enterRequiresModifier(JParser.RequiresModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#requiresModifier}.
	 * @param ctx the parse tree
	 */
	void exitRequiresModifier(JParser.RequiresModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(JParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(JParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#normalClassDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNormalClassDeclaration(JParser.NormalClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#normalClassDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNormalClassDeclaration(JParser.NormalClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassModifier(JParser.ClassModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassModifier(JParser.ClassModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(JParser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(JParser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeParameterList}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameterList(JParser.TypeParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeParameterList}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameterList(JParser.TypeParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classExtends}.
	 * @param ctx the parse tree
	 */
	void enterClassExtends(JParser.ClassExtendsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classExtends}.
	 * @param ctx the parse tree
	 */
	void exitClassExtends(JParser.ClassExtendsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classImplements}.
	 * @param ctx the parse tree
	 */
	void enterClassImplements(JParser.ClassImplementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classImplements}.
	 * @param ctx the parse tree
	 */
	void exitClassImplements(JParser.ClassImplementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceTypeList}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceTypeList(JParser.InterfaceTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceTypeList}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceTypeList(JParser.InterfaceTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classPermits}.
	 * @param ctx the parse tree
	 */
	void enterClassPermits(JParser.ClassPermitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classPermits}.
	 * @param ctx the parse tree
	 */
	void exitClassPermits(JParser.ClassPermitsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(JParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(JParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(JParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(JParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassMemberDeclaration(JParser.ClassMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassMemberDeclaration(JParser.ClassMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(JParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(JParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#fieldModifier}.
	 * @param ctx the parse tree
	 */
	void enterFieldModifier(JParser.FieldModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#fieldModifier}.
	 * @param ctx the parse tree
	 */
	void exitFieldModifier(JParser.FieldModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorList(JParser.VariableDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorList(JParser.VariableDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(JParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(JParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(JParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(JParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(JParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(JParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannType}.
	 * @param ctx the parse tree
	 */
	void enterUnannType(JParser.UnannTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannType}.
	 * @param ctx the parse tree
	 */
	void exitUnannType(JParser.UnannTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannPrimitiveType}.
	 * @param ctx the parse tree
	 */
	void enterUnannPrimitiveType(JParser.UnannPrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannPrimitiveType}.
	 * @param ctx the parse tree
	 */
	void exitUnannPrimitiveType(JParser.UnannPrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannReferenceType}.
	 * @param ctx the parse tree
	 */
	void enterUnannReferenceType(JParser.UnannReferenceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannReferenceType}.
	 * @param ctx the parse tree
	 */
	void exitUnannReferenceType(JParser.UnannReferenceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannClassOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterUnannClassOrInterfaceType(JParser.UnannClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannClassOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitUnannClassOrInterfaceType(JParser.UnannClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#uCOIT}.
	 * @param ctx the parse tree
	 */
	void enterUCOIT(JParser.UCOITContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#uCOIT}.
	 * @param ctx the parse tree
	 */
	void exitUCOIT(JParser.UCOITContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannClassType}.
	 * @param ctx the parse tree
	 */
	void enterUnannClassType(JParser.UnannClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannClassType}.
	 * @param ctx the parse tree
	 */
	void exitUnannClassType(JParser.UnannClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterUnannInterfaceType(JParser.UnannInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitUnannInterfaceType(JParser.UnannInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannTypeVariable}.
	 * @param ctx the parse tree
	 */
	void enterUnannTypeVariable(JParser.UnannTypeVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannTypeVariable}.
	 * @param ctx the parse tree
	 */
	void exitUnannTypeVariable(JParser.UnannTypeVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unannArrayType}.
	 * @param ctx the parse tree
	 */
	void enterUnannArrayType(JParser.UnannArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unannArrayType}.
	 * @param ctx the parse tree
	 */
	void exitUnannArrayType(JParser.UnannArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(JParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(JParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodModifier}.
	 * @param ctx the parse tree
	 */
	void enterMethodModifier(JParser.MethodModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodModifier}.
	 * @param ctx the parse tree
	 */
	void exitMethodModifier(JParser.MethodModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodHeader}.
	 * @param ctx the parse tree
	 */
	void enterMethodHeader(JParser.MethodHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodHeader}.
	 * @param ctx the parse tree
	 */
	void exitMethodHeader(JParser.MethodHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#result}.
	 * @param ctx the parse tree
	 */
	void enterResult(JParser.ResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#result}.
	 * @param ctx the parse tree
	 */
	void exitResult(JParser.ResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclarator(JParser.MethodDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclarator(JParser.MethodDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#receiverParameter}.
	 * @param ctx the parse tree
	 */
	void enterReceiverParameter(JParser.ReceiverParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#receiverParameter}.
	 * @param ctx the parse tree
	 */
	void exitReceiverParameter(JParser.ReceiverParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(JParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(JParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(JParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(JParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableArityParameter}.
	 * @param ctx the parse tree
	 */
	void enterVariableArityParameter(JParser.VariableArityParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableArityParameter}.
	 * @param ctx the parse tree
	 */
	void exitVariableArityParameter(JParser.VariableArityParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(JParser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(JParser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#throwsT}.
	 * @param ctx the parse tree
	 */
	void enterThrowsT(JParser.ThrowsTContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#throwsT}.
	 * @param ctx the parse tree
	 */
	void exitThrowsT(JParser.ThrowsTContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#exceptionTypeList}.
	 * @param ctx the parse tree
	 */
	void enterExceptionTypeList(JParser.ExceptionTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#exceptionTypeList}.
	 * @param ctx the parse tree
	 */
	void exitExceptionTypeList(JParser.ExceptionTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#exceptionType}.
	 * @param ctx the parse tree
	 */
	void enterExceptionType(JParser.ExceptionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#exceptionType}.
	 * @param ctx the parse tree
	 */
	void exitExceptionType(JParser.ExceptionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(JParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(JParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#instanceInitializer}.
	 * @param ctx the parse tree
	 */
	void enterInstanceInitializer(JParser.InstanceInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#instanceInitializer}.
	 * @param ctx the parse tree
	 */
	void exitInstanceInitializer(JParser.InstanceInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#staticInitializer}.
	 * @param ctx the parse tree
	 */
	void enterStaticInitializer(JParser.StaticInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#staticInitializer}.
	 * @param ctx the parse tree
	 */
	void exitStaticInitializer(JParser.StaticInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(JParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(JParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constructorModifier}.
	 * @param ctx the parse tree
	 */
	void enterConstructorModifier(JParser.ConstructorModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constructorModifier}.
	 * @param ctx the parse tree
	 */
	void exitConstructorModifier(JParser.ConstructorModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constructorDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclarator(JParser.ConstructorDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constructorDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclarator(JParser.ConstructorDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#simpleTypeName}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeName(JParser.SimpleTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#simpleTypeName}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeName(JParser.SimpleTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void enterConstructorBody(JParser.ConstructorBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void exitConstructorBody(JParser.ConstructorBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#explicitConstructorInvocation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitConstructorInvocation(JParser.ExplicitConstructorInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#explicitConstructorInvocation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitConstructorInvocation(JParser.ExplicitConstructorInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(JParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(JParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumBody}.
	 * @param ctx the parse tree
	 */
	void enterEnumBody(JParser.EnumBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumBody}.
	 * @param ctx the parse tree
	 */
	void exitEnumBody(JParser.EnumBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumConstantList}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstantList(JParser.EnumConstantListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumConstantList}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstantList(JParser.EnumConstantListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(JParser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(JParser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumConstantModifier}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstantModifier(JParser.EnumConstantModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumConstantModifier}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstantModifier(JParser.EnumConstantModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumBodyDeclarations(JParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumBodyDeclarations(JParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordDeclaration(JParser.RecordDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordDeclaration(JParser.RecordDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordHeader}.
	 * @param ctx the parse tree
	 */
	void enterRecordHeader(JParser.RecordHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordHeader}.
	 * @param ctx the parse tree
	 */
	void exitRecordHeader(JParser.RecordHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordComponentList}.
	 * @param ctx the parse tree
	 */
	void enterRecordComponentList(JParser.RecordComponentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordComponentList}.
	 * @param ctx the parse tree
	 */
	void exitRecordComponentList(JParser.RecordComponentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordComponent}.
	 * @param ctx the parse tree
	 */
	void enterRecordComponent(JParser.RecordComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordComponent}.
	 * @param ctx the parse tree
	 */
	void exitRecordComponent(JParser.RecordComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableArityRecordComponent}.
	 * @param ctx the parse tree
	 */
	void enterVariableArityRecordComponent(JParser.VariableArityRecordComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableArityRecordComponent}.
	 * @param ctx the parse tree
	 */
	void exitVariableArityRecordComponent(JParser.VariableArityRecordComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordComponentModifier}.
	 * @param ctx the parse tree
	 */
	void enterRecordComponentModifier(JParser.RecordComponentModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordComponentModifier}.
	 * @param ctx the parse tree
	 */
	void exitRecordComponentModifier(JParser.RecordComponentModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordBody}.
	 * @param ctx the parse tree
	 */
	void enterRecordBody(JParser.RecordBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordBody}.
	 * @param ctx the parse tree
	 */
	void exitRecordBody(JParser.RecordBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#recordBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordBodyDeclaration(JParser.RecordBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#recordBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordBodyDeclaration(JParser.RecordBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#compactConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterCompactConstructorDeclaration(JParser.CompactConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#compactConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitCompactConstructorDeclaration(JParser.CompactConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(JParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(JParser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#normalInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNormalInterfaceDeclaration(JParser.NormalInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#normalInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNormalInterfaceDeclaration(JParser.NormalInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceModifier(JParser.InterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceModifier(JParser.InterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceExtends}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceExtends(JParser.InterfaceExtendsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceExtends}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceExtends(JParser.InterfaceExtendsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfacePermits}.
	 * @param ctx the parse tree
	 */
	void enterInterfacePermits(JParser.InterfacePermitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfacePermits}.
	 * @param ctx the parse tree
	 */
	void exitInterfacePermits(JParser.InterfacePermitsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(JParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(JParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDeclaration(JParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDeclaration(JParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constantDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclaration(JParser.ConstantDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constantDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclaration(JParser.ConstantDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constantModifier}.
	 * @param ctx the parse tree
	 */
	void enterConstantModifier(JParser.ConstantModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constantModifier}.
	 * @param ctx the parse tree
	 */
	void exitConstantModifier(JParser.ConstantModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaration(JParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaration(JParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#interfaceMethodModifier}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodModifier(JParser.InterfaceMethodModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#interfaceMethodModifier}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodModifier(JParser.InterfaceMethodModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotationInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationInterfaceDeclaration(JParser.AnnotationInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotationInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationInterfaceDeclaration(JParser.AnnotationInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotationInterfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationInterfaceBody(JParser.AnnotationInterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotationInterfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationInterfaceBody(JParser.AnnotationInterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotationInterfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationInterfaceMemberDeclaration(JParser.AnnotationInterfaceMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotationInterfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationInterfaceMemberDeclaration(JParser.AnnotationInterfaceMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotationInterfaceElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationInterfaceElementDeclaration(JParser.AnnotationInterfaceElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotationInterfaceElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationInterfaceElementDeclaration(JParser.AnnotationInterfaceElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotationInterfaceElementModifier}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationInterfaceElementModifier(JParser.AnnotationInterfaceElementModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotationInterfaceElementModifier}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationInterfaceElementModifier(JParser.AnnotationInterfaceElementModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(JParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(JParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(JParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(JParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#normalAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterNormalAnnotation(JParser.NormalAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#normalAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitNormalAnnotation(JParser.NormalAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#elementValuePairList}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairList(JParser.ElementValuePairListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#elementValuePairList}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairList(JParser.ElementValuePairListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(JParser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(JParser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(JParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(JParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(JParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(JParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#elementValueList}.
	 * @param ctx the parse tree
	 */
	void enterElementValueList(JParser.ElementValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#elementValueList}.
	 * @param ctx the parse tree
	 */
	void exitElementValueList(JParser.ElementValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#markerAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterMarkerAnnotation(JParser.MarkerAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#markerAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitMarkerAnnotation(JParser.MarkerAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#singleElementAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterSingleElementAnnotation(JParser.SingleElementAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#singleElementAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitSingleElementAnnotation(JParser.SingleElementAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(JParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(JParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableInitializerList}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializerList(JParser.VariableInitializerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableInitializerList}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializerList(JParser.VariableInitializerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatements(JParser.BlockStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatements(JParser.BlockStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(JParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(JParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#localClassOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalClassOrInterfaceDeclaration(JParser.LocalClassOrInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#localClassOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalClassOrInterfaceDeclaration(JParser.LocalClassOrInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#localVariableType}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableType(JParser.LocalVariableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#localVariableType}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableType(JParser.LocalVariableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(JParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(JParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(JParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(JParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#statementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterStatementNoShortIf(JParser.StatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#statementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitStatementNoShortIf(JParser.StatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#statementWithoutTrailingSubstatement}.
	 * @param ctx the parse tree
	 */
	void enterStatementWithoutTrailingSubstatement(JParser.StatementWithoutTrailingSubstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#statementWithoutTrailingSubstatement}.
	 * @param ctx the parse tree
	 */
	void exitStatementWithoutTrailingSubstatement(JParser.StatementWithoutTrailingSubstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#emptyStatement_}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement_(JParser.EmptyStatement_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#emptyStatement_}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement_(JParser.EmptyStatement_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(JParser.LabeledStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(JParser.LabeledStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#labeledStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatementNoShortIf(JParser.LabeledStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#labeledStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatementNoShortIf(JParser.LabeledStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(JParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(JParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(JParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(JParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfThenStatement(JParser.IfThenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfThenStatement(JParser.IfThenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#ifThenElseStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatement(JParser.IfThenElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#ifThenElseStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatement(JParser.IfThenElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#ifThenElseStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatementNoShortIf(JParser.IfThenElseStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#ifThenElseStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatementNoShortIf(JParser.IfThenElseStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#assertStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssertStatement(JParser.AssertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#assertStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssertStatement(JParser.AssertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(JParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(JParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchBlock}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlock(JParser.SwitchBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchBlock}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlock(JParser.SwitchBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchRule}.
	 * @param ctx the parse tree
	 */
	void enterSwitchRule(JParser.SwitchRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchRule}.
	 * @param ctx the parse tree
	 */
	void exitSwitchRule(JParser.SwitchRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(JParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(JParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(JParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(JParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#caseConstant}.
	 * @param ctx the parse tree
	 */
	void enterCaseConstant(JParser.CaseConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#caseConstant}.
	 * @param ctx the parse tree
	 */
	void exitCaseConstant(JParser.CaseConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(JParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(JParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#whileStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatementNoShortIf(JParser.WhileStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#whileStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatementNoShortIf(JParser.WhileStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(JParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(JParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(JParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(JParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#forStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterForStatementNoShortIf(JParser.ForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#forStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitForStatementNoShortIf(JParser.ForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#basicForStatement}.
	 * @param ctx the parse tree
	 */
	void enterBasicForStatement(JParser.BasicForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#basicForStatement}.
	 * @param ctx the parse tree
	 */
	void exitBasicForStatement(JParser.BasicForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#basicForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterBasicForStatementNoShortIf(JParser.BasicForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#basicForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitBasicForStatementNoShortIf(JParser.BasicForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(JParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(JParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(JParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(JParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#statementExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpressionList(JParser.StatementExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#statementExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpressionList(JParser.StatementExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enhancedForStatement}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForStatement(JParser.EnhancedForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enhancedForStatement}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForStatement(JParser.EnhancedForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#enhancedForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForStatementNoShortIf(JParser.EnhancedForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#enhancedForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForStatementNoShortIf(JParser.EnhancedForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(JParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(JParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(JParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(JParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(JParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(JParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStatement(JParser.ThrowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStatement(JParser.ThrowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 */
	void enterSynchronizedStatement(JParser.SynchronizedStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 */
	void exitSynchronizedStatement(JParser.SynchronizedStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(JParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(JParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#catches}.
	 * @param ctx the parse tree
	 */
	void enterCatches(JParser.CatchesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#catches}.
	 * @param ctx the parse tree
	 */
	void exitCatches(JParser.CatchesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(JParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(JParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#catchFormalParameter}.
	 * @param ctx the parse tree
	 */
	void enterCatchFormalParameter(JParser.CatchFormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#catchFormalParameter}.
	 * @param ctx the parse tree
	 */
	void exitCatchFormalParameter(JParser.CatchFormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(JParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(JParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(JParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(JParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#tryWithResourcesStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryWithResourcesStatement(JParser.TryWithResourcesStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#tryWithResourcesStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryWithResourcesStatement(JParser.TryWithResourcesStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterResourceSpecification(JParser.ResourceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitResourceSpecification(JParser.ResourceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#resourceList}.
	 * @param ctx the parse tree
	 */
	void enterResourceList(JParser.ResourceListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#resourceList}.
	 * @param ctx the parse tree
	 */
	void exitResourceList(JParser.ResourceListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(JParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(JParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#variableAccess}.
	 * @param ctx the parse tree
	 */
	void enterVariableAccess(JParser.VariableAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#variableAccess}.
	 * @param ctx the parse tree
	 */
	void exitVariableAccess(JParser.VariableAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#yieldStatement}.
	 * @param ctx the parse tree
	 */
	void enterYieldStatement(JParser.YieldStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#yieldStatement}.
	 * @param ctx the parse tree
	 */
	void exitYieldStatement(JParser.YieldStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(JParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(JParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typePattern}.
	 * @param ctx the parse tree
	 */
	void enterTypePattern(JParser.TypePatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typePattern}.
	 * @param ctx the parse tree
	 */
	void exitTypePattern(JParser.TypePatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(JParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(JParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#groupedExpression}.
	 * @param ctx the parse tree
	 */
	void enterGroupedExpression(JParser.GroupedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#groupedExpression}.
	 * @param ctx the parse tree
	 */
	void exitGroupedExpression(JParser.GroupedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(JParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(JParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#primaryNoNewArray}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryNoNewArray(JParser.PrimaryNoNewArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#primaryNoNewArray}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryNoNewArray(JParser.PrimaryNoNewArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#pNNA}.
	 * @param ctx the parse tree
	 */
	void enterPNNA(JParser.PNNAContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#pNNA}.
	 * @param ctx the parse tree
	 */
	void exitPNNA(JParser.PNNAContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classLiteral}.
	 * @param ctx the parse tree
	 */
	void enterClassLiteral(JParser.ClassLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classLiteral}.
	 * @param ctx the parse tree
	 */
	void exitClassLiteral(JParser.ClassLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classInstanceCreationExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassInstanceCreationExpression(JParser.ClassInstanceCreationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classInstanceCreationExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassInstanceCreationExpression(JParser.ClassInstanceCreationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unqualifiedClassInstanceCreationExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedClassInstanceCreationExpression(JParser.UnqualifiedClassInstanceCreationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unqualifiedClassInstanceCreationExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedClassInstanceCreationExpression(JParser.UnqualifiedClassInstanceCreationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#classOrInterfaceTypeToInstantiate}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceTypeToInstantiate(JParser.ClassOrInterfaceTypeToInstantiateContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#classOrInterfaceTypeToInstantiate}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceTypeToInstantiate(JParser.ClassOrInterfaceTypeToInstantiateContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentsOrDiamond(JParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentsOrDiamond(JParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayCreationExpression}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreationExpression(JParser.ArrayCreationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayCreationExpression}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreationExpression(JParser.ArrayCreationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayCreationExpressionWithoutInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreationExpressionWithoutInitializer(JParser.ArrayCreationExpressionWithoutInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayCreationExpressionWithoutInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreationExpressionWithoutInitializer(JParser.ArrayCreationExpressionWithoutInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayCreationExpressionWithInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreationExpressionWithInitializer(JParser.ArrayCreationExpressionWithInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayCreationExpressionWithInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreationExpressionWithInitializer(JParser.ArrayCreationExpressionWithInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#dimExprs}.
	 * @param ctx the parse tree
	 */
	void enterDimExprs(JParser.DimExprsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#dimExprs}.
	 * @param ctx the parse tree
	 */
	void exitDimExprs(JParser.DimExprsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#dimExpr}.
	 * @param ctx the parse tree
	 */
	void enterDimExpr(JParser.DimExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#dimExpr}.
	 * @param ctx the parse tree
	 */
	void exitDimExpr(JParser.DimExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(JParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(JParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(JParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(JParser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodInvocation}.
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation(JParser.MethodInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodInvocation}.
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation(JParser.MethodInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(JParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(JParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#methodReference}.
	 * @param ctx the parse tree
	 */
	void enterMethodReference(JParser.MethodReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#methodReference}.
	 * @param ctx the parse tree
	 */
	void exitMethodReference(JParser.MethodReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(JParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(JParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#pfE}.
	 * @param ctx the parse tree
	 */
	void enterPfE(JParser.PfEContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#pfE}.
	 * @param ctx the parse tree
	 */
	void exitPfE(JParser.PfEContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#postIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostIncrementExpression(JParser.PostIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#postIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostIncrementExpression(JParser.PostIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#postDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostDecrementExpression(JParser.PostDecrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#postDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostDecrementExpression(JParser.PostDecrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(JParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(JParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#preIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreIncrementExpression(JParser.PreIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#preIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreIncrementExpression(JParser.PreIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#preDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreDecrementExpression(JParser.PreDecrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#preDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreDecrementExpression(JParser.PreDecrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionNotPlusMinus(JParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionNotPlusMinus(JParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(JParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(JParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(JParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(JParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(JParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(JParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(JParser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(JParser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(JParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(JParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(JParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(JParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(JParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(JParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(JParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(JParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(JParser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(JParser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(JParser.ConditionalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(JParser.ConditionalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(JParser.ConditionalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(JParser.ConditionalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(JParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(JParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(JParser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(JParser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(JParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(JParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void enterLeftHandSide(JParser.LeftHandSideContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void exitLeftHandSide(JParser.LeftHandSideContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(JParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(JParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(JParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(JParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameters(JParser.LambdaParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameters(JParser.LambdaParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaParameterList}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameterList(JParser.LambdaParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaParameterList}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameterList(JParser.LambdaParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaParameter}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameter(JParser.LambdaParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaParameter}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameter(JParser.LambdaParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaParameterType}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameterType(JParser.LambdaParameterTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaParameterType}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameterType(JParser.LambdaParameterTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void enterLambdaBody(JParser.LambdaBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void exitLambdaBody(JParser.LambdaBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#switchExpression}.
	 * @param ctx the parse tree
	 */
	void enterSwitchExpression(JParser.SwitchExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#switchExpression}.
	 * @param ctx the parse tree
	 */
	void exitSwitchExpression(JParser.SwitchExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(JParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(JParser.ConstantExpressionContext ctx);
}