package com.retiman.template.lang.testing;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public interface JavaInterface {
  // Java 8 introduced default methods in interfaces.
  default int getValue() {
    return this.getPrivateValue() * 2;
  }

  // Java 9 introduced private methods in interfaces.  However, it appears that SpotBugs cannot
  // determine that this method is being called by the default method in the interface.
  @SuppressFBWarnings("UPM_UNCALLED_PRIVATE_METHOD")
  private int getPrivateValue() {
    return 20;
  }
}
