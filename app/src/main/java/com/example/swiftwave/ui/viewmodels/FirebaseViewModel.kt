package com.example.swiftwave.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftwave.auth.UserData
import com.example.swiftwave.data.model.MessagesData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import com.google.firebase.database.getValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirebaseViewModel(
    val userData: UserData
) : ViewModel() {

    lateinit var Chatdb: DatabaseReference
    var chattingWith by mutableStateOf<UserData?>(null)
    var text by mutableStateOf("")

    private val _allUsers = MutableStateFlow<List<UserData>>(emptyList())
    val allUsers : StateFlow<List<UserData>> get() = _allUsers.asStateFlow()

    init{
        val Userdb = FirebaseDatabase.getInstance().getReference("Users")
        Chatdb = FirebaseDatabase.getInstance().getReference("ChatRoom")
        startPeriodicExecution()
        viewModelScope.launch {
            Userdb.get().addOnSuccessListener { dataSnapshot ->
                if(dataSnapshot.exists()){
                    _allUsers.value = dataSnapshot.getValue<MutableList<UserData>>()!!
                }
            }.addOnSuccessListener {
                val curList = allUsers.value.orEmpty().toMutableList()
                if(!curList.contains(userData)){
                    curList.add(userData)
                    Userdb.setValue(curList)
                }
            }
        }
    }

    private val _allChats = MutableStateFlow<Map<String, MutableList<MessagesData>>>(emptyMap())
    val allChats: StateFlow<Map<String, MutableList<MessagesData>>> = _allChats.asStateFlow()

    fun fetchAllChats(){
        viewModelScope.launch {
            Chatdb.get().addOnSuccessListener {dataSnapshot ->
                if(dataSnapshot.exists()){
                    _allChats.value = dataSnapshot.getValue<Map<String,MutableList<MessagesData>>>()!!
                }
            }
        }
    }

    fun sendMessage(){
        val roomID = generate_roomID(userData,chattingWith)
        viewModelScope.launch {
            val curMap = allChats.value.orEmpty().toMutableMap()
            if(!curMap.containsKey(roomID)){
                val messageList = listOf(MessagesData(userData.userId.toString(),text,System.currentTimeMillis().toString()))
                curMap[roomID] = messageList.toMutableList()
                Chatdb.setValue(curMap)
            }else{
                curMap[roomID]?.add(MessagesData(userData.userId.toString(),text,System.currentTimeMillis().toString()))
                Chatdb.setValue(curMap)
            }
            fetchAllChats()
        }
    }

    fun generate_roomID(
        userData1: UserData,
        userData2: UserData?
    ) :String {
        val roomID = (userData1.userId.toString()+ userData2?.userId.toString()).toCharArray()
        return String(roomID.sorted().toCharArray())
    }

    private var job: Job? = null

    fun startPeriodicExecution() {
        job = viewModelScope.launch {
            while (true) {
                fetchAllChats()
                delay(1000)
            }
        }
    }

    fun stopPeriodicExecution() {
        job?.cancel()
    }
}