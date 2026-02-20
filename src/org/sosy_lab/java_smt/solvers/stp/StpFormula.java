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
    private final int exprId;
    private final String cachedToString;

    public StpFormula(long stpExpr, long stp) {
        this.stpExpr = stpExpr;
        this.stp = stp;
        this.exprId = StpJNI.getExprID(stpExpr);
        this.cachedToString = computeToString(stpExpr);
    }

    public long getExpr() {
        return stpExpr;
    }

    @Override
    public final String toString() {
        return cachedToString;
    }

    private static String computeToString(long expr) {
        String s = StpJNI.exprString(expr);
        if (s != null) {
            String trimmed = s.trim();
            if (!trimmed.isBlank()) {
                String normalized = normalizeArraySelectSyntax(trimmed);
                return normalized;
            }
        }
        return "stp-expr#" + StpJNI.getExprID(expr);
    }

    private static String normalizeArraySelectSyntax(String exprString) {
        if (!exprString.endsWith("]") || exprString.indexOf('[') < 0) {
            return exprString;
        }
        if (exprString.startsWith("(")) {
            return exprString;
        }

        int firstBracket = exprString.indexOf('[');
        String base = exprString.substring(0, firstBracket).trim();
        if (base.isEmpty()) {
            return exprString;
        }

        java.util.ArrayList<String> indices = new java.util.ArrayList<>();
        int pos = firstBracket;
        while (pos < exprString.length() && exprString.charAt(pos) == '[') {
            int end = exprString.indexOf(']', pos + 1);
            if (end < 0) {
                return exprString; // malformed
            }
            String idx = exprString.substring(pos + 1, end).trim();
            if (idx.isEmpty()) {
                return exprString;
            }
            indices.add(idx);
            pos = end + 1;
        }
        if (pos != exprString.length()) {
            return exprString;
        }

        String result = base;
        for (String idx : indices) {
            result = "(select " + result + " " + idx + ")";
        }
        return result;
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
        return stp == other.stp && exprId == other.exprId;
    }

    @Override
    public final int hashCode() {
        int result = Long.hashCode(stp);
        result = 31 * result + exprId;
        return result;
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
