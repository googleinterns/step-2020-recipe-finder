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

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/* Servlet that:
 * in Post request, returns an audio for the text in the request */
@WebServlet("/api/text-to-speech")
public class TextToSpeechServlet extends AuthenticationServlet {

  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getReader().readLine();
    try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {

      SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

      VoiceSelectionParams voice =
          VoiceSelectionParams.newBuilder()
              .setLanguageCode("en-US")
              .setSsmlGender(SsmlVoiceGender.NEUTRAL)
              .build();

      AudioConfig audioConfig =
          AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

      SynthesizeSpeechResponse speechResponse =
          textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

      // Get the audio contents from the response
      ByteString audioContents = speechResponse.getAudioContent();

      byte[] audio = audioContents.toByteArray();
      System.out.println(Arrays.toString(audio));
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(audio));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }
}
