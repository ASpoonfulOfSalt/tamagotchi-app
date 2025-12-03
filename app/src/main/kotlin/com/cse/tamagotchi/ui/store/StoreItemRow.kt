package com.cse.tamagotchi.ui.store

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Hat
import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.ui.theme.*

@Composable
fun StoreItemRow(
    item: StoreItem,
    quantityOwned: Int,
    currentHat: Hat?,
    onPurchaseClick: (StoreItem) -> Unit,
    onEquipClick: (StoreItem) -> Unit,
    isHat: Boolean,
    hatType: Hat?,
    isDarkMode: Boolean
) {
    val view = LocalView.current
    val isEquipped = isHat && currentHat == hatType
    val canEquip = isHat && quantityOwned > 0

    // Cute bubble background
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.name,
                modifier = Modifier.size(52.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Cute description
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriceBadge(item.price, isDarkMode)

                    if (quantityOwned > 0) {
                        OwnedBadge(quantityOwned)
                    }
                }
            }

            Button(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    if (canEquip) onEquipClick(item) else onPurchaseClick(item)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (isEquipped) DestructiveRed
                        else if (isDarkMode) DarkModeGreen else LightModeGreen,
                    contentColor = if (isDarkMode) PureWhite else DarkGrey
                )
            ) {
                Text(
                    text = when {
                        isEquipped -> "Unequip"
                        canEquip -> "Equip"
                        else -> "Buy"
                    }
                )
            }
        }
    }
}
