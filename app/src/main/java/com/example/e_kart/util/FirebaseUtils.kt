package com.example.e_kart.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T {

    return suspendCancellableCoroutine {cont ->
        addOnCompleteListener{
            if(it.exception !=null){
                //agar kauno exception hai toh
                cont.resumeWithException(it.exception!!)

            }else{
                cont.resume(it.result,null)
            }
        }
    }
}