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

public class StpBooleanFormulaManager extends AbstractBooleanFormulaManager<Long, Long, Long, Long> {
    private final long stp;
    private final long pTrue;
    private final long pFalse;

    StpBooleanFormulaManager(StpFormulaCreator pCreator) {
        super(pCreator);
        this.stp = pCreator.getEnv();
        pTrue = StpJNI.vc_trueExpr(stp);
        pFalse = StpJNI.vc_falseExpr(stp);
    }


    @Override
    protected Long makeVariableImpl(String pVar) {
        long boolType = StpJNI.vc_boolType(stp);
        return StpJNI.vc_varExpr(stp, pVar, boolType);
    }

    @Override
    protected Long makeBooleanImpl(boolean value) {
        return value ? pTrue : pFalse;
    }

    @Override
    protected Long not(Long pParam1) {
        return StpJNI.vc_notExpr(stp, pParam1);
    }

    @Override
    protected Long and(Long pParam1, Long pParam2) {
        if (isTrue(pParam1)) {
            return pParam2;
        } else if (isTrue(pParam2)) {
            return pParam1;
        } else if (isFalse(pParam1)) {
            return pFalse;
        } else if (isFalse(pParam2)) {
            return pFalse;
        } else if (pParam1.equals(pParam2)) {
            return pParam1;
        }
        return StpJNI.vc_andExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long or(Long pParam1, Long pParam2) {
        if (isTrue(pParam1)) {
            return pTrue;
        } else if (isTrue(pParam2)) {
            return pTrue;
        } else if (isFalse(pParam1)) {
            return pParam2;
        } else if (isFalse(pParam2)) {
            return pParam1;
        } else if (pParam1.equals(pParam2)) {
            return pParam1;
        }
        return StpJNI.vc_orExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long xor(Long pParam1, Long pParam2) {
        return StpJNI.vc_xorExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long equivalence(Long bits1, Long bits2) {
        return StpJNI.vc_iffExpr(stp, bits1, bits2);
    }

    @Override
    protected boolean isTrue(Long bits) {
        return bits.equals(pTrue);
    }

    @Override
    protected boolean isFalse(Long bits) {
        return bits.equals(pFalse);
    }

    @Override
    protected Long ifThenElse(Long cond, Long f1, Long f2) {
        if (isTrue(cond)) {
            return f1;
        } else if (isFalse(cond)) {
            return f2;
        } else if (f1.equals(f2)) {
            return f1;
        } else if (isTrue(f1) && isFalse(f2)) {
            return cond;
        } else if (isFalse(f1) && isTrue(f2)) {
            return not(cond);
        }
        return StpJNI.vc_iteExpr(stp, cond, f1, f2);
    }
}
