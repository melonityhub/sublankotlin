
package com.androyal.subxplayer.ui.widgets.browser

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserTopAppBar(title:String, onSearch:(String)->Unit, onSettings:()->Unit, onCast:()->Unit, isCasting:Boolean){
    var query by remember{ mutableStateOf("")}
    TopAppBar(
        title={ Text(title) },
        actions={
            IconButton(onClick=onCast){ Icon(Icons.Default.Cast, contentDescription="Cast", tint= if(isCasting) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) }
            IconButton(onClick=onSettings){ Icon(Icons.Default.Settings, null)}
        }
    )
}

@Composable
fun BrowserTreeBreadcrumbBar(path:List<String>, onNavigate:(Int)->Unit){
    ScrollableTabRow(selectedTabIndex= path.lastIndex){
        path.forEachIndexed{ i, name -> Tab(selected=i==path.lastIndex, onClick={onNavigate(i)}, text={Text(name)})}
    }
}
