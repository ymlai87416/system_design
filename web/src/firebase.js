// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, GoogleAuthProvider } from "firebase/auth";
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

export { auth, provider };