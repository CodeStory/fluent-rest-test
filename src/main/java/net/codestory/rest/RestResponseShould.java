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

class RestResponseShould implements Should {
  private final RestResponse response;
  private final boolean negate;

  RestResponseShould(RestResponse response, boolean negate) {
    this.response = response;
    this.negate = negate;
  }

  // Modifiers

  @Override
  public RestResponseShould not() {
    return new RestResponseShould(response, !negate);
  }

  // Verifications

  // TODO: shouldn't be able to write should().should()
  @Override
  public RestResponseShould should() {
    return new RestResponseShould(response, false);
  }

  @Override
  public RestResponseShould respond(int statusCode) {
    return assertEquals("status code", response.code(), statusCode);
  }

  @Override
  public RestResponseShould succeed() {
      return assertBetween("status code", response.code(), 200, 299);
  }

  @Override
  public RestResponseShould fail() {
      return assertBetween("status code", response.code(), 400, 599);
  }

  @Override
  public RestResponseShould contain(String content) {
    return assertContains(response.bodyAsString(), content);
  }

  @Override
  public RestResponseShould beEmpty() {
    return assertEmpty(response.bodyAsString());
  }

  @Override
  public RestResponseShould haveType(String contentType) {
    return assertContains(response.contentType(), contentType);
  }

  @Override
  public RestResponseShould haveCookie(String name, String value) {
    return assertEquals("cookie " + name, response.cookie(name), value);
  }

  @Override
  public RestResponseShould haveHeader(String name, String value) {
    return assertEquals("header " + name, response.header(name), value);
  }

  // Verifications
  private RestResponseShould assertEquals(String what, Object actual, Object expected) {
    if (negate == Objects.equals(expected, actual)) {
      throw new AssertionError(format("Expecting [%s] to be [%s]. It was [%s]", what, expected, actual));
    }
    return this;
  }

  private RestResponseShould assertContains(String actual, String expected) {
    if (negate == actual.contains(expected)) {
      throw new AssertionError(format("Expecting [%s] to contain [%s]", actual, expected));
    }
    return this;
  }

  private RestResponseShould assertEmpty(String actual) {
    if (negate == actual.isEmpty()) {
      throw new AssertionError(format("Expecting [%s] to be empty", actual));
    }
    return this;
  }

  private RestResponseShould assertBetween(String what, int actual, int lowerBound, int higherBound) {
    if (negate == ((actual >= lowerBound) && (actual <= higherBound))) {
        throw new AssertionError(format("Expecting [%s] to be between [%d] and [%d]. It was [%s]", what, lowerBound, higherBound, actual));
    }
    return this;
  }
}
