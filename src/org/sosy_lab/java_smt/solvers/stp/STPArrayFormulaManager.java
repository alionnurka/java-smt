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
import org.sosy_lab.java_smt.basicimpl.AbstractArrayFormulaManager;

public class STPArrayFormulaManager extends AbstractArrayFormulaManager<Long, Long, Long, Long> {

    @Override
    protected Long select(Long pArray, Long pIndex) {
        return 0L;
    }

    @Override
    protected Long store(Long pArray, Long pIndex, Long pValue) {
        return 0L;
    }

    @Override
    protected <TI extends Formula, TE extends Formula> Long internalMakeArray(String pName, FormulaType<TI> pIndexType, FormulaType<TE> pElementType) {
        return 0L;
    }

    @Override
    protected <TI extends Formula, TE extends Formula> Long internalMakeArray(FormulaType<TI> pIndexType, FormulaType<TE> pElementType, Long defaultElement) {
        return 0L;
    }

    @Override
    protected Long equivalence(Long pArray1, Long pArray2) {
        return 0L;
    }

    /**
     * Declare a new array with exactly the given name.
     *
     * <p>Please make sure that the given name is valid in SMT-LIB2. Take a look at {@link
     * FormulaManager#isValidName} for further information.
     *
     * <p>This method does not quote or unquote the given name, but uses the plain name "AS IS".
     * {@link Formula#toString} can return a different String than the given one.
     *
     * @param pName
     * @param type  The type of the array, consisting of index type and element type
     */
    @Override
    public <TI extends Formula, TE extends Formula> ArrayFormula<TI, TE> makeArray(String pName, FormulaType.ArrayFormulaType<TI, TE> type) {
        return super.makeArray(pName, type);
    }

    /**
     * Create a new array constant with values initialized to defaultElement.
     *
     * @param type
     * @param defaultElement The default value of all entries in the array.
     */
    @Override
    public <TI extends Formula, TE extends Formula> ArrayFormula<TI, TE> makeArray(FormulaType.ArrayFormulaType<TI, TE> type, TE defaultElement) {
        return super.makeArray(type, defaultElement);
    }
}
