import React from "react";
import { auth, provider } from "./firebase";
import "./Login.css";
import { signInWithPopup } from "firebase/auth";

function Login() {
  const signIn = () => {
    signInWithPopup(auth, provider).catch((error) => alert(error.message));
  };
  
  return (
    <div className="login">
      <button onClick={signIn}>Sign In</button>
    </div>
  );
}

export default Login;