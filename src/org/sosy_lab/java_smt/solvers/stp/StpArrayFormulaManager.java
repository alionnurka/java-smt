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

import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.FormulaType.BitvectorType;
import org.sosy_lab.java_smt.basicimpl.AbstractArrayFormulaManager;
import static com.google.common.base.Preconditions.checkArgument;


public class StpArrayFormulaManager extends AbstractArrayFormulaManager<Long, Long, Long, Long> {

    private final long stp;

    protected StpArrayFormulaManager(StpFormulaCreator formulaCreator) {
        super(formulaCreator);
        this.stp = formulaCreator.getEnv();
    }

    @Override
    protected Long select(Long arrayAddress, Long arrayIndex) {
        long environment = stp;
        return StpJNI.vc_readExpr(environment, arrayAddress, arrayIndex);
    }

    @Override
    protected Long store(Long arrayAddress, Long arrayIndex, Long valueAtIndex) {
        long environment = stp;
        return StpJNI.vc_writeExpr(environment, arrayAddress, arrayIndex, valueAtIndex);
    }

    @Override
    protected <TI extends Formula, TE extends Formula> Long internalMakeArray(String arrayName, FormulaType<TI> indexType, FormulaType<TE> elemType) {
        checkArgument(
            indexType.isBitvectorType() &&
            elemType.isBitvectorType(),
            "STP supports bitvector arrays only.");
        
        long env = stp;
        
        BitvectorType indexBitvectorType = (BitvectorType) indexType;
        BitvectorType elementBitvectorType = (BitvectorType) elemType;
        
        long indexSort =
            StpJNI.vc_bvType(env, indexBitvectorType.getSize());
        
        long elementSort =
            StpJNI.vc_bvType(env, elementBitvectorType.getSize());
        
        long arrayType =
            StpJNI.vc_arrayType(env, indexSort, elementSort);
        
        long array =
            StpJNI.vc_varExpr(env, arrayName, arrayType);
        
        return array;
    }

    @Override
    protected <TI extends Formula, TE extends Formula> Long internalMakeArray(FormulaType<TI> pIndexType, FormulaType<TE> pElementType, Long defaultElement) {
        throw new UnsupportedOperationException("STP does not provide a way to create constant arrays");
    }

    @Override
    protected Long equivalence(Long array1, Long array2) {
        long env = stp;
        return StpJNI.vc_eqExpr(env, array1, array2);
    }
}
