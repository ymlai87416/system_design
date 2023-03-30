import React, {useState} from "react";
import { auth, provider, getToken2, onMessageListener} from "./firebase";
import "./Dashboard.css";
import { useForm } from "react-hook-form";
import axios from 'axios';
import { useAuthState } from "react-firebase-hooks/auth";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Dashboard() {
    const { register, handleSubmit, formState: {errors} } = useForm();
    const [message, setMessage] = useState([]);
    const [wordcount, setWordCount] = useState([]);
    const [user, setUser] = useState("");
    const [idtoken, setIdtoken] = useState("");

    //messaging
    const [isTokenFound, setTokenFound] = useState(false);

    const baseUrl = "https://sd.ymlai87416.com/api/v1/"

    React.useEffect(() => {
      const fetchData = initDashboard;
      fetchData().catch(console.error);

    }, [])


    const initDashboard = async () =>{
      const token = await auth.currentUser.getIdToken();
      setIdtoken(token);
      const user = await getUser(token);
      if(user == null) {
        console.log("user empty"); 
        return;
      }
      setUser(user.data);

      getToken2(setTokenFound);

      onMessageListener().then(payload => {
        notify( payload.notification.title + " " + payload.notification.body);
        console.log(payload);
      }).catch(err => console.log('failed: ', err));

      const userId = user.data.id;

      const messageF = getMessage(token, userId);
      const wordcountF = getWordCount(token, userId);

      const messageR = await messageF
      setMessage(messageR.data)
      const wordcountR = await wordcountF
      setWordCount(wordcountR.data)
      
    }

    const getUser = (idToken) =>{
      const res = axios({
        url: baseUrl+"user",
        method: "GET",
        headers: {
          Authorization: "Bearer " + idToken,
        },
      })
      
      return res;
    }

    const getMessage = (idToken, userid) =>{
      const message = axios({
          url: baseUrl+"user/" + userid + "/messages",
          method: "GET",
          headers: {
            Authorization: "Bearer " + idToken,
          },
        })
        
        return message
    }

    const onSubmit = (data) => {
      console.log(data);
      sendMessage(idtoken, user.id, data.inputText)
        .then(() => console.log("Completed"))
    }
  
    const sendMessage = (idToken, userid, text) => {
      const result = axios({
        url: baseUrl+"user/" + userid + "/messages",
          method: "POST",
          headers: {
            Authorization: "Bearer " + idToken,
            'Content-Type' : "text/plain",
          },
          data: text,
        })

        return result
    }
    
    const getWordCount = (idToken, userid) =>{
      const result = axios({
        url: baseUrl+"user/" + userid + "/wordcount",
        method: "GET",
        headers: {
          Authorization: "Bearer " + idToken,
        },
      })
      
      return result
    }

  const messageList = message.map( (v) => {
      return <li  className="message" key={v.id}> 
        {v.body}
      </li>
      })

  const wordCountList=  wordcount.sort((a, b)=> b.count- a.count).slice(0, 20).map( (v) => {
    return <li key={v.id.word}> 
        {v.id.word + "-" + v.count}
      </li>
      })
  
  const notify = (message) => toast(message, {
    position: "top-right",
    autoClose: 5000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    });
    
  return (
    <div className="dashboard">
      <ToastContainer 
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover/>
        
      <form onSubmit={handleSubmit(onSubmit)}>
          {/* include validation with required or other standard HTML validation rules */}
          <textarea className="input__field" {...register("inputText", { required: true })} />
          {/* errors will return when field validation fails  */}
          {errors.inputText && <span>This field is required</span>}

          <input type="submit" />
      </form>

      <div>
        <h2>Top 20 words</h2>
        <ul class="wordcount">
          {wordCountList}
        </ul>
      </div>

      <div >
          <h2>List of message</h2>
          <ul>{messageList}</ul>
      </div>
    </div>
  );
}
 

export default Dashboard;