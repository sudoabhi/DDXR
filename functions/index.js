'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const eventdiscussions = admin.initializeApp({
       databaseURL: "https://eventdiscussions.firebaseio.com"
       }, 'eventdiscussions');

const studentdiscussions=admin.initializeApp({
databaseURL: "https://ddrx-172d3-2a5f7.firebaseio.com/"
},'ddrx-172d3-2a5f7');


const eventadditions=admin.initializeApp({
databaseURL: "https://ddrx-events-172d3.firebaseio.com/"
},'ddrx-events-172d3');

const adminDatabase=admin.initializeApp({
databaseURL: "https://ddrx-admin-172d3.firebaseio.com/"},'ddrx-admin-172d3');


   //Comments to posts ( eventsdiscussions)
    exports.SubscribeUserToHisPost= functions.database.instance('eventdiscussions').ref('/{EventKey}/{PostKey}/Comments/{CommentKey}').onCreate((snapshot,context) => {

       const commenter=snapshot.val();
       var db = admin.database();
       var commenterUID=commenter.UserUID;
        var UploadedBy;
       var commenterName=commenter.UserName;


       return snapshot.ref.parent.parent.once('value').then(function(snapshot){

        const managerUser = snapshot.val();
        console.log(managerUser);
       UploadedBy=managerUser.UploadedBy;

       console.log("Hello");



        return admin.database().ref('/' +UploadedBy).once('value').then(function(snapshot){

        console.log(JSON.stringify(snapshot.val()));

        console.log(UploadedBy);

        const managerUser2= snapshot.val();
        console.log(managerUser2);
        var token=managerUser2.Token;

         var message;
         if(UploadedBy===commenterUID){
         message = "You commented on your post";
         }
         else{
          message= commenterName+" commented on your post";
         }
         var titledisplay="Someone Commented";
         var payload = {
         notification: {
         title: titledisplay,
         body: message,
         sound: "default"
          }
          };

      return admin.messaging().sendToDevice(token, payload)
        .then(function (response) {
        console.log("Successfully sent message:", response);
         console.log(response.results[0].error);
          })
         .catch(function (error) {
         console.log("Error sending message:", error);
         });

});
});
});


   //Likes to posts(eventdiscussions)
 exports.LikeUsersPost= functions.database.instance('eventdiscussions').ref('/{EventKey}/{PostKey}/LikeStatus/{UserUID}').onUpdate((change,context) => {



      const before=change.before.val();
      const UserUIDX=context.params.UserUID;
      var UploadedBy;
      const after=change.after.val();
      if(before===after||after==='false'){
          console.log("Nothing happened");
          return null;
      }
      else{


             var afterX=after;
             var beforeX=before;


              return change.before.ref.parent.parent.once('value').then(function(snapshot){

                     const managerUser = snapshot.val();
                     console.log(managerUser);
                    UploadedBy=managerUser.UploadedBy;

                    console.log(UserUIDX);


              return admin.database().ref('/' +UserUIDX).once('value').then(function(snapshot){


                   const Commenter=snapshot.val();
                   var commenterName=Commenter.UserName;












              return admin.database().ref('/' +UploadedBy).once('value').then(function(snapshot){

                     console.log(JSON.stringify(snapshot.val()));

                     console.log(UploadedBy);

                     const managerUser2= snapshot.val();
                     console.log(managerUser2);
                     var token=managerUser2.Token;

                      var message;
                      if(UploadedBy===UserUIDX){
                      message = "You liked your own post";
                      }
                      else{
                       message= commenterName+" liked your post";
                      }
                      var titledisplay="Your post is getting likes! Yeyeee!!";
                      var payload = {
                      notification: {
                      title: titledisplay,
                      body: message,
                      sound: "default"
                       }
                       };

                         return admin.messaging().sendToDevice(token, payload)
                               .then(function (response) {
                               console.log("Successfully sent message:", response);
                                console.log(response.results[0].error);
                                 })
                                .catch(function (error) {
                                console.log("Error sending message:", error);
                                });

      });

      });


});


}

});


   //Comments to posts ( studentdiscussions)
      exports.SubscribeStudentToHisPost= functions.database.instance('ddrx-172d3-2a5f7').ref('/{PostKey}/Comments/{CommentKey}').onCreate((snapshot,context) => {

         const commenter=snapshot.val();
         var db = admin.database();
         var commenterUID=commenter.UserUID;
          var UploadedBy;
         var commenterName=commenter.UserName;


         return snapshot.ref.parent.parent.once('value').then(function(snapshot){

          const managerUser = snapshot.val();
          console.log(managerUser);
         UploadedBy=managerUser.UploadedBy;

         console.log("Hello");



          return admin.database().ref('/' +UploadedBy).once('value').then(function(snapshot){

          console.log(JSON.stringify(snapshot.val()));

          console.log(UploadedBy);

          const managerUser2= snapshot.val();
          console.log(managerUser2);
          var token=managerUser2.Token;

           var message;
           if(UploadedBy===commenterUID){
           message = "You commented on your post";
           }
           else{
            message= commenterName+" commented on your post";
           }
           var titledisplay="Someone Commented";
           var payload = {
           notification: {
           title: titledisplay,
           body: message,
           sound: "default"
            }
            };

        return admin.messaging().sendToDevice(token, payload)
          .then(function (response) {
          console.log("Successfully sent message:", response);
           console.log(response.results[0].error);
            })
           .catch(function (error) {
           console.log("Error sending message:", error);
           });

  });
  });
  });


   //Likes to posts(studentdiscussions)
   exports.LikeStudentsPost= functions.database.instance('ddrx-172d3-2a5f7').ref('/{PostKey}/LikeStatus/{UserUID}').onUpdate((change,context) => {



        const before=change.before.val();
        const UserUIDX=context.params.UserUID;
        var UploadedBy;
        const after=change.after.val();
        if(before===after||after==='false'){
            console.log("Nothing happened");
            return null;
        }
        else{


               var afterX=after;
               var beforeX=before;


                return change.before.ref.parent.parent.once('value').then(function(snapshot){

                       const managerUser = snapshot.val();
                       console.log(managerUser);
                      UploadedBy=managerUser.UploadedBy;

                      console.log(UserUIDX);


                return admin.database().ref('/' +UserUIDX).once('value').then(function(snapshot){


                     const Commenter=snapshot.val();
                     var commenterName=Commenter.UserName;












                return admin.database().ref('/' +UploadedBy).once('value').then(function(snapshot){

                       console.log(JSON.stringify(snapshot.val()));

                       console.log(UploadedBy);

                       const managerUser2= snapshot.val();
                       console.log(managerUser2);
                       var token=managerUser2.Token;

                        var message;
                        if(UploadedBy===UserUIDX){
                        message = "You liked your own post";
                        }
                        else{
                         message= commenterName+" liked your post";
                        }
                        var titledisplay="Your post is getting likes! Yeyeee!!";
                        var payload = {
                        notification: {
                        title: titledisplay,
                        body: message,
                        sound: "default"
                         }
                         };

                           return admin.messaging().sendToDevice(token, payload)
                                 .then(function (response) {
                                 console.log("Successfully sent message:", response);
                                  console.log(response.results[0].error);
                                   })
                                  .catch(function (error) {
                                  console.log("Error sending message:", error);
                                  });

        });

        });


  });


  }

  });



  //User Adding a topic in event discussions

  exports.TopicAddedInMyEvent=functions.database.instance('eventdiscussions').ref('/{EventKey}/{PostKey}').onCreate((snapshot,context) => {

         const EventKey=context.params.EventKey;
         const data3=snapshot.val();
         var CreaterName=data3.UserName;
         var CreaterUID=data3.UploadedBy;

          return admin.database(eventadditions).ref('/' +EventKey).once('value').then(function(snapshot){

                             const data=snapshot.val();
                             var UploadedBy=data.UploadedBy;

                             if(UploadedBy===CreaterUID){
                                return null;
                             }

                             return admin.database(adminDatabase).ref('/'+UploadedBy).once('value').then(function(snapshot){

                             const data2=snapshot.val();
                             var token=data2.Token;

                             var message= CreaterName+" added a topic in your event.";

                                                     var titledisplay="People are discussing your event";
                                                     var payload = {
                                                     notification: {
                                                     title: titledisplay,
                                                     body: message,
                                                     sound: "default"
                                                      }
                                                      };

                                                        return admin.messaging().sendToDevice(token, payload)
                                                              .then(function (response) {
                                                              console.log("Successfully sent message:", response);
                                                               console.log(response.results[0].error);
                                                                })
                                                               .catch(function (error) {
                                                               console.log("Error sending message:", error);
                                                               });
                                  });
                                  });
                                  });


   //User showing interest to organizers event

   exports.UserInterestedInMyEvent=functions.database.instance('ddrx-events-172d3').ref('/{EventKey}/Interested/{UserUID}').onCreate((snapshot,context)  => {

    const UserUID=context.params.UserUID;

    return snapshot.ref.parent.parent.once('value').then(function(snapshot){

           const UploaderInfo= snapshot.val();
           var UploaderUID= UploaderInfo.UploadedBy;



           if(UserUID===UploaderUID){

                                  console.log("Nothing happened");
                                               return null;
                                           }
                                   else{

                                        return admin.database().ref('/' +UserUID).once('value').then(function(snapshot){

                                        const UserInfo= snapshot.val();
                                        var UserName=UserInfo.UserName;


                                         return admin.database(adminDatabase).ref('/'+UploaderUID).once('value').then(function(snapshot){

                                                      const UploaderInfo2=snapshot.val();
                                                      var token=UploaderInfo2.Token;


                                         var message= UserName+" showed interest to your event.";

                                                                                           var titledisplay="People are showing interest to your event";
                                                                                           var payload = {
                                                                                           notification: {
                                                                                           title: titledisplay,
                                                                                           body: message,
                                                                                           sound: "default"
                                                                                            }
                                                                                            };

                                                                                              return admin.messaging().sendToDevice(token, payload)
                                                                                                    .then(function (response) {
                                                                                                    console.log("Successfully sent message:", response);
                                                                                                     console.log(response.results[0].error);
                                                                                                      })
                                                                                                     .catch(function (error) {
                                                                                                     console.log("Error sending message:", error);
                                                                                                     });


                                             });







                                        });



                        }






        });






   });

   //ManualNotification by Admin to Attendeess
   exports.ManualNotifications=functions.database.instance('manualnotifications').ref('/{PushKey}').onCreate((snapshot,context)  => {

      const NotificationModel= snapshot.val();
      var Notification= NotificationModel.Notification;

       var message= ""+Notification;

       var titledisplay="Organizer has send you a new notification";
       var payload = {
       notification: {
       title: titledisplay,
       body: message,
       click_action: "com.example.androdev.ddxr.MyManualNotification",
       sound: "default"
        },
        data : {
              "manual": "manual"
            }
        };

        return admin.messaging().sendToTopic("all", payload)
                .then(function (response) {
                console.log("Successfully sent message:", response);
                console.log(response.results[0].error);
                 })
                 .catch(function (error) {
                 console.log("Error sending message:", error);
                  });





   });





