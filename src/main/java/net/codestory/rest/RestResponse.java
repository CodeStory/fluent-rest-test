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

import com.squareup.okhttp.Response;

import java.io.IOException;

class RestResponse {
  private final Response response;

  private String bodyAsString;

  RestResponse(Response response) {
    this.response = response;
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
    return response.header("Content-Type");
  }
}
