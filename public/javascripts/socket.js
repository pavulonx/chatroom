var sock = new SockJS('http://localhost:9000/chat-ws');
sock.onopen = function () {
    console.log('open');
};
sock.onmessage = function (e) {
    console.log('message', e.data);
    alert('received message echoed from server: ' + e.data);
};
sock.onclose = function () {
    console.log('close');
};

function send(message) {
    if (sock.readyState === SockJS.OPEN) {
        console.log("sending message");
        sock.send(message);
    } else {
        console.log("The socket is not open.");
    }
}



