/*
 * This file is part of JavaSMT,
 * an API wrapper for a collection of SMT solvers:
 * https://github.com/sosy-lab/java-smt
 *
 * SPDX-FileCopyrightText: 2024 Dirk Beyer <https://www.sosy-lab.org>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sosy_lab.java_smt.solvers.stp;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.visitors.FormulaVisitor;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

import java.util.List;

public class STPFormulaCreator extends FormulaCreator<Long, Long, Long, Long> {
    protected STPFormulaCreator(Long aLong, Long boolType, @Nullable Long pIntegerType, @Nullable Long pRationalType, @Nullable Long stringType, @Nullable Long regexType) {
        super(aLong, boolType, pIntegerType, pRationalType, stringType, regexType);
    }

    /**
     * @param bitwidth
     * @return
     */
    @Override
    public Long getBitvectorType(int bitwidth) {
        return 0L;
    }

    /**
     * @param type
     * @return
     */
    @Override
    public Long getFloatingPointType(FormulaType.FloatingPointType type) {
        return 0L;
    }

    /**
     * @param indexType
     * @param elementType
     * @return
     */
    @Override
    public Long getArrayType(Long indexType, Long elementType) {
        return 0L;
    }

    /**
     * @param aLong
     * @param varName
     * @return
     */
    @Override
    public Long makeVariable(Long aLong, String varName) {
        return 0L;
    }

    /**
     * @param formula
     * @return
     */
    @Override
    public FormulaType<?> getFormulaType(Long formula) {
        return null;
    }

    /**
     * @param visitor
     * @param formula
     * @param f
     * @see FormulaManager#visit
     */
    @Override
    public <R> R visit(FormulaVisitor<R> visitor, Formula formula, Long f) {
        return null;
    }

    /**
     * @param declaration
     * @param args
     * @return
     */
    @Override
    public Long callFunctionImpl(Long declaration, List<Long> args) {
        return 0L;
    }

    /**
     * @param pName
     * @param pReturnType
     * @param pArgTypes
     * @return
     */
    @Override
    public Long declareUFImpl(String pName, Long pReturnType, List<Long> pArgTypes) {
        return 0L;
    }

    /**
     * @param pTFormulaInfo
     * @return
     */
    @Override
    protected Long getBooleanVarDeclarationImpl(Long pTFormulaInfo) {
        return 0L;
    }
}
