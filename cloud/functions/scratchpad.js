/**
 * Redondear la fecha al inicio del día 
 */
const convert = require('js-joda').convert;
const Instant = require('js-joda').Instant;
const LocalDate = require('js-joda').LocalDate;
const ZoneId = require('js-joda').ZoneId;

var input = 1533428346321; 

function atStartOfDay(dateTimeInMillis) {
    var instant = Instant.ofEpochMilli(dateTimeInMillis);
    var datetime = LocalDate.ofInstant(instant, ZoneId.UTC);
    var start = datetime.atStartOfDay();
    return start.toInstant(ZoneId.UTC).toEpochMilli(); 
}

function getHumanReadableDate(dateTimeInMillis) {
    var instant = Instant.ofEpochMilli(dateTimeInMillis);
    var datetime = LocalDate.ofInstant(instant, ZoneId.UTC);
    var date = datetime.atStartOfDay();
    return  date.dayOfMonth() + '/' + 
            date.monthValue() + '/' +
            date.year();
}

// console.log(input);
// console.log(atStartOfDay(input));
// console.log(new Date(input));
// console.log(new Date(atStartOfDay(input)));
// console.log(getHumanReadableDate(input));

/**
 * Probando transacciones 
 */
var config = {
    apiKey: "AIzaSyCv6ci36cntuWBjA3H6UzogXss5s3p5PS0",
    authDomain: "rommie-91186.firebaseapp.com",
    databaseURL: 'https://rommie-91186.firebaseio.com/',
    storageBucket: "gs://rommie-91186.appspot.com"
};

const admin = require('firebase-admin');

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: 'https://rommie-91186.firebaseio.com/'
});

const database = admin.database();

database.ref('/edepa5/favorites/{userId}/{eventId}').onWrite((snapshot, context) => {

    const scheduleRef = snapshot.after.ref.parent.parent.child('schedule');
    const countRef = scheduleRef.child(context.eventId).child('favoritesAmount');
    countRef.once('value').then(function(snapshot) {
        var count = (snapshot.val() && snapshot.val().favoritesAmout) || 0;
        console.log("Getting count", count);
    });
});

const julianID = 'J5oUpZO7EbT2ChY0whwwmTpRl7w1';

// database.ref('/edepa5/favorites/{pushId}/{favId}')
//     .onCreate((snapshot, context) => {
//         const data = snapshot.val();
//     }
// );

// function toggleStar(postRef, uid) {
//     postRef.transaction(function(post) {
//       if (post) {
//         if (post.stars && post.stars[uid]) {
//           post.starCount--;
//           post.stars[uid] = null;
//         } else {
//           post.starCount++;
//           if (!post.stars) {
//             post.stars = {};
//           }
//           post.stars[uid] = true;
//         }
//       }
//       return post;
//     });
//   }
// }

