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
import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.basicimpl.AbstractProverWithAllSat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class STPAbstractProver<T> extends AbstractProverWithAllSat<T> {
    protected STPAbstractProver(Set<SolverContext.ProverOptions> pOptions, BooleanFormulaManager pBmgr, ShutdownNotifier pShutdownNotifier) {
        super(pOptions, pBmgr, pShutdownNotifier);
    }

    @Override
    protected Evaluator getEvaluatorWithoutChecks() throws SolverException, InterruptedException {
        return null;
    }

    @Override
    protected boolean hasPersistentModel() {
        return false;
    }

    @Override
    protected void pushImpl() throws InterruptedException {

    }

    @Override
    protected void popImpl() {

    }

    @Override
    protected @Nullable T addConstraintImpl(BooleanFormula constraint) throws InterruptedException {
        return null;
    }

    @Override
    protected boolean isUnsatImpl() throws SolverException, InterruptedException {
        return false;
    }

    @Override
    public boolean isUnsatWithAssumptions(Collection<BooleanFormula> assumptions) throws SolverException, InterruptedException {
        return false;
    }

    @Override
    public Model getModel() throws SolverException {
        return null;
    }

    @Override
    public List<BooleanFormula> getUnsatCore() {
        return List.of();
    }

    @Override
    public Optional<List<BooleanFormula>> unsatCoreOverAssumptions(Collection<BooleanFormula> assumptions) throws SolverException, InterruptedException {
        return Optional.empty();
    }
}
