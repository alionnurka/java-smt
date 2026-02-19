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


import java.math.BigInteger;

public class StpBitvectorFormulaManager extends AbstractBitvectorFormulaManager<Long, Long, Long, Long> {

    private final long stp;

    StpBitvectorFormulaManager(
            StpFormulaCreator creator, StpBooleanFormulaManager pBmgr) {
        super(creator, pBmgr);
        this.stp = creator.getEnv();
    }

    @Override
    protected Long makeBitvectorImpl(int length, Long pParam1) {
        int current = StpJNI.vc_getBVLength(stp,  pParam1);
        if(current == length) {
            return pParam1;
        } else if(current < length) {
            return StpJNI.vc_bvSignExtend(stp, pParam1, length);
        } else {
            // Truncate from 0th posiotion to position of length
            return StpJNI.vc_bvExtract(stp, pParam1, length - 1, 0);
        }
    }

    @Override
    protected Long toIntegerFormulaImpl(Long pI, boolean signed) {
        throw new UnsupportedOperationException("STP does not support Integer theory.");
    }

    @Override
    protected Long negate(Long pParam1) {
        return StpJNI.vc_bvUMinusExpr(stp, pParam1);
    }

    @Override
    protected Long add(Long pParam1, Long pParam2) {
        int width = StpJNI.vc_getBVLength(stp, pParam1);
        return StpJNI.vc_bvPlusExpr(stp, width, pParam1, pParam2);
    }

    @Override
    protected Long subtract(Long pParam1, Long pParam2) {
        int width = StpJNI.vc_getBVLength(stp, pParam1);
        return StpJNI.vc_bvMinusExpr(stp, width, pParam1, pParam2);
    }

    @Override
    protected Long divide(Long pParam1, Long pParam2, boolean signed) {
        int width = StpJNI.vc_getBVLength(stp, pParam1);
        if(signed) {
            return StpJNI.vc_sbvDivExpr(stp, width, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvDivExpr(stp, width, pParam1, pParam2);
        }
    }

    @Override
    protected Long remainder(Long pParam1, Long pParam2, boolean signed) {
        int width = StpJNI.vc_getBVLength(stp, pParam1);
        if(signed) {
            return StpJNI.vc_sbvRemExpr(stp, width, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvRemExpr(stp, width, pParam1, pParam2);
        }
    }

    @Override
    protected Long smodulo(Long a, Long b) {
        int w = StpJNI.vc_getBVLength(stp, a);
        return StpJNI.vc_sbvModExpr(stp, w, a, b);
    }


    @Override
    protected Long multiply(Long a, Long b) {
        int w = StpJNI.vc_getBVLength(stp, a);
        return StpJNI.vc_bvMultExpr(stp, w, a, b);
    }

    @Override
    protected Long equal(Long pParam1, Long pParam2) {
        return StpJNI.vc_eqExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long greaterThan(Long pParam1, Long pParam2, boolean signed) {
        if(signed) {
            return StpJNI.vc_sbvGtExpr(stp, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvGtExpr(stp, pParam1, pParam2);
        }
    }

    @Override
    protected Long greaterOrEquals(Long pParam1, Long pParam2, boolean signed) {
        if(signed) {
            return StpJNI.vc_sbvGeExpr(stp, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvGeExpr(stp, pParam1, pParam2);
        }
    }

    @Override
    protected Long lessThan(Long pParam1, Long pParam2, boolean signed) {
        if(signed) {
            return StpJNI.vc_sbvLtExpr(stp, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvLtExpr(stp, pParam1, pParam2);
        }
    }

    @Override
    protected Long lessOrEquals(Long pParam1, Long pParam2, boolean signed) {
        if(signed) {
            return StpJNI.vc_sbvLeExpr(stp, pParam1, pParam2);
        } else {
            return StpJNI.vc_bvLeExpr(stp, pParam1, pParam2);
        }
    }

    @Override
    protected Long not(Long pParam1) {
        return StpJNI.vc_bvNotExpr(stp, pParam1);
    }

    @Override
    protected Long and(Long pParam1, Long pParam2) {
        return StpJNI.vc_bvAndExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long or(Long pParam1, Long pParam2) {
        return StpJNI.vc_bvOrExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long xor(Long pParam1, Long pParam2) {
        return StpJNI.vc_bvXorExpr(stp, pParam1, pParam2);
    }

    @Override
    protected Long makeBitvectorImpl(int pLength, BigInteger pI) {
        BigInteger transformed = transformValueToRange(pLength, pI);
        return StpJNI.vc_bvConstExprFromDecStr(stp, pLength, transformed.toString());
    }

    @Override
    protected Long makeVariableImpl(int pLength, String pVar) {
        long type = StpJNI.vc_bvType(stp, pLength);
        return StpJNI.vc_varExpr(stp, pVar, type);
    }

    @Override
    protected Long shiftRight(Long pNumber, Long toShift, boolean signed) {
        int width = StpJNI.vc_getBVLength(stp, pNumber);
        if(signed) {
            return StpJNI.vc_bvSignedRightShiftExprExpr(stp, width, pNumber, toShift);
        } else {
            return StpJNI.vc_bvRightShiftExprExpr(stp, width, pNumber, toShift);
        }
    }

    @Override
    protected Long shiftLeft(Long pNumber, Long pToShift) {
        int width = StpJNI.vc_getBVLength(stp, pNumber);
        return StpJNI.vc_bvLeftShiftExprExpr(stp, width, pNumber, pToShift);

    }

    @Override
    protected Long concat(Long number, Long pAppend) {
        return StpJNI.vc_bvConcatExpr(stp, number, pAppend);
    }

    @Override
    protected Long extract(Long pNumber, int pMsb, int pLsb) {
        return StpJNI.vc_bvExtract(stp, pNumber, pMsb, pLsb);
    }

    @Override
    protected Long extend(Long pNumber, int pExtensionBits, boolean pSigned) {
        if (pSigned) {
            int newWidth = StpJNI.vc_getBVLength(stp, pNumber) + pExtensionBits;
            return StpJNI.vc_bvSignExtend(stp, pNumber, newWidth);
        } else {
            long zeros = StpJNI.vc_bvConstExprFromInt(stp, pExtensionBits, 0);
            return StpJNI.vc_bvConcatExpr(stp, zeros, pNumber);
        }
    }
}
