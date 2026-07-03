// ui/feature/members/components/MemberSearchBar.kt
package com.upsjb.movilsantarosa.ui.feature.members.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.R

@Composable
fun MemberSearchBar(
    searchText: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = { Text("Buscar socio...") },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true, name = "Member Search Bar")
@Composable
fun PreviewMemberSearchBar() {
    var searchText by remember { mutableStateOf("") }
    MemberSearchBar(
        searchText = searchText,
        onSearchChange = { searchText = it },
    )
}

@Preview(showBackground = true, name = "Member Search Bar - With Text")
@Composable
fun PreviewMemberSearchBarWithText() {
    var searchText by remember { mutableStateOf("Juan") }
    MemberSearchBar(
        searchText = searchText,
        onSearchChange = { searchText = it },
    )
}