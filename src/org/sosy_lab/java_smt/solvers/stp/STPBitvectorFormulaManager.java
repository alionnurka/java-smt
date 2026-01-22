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

import org.sosy_lab.java_smt.basicimpl.AbstractBitvectorFormulaManager;
import org.sosy_lab.java_smt.basicimpl.AbstractBooleanFormulaManager;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

import java.math.BigInteger;

public class STPBitvectorFormulaManager extends AbstractBitvectorFormulaManager<Long, Long, Long, Long> {

    protected STPBitvectorFormulaManager(FormulaCreator<Long, Long, Long, Long> pCreator, AbstractBooleanFormulaManager<Long, Long, Long, Long> pBmgr) {
        super(pCreator, pBmgr);
    }

    @Override
    protected Long makeBitvectorImpl(int length, Long pParam1) {
        return 0L;
    }

    @Override
    protected Long toIntegerFormulaImpl(Long pI, boolean signed) {
        return 0L;
    }

    @Override
    protected Long negate(Long pParam1) {
        return 0L;
    }

    @Override
    protected Long add(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long subtract(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long divide(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long remainder(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long smodulo(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long multiply(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long equal(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long greaterThan(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long greaterOrEquals(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long lessThan(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long lessOrEquals(Long pParam1, Long pParam2, boolean signed) {
        return 0L;
    }

    @Override
    protected Long not(Long pParam1) {
        return 0L;
    }

    @Override
    protected Long and(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long or(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long xor(Long pParam1, Long pParam2) {
        return 0L;
    }

    @Override
    protected Long makeBitvectorImpl(int pLength, BigInteger pI) {
        return 0L;
    }

    @Override
    protected Long makeVariableImpl(int pLength, String pVar) {
        return 0L;
    }

    @Override
    protected Long shiftRight(Long pNumber, Long toShift, boolean signed) {
        return 0L;
    }

    @Override
    protected Long shiftLeft(Long pNumber, Long pToShift) {
        return 0L;
    }

    @Override
    protected Long concat(Long number, Long pAppend) {
        return 0L;
    }

    @Override
    protected Long extract(Long pNumber, int pMsb, int pLsb) {
        return 0L;
    }

    @Override
    protected Long extend(Long pNumber, int pExtensionBits, boolean pSigned) {
        return 0L;
    }
}
