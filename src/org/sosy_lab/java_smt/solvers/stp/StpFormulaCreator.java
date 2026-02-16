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

import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.visitors.FormulaVisitor;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

import java.util.List;

public class StpFormulaCreator extends FormulaCreator<Long, Long, Long, Long> {
    StpFormulaCreator(Long stp) {
        super(stp, StpJNI.vc_boolType(stp), null, null, null, null);
    }

    @Override
    public Long extractInfo(Formula pT) {
        return super.extractInfo(pT);
    }

    @Override
    public Long getBitvectorType(int bitwidth) {
        return StpJNI.vc_bvType(getEnv(), bitwidth);
    }

    @Override
    public Long getFloatingPointType(FormulaType.FloatingPointType type) {
        throw new UnsupportedOperationException("Floating point operations are not supported by STP.");
    }

    @Override
    public Long getArrayType(Long indexType, Long elementType) {
        return StpJNI.vc_arrayType(getEnv(), indexType, elementType);
    }

    @Override
    public Long makeVariable(Long aLong, String varName) {
        return StpJNI.vc_varExpr(getEnv(), varName, aLong);
    }

    private static final int BITVECTOR_TYPE = 1;
    private static final int ARRAY_TYPE = 2;

    @Override
    public FormulaType<?> getFormulaType(Long formula) {
        int type = StpJNI.getType(formula);
        if (type == StpJNI.BOOLEAN_TYPE_get()) {
            return FormulaType.BooleanType;
        }
        if (type == BITVECTOR_TYPE) {
            int bvWidth = StpJNI.getBVLength(formula);
            return FormulaType.getBitvectorTypeWithSize(bvWidth);
        }
        if (type == ARRAY_TYPE) {
            int indexWidth = StpJNI.getIWidth(formula);
            int valueWidth = StpJNI.getVWidth(formula);
            return FormulaType.getArrayType(
                    FormulaType.getBitvectorTypeWithSize(indexWidth),
                    FormulaType.getBitvectorTypeWithSize(valueWidth));
        }
        throw new AssertionError("Formula was not recognized by STP: " + formula);
    }


    @Override
    public <R> R visit(FormulaVisitor<R> visitor, Formula formula, Long f) {
        throw new UnsupportedOperationException(
                "Formula visiting not supported by STP.");
    }

    @Override
    public Long callFunctionImpl(Long declaration, List<Long> args) {
        throw new UnsupportedOperationException(
                "Functions are not supported by STP, use arrays instead.");
    }

    @Override
    public Long declareUFImpl(String pName, Long pReturnType, List<Long> pArgTypes) {
        throw new UnsupportedOperationException(
                "Functions are not supported by STP, use arrays instead.");
    }

    @Override
    protected Long getBooleanVarDeclarationImpl(Long pTFormulaInfo) {
        return pTFormulaInfo;
    }

}
