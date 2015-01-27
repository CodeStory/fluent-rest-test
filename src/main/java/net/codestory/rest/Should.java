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

import static java.lang.String.format;

public class Should {
  private final RestResponse response;
  private final boolean negate;

  Should(RestResponse response, boolean negate) {
    this.response = response;
    this.negate = negate;
  }

  public Should not() {
    return new Should(response, !negate);
  }

  // TODO: shouldn't be able to write should().should()
  public Should should() {
    return new Should(response, false);
  }

  public Should respond(int statusCode) {
    return assertEquals("status code", response.code(), statusCode);
  }

  public Should contain(String content) {
    return assertContains(response.bodyAsString(), content);
  }

  public Should beEmpty() {
    return assertEmpty(response.bodyAsString());
  }

  public Should haveType(String contentType) {
    return assertContains(response.contentType(), contentType);
  }

  public Should haveCookie(String name, String value) {
    return assertEquals("cookie " + name, response.cookie(name), value);
  }

  public Should haveHeader(String name, String value) {
    return assertEquals("header " + name, response.header(name), value);
  }

  // Verifications
  private Should assertEquals(String what, Object actual, Object expected) {
    if (negate == Objects.equals(expected, actual)) {
      throw new AssertionError(format("Expecting [%s] to be [%s]. It was [%s]", what, expected, actual));
    }
    return this;
  }

  private Should assertContains(String actual, String expected) {
    if (negate == actual.contains(expected)) {
      throw new AssertionError(format("Expecting [%s] to contain [%s]", actual, expected));
    }
    return this;
  }

  private Should assertEmpty(String actual) {
    if (negate == actual.isEmpty()) {
      throw new AssertionError(format("Expecting [%s] to be empty", actual));
    }
    return this;
  }
}
