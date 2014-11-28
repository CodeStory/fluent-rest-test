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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class RestAssert {
  private final RestResponse response;

  RestAssert(String url) {
    Request request = new Request.Builder().url(url).build();
    OkHttpClient client = new OkHttpClient();

    try {
      response = new RestResponse(client.newCall(request).execute());
    } catch (IOException e) {
      throw new RuntimeException("Unable to query: " + url, e);
    }
  }

  public RestAssert produces(int statusCode) {
    if (response.code() != statusCode) {
      throw new AssertionError(response.code());
    }
    return this;
  }

  public RestAssert produces(String content) {
    String actualContent = response.bodyAsString();
    if (!actualContent.equals(content)) {
      throw new AssertionError(actualContent);
    }
    return this;
  }

  public RestAssert produces(String contentType, String content) {
    String actualContentType = response.contentType();
    if (!actualContentType.equals(contentType)) {
      throw new AssertionError(actualContentType);
    }
    produces(content);
    return this;
  }

  public RestAssert produces(int statusCode, String content) {
    produces(statusCode);
    produces(content);
    return this;
  }

  public RestAssert produces(int statusCode, String contentType, String content) {
    produces(statusCode);
    produces(contentType, content);
    return this;
  }
}
