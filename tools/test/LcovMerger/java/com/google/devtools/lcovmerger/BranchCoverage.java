// Copyright 2018 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.lcovmerger;

import com.google.auto.value.AutoValue;

/**
 * Stores branch coverage information.
 */
@AutoValue
abstract class BranchCoverage {

  static BranchCoverage create(
      int lineNumber, int blockNumber, int branchNumber, boolean wasExecuted, int nrOfExecutions) {
    assert (wasExecuted && nrOfExecutions > 0) || (!wasExecuted && nrOfExecutions == 0);
    return new AutoValue_BranchCoverage(
        lineNumber, blockNumber, branchNumber, wasExecuted, nrOfExecutions);
  }

  /**
   * Merges two given instances of {@link BranchCoverage}.
   *
   * Calling {@code lineNumber()}, {@code blockNumber()} and {@code branchNumber()} must return the
   * same values for {@code first} and {@code second}.
   */
  static BranchCoverage merge(BranchCoverage first, BranchCoverage second) {
    assert first.lineNumber() == second.lineNumber();
    assert first.blockNumber() == second.blockNumber();
    assert first.branchNumber() == second.branchNumber();

    return create(
        first.lineNumber(),
        first.blockNumber(),
        first.branchNumber(),
        first.wasExecuted() || second.wasExecuted(),
        first.nrOfExecutions() + second.nrOfExecutions());
  }

  abstract int lineNumber();
  // The two numbers below should be -1 for non-gcc emitted coverage (e.g. Java).
  abstract int blockNumber();  // internal gcc internal ID for the branch
  abstract int branchNumber(); // internal gcc internal ID for the branch
  abstract boolean wasExecuted();
  abstract int nrOfExecutions();
}
