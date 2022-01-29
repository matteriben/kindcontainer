/*
Copyright 2020-2022 Alex Stockinger

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.dajudge.kindcontainer;

import static com.dajudge.kindcontainer.TestUtils.stringResource;
import static java.util.Collections.singletonList;

/**
 * Manages containers for testing using the singleton pattern:
 * in https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/
 */
public class StaticContainers {
    public static final KindContainer<?> KIND = new KindContainer<>()
            .withExposedPorts(30000)
            .withNodeReadyTimeout(60)
            .waitingFor(NullWaitStrategy.INSTANCE)
            .withCaCerts(singletonList(stringResource("test.crt")));

    public static ApiServerContainer<?> API_SERVER = new ApiServerContainer<>();

    static {
        KIND.start();
        API_SERVER.start();
        Runtime.getRuntime().addShutdownHook(new Thread(KIND::close));
        Runtime.getRuntime().addShutdownHook(new Thread(API_SERVER::close));
    }
}