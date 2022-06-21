import logo from './logo.svg';
import './App.css';
import { auth, provider } from "./firebase";
import Login from "./Login";
import { useAuthState } from "react-firebase-hooks/auth";
import { useForm } from "react-hook-form";

function App() {

  const [user] = useAuthState(auth);

  const signOut = (e) => {
    e.preventDefault();
    auth.signOut();
  };

  return (
    <div className="App">
      <header className="App-header">
        <div>
          {user ? (
            <div>
              <h1>Hello, {user.displayName}</h1>
              <h1>You are signed in as {user.email}</h1>
              <button onClick={signOut}>Sign Out</button>
              
              <Dashboard />
            </div>
          ) : (
            <Login />
          )}
        </div>
      </header>
    </div>
  );
}

export default App;
