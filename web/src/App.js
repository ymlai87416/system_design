import logo from './logo.svg';
import './App.css';
import { auth, provider } from "./firebase";
import Login from "./Login";
import Dashboard from "./Dashboard";
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
              <div class="flex-parent-element">
                <div class="flex-child-element magenta">
                  <h3>Hello, {user.displayName}</h3>
                  <h4>You are signed in as {user.email}</h4>
                </div>
                <div class="flex-child-element green">
                  <button class="logout" onClick={signOut}>Sign Out</button></div>
              </div>
              
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
