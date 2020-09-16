/* Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

import React, {Component} from 'react';
import '../text/InputText.css';
import MicRecorder from 'mic-recorder-to-mp3';


class InputAudio extends Component {
    constructor(properties) {
        super(properties);
        this.state = {
            isRecording: false,
            recorder: '',
            blobURL: '',
            isBlocked: false,
        };
        

    }
    start = () => {
          const Mp3Recorder = new MicRecorder({ bitRate: 128 });
          if (this.state.isBlocked) {
            console.log('Permission Denied');
          } else {
            Mp3Recorder
              .start()
              .then(() => {
                this.setState({ isRecording: true });
                this.setState({ recorder: Mp3Recorder });
              }).catch((e) => console.error(e));
          }
        };
    
    stop = () => {
        console.log(this.state.recorder);
        this.state.recorder
          .stop()
          .getMp3()
          .then(([buffer, blob]) => {
            const file = new File(buffer, 'ingredients.mp3', {
                type: blob.type,
                lastModified: Date.now()
            });
          const blobURL = URL.createObjectURL(file)
          localStorage.setItem("file", blobURL);
          this.setState({ blobURL, isRecording: false });
          }).catch((e) => console.log(e));
    };
    transcribe = () => {
    const fs = require('fs');
    // Imports the Google Cloud client library
    const speech = require('@google-cloud/speech');
    // Creates a client
    const client = new speech.SpeechClient();
    /**
    * TODO(developer): Uncomment the following lines before running the sample.
    */
    const filename = this.state.blobURL;
    const encoding = 'LINEAR16';
    const sampleRateHertz = 16000;
    const languageCode = 'en-US';

    const request = {
    config: {
        encoding: encoding,
        sampleRateHertz: sampleRateHertz,
        languageCode: languageCode,
    },
    interimResults: false, // If you want interim results, set this to true
    };

    // Stream the audio to the Google Cloud Speech API
    const recognizeStream = client
    .streamingRecognize(request)
    .on('error', console.error)
    .on('data', data => {
        localStorage.setItem("transcript", data.results[0].alternatives[0].transcript)
        console.log(
        `Transcription: ${data.results[0].alternatives[0].transcript}`
        );
    });

    // Stream an audio file from disk to the Speech API, e.g. "./resources/audio.raw"
    fs.createReadStream(filename).pipe(recognizeStream);
    }

    componentDidMount() {
      navigator.getUserMedia({ audio: true },
        () => {
            console.log('Permission Granted');
            this.setState({ isBlocked: false });
        },
        () => {
            console.log('Permission Denied');
            this.setState({ isBlocked: true })
        },
      );
    }
    render() {        
        return (
          <div className="audio">
            <button onClick={this.start} disabled={this.state.isRecording}>
            Record
            </button>
            <button onClick={this.stop} disabled={!this.state.isRecording}>
            Stop
            </button>
            <button onClick={this.transcribe} disabled={this.state.isRecording}>
            Transcribe
            </button>
            <audio src={this.state.blobURL} controls="controls" />
            <a href={localStorage.getItem("file")} download="audioFile">download</a>
            <p>{localStorage.getItem("transcript")}</p>
          </div>
        );
      }
    }
export default InputAudio;