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
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class StpTheoremProver extends StpAbstractProver<Void> implements ProverEnvironment {

    protected StpTheoremProver(
            StpFormulaManager manager,
            StpFormulaCreator creator,
            long stp,
            ShutdownNotifier pShutdownNotifier,
            Set<ProverOptions> pOptions,
            AtomicBoolean pIsAnyStackAlive) {
        super(manager, creator, stp, pShutdownNotifier, pOptions, pIsAnyStackAlive);
    }
}
