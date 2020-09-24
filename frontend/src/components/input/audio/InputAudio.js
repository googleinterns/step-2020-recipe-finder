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
            audio: [],
            transcript: '',
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
          this.getByteArray(file).then((byteArray) => {
            console.log(byteArray);
            const request = new Request("/api/speech-to-text", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            body: JSON.stringify(byteArray),
            });
            fetch(request)
            //   .then(handleResponseError)
            .then((response) => response.json())
            .then((json) =>
                this.setState({
                isRecording: false,
                audio: byteArray,
                transcript: json,
                })
            )
            .catch((error) => this.setState({ error: error }));
          }); 
          });
    }
    
    getByteArray(file) {
        let fileReader = new FileReader();
        return new Promise(function(resolve, reject) {
            fileReader.readAsArrayBuffer(file);
            fileReader.onload = function(ev) {
                const array = new Int8Array(ev.target.result);
                const fileByteArray = [];
                for (let i = 0; i < array.length; i++) {
                    fileByteArray.push(array[i]);
                }
                resolve(array);  // successful
            }
            fileReader.onerror = reject; // call reject if error
        })
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
            <p>{this.state.transcript}</p>
          </div>
        );
      }
    }
export default InputAudio;
