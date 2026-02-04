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

public class STPFormulaCreator extends FormulaCreator<Long, Long, Long, Long> {
    STPFormulaCreator(Long stp) {
        super(stp, null, null, null, null, null);
    }


    @Override
    public Long getBitvectorType(int bitwidth) {
        return stpJNI.vc_bvType(getEnv(), bitwidth);
    }

    @Override
    public Long getFloatingPointType(FormulaType.FloatingPointType type) {
        throw new UnsupportedOperationException("Floating point operations are not supported by STP.");
    }

    @Override
    public Long getArrayType(Long indexType, Long elementType) {
        return stpJNI.vc_arrayType(getEnv(), indexType, elementType);
    }

    @Override
    public Long makeVariable(Long aLong, String varName) {
        return stpJNI.vc_varExpr(getEnv(), varName, aLong);
    }

    @Override
    public FormulaType<?> getFormulaType(Long formula) {

        if (stpJNI.vc_isBool(formula) == 1) {
            return FormulaType.BooleanType;
        }

        int bvWidth = stpJNI.getBVLength(formula);
        if(bvWidth > 0) {
            return FormulaType.getBitvectorTypeWithSize(bvWidth);
        }

        int indexWitdth = stpJNI.getIWidth(formula);
        int valueWidth = stpJNI.getVWidth(formula);
        if(indexWitdth > 0 && valueWidth > 0) {
            return FormulaType.getArrayType(
                    FormulaType.getBitvectorTypeWithSize(indexWitdth),
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
