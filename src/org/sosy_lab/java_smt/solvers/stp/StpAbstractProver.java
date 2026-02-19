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
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Evaluator;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverException;
import org.sosy_lab.java_smt.api.Model;
import org.sosy_lab.java_smt.basicimpl.AbstractProverWithAllSat;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class StpAbstractProver<T> extends AbstractProverWithAllSat<T> {

    private final long stp;
    private final StpFormulaCreator creator;
    private final AtomicBoolean isAnyStackAlive;

    protected StpAbstractProver(
        StpFormulaManager manager,
        StpFormulaCreator creator,
        long stp,
        ShutdownNotifier shutdownNotifier,
        Set<SolverContext.ProverOptions> pOptions,
        AtomicBoolean pIsAnyStackAlive) {
        super(pOptions, manager.getBooleanFormulaManager(), shutdownNotifier);
        this.creator = creator;
        this.stp = stp;
        this.isAnyStackAlive = pIsAnyStackAlive;
        Preconditions.checkState(
            !this.isAnyStackAlive.getAndSet(true),
            "STP does not support the usage of multiple "
                + "solver stacks at the same time. Please close any existing solver stack.");
        StpJNI.vc_push(stp);
    }

    @Override
    protected Evaluator getEvaluatorWithoutChecks() throws SolverException, InterruptedException {
        throw new UnsupportedOperationException("STP does not support model evaluation, only returns counterexamples.");
    }

    @Override
    protected boolean hasPersistentModel() {
        return false;
    }

    @Override
    protected void pushImpl() throws InterruptedException {
        StpJNI.vc_push(stp);
    }

    @Override
    protected void popImpl() {
        StpJNI.vc_pop(stp);
    }

    @Override
    public void close() {
        if (!closed) {
            for (int i = 0; i < size() + 1; i++) {
                StpJNI.vc_pop(stp);
            }
            Preconditions.checkState(this.isAnyStackAlive.getAndSet(false));
        }
        super.close();
    }

    @Override
    protected @Nullable T addConstraintImpl(BooleanFormula constraint) throws InterruptedException {
        long expression = creator.extractInfo(constraint);
        StpJNI.vc_assertFormula(stp, expression);
        return null;
    }

    /**
     * Checks whether the currently asserted constraints are UNSAT.
     *
     * <p>STP is historically designed as a <em>validity checker</em> rather than a
     * traditional SMT solver with an explicit {@code check-sat} command.
     * Its core query operation {@code vc_query(vc, Q)} asks whether the implication
     * {@code (assertions ⇒ Q)} is <em>valid</em> (true for all models).</p>
     *
     * <p>To determine unsatisfiability of the asserted constraints {@code A},
     * JavaSMT queries the validity of {@code false} under those assertions:</p>
     *
     * <pre>
     *   A is UNSAT  ⇔  (A ⇒ false) is VALID
     * </pre>
     *
     * <p>Using the STP C API, this is implemented by calling:</p>
     *
     * <pre>
     *   vc_query(vc, false)
     * </pre>
     *
     * <p>STP returns:</p>
     * <ul>
     *   <li>{@code 0} (INVALID) if a counterexample exists,
     *       meaning the assertions are SAT</li>
     *   <li>{@code 1} (VALID)   if the implication holds for all models,
     *       meaning the assertions are UNSAT</li>
     * </ul>
     *
     * <p>This method translates STP's validity-based semantics into JavaSMT's
     * satisfiability-based interface.</p>
     *
     * @return {@code true} if the asserted constraints are UNSAT, {@code false} otherwise
     * @throws SolverException if the solver encounters an error
     */
    @Override
    protected boolean isUnsatImpl() throws SolverException {
        int result = StpJNI.vc_query(stp, StpJNI.vc_falseExpr(stp));
        if (result == 0) {
            return false;
        } else if (result == 1) {
            return true;
        }
        throw new SolverException("STP vc_query returned unexpected status " + result);
    }


    @Override
    public boolean isUnsatWithAssumptions(Collection<BooleanFormula> assumptions) throws AssertionError {
        throw new UnsupportedOperationException("STP does not support assumptions");
    }

    @Override
    public Model getModel() throws SolverException {
        throw new UnsupportedOperationException("STP does not support model generation");
    }

    @Override
    public List<BooleanFormula> getUnsatCore() {
        throw new UnsupportedOperationException("STP does NOT support unsat cores");
    }

    @Override
    public Optional<List<BooleanFormula>> unsatCoreOverAssumptions(Collection<BooleanFormula> assumptions) throws SolverException, InterruptedException {
        throw new UnsupportedOperationException("This solver does not support UNSAT cores over assumptions");
    }
}
