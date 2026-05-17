

function extractEmails(text) {
    const emailPattern = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/g;
    const emails = text.match(emailPattern);
    console.log('Emails:', emails);
}

function extractPhoneNumbers(text) {
    const phonePattern = /\(\d{3}\) \d{3}-\d{4}/g;
    const phoneNumbers = text.match(phonePattern);
    console.log('Phone Numbers:', phoneNumbers);
}

function extractDates(text) {
    const datePattern = /\b\d{2}\/\d{2}\/\d{4}\b/g;
    const dates = text.match(datePattern);
    console.log('Dates:', dates);
}

function extractUrls(text) {
    const urlPattern = /https?:\/\/[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(\/\S*)?/g;
    const urls = text.match(urlPattern);
    console.log('URLs:', urls);
}

extractEmails(text);
extractPhoneNumbers(text);
extractDates(text);
extractUrls(text);
