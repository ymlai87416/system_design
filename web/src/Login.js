import React from "react";
import { auth, provider } from "./firebase";
import "./Login.css";
import { signInWithPopup } from "firebase/auth";

function Login() {
  const signIn = async () => {
    try{
      const {credential} = await signInWithPopup(auth, provider);
    }catch(error) {
      alert(error.message)
    }
  };
  
  return (
    <div> 
      <div> 
      <p>This is a stupid project to conslidate my knowledge on system design. Better turn back now.</p>
      <p>If you are interested, you can check </p> 
      <a href="https://github.com/ymlai87416/system_design">Here</a>
      </div>
      <div className="login">
      
      <button onClick={signIn}>Sign In</button>
    </div>
    </div>
    
  );
}

export default Login;