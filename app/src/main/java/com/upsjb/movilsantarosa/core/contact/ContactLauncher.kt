package com.upsjb.movilsantarosa.core.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import androidx.core.net.toUri

class ContactLauncher @Inject constructor(
    private val context: Context
) {
    fun openWhatsApp(phone: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "https://wa.me/$phone".toUri()
        }
        context.startActivity(intent)
    }

    fun makePhoneCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
        }
        context.startActivity(intent)
    }
}

@Composable
fun rememberContactLauncher(): ContactLauncher {
    val context = LocalContext.current

    return remember {
        ContactLauncher(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ContactModule {

    @Provides
    fun provideContactLauncher(
        @ApplicationContext context: Context
    ): ContactLauncher {
        return ContactLauncher(context)
    }
}