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

import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.InterpolatingProverEnvironment;
import org.sosy_lab.java_smt.api.OptimizationProverEnvironment;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.basicimpl.AbstractSolverContext;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class STPSolverContext extends AbstractSolverContext {
    private final STPFormulaManager manager;
    private final STPFormulaCreator creator;
    private final ShutdownNotifier shutdownNotifier;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    STPSolverContext(STPFormulaManager manager, STPFormulaCreator creator,
                     ShutdownNotifier shutdownNotifier) {
        super(manager);
        this.manager = manager;
        this.creator = creator;
        this.shutdownNotifier = shutdownNotifier;
    }

    @Override
    protected ProverEnvironment newProverEnvironment0(Set<ProverOptions> options) {
        return null;
    }

    @Override
    protected InterpolatingProverEnvironment<?> newProverEnvironmentWithInterpolation0(Set<ProverOptions> pSet) {
        throw new UnsupportedOperationException("STP does not support interpolation");
    }

    @Override
    protected OptimizationProverEnvironment newOptimizationProverEnvironment0(
            Set<ProverOptions> pSet) {
        throw new UnsupportedOperationException("STP does not support optimization");
    }

    @Override
    protected boolean supportsAssumptionSolving() {
        return false;
    }

    @Override
    public String getVersion() {
        return "Git version Tag: " + stpJNI.get_git_version_tag();
    }

    @Override
    public SolverContextFactory.Solvers getSolverName() {
        return SolverContextFactory.Solvers.STP;
    }


    @Override
    public void close() {
        if (!closed.getAndSet(true)) {
            stpJNI.vc_Destroy(creator.getEnv());
        }
    }
}
