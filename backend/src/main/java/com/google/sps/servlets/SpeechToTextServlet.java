// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that:
 * in Post request, returns byte array of the audio for the text in the request */
@WebServlet("/api/speech-to-text")
public class SpeechToTextServlet extends AuthenticationServlet {

  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }
  
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {

        // The path to the audio file to transcribe
        String audioString = request.getReader().lines().collect(Collectors.joining());
        JsonObject audioJson = JsonParser.parseString(audioString).getAsJsonObject();
        byte[] data = new byte[audioJson.size()];
          for (int i = 0; i < audioJson.size(); i++) {
          data[i] = Byte.parseByte(audioJson.get(Integer.toString(i)).getAsString());
          }

        // Reads the audio file into memory

        ByteString audioBytes = ByteString.copyFrom(data);
        System.out.println("testing");
        System.out.println(audioBytes);
        // Builds the sync recognize request
        RecognitionConfig config =
            RecognitionConfig.newBuilder()
                .setEncoding(AudioEncoding.LINEAR16)
                .setSampleRateHertz(48000)
                .setLanguageCode("en-US")
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();
        String transcripts = "";
        // Performs speech recognition on the audio file
        RecognizeResponse speechResponse = speechClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = speechResponse.getResultsList();
        System.out.println("part 2");
        System.out.println(results);

        for (SpeechRecognitionResult result : results) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcription: %s%n", alternative.getTranscript());
            transcripts += alternative.getTranscript();
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(transcripts));
        }
    }
}
  