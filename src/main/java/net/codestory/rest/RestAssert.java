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

import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.function.Function.identity;

public class RestAssert {
  private final String url;
  private final Function<OkHttpClient, OkHttpClient> configureClient;
  private final Function<Request.Builder, Request.Builder> configureRequest;

  private RestResponse response;

  RestAssert(String url) {
    this(url, identity(), identity());
  }

  RestAssert(String url, Function<Request.Builder, Request.Builder> configureRequest) {
    this(url, identity(), configureRequest);
  }

  private RestAssert(String url, Function<OkHttpClient, OkHttpClient> configureClient, Function<Request.Builder, Request.Builder> configureRequest) {
    this.url = url;
    this.configureRequest = configureRequest;
    this.configureClient = configureClient;
  }

  // Configuration
  public RestAssert withHeader(String name, String value) {
    return withRequest(request -> request.addHeader(name, value));
  }

  public RestAssert withPreemptiveAuthentication(String login, String password) {
    return withRequest(request -> request.addHeader("Authorization", Credentials.basic(login, password)));
  }

  public RestAssert withAuthentication(String login, String password) {
    return withClient(client -> client.setAuthenticator(new Authenticator() {
      AtomicInteger tries = new AtomicInteger(0);

      @Override
      public Request authenticate(Proxy proxy, Response response) throws IOException {
        if (tries.getAndIncrement() > 0) {
          return null;
        }
        String credential = Credentials.basic(login, password);
        return response.request().newBuilder().header("Authorization", credential).build();
      }

      @Override
      public Request authenticateProxy(Proxy proxy, Response response) {
        return null;
      }
    }));
  }

  private RestAssert withRequest(Function<Request.Builder, Request.Builder> configure) {
    return new RestAssert(url, configureClient, configureRequest.andThen(configure));
  }

  private RestAssert withClient(Function<OkHttpClient, OkHttpClient> configure) {
    return new RestAssert(url, configureClient.andThen(configure), configureRequest);
  }

  // Assertions
  public RestAssert produces(int statusCode) {
    return assertEquals(statusCode, response().code());
  }

  public RestAssert produces(String content) {
    return assertEquals(content, response().bodyAsString());
  }

  private RestAssert producesContentType(String contentType) {
    return assertEquals(contentType, response().contentType());
  }

  public RestAssert produces(String contentType, String content) {
    return producesContentType(contentType).produces(content);
  }

  public RestAssert produces(int statusCode, String content) {
    return produces(statusCode).produces(content);
  }

  public RestAssert produces(int statusCode, String contentType, String content) {
    return produces(statusCode).produces(contentType, content);
  }

  public RestAssert producesCookie(String name, String value) {
    return assertEquals(value, response().getCookie(name));
  }

  private RestResponse response() {
    if (response == null) {
      response = RestResponse.call(url, configureClient, configureRequest);
    }
    return response;
  }

  // Verifications
  private RestAssert assertEquals(Object expectedValue, Object actualValue) {
    if (!Objects.equals(expectedValue, actualValue)) {
      throw new AssertionError(String.format("Expecting \"%s\" was \"%s\"", expectedValue, actualValue));
    }
    return this;
  }
}
