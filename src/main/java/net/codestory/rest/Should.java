/**
 * Copyright (C) 2013-2014 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.rest;

import java.util.Objects;

public class Should {
  private final RestResponse response;

  Should(RestResponse response) {
    this.response = response;
  }

  public Should respond(int statusCode) {
    return assertEquals(statusCode, response.code());
  }

  public Should contain(String content) {
    return assertContains(content, response.bodyAsString());
  }

  public Should haveType(String contentType) {
    return assertContains(contentType, response.contentType());
  }

  public Should haveCookie(String name, String value) {
    return assertEquals(value, response.cookie(name));
  }

  public Should haveHeader(String name, String value) {
    return assertEquals(value, response.header(name));
  }

  // Verifications
  private Should assertEquals(Object expectedValue, Object actualValue) {
    if (!Objects.equals(expectedValue, actualValue)) {
      throw new AssertionError(String.format("Expecting \"%s\" was \"%s\"", expectedValue, actualValue));
    }
    return this;
  }

  private Should assertContains(String expectedValue, String actualValue) {
    if (!actualValue.contains(expectedValue)) {
      throw new AssertionError(String.format("Expecting \"%s\" to contain \"%s\"", actualValue, expectedValue));
    }
    return this;
  }
}
