#include <jni.h>
#include <stdint.h>
#include "org_sosy_lab_java_smt_solvers_stp_STPJNI.h"

/* STP headers */
#include <stp/c_interface.h>

JNIEXPORT jlong JNICALL
Java_org_sosy_1lab_java_1smt_solvers_stp_STPJNI_createVC(
    JNIEnv *env, jclass cls) {

  VC vc = vc_createValidityChecker();
  return (jlong)(uintptr_t)vc;
}

JNIEXPORT void JNICALL
Java_org_sosy_1lab_java_1smt_solvers_stp_STPJNI_destroyVC(
    JNIEnv *env, jclass cls, jlong vcPtr) {

  VC vc = (VC)(uintptr_t)vcPtr;
  vc_Destroy(vc);
}
