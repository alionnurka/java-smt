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

import static java.util.Objects.requireNonNull;

import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.BitvectorFormula;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.FormulaType.ArrayFormulaType;
import org.sosy_lab.java_smt.api.visitors.FormulaVisitor;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;
import org.sosy_lab.java_smt.solvers.stp.StpFormula.StpArrayFormula;
import org.sosy_lab.java_smt.solvers.stp.StpFormula.StpBitvectorFormula;
import org.sosy_lab.java_smt.solvers.stp.StpFormula.StpBooleanFormula;

import java.util.List;

public class StpFormulaCreator extends FormulaCreator<Long, Long, Long, Long> {
    StpFormulaCreator(Long stp) {
        super(requireNonNull(stp), StpJNI.vc_boolType(stp), null, null, null, null);
    }

    @Override
    public Long getBitvectorType(int bitwidth) {
        return StpJNI.vc_bvType(getEnv(), bitwidth);
    }

    @Override
    public Long getFloatingPointType(FormulaType.FloatingPointType type) {
        throw new UnsupportedOperationException(
                "Floating point operations are not supported by STP.");
    }

    @Override
    public Long getArrayType(Long indexType, Long elementType) {
        return StpJNI.vc_arrayType(getEnv(), indexType, elementType);
    }

    @Override
    public Long makeVariable(Long aLong, String varName) {
        return StpJNI.vc_varExpr(getEnv(), varName, aLong);
    }

    private static final int BOOLEAN_TYPE = 0;
    private static final int BITVECTOR_TYPE = 1;
    private static final int ARRAY_TYPE = 2;

    @Override
    public FormulaType<?> getFormulaType(Long formula) {
        int type = StpJNI.getType(formula);
        switch (type) {
            case BOOLEAN_TYPE:
                return FormulaType.BooleanType;
            case BITVECTOR_TYPE:
                return FormulaType.getBitvectorTypeWithSize(StpJNI.getBVLength(formula));
            case ARRAY_TYPE:
                return FormulaType.getArrayType(
                        FormulaType.getBitvectorTypeWithSize(StpJNI.getIWidth(formula)),
                        FormulaType.getBitvectorTypeWithSize(StpJNI.getVWidth(formula)));
            default:
                throw new AssertionError("Formula was not recognized by STP: " + formula);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Formula> FormulaType<T> getFormulaType(T formula) {
        // Needed for bitvectors/arrays: the default implementation in FormulaCreator throws.
        // For STP we can classify terms via getType/width functions, so unwrap and delegate.
        return (FormulaType<T>) getFormulaType(extractInfo(requireNonNull(formula)));
    }

    @Override
    public Long extractInfo(Formula formula) {
        if (formula instanceof StpFormula) {
            return ((StpFormula) formula).getExpr();
        }
        return super.extractInfo(requireNonNull(formula));
    }

    @Override
    public <R> R visit(FormulaVisitor<R> visitor, Formula formula, Long f) {
        throw new UnsupportedOperationException("Formula visiting not supported by STP.");
    }

    @Override
    public Long callFunctionImpl(Long declaration, List<Long> args) {
        throw new UnsupportedOperationException(
                "Functions are not supported by STP, use arrays instead.");
    }

    @Override
    public Long declareUFImpl(String pName, Long pReturnType, List<Long> pArgTypes) {
        throw new UnsupportedOperationException(
                "Functions are not supported by STP, use arrays instead.");
    }

    @Override
    protected Long getBooleanVarDeclarationImpl(Long formulaInfo) {
        return formulaInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Formula> T encapsulate(FormulaType<T> type, Long expr) {
        assert type.equals(getFormulaType(expr)) : String.format(
                "Trying to encapsulate formula of type %s as %s",
                getFormulaType(expr),
                type);
        if (type.isBooleanType()) {
            return (T) new StpBooleanFormula(expr, getEnv());
        } else if (type.isArrayType()) {
            ArrayFormulaType<?, ?> arrFt = (ArrayFormulaType<?, ?>) type;
            return (T) new StpArrayFormula<>(
                    expr,
                    arrFt.getIndexType(),
                    arrFt.getElementType(),
                    getEnv());
        } else if (type.isBitvectorType()) {
            return (T) new StpBitvectorFormula(expr, getEnv());
        }
        throw new IllegalArgumentException(
                "Cannot create formulas of type " + type + " in Boolector.");
    }

    @Override
    public BooleanFormula encapsulateBoolean(Long formula) {
        assert getFormulaType(formula).isBooleanType()
                : "Unexpected formula type for Boolean formula: " + getFormulaType(formula);
        return new StpBooleanFormula(formula, getEnv());
    }

    @Override
    public BitvectorFormula encapsulateBitvector(Long formula) {
        assert getFormulaType(formula).isBitvectorType()
                : "Unexpected formula type for BV formula: " + getFormulaType(formula);
        return new StpBitvectorFormula(formula, getEnv());
    }

    @Override
    @SuppressWarnings("MethodTypeParameterName")
    protected <TI extends Formula, TE extends Formula> ArrayFormula<TI, TE>
            encapsulateArray(Long formula, FormulaType<TI> indexType, FormulaType<TE> elemType) {
        assert getFormulaType(formula).isArrayType()
                : "Unexpected formula type for array formula: " + getFormulaType(formula);
        return new StpArrayFormula<>(formula, indexType, elemType, getEnv());
    }

}
