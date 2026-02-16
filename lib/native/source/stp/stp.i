/* stp.i - SWIG interface for STP C API
 * JavaSMT - https://github.com/sosy-lab/java-smt
 * SPDX-License-Identifier: Apache-2.0
 */

%module Stp
%{
#include "c_interface.h"
%}

/* Ignore functions with output parameters that need custom typemaps */
%ignore vc_printExprToBuffer;
%ignore vc_printQueryStateToBuffer;
%ignore vc_printCounterExampleToBuffer;
%ignore vc_printBVBitStringToBuffer;
%ignore vc_getCounterExampleArray;

/* Ignore array-input functions (Expr* children) - use vc_andExpr/vc_orExpr in Java */
%ignore vc_andExprN;
%ignore vc_orExprN;
%ignore vc_bvPlusExprN;

/* Ignore parseMemExpr (Expr* output params) */
%ignore vc_parseMemExpr;

%include "c_interface.h"
