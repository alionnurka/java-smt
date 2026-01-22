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

    @Override
    protected ProverEnvironment newProverEnvironment0(Set<ProverOptions> options) {
        return null;
    }

    @Override
    protected InterpolatingProverEnvironment<?> newProverEnvironmentWithInterpolation0(Set<ProverOptions> pSet) {
        return null;
    }

    @Override
    protected OptimizationProverEnvironment newOptimizationProverEnvironment0(Set<ProverOptions> pSet) {
        return null;
    }

    @Override
    protected boolean supportsAssumptionSolving() {
        return false;
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public SolverContextFactory.Solvers getSolverName() {
        return null;
    }

    @Override
    public void close() {

    }
}
