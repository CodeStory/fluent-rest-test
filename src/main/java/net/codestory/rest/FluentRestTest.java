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

import net.codestory.rest.misc.PostBody;

public interface FluentRestTest {
  int port();

  default String baseUrl() {
    return "http://localhost:" + port();
  }

  default RestAssert get(String path) {
    return new RestAssert(baseUrl() + path);
  }

  default RestAssert delete(String path) {
    return get(path).withRequest(request -> request.delete());
  }

  default RestAssert post(String path) {
    return get(path).withRequest(request -> request.post(PostBody.none()));
  }

  default RestAssert post(String path, String body) {
    return get(path).withRequest(request -> request.post(PostBody.json(body)));
  }

  default RestAssert post(String path, String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
    return get(path).withRequest(request -> request.post(PostBody.form(firstParameterName, firstParameterValue, parameterNameValuePairs)));
  }
}