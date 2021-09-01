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
package com.okta.idx.kotlin.dto

import com.google.common.truth.Truth.assertThat
import com.okta.idx.kotlin.client.toJsonContent
import com.okta.idx.kotlin.dto.v1.Response
import com.okta.idx.kotlin.dto.v1.toIdxResponse
import com.okta.idx.kotlin.infrastructure.stringFromResources
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class IdxResponseTest {
    val json = Json {
        ignoreUnknownKeys = true
    }

    @Test fun testJson() {
        val response = json.decodeFromString<Response>(stringFromResources("dto/idx_response.json"))
        assertThat(response).isNotNull()
        val idxResponse = response.toIdxResponse()
        assertThat(idxResponse).isNotNull()
    }

    @Test fun testAuthenticators() {
        val response = json.decodeFromString<Response>(stringFromResources("dto/select_authenticator.json"))
        assertThat(response).isNotNull()
        val idxResponse = response.toIdxResponse()
        assertThat(idxResponse).isNotNull()
        val remediation = idxResponse.remediations[IdxRemediation.Type.SELECT_AUTHENTICATOR_AUTHENTICATE]!!
        val field = remediation.form["authenticator"]
        field?.selectedOption = field?.options!![0]
        val requestJson = remediation.toJsonContent().toString()
        assertThat(requestJson).isEqualTo("""{"authenticator":{"id":"auttbu5xxmIlrSqER5d6","methodType":"email"},"stateHandle":"02jbM3ltYruI-MEq8h8RCpHfnjPy-kJxpVq2HlfO2l"}""")
    }
}
