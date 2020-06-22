package com.mfriend.djapp.messaging

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Gives methods to get requests
 */
class RequestsRepo(private val db: FirebaseFirestore = Firebase.firestore) {
    /**
     * Get updates of the requests for party [code]
     */
    suspend fun getRequests(code: String): Flow<List<String>> = callbackFlow {
        val document = db.collection("party").document(code).collection("request")
        val subscription = document.addSnapshotListener { snapshot, _ ->
            val requests =
                snapshot?.documents?.mapNotNull { it.toObject(RequestDto::class.java) } ?: return@addSnapshotListener
            sendBlocking(requests.mapNotNull { it.songUri })
        }
        awaitClose { subscription.remove() }
    }

    /**
     * Add request
     */
    suspend fun addRequest(code: String, request: String) = suspendCancellableCoroutine<Unit> { cont ->
        db.collection("party").document(code).collection("request").document().set(RequestDto(request))
            .addOnCompleteListener { cont.resume(Unit) }
    }
}