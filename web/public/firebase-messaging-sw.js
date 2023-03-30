// Scripts for firebase and firebase messaging
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

const firebaseConfig = {
    apiKey: "AIzaSyCH4DFHSGA9pgdK0zvTBC8rzSHqza90eIY",
    authDomain: "system-design-a685c.firebaseapp.com",
    projectId: "system-design-a685c",
    storageBucket: "system-design-a685c.appspot.com",
    messagingSenderId: "335087282552",
    appId: "1:335087282552:web:1d164535ad28f433cb008d",
    measurementId: "G-SZNR9Y56JX"
};

firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
const messaging = firebase.messaging();
  
messaging.onBackgroundMessage(function(payload) {
    console.log('Received background message ', payload);
  
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
      body: payload.notification.body,
    };
  
    self.registration.showNotification(notificationTitle,
      notificationOptions);
  });