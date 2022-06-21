import React from "react";
import { auth, provider } from "./firebase";
import "./Dashboard.css";
import { signInWithPopup } from "firebase/auth";
import { useForm } from "react-hook-form";

function Login() {
    const [user, idToken] = useAuthState(auth);
    const { register, handleSubmit } = useForm();
    const [message, setMessage] = useState([]);
    const [wordcount, setWordCount] = useState([]);

    React.useEffect(() => initDashboard(), [])

    const signOut = (e) => {
      e.preventDefault();
      auth.signOut();
    };

    const initDashboard = (e) =>{
        const promise1 = new Promise((resolve, reject) => { resolve(getMessage()); });
        const promise2 = new Promise((resolve, reject) => { resolve(getDashboard()); });

        Promise.all([promise1, promise2]).then((values) => {
            setMessage(values[0]);
            setWordCount(values[0]);
        });
    }
  
    const getMessage = (e) =>{
        axios({
            url: "http://localhost/api/message",
            method: "GET",
            headers: {
              Authorization: "Bearer " + idToken,
            },
          })
            .then((res) => {
              setData(res.data);
              setStatus(res.status);
            })
            .catch((error) => {
              if (error.response) {
                setData(error.response.data.message);
                setStatus(error.response.data.code);
              }
            });
    }
  
    const sendMessage = (e) => {
        axios({
            url: process.env.NEXT_PUBLIC_MIDDLEWARE_URL + url,
            method: "GET",
            headers: {
              Authorization: "Bearer " + idToken,
            },
          })
            .then((res) => {
              setData(res.data);
              setStatus(res.status);
            })
            .catch((error) => {
              if (error.response) {
                setData(error.response.data.message);
                setStatus(error.response.data.code);
              }
            });
    }
    
    const getDashboard = (e) =>{
        fetch("https://api.example.com/items")
        .then(res => res.json())
        .then(
            (result) => {
                setMessage(result);
            },
            (error) => { console.log("error getting message.")}
        )
    }
  
  return (
    <div className="dashboard">
        
        <form onSubmit={handleSubmit(sendMessage)}>
            {/* include validation with required or other standard HTML validation rules */}
            <input {...register("exampleRequired", { required: true })} />
            {/* errors will return when field validation fails  */}
            {errors.exampleRequired && <span>This field is required</span>}

            <input type="submit" />
        </form>

        <div>
            {
                wordcount.map( (v, i) => {
                <div key={i}> 
                <input value={v} onChange={e => setInput(i, e.target.value)} />
                </div>
                })
            }
        </div>

        <div >
            {
                message.map( (v, i) => {
                <div key={i}> 
                <input value={v} onChange={e => setInput(i, e.target.value)} />
                </div>
                })
            }
        </div>
    </div>
  );
}

function 

export default Dashboard;