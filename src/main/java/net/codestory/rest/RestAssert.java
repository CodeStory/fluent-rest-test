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
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RestAssert {
  private final Response response;

  RestAssert(String url) {
    try {
      Request request = new Request.Builder().url(url).build();

      OkHttpClient client = new OkHttpClient();

      response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new AssertionError("Unable to query: " + url, e);
    }
  }

  // Assertions
  public void produces(String content) {
    try {
      if (!response.body().string().contains(content)) {
        throw new AssertionError("TODO");
      }
    } catch (IOException e) {
      throw new AssertionError("Unable to read response as String", e);
    }
  }
}
