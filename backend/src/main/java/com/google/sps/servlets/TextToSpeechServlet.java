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
import com.google.protobuf.ByteString;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

/* Servlet that:
 * in Post request, returns an audio for the text in the request */
@WebServlet("/api/text-to-speech")
public class TextToSpeechServlet extends AuthenticationServlet {

  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getReader().readLine();
    // Instantiates a client
    try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
      // Set the text input to be synthesized
      SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

      // Build the voice request, select the language code ("en-US") and the ssml voice gender
      // ("neutral")
      VoiceSelectionParams voice =
          VoiceSelectionParams.newBuilder()
              .setLanguageCode("en-US")
              .setSsmlGender(SsmlVoiceGender.NEUTRAL)
              .build();

      // Select the type of audio file you want returned
      AudioConfig audioConfig =
          AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

      // Perform the text-to-speech request on the text input with the selected voice parameters and
      // audio file type
      SynthesizeSpeechResponse speechResponse =
          textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

      // Get the audio contents from the response
      ByteString audioContents = speechResponse.getAudioContent();

      // Write the response to the output file.
      try  {
        byte[] audio = audioContents.toByteArray();
        ServletOutputStream stream = null;         
        try{                   
            stream = response.getOutputStream();             
            response.setContentType("audio/mp3");  
            stream.write(audio);         
            } catch (IOException ioe){                  
                throw new ServletException(ioe.getMessage());                   
                } finally {                  
                if(stream != null)                 
                stream.close();             
                }       
            }


      }
    }
  }

  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response)
      throws IOException {}
}
