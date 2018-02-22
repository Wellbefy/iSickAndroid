package se.galvend.isick.classes

import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.messaging.MessageStatus

/**
 * Created by dennisgalven on 2018-02-22.
 * Mail manager class
 */
class MailManager {
    fun sendMail(name: String, message: String, mail: String, callback: (BackendlessFault?) -> Unit) {
        Backendless.Messaging.sendTextEmail("$name frånvaro", message, mail, object: AsyncCallback<MessageStatus> {
            override fun handleResponse(response: MessageStatus?) {
                callback(null)
            }

            override fun handleFault(fault: BackendlessFault?) {
                callback(fault)
            }
        })
    }
}