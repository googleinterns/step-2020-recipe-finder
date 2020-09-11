import Modal from "react-bootstrap/Modal";
import Carousel from "react-bootstrap/Carousel";
import navigateNext from "../../icons/navigate_next.svg";
import navigatePrevious from "../../icons/navigate_previous.svg";
import signup from "../../walkthrough-images/sign-up.png";
import inputtext from "../../walkthrough-images/input-text.png";
import scanning from "../../walkthrough-images/scanning.png";
import recommended from "../../walkthrough-images/recommended.png";
import tutorial from "../../walkthrough-images/tutorial.png";
import favourites from "../../walkthrough-images/favourites.png";
import history from "../../walkthrough-images/history.png";
import home from "../../walkthrough-images/home.png";
import done from "../../walkthrough-images/done.png";
import Button from "react-bootstrap/Button";
import React, { Component } from "react";

class Walkthrough extends Component {
  render() {
    return (
      <Modal
        show={this.props.showModal}
        className="walkthrough-carousel"
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header>
          <Modal.Title className="w-100">
            Here's how Recipe Finder works:
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Carousel
            interval={2000}
            nextIcon={<img src={navigateNext} alt="next step" />}
            prevIcon={<img src={navigatePrevious} alt="previous step" />}
          >
            {this.getWalkthrough().map((step, i) => (
              <Carousel.Item key={i} className="walkthrough-step">
                <img src={step.image} alt="" className="walkthrough-image" />
                <p className="walkthrough-text">{step.text}</p>
              </Carousel.Item>
            ))}
          </Carousel>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={this.props.handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  getWalkthrough() {
    return [
      {
        text:
          "1. Login with your Google account and enter your name with dietary requirements",
        image: signup,
      },
      { text: "2. Enter your ingredients", image: inputtext },
      {
        text: "3. Wait for us to look for suitable recipes for you",
        image: scanning,
      },
      { text: "4. Select a recipe you want to cook", image: recommended },
      { text: "5. Cook with recipe steps read out to you", image: tutorial },
      {
        text:
          "6. Enjoy eating your masterpiece and don't forget to save it to favourites if you liked it!",
        image: done,
      },
      {
        text: "You can view recipes that you've cooked in the past ..",
        image: favourites,
      },
      {
        text: ".. and view your favourite recipes in your account",
        image: history,
      },
      { text: "Welcome to Recipe Finder!", image: home },
    ];
  }
}
export default Walkthrough;
