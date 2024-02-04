package com.example.swiftwave.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwave.data.model.MessagesData
import com.example.swiftwave.ui.viewmodels.FirebaseViewModel
import com.example.swiftwave.ui.viewmodels.TaskViewModel

@Composable
fun chatCard(
    messagesData: MessagesData,
    firebaseViewModel: FirebaseViewModel,
    taskViewModel: TaskViewModel
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start =
                if (messagesData.senderId == firebaseViewModel.userData.userId) {
                    35.dp
                } else {
                    0.dp
                },
                end = if (messagesData.senderId == firebaseViewModel.userData.userId) {
                    0.dp
                } else {
                    35.dp
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            if(messagesData.senderId==firebaseViewModel.userData.userId){
                Arrangement.End
            }else{
                Arrangement.Start
            }
    ){
        Card(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                if(messagesData.senderId==firebaseViewModel.userData.userId){
                    MaterialTheme.colorScheme.inversePrimary
                }else{
                    MaterialTheme.colorScheme.secondaryContainer
                }
            )
        ) {
            Column {
                Text(
                    text = messagesData.message.toString(),
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp
                        ),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row (
                    modifier = Modifier
                        .padding(
                            bottom = 10.dp,
                            start = 10.dp
                        )
                ){
                    Text(
                        text = taskViewModel.getTime(messagesData.timestamp?.toLong() ?: 0),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}