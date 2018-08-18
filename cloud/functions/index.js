const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const Instant = require('js-joda').Instant;
const LocalDate = require('js-joda').LocalDate;
const ZoneId = require('js-joda').ZoneId;


function atStartOfDay(dateTimeInMillis) {
    var instant = Instant.ofEpochMilli(dateTimeInMillis);
    var datetime = LocalDate.ofInstant(instant, ZoneId.UTC);
    var start = datetime.atStartOfDay();
    return start.toInstant(ZoneId.UTC).toEpochMilli(); 
}

exports.markAsDelivered = functions.database.ref('/edepa5/chat/{pushId}')
    .onCreate((snapshot, context) => {
        const message = snapshot.val();
        console.log('markAsDelivered', context.params.pushId, message);
        return snapshot.ref.child('delivered').set(true);
    }
);

exports.addDateToEvent = functions.database.ref('/edepa5/schedule/{pushId}')
    .onCreate((snapshot, context) => {
        const event = snapshot.val();
        var datetime = event.start; 
        console.log('addDateToEvent', context.params.pushId, event.title);
        console.log('atStartOfDay', datetime, atStartOfDay(datetime));
        return snapshot.ref.child('date').set(atStartOfDay(datetime));
    }
);

exports.updateDateToEvent = functions.database.ref('/edepa5/schedule/{pushId}/start')
    .onUpdate((snapshot, context) => {
        const event = snapshot.after.ref.parent;
        const datetime = snapshot.after.val();
        console.log('updateDateToEvent', context.params.pushId, event.title);
        console.log('atStartOfDay', datetime, atStartOfDay(datetime));
        return event.ref.child('date').set(atStartOfDay(datetime));
    }
);

// exports.pushNotification = functions.database.ref('/edepa5/news/{pushId}')
//     .onCreate((snapshot, context) => {

//         const data = snapshot.val();
//         console.log('Sending notification for: ', data);

//         const payload = {
//             notification: {
//                 title: 'Nueva noticia',
//                 body: data.content,
//                 sound: "default"
//             }, data: { news: 'news' }
//         };

//         console.log('Getting payload: ', payload);

//         const options = {
//             priority: "high",
//             timeToLive: 60 * 60 * 24
//         };

//         admin.messaging().sendToTopic("news", payload, options)
//         .then(function(response){
//             console.log('Succesfullt sent notification: ', response);
//         })
//         .catch(function(error){
//             console.log('Error sending notification: ', error)
//         })
//     }
// );

exports.increaseFavorites = functions.database.ref('/edepa5/favorites/{userId}/{eventId}')
    .onWrite((snapshot, context) => {

        const beforeValue = snapshot.before.val();
        console.log('beforeValue', beforeValue, snapshot.before._fullPath());

        const afterValue = snapshot.after.val();
        console.log('afterValue', afterValue, snapshot.after._fullPath());

        const userId = context.params.userId;
        const eventId = context.params.eventId;
        console.log('params', userId, eventId);

        const increase = snapshot.after.exists() && !snapshot.before.exists();
        const decrease = !snapshot.after.exists() && snapshot.before.exists();
        const increment = increase ? 1 : decrease ? -1 : null;
        console.log('increment', increment);

        const scheduleRef = snapshot.after.ref.parent.parent.parent.child('schedule');
        return scheduleRef.child(eventId).child('favorites').transaction((current) => {
            var newValue = (current || 0) + increment;
            return newValue >= 0 ? newValue: 0;
        
        }).then(() => {
            return console.log('Counter correctly updated');
        });

    }
);