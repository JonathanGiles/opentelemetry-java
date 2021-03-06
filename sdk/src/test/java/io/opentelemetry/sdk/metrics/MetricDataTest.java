/*
 * Copyright 2019, OpenTelemetry Authors
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

import io.opentelemetry.sdk.metrics.MetricData.Descriptor;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link MetricData}. */
@RunWith(JUnit4.class)
public class MetricDataTest {
  @Rule public final ExpectedException thrown = ExpectedException.none();

  private static final Descriptor METRIC_DESCRIPTOR =
      Descriptor.createInternal(
          "metric_name",
          "metric_description",
          "ms",
          Descriptor.Type.MONOTONIC_INT64,
          Collections.singletonList("key"),
          Collections.singletonMap("key_const", "value_const"));
  private static final long START_EPOCH_NANOS = TimeUnit.MILLISECONDS.toNanos(1000);
  private static final long EPOCH_NANOS = TimeUnit.MILLISECONDS.toNanos(2000);

  @Test
  public void testGet() {
    MetricData metricData =
        MetricData.createInternal(METRIC_DESCRIPTOR, START_EPOCH_NANOS, EPOCH_NANOS);
    assertThat(metricData.getDescriptor()).isEqualTo(METRIC_DESCRIPTOR);
    assertThat(metricData.getStartEpochNanos()).isEqualTo(START_EPOCH_NANOS);
    assertThat(metricData.getEpochNanos()).isEqualTo(EPOCH_NANOS);
  }

  @Test
  public void create_NullDescriptor() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("descriptor");
    MetricData.createInternal(null, START_EPOCH_NANOS, EPOCH_NANOS);
  }
}
