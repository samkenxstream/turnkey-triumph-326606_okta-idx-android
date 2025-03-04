/*
 * Copyright 2021-Present Okta, Inc.
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
package com.okta.idx.android.infrastructure.network

import com.okta.idx.android.network.mock.NetworkDispatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class MockWebServerStatement(
    private val baseStatement: Statement,
    private val networkDispatcher: NetworkDispatcher,
    private val description: Description
) : Statement() {
    override fun evaluate() {
        try {
            baseStatement.evaluate()
            val numberRemainingInQueue = networkDispatcher.numberRemainingInQueue()
            if (numberRemainingInQueue != 0) {
                throw IllegalStateException(
                    "${description.testClass}#${description.methodName} - mock responses is not empty. Remaining: $numberRemainingInQueue."
                )
            }
        } finally {
            networkDispatcher.clear()
        }
    }
}
