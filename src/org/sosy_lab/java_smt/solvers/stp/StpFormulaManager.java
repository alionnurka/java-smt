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

import org.sosy_lab.java_smt.basicimpl.*;

import java.io.IOException;

public class StpFormulaManager extends AbstractFormulaManager<Long, Long, Long, Long> {

    protected StpFormulaManager(
        StpFormulaCreator formulaCreator, 
        StpUFManager functionManager, 
        StpBooleanFormulaManager booleanManager,
        StpBitvectorFormulaManager bitvectorManager,
        StpArrayFormulaManager arrayManager
        )
        {
        super(formulaCreator, 
            functionManager, 
            booleanManager, 
            null,
            null, 
            bitvectorManager, 
            null, 
            null, 
            arrayManager, 
            null, 
            null, 
            null
            );
    }

    @Override
    protected Long equalImpl(Long firstArg, Long secondArg) {
        return StpJNI.vc_eqExpr(getEnvironment(), firstArg, secondArg);
    }

    @Override
    protected Long parseImpl(String formulaStr) throws IllegalArgumentException {
        throw new UnsupportedOperationException("STP cannot parse single formulas from string");
    }

    @Override
    protected String dumpFormulaImpl(Long formula) throws IOException {
        return StpJNI.vc_printSMTLIB(getEnvironment(), formula);
    }

}
