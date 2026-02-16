package org.sosy_lab.java_smt.solvers.stp;

import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.SolverContext;

public class StpGeneralRunner {

    public static void main(String[] args) throws InvalidConfigurationException {
        Configuration config = Configuration.defaultConfiguration();
        LogManager logger = BasicLogManager.create(config);
        ShutdownNotifier notifier = ShutdownNotifier.createDummy();
        Solvers solver = Solvers.STP;

        try (SolverContext context =
                SolverContextFactory.createSolverContext(config, logger, notifier, solver)) {
            System.out.println("Solver created successfully.");
            FormulaManager fmgr = context.getFormulaManager();

        }
    }
}
