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

  Should(RestResponse response) {
    this.response = response;
  }

  public Should respond(int statusCode) {
    return assertEquals(response.code(), statusCode);
  }

  public Should contain(String content) {
    return assertContains(response.bodyAsString(), content);
  }

  public Should haveType(String contentType) {
    return assertContains(response.contentType(), contentType);
  }

  public Should haveCookie(String name, String value) {
    return assertEquals(response.cookie(name), value);
  }

  public Should haveHeader(String name, String value) {
    return assertEquals(response.header(name), value);
  }

  // Verifications
  private Should assertEquals(Object actual, Object expected) {
    if (!Objects.equals(expected, actual)) {
      throw new AssertionError(format("Expecting \"%s\" was \"%s\"", expected, actual));
    }
    return this;
  }

  private Should assertContains(String actual, String expected) {
    if (!actual.contains(expected)) {
      throw new AssertionError(format("Expecting \"%s\" to contain \"%s\"", actual, expected));
    }
    return this;
  }
}
