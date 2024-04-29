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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
	private final String contentType;
	private final String content;
	private final int code;
	private final Map<String, List<String>> headers;

	public Response(String contentType, String content, int code) {
		this(contentType, content, code, new HashMap<>());
	}

	public Response(String contentType, String content, int code, Map<String, List<String>> headers) {
		this.contentType = contentType;
		this.content = content;
		this.code = code;
		this.headers = headers;
	}

	public String contentType() {
		return contentType;
	}

	public String content() {
		return content;
	}

	public int code() {
		return code;
	}

	public List<String> header(String headerName) {
		return headers.get(headerName);
	}
}
