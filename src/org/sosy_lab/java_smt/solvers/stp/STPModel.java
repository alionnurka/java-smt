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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.sosy_lab.java_smt.basicimpl.AbstractModel;
import org.sosy_lab.java_smt.basicimpl.AbstractProver;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

public class STPModel extends AbstractModel<Long, Long, Long> {
  private final long stp;
  private final ImmutableList<ValueAssignment> assignments;

  protected STPModel(AbstractProver<?> prover, FormulaCreator<Long, Long, Long, ?> creator) {
    super(prover, creator);
    stp = creator.getEnv();
    assignments = buildAssignments(creator);
  }

  @Override
  protected @Nullable Long evalImpl(Long formula) {
    long evaluated = StpJNI.vc_getCounterExample(stp, formula);
    return evaluated == 0L ? null : evaluated;
  }

  @Override
  public ImmutableList<ValueAssignment> asList() {
    return assignments;
  }

  private ImmutableList<ValueAssignment> buildAssignments(FormulaCreator<Long, Long, Long, ?> pCreator) {
    if (!(pCreator instanceof StpFormulaCreator)) {
      return ImmutableList.of();
    }
    StpFormulaCreator stpCreator = (StpFormulaCreator) pCreator;
    ImmutableMap<String, Long> vars = stpCreator.getDeclaredVariables();
    ImmutableList.Builder<ValueAssignment> out = ImmutableList.builder();
    for (Map.Entry<String, Long> e : vars.entrySet()) {
      String name = e.getKey();
      Long var = e.getValue();
      Long valueTerm = evalImpl(var);
      if (valueTerm == null) {
        continue;
      }
      Object value = creator.convertValue(var, valueTerm);
      if (value == null) {
        continue;
      }
      long eq = StpJNI.vc_eqExpr(stp, var, valueTerm);
      out.add(
          new ValueAssignment(
              creator.encapsulateWithTypeOf(var),
              creator.encapsulateWithTypeOf(valueTerm),
              creator.encapsulateBoolean(eq),
              name,
              value,
              ImmutableList.of()));
    }
    return out.build();
  }
}
