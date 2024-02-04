package com.example.swiftwave.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.swiftwave.auth.UserData
import com.example.swiftwave.ui.viewmodels.FirebaseViewModel
import com.example.swiftwave.ui.viewmodels.TaskViewModel

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun messageCard(
    userData: UserData,
    firebaseViewModel: FirebaseViewModel,
    navController: NavController,
    taskViewModel: TaskViewModel
){
    val chatList = firebaseViewModel.allChats.collectAsState(initial = emptyMap())
    val roomID = firebaseViewModel.generate_roomID(firebaseViewModel.userData,userData)
    val recentChat = chatList.value.get(roomID)?.get(chatList.value.get(roomID)!!.size-1)?.message
    val recentTime = taskViewModel.getTime(chatList.value.get(roomID)?.get(chatList.value.get(roomID)!!.size-1)?.timestamp?.toLong() ?: 0)
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        onClick = {
            firebaseViewModel.chattingWith = userData
            navController.navigate("PersonChat")
        }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            GlideImage(
                model = userData.profilePictureUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .size(70.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = userData.username.toString(),
                    fontSize = 25.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                if(recentChat.toString()!="null"){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = recentChat.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = recentTime,
                            color = Color.Gray,
                        )
                    }
                }
            }
        }
    }
}