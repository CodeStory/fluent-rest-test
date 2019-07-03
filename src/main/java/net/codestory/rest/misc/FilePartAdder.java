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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;
import java.io.InputStream;

public class FilePartAdder implements PartAdder {
    private final String name;
    private final String fileName;
    private final String mediaTypeString;
    private InputStream inputStream;

    public FilePartAdder(String name, String fileName, String mediaTypeString, InputStream inputStream) {
        this.name = name;
        this.fileName = fileName;
        this.mediaTypeString = mediaTypeString;
        this.inputStream = inputStream;
    }

    @Override
    public void addPartTo(MultipartBody.Builder multipartBuilder) {
        multipartBuilder.addFormDataPart(name, fileName, new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse(mediaTypeString);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                int nbBytesRead;
                for (byte[] buffer = new byte[4096]; (nbBytesRead = inputStream.read(buffer)) > 0; ) {
                    sink.write(buffer, 0, nbBytesRead);
                }
            }
        });
    }
}
