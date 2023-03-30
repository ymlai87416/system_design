// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, GoogleAuthProvider } from "firebase/auth";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCH4DFHSGA9pgdK0zvTBC8rzSHqza90eIY",
  authDomain: "system-design-a685c.firebaseapp.com",
  projectId: "system-design-a685c",
  storageBucket: "system-design-a685c.appspot.com",
  messagingSenderId: "335087282552",
  appId: "1:335087282552:web:1d164535ad28f433cb008d",
  measurementId: "G-SZNR9Y56JX"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

const auth = getAuth();
const provider = new GoogleAuthProvider();
const messaging = getMessaging(app);

const getToken2 = (setTokenFound) => {
  return getToken(messaging, {vapidKey: 'BM1cqbr-f5bKcAzAJIPk7G08b13TkI7S-0b1jfWMW_5P43cK__BJtyNYUwxyTY9LtrJ7BNmjtdt9aK0B2f2gS0M'}).then((currentToken) => {
    if (currentToken) {
      console.log('current token for client: ', currentToken);
      setTokenFound(true);
      // Track the token -> client mapping, by sending to backend server
      // show on the UI that permission is secured
    } else {
      console.log('No registration token available. Request permission to generate one.');
      setTokenFound(false);
      // shows on the UI that permission is required 
    }
  }).catch((err) => {
    console.log('An error occurred while retrieving token. ', err);
    // catch error while creating client token
  });
}

const onMessageListener = () =>
  new Promise((resolve) => {
    onMessage(messaging, (payload) => {
      resolve(payload);
    });
});

export { auth, provider, analytics, messaging, getToken2, onMessageListener};