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
import java.util.function.Function;

import static java.util.function.Function.identity;

public class RestAssert {
  private final String url;
  private final Function<OkHttpClient, OkHttpClient> configureClient;
  private final Function<Request.Builder, Request.Builder> configureRequest;

  RestAssert(String url) {
    this(url, identity(), identity());
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
    return withRequest(request -> addBasicAuthHeader(request, login, password));
  }

  public RestAssert withAuthentication(String login, String password) {
    return withClient(client -> client.setAuthenticator(new Authenticator() {
      AtomicInteger tries = new AtomicInteger(0);

      @Override
      public Request authenticate(Proxy proxy, Response response) {
        if (tries.getAndIncrement() > 0) {
          return null;
        }
        return addBasicAuthHeader(response.request().newBuilder(), login, password).build();
      }

      @Override
      public Request authenticateProxy(Proxy proxy, Response response) {
        return null;
      }
    }));
  }

  RestAssert withRequest(Function<Request.Builder, Request.Builder> configure) {
    return new RestAssert(url, configureClient, configureRequest.andThen(configure));
  }

  RestAssert withClient(Function<OkHttpClient, OkHttpClient> configure) {
    return new RestAssert(url, configureClient.andThen(configure), configureRequest);
  }

  // Assertions
  public Should should() {
    try {
      return new Should(RestResponse.call(url, configureClient, configureRequest));
    } catch (IOException e) {
      throw new RuntimeException("Unable to query: " + url, e);
    }
  }

  // Internal
  private static Request.Builder addBasicAuthHeader(Request.Builder request, String login, String password) {
    return request.addHeader("Authorization", Credentials.basic(login, password));
  }
}
