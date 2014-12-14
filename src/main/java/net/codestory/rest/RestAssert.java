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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

import static java.util.function.UnaryOperator.identity;

public class RestAssert {
  private final String url;
  private final UnaryOperator<OkHttpClient> configureClient;
  private final UnaryOperator<Request.Builder> configureRequest;

  RestAssert(String url) {
    this(url, identity(), identity());
  }

  private RestAssert(String url, UnaryOperator<OkHttpClient> configureClient, UnaryOperator<Request.Builder> configureRequest) {
    this.url = url;
    this.configureRequest = configureRequest;
    this.configureClient = configureClient;
  }

  // Configuration
  public RestAssert withHeader(String name, String value) {
    return withRequest(addHeader(name, value));
  }

  public RestAssert withPreemptiveAuthentication(String login, String password) {
    return withRequest(addBasicAuthHeader(login, password));
  }

  public RestAssert withAuthentication(String login, String password) {
    return withClient(setAuthenticator(new Authenticator() {
      AtomicInteger tries = new AtomicInteger(0);

      @Override
      public Request authenticate(Proxy proxy, Response response) {
        if (tries.getAndIncrement() > 0) {
          return null;
        }
        return addBasicAuthHeader(login, password).apply(response.request().newBuilder()).build();
      }

      @Override
      public Request authenticateProxy(Proxy proxy, Response response) {
        return null;
      }
    }));
  }

  RestAssert withRequest(UnaryOperator<Request.Builder> configure) {
    return new RestAssert(url, configureClient, request -> configure.apply(configureRequest.apply(request)));
  }

  RestAssert withClient(UnaryOperator<OkHttpClient> configure) {
    return new RestAssert(url, client -> configure.apply(configureClient.apply(client)), configureRequest);
  }

  // Assertions
  public Should should() {
    try {
      return new Should(RestResponse.call(url, configureClient, configureRequest));
    } catch (IOException e) {
      throw new RuntimeException("Unable to query: " + url, e);
    }
  }

  // Client modification
  private static UnaryOperator<OkHttpClient> setAuthenticator(Authenticator authenticator) {
    return client -> client.setAuthenticator(authenticator);
  }

  // Request configuration
  private static UnaryOperator<Request.Builder> addBasicAuthHeader(String login, String password) {
    return addHeader("Authorization", Credentials.basic(login, password));
  }

  private static UnaryOperator<Request.Builder> addHeader(String name, String value) {
    return request -> request.addHeader(name, value);
  }
}
