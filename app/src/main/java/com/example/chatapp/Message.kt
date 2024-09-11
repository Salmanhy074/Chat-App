package com.example.chatapp

class Message {

    var senderEmail: String? = null
    var receiverEmail: String? = null
    var senderId: String? = null
    var receiverId: String? = null
    var message: String? = null

    constructor(){}

    constructor( senderEmail: String?, receiverEmail: String?, senderId: String?, receiverId: String?, message: String?) {
        this.senderEmail = senderEmail
        this.receiverEmail = receiverEmail
        this.senderId = senderId
        this.receiverId = receiverId
        this.message = message
    }

}
