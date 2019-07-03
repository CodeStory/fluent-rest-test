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


import okhttp3.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class RestResponse {
  private final MemoryCookieJar cookieJar;
  private final okhttp3.Response response;

  private String bodyAsString;

  private RestResponse(MemoryCookieJar cookieJar, okhttp3.Response response) {
    this.cookieJar = cookieJar;
    this.response = response;
  }

  static RestResponse call(String url, Function<OkHttpClient.Builder, OkHttpClient.Builder> configureClient, Function<Request.Builder, Request.Builder> configureRequest) throws IOException {
    MemoryCookieJar cookieJar = new MemoryCookieJar();
    Request.Builder request = configureRequest.apply(new Request.Builder().url(url));
    OkHttpClient.Builder client = configureClient.apply(new OkHttpClient.Builder().cookieJar(cookieJar));

    OkHttpClient okHttpClient = client.build();
    okhttp3.Response response = okHttpClient.newCall(request.build()).execute();

    return new RestResponse(cookieJar, response);
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
    List<Cookie> cookies = cookieJar.map.values().stream().flatMap(List::stream).collect(Collectors.toList());
    return cookies.stream().filter(cookie -> cookie.name().equals(name)).findFirst().map(Cookie::value).orElse(null);
  }

  static class MemoryCookieJar implements CookieJar {
    Map<HttpUrl, List<Cookie>> map = Collections.synchronizedMap(new HashMap<>());
    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
      map.put(httpUrl, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
      return map.getOrDefault(httpUrl, new ArrayList<>());
    }
  }
}
