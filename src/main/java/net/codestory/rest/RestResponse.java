/**
 * Copyright (C) 2013-2015 all@code-story.net
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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;
import java.util.function.Function;

import static java.net.CookiePolicy.ACCEPT_ALL;

class RestResponse {
  private final CookieManager cookieManager;
  private final Response response;

  private String bodyAsString;

  private RestResponse(CookieManager cookieManager, Response response) {
    this.cookieManager = cookieManager;
    this.response = response;
  }

  static RestResponse call(String url, Function<OkHttpClient, OkHttpClient> configureClient, Function<Request.Builder, Request.Builder> configureRequest) throws IOException {
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(ACCEPT_ALL);

    Request.Builder request = configureRequest.apply(new Request.Builder().url(url));
    OkHttpClient client = configureClient.apply(new OkHttpClient().setCookieHandler(cookieManager));

    Response response = client.newCall(request.build()).execute();

    return new RestResponse(cookieManager, response);
  }

  public int code() {
    return response.code();
  }

  public String bodyAsString() {
    if (bodyAsString == null) {
      try {
        bodyAsString = response.body().string();
      } catch (IOException e) {
        throw new RuntimeException("Unable to read response as String", e);
      }
    }

    return bodyAsString;
  }

  public String contentType() {
    return header("Content-Type");
  }

  public String header(String name) {
    return response.header(name);
  }

  public String cookie(String name) {
    List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
    return cookies.stream().filter(cookie -> cookie.getName().equals(name)).findFirst().map(cookie -> cookie.getValue()).orElse(null);
  }
}
