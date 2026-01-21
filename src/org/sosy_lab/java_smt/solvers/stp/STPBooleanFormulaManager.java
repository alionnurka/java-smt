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

import org.sosy_lab.java_smt.basicimpl.AbstractBooleanFormulaManager;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

public class STPBooleanFormulaManager extends AbstractBooleanFormulaManager<Long, Long, Long, Long> {
    protected STPBooleanFormulaManager(FormulaCreator<Long, Long, Long, Long> pCreator) {
        super(pCreator);
    }

    /**
     * @param pVar
     * @return
     */
    @Override
    protected Long makeVariableImpl(String pVar) {
        return 0L;
    }

    /**
     * @param value
     * @return
     */
    @Override
    protected Long makeBooleanImpl(boolean value) {
        return 0L;
    }

    /**
     * @param pParam1
     * @return
     */
    @Override
    protected Long not(Long pParam1) {
        return 0L;
    }

    /**
     * @param pParam1
     * @param pParam2
     * @return
     */
    @Override
    protected Long and(Long pParam1, Long pParam2) {
        return 0L;
    }

    /**
     * @param pParam1
     * @param pParam2
     * @return
     */
    @Override
    protected Long or(Long pParam1, Long pParam2) {
        return 0L;
    }

    /**
     * @param pParam1
     * @param pParam2
     * @return
     */
    @Override
    protected Long xor(Long pParam1, Long pParam2) {
        return 0L;
    }

    /**
     * @param bits1
     * @param bits2
     * @return
     */
    @Override
    protected Long equivalence(Long bits1, Long bits2) {
        return 0L;
    }

    /**
     * @param bits
     * @return
     */
    @Override
    protected boolean isTrue(Long bits) {
        return false;
    }

    /**
     * @param bits
     * @return
     */
    @Override
    protected boolean isFalse(Long bits) {
        return false;
    }

    /**
     * @param cond
     * @param f1
     * @param f2
     * @return
     */
    @Override
    protected Long ifThenElse(Long cond, Long f1, Long f2) {
        return 0L;
    }
}
