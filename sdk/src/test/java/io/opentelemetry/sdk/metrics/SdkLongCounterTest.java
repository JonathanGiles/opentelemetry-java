/*
 * Copyright 2020, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.sdk.metrics;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import io.opentelemetry.metrics.LabelSet;
import io.opentelemetry.metrics.LongCounter;
import io.opentelemetry.metrics.LongCounter.BoundLongCounter;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link SdkLongCounter}. */
@RunWith(JUnit4.class)
public class SdkLongCounterTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testLongCounter() {
    MeterSdk testSdk = new MeterSdk();
    LabelSet labelSet = testSdk.createLabelSet("K", "v");

    LongCounter longCounter =
        SdkLongCounter.Builder.builder("testCounter")
            .setConstantLabels(ImmutableMap.of("sk1", "sv1"))
            .setLabelKeys(Collections.singletonList("sk1"))
            .setDescription("My very own counter")
            .setUnit("metric tonnes")
            .setMonotonic(true)
            .build();

    longCounter.add(45, testSdk.createLabelSet());

    BoundLongCounter boundLongCounter = longCounter.bind(labelSet);
    boundLongCounter.add(334);
    BoundLongCounter duplicateBoundCounter = longCounter.bind(testSdk.createLabelSet("K", "v"));
    assertThat(duplicateBoundCounter).isEqualTo(boundLongCounter);

    // todo: verify that this has done something, when it has been done.
    longCounter.unbind(boundLongCounter);
  }

  @Test
  public void testLongCounter_monotonicity() {
    MeterSdk testSdk = new MeterSdk();

    LongCounter longCounter =
        SdkLongCounter.Builder.builder("testCounter").setMonotonic(true).build();

    thrown.expect(IllegalArgumentException.class);
    longCounter.add(-45, testSdk.createLabelSet());
  }

  @Test
  public void testBoundLongCounter_monotonicity() {
    MeterSdk testSdk = new MeterSdk();

    LongCounter longCounter =
        SdkLongCounter.Builder.builder("testCounter").setMonotonic(true).build();

    thrown.expect(IllegalArgumentException.class);
    longCounter.bind(testSdk.createLabelSet()).add(-9);
  }
}
