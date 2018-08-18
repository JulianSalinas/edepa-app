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

console.log(input);
console.log(atStartOfDay(input));
console.log(new Date(input));
console.log(new Date(atStartOfDay(input)));
console.log(getHumanReadableDate(input));
