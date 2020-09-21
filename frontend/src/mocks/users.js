const users = {
  notLoggedIn: { isFirstTime: false, isLoggedIn: false, logUrl: "url" },
  firstTimer: { isFirstTime: true, isLoggedIn: true, logUrl: "url" },
  loggedIn: { isFirstTime: false, isLoggedIn: true, logUrl: "url" },
};

function login(user) {
  if (users[user]) {
    return users[user];
  }
  const error = new Error("Internal Server Error");
  error.status = 500;
  throw error;
}
export { login };
