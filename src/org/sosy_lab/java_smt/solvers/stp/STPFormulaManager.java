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
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.RationalFormulaManager;
import org.sosy_lab.java_smt.basicimpl.*;

import java.io.IOException;

public class STPFormulaManager extends AbstractFormulaManager<Long, Long, Long, Long> {

    protected STPFormulaManager(FormulaCreator<Long, Long, Long, Long> pFormulaCreator, AbstractUFManager<Long, ?, Long, Long> functionManager, AbstractBooleanFormulaManager<Long, Long, Long, Long> booleanManager, @Nullable IntegerFormulaManager pIntegerManager, @Nullable RationalFormulaManager pRationalManager, @Nullable AbstractBitvectorFormulaManager<Long, Long, Long, Long> bitvectorManager, @Nullable AbstractFloatingPointFormulaManager<Long, Long, Long, Long> floatingPointManager, @Nullable AbstractQuantifiedFormulaManager<Long, Long, Long, Long> quantifiedManager, @Nullable AbstractArrayFormulaManager<Long, Long, Long, Long> arrayManager, @Nullable AbstractSLFormulaManager<Long, Long, Long, Long> slManager, @Nullable AbstractStringFormulaManager<Long, Long, Long, Long> strManager, @Nullable AbstractEnumerationFormulaManager<Long, Long, Long, Long> enumManager) {
        super(pFormulaCreator, functionManager, booleanManager, pIntegerManager, pRationalManager, bitvectorManager, floatingPointManager, quantifiedManager, arrayManager, slManager, strManager, enumManager);
    }

    @Override
    protected Long parseImpl(String formulaStr) throws IllegalArgumentException {
        return 0L;
    }

    @Override
    protected String dumpFormulaImpl(Long t) throws IOException {
        return "";
    }

}
