package io.github.chkrb.pqcompanion.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.chkrb.pqcompanion.R
import io.github.chkrb.pqcompanion.ui.NavDestination

@Composable
fun HomePage(navController: NavController) {
    Scaffold {
        paddingValues ->
        Column(
            modifier=Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(paddingValues),
            horizontalAlignment=Alignment.CenterHorizontally,
            verticalArrangement=Arrangement.Center
        ){
            // App name
            Text(
                text = "POSqueue",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            //subtitle
            Text(
                text = "Scan QR code and find the \nproducts available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            //qr code scan 
            Box(
                modifier = Modifier.size(190.dp).clip(RoundedCornerShape(95.dp)),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.qr_code_illustration),
                    contentDescription = "QR code scanning illustration",
                    modifier = Modifier.size(130.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Spacer(modifier = Modifier.height(40.dp)) 
            //Scan button         
            OutlinedButton(
                onClick = { 
                    navController.navigate(NavDestination.SHOP_SCAN.route()) 
                },
                modifier = Modifier.width(280.dp).height(60.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Scan QR Code",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                //Instruction
                Text(
                    text = "Scan the QR displayed at the shop",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
            )
        }
    }
}

