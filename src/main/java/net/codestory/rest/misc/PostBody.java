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
package net.codestory.rest.misc;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

public class PostBody {
  private PostBody() {
    // Static class
  }

  public static RequestBody none() {
    return null;
  }

  public static RequestBody json(String body) {
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
  }

  public static RequestBody form(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
    FormEncodingBuilder form = new FormEncodingBuilder();

    form.add(firstParameterName, firstParameterValue.toString());
    for (int i = 0; i < parameterNameValuePairs.length; i += 2) {
      form.add(parameterNameValuePairs[i].toString(), parameterNameValuePairs[i + 1].toString());
    }

    return form.build();
  }
}
