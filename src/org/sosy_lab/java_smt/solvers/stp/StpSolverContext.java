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
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.io.PathCounterTemplate;
import org.sosy_lab.java_smt.SolverContextFactory;
import com.google.common.base.Preconditions;
import org.sosy_lab.java_smt.api.FloatingPointRoundingMode;
import org.sosy_lab.java_smt.api.InterpolatingProverEnvironment;
import org.sosy_lab.java_smt.api.OptimizationProverEnvironment;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.basicimpl.AbstractSolverContext;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class StpSolverContext extends AbstractSolverContext {

    private final StpFormulaManager formulaManager;
    private final StpFormulaCreator formulaCreator;
    private final ShutdownNotifier shutdownNotifier;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    StpSolverContext(
        StpFormulaManager formulaManager,
        StpFormulaCreator formulaCreator,
        ShutdownNotifier shutdownNotifier) {
      super(formulaManager);
      this.formulaManager = formulaManager;
      this.formulaCreator = formulaCreator;
      this.shutdownNotifier = shutdownNotifier;
    }

    @Override
    protected ProverEnvironment newProverEnvironment0(Set<ProverOptions> options) {
        Preconditions.checkState(!closed.get(), "solver context is already closed");
        return new StpTheoremProver(
                formulaManager,
                formulaCreator,
                formulaCreator.getEnv(),
                shutdownNotifier,
                options,
                new AtomicBoolean(false));
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
        return "Git version Tag: " + StpJNI.get_git_version_tag();
    }

    @Override
    public SolverContextFactory.Solvers getSolverName() {
        return SolverContextFactory.Solvers.STP;
    }


    @Override
    public void close() {
        if (!closed.getAndSet(true)) {
            StpJNI.vc_Destroy(formulaCreator.getEnv());
        }
    }

    public static SolverContext create(
        Configuration config,
        ShutdownNotifier shutdownNotifier,
        Consumer<String> loader) {
            loader.accept("javasmtstp");
            final long stp = StpJNI.vc_createValidityChecker();
            StpFormulaCreator formulaCreator = new StpFormulaCreator(stp);
            StpUFManager functionTheory = new StpUFManager(formulaCreator);
            StpBooleanFormulaManager booleanManager = new StpBooleanFormulaManager(formulaCreator);
            StpBitvectorFormulaManager bitvectorManager = new StpBitvectorFormulaManager(formulaCreator, booleanManager);
            StpArrayFormulaManager arrayManager = new StpArrayFormulaManager(formulaCreator);
            StpFormulaManager formulaManager = new StpFormulaManager(
                formulaCreator, functionTheory, booleanManager, bitvectorManager, arrayManager);
            return new StpSolverContext(formulaManager, formulaCreator, shutdownNotifier);
    }
}