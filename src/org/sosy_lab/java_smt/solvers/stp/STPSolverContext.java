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

import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.InterpolatingProverEnvironment;
import org.sosy_lab.java_smt.api.OptimizationProverEnvironment;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.basicimpl.AbstractSolverContext;

import java.util.Collection;
import java.util.Set;

public class STPSolverContext extends AbstractSolverContext {
    protected STPSolverContext(FormulaManager fmgr) {
        super(fmgr);
    }

    /**
     * @param options
     * @return
     */
    @Override
    protected ProverEnvironment newProverEnvironment0(Set<ProverOptions> options) {
        return null;
    }

    /**
     * @param pSet
     * @return
     */
    @Override
    protected InterpolatingProverEnvironment<?> newProverEnvironmentWithInterpolation0(Set<ProverOptions> pSet) {
        return null;
    }

    /**
     * @param pSet
     * @return
     */
    @Override
    protected OptimizationProverEnvironment newOptimizationProverEnvironment0(Set<ProverOptions> pSet) {
        return null;
    }

    /**
     * Whether the solver supports solving under some given assumptions (with all corresponding
     * features) by itself, i.e., whether {@link
     * ProverEnvironment#isUnsatWithAssumptions(Collection)} and {@link
     * InterpolatingProverEnvironment#isUnsatWithAssumptions(Collection)} are fully
     * implemented.
     *
     * <p>Otherwise, i.e., if this method returns {@code false}, the solver does not need to support
     * this feature and may simply {@code throw UnsupportedOperationException} in the respective
     * methods. This class will wrap the prover environments and provide an implementation of the
     * feature.
     *
     * <p>This method is expected to always return the same value. Otherwise, the behavior of this
     * class is undefined.
     */
    @Override
    protected boolean supportsAssumptionSolving() {
        return false;
    }

    /**
     * Get version information out of the solver.
     *
     * <p>In most cases, the version contains the name of the solver followed by some numbers and
     * additional info about the version, e.g., "SMTInterpol 2.5-12-g3d15a15c".
     */
    @Override
    public String getVersion() {
        return "";
    }

    /**
     * Get solver name (MATHSAT5/Z3/etc...).
     *
     * <p>This is an uppercase String matching the enum identifier from {@link Solvers}
     */
    @Override
    public SolverContextFactory.Solvers getSolverName() {
        return null;
    }

    /**
     * Close the solver context.
     *
     * <p>After calling this method, further access to any object that had been returned from this
     * context is not wanted, i.e., it depends on the solver. Java-based solvers might wait for the
     * next garbage collection with any cleanup operation. Native solvers might even reference invalid
     * memory and cause segmentation faults.
     *
     * <p>Necessary for the solvers implemented in native code, frees the associated memory.
     */
    @Override
    public void close() {

    }
}
