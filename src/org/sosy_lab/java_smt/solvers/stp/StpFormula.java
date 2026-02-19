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

import com.google.errorprone.annotations.Immutable;
import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.BitvectorFormula;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;

public class StpFormula implements Formula {
    private final long stpExpr;
    private final long stp;

    public StpFormula(long stpExpr, long stp) {
        this.stpExpr = stpExpr;
        this.stp = stp;
    }

    public long getExpr() {
        return stpExpr;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof StpFormula)) {
            return false;
        }

        StpFormula other = (StpFormula) o;
        return stp == other.stp && stpExpr == other.stpExpr;
    }

    @Override
    public final int hashCode() {
        return StpJNI.getExprID(stpExpr);
    }

    @Immutable
    static final class StpBitvectorFormula extends StpFormula implements BitvectorFormula {
        StpBitvectorFormula(long expr, long stp) {
            super(expr, stp);
        }
    }

    @Immutable
    static final class StpBooleanFormula extends StpFormula implements BooleanFormula {
        StpBooleanFormula(long expr, long stp) {
            super(expr, stp);
        }
    }

    @SuppressWarnings("ClassTypeParameterName")
    static final class StpArrayFormula<TI extends Formula, TE extends Formula> extends StpFormula
            implements ArrayFormula<TI, TE> {

        private final FormulaType<TI> indexType;
        private final FormulaType<TE> elementType;

        StpArrayFormula(
                long pTerm,
                FormulaType<TI> pIndexType,
                FormulaType<TE> pElementType,
                long btor) {
            super(pTerm, btor);
            indexType = pIndexType;
            elementType = pElementType;
        }

        public FormulaType<TI> getIndexType() {
            return indexType;
        }

        public FormulaType<TE> getElementType() {
            return elementType;
        }
    }

}
