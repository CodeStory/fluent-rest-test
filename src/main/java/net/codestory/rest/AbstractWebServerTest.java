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

import static com.squareup.okhttp.Request.Builder;

public abstract class AbstractWebServerTest {
  // GET
  protected RestAssert get(String path) {
    return new RestAssert(path);
  }

  public class RestAssert {
    private final Response response;

    private RestAssert(String path) {
      try {
        Request request = new Builder().url("http://localhost:" + getPort() + path).build();

        OkHttpClient client = new OkHttpClient();

        response = client.newCall(request).execute();
      } catch (IOException e) {
        throw new AssertionError("Unable to query: " + path, e);
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

  protected abstract int getPort();
}