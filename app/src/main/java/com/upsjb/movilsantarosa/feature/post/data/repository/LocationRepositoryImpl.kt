package com.upsjb.movilsantarosa.feature.post.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.LocationServices
import com.upsjb.movilsantarosa.feature.post.domain.model.Location
import com.upsjb.movilsantarosa.feature.post.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : LocationRepository {

    private val client =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<Location> =
        suspendCancellableCoroutine { continuation ->

            client.lastLocation
                .addOnSuccessListener { location ->

                    if (location == null) {
                        continuation.resume(
                            Result.failure(
                                Exception("No se pudo obtener la ubicación.")
                            )
                        )
                        return@addOnSuccessListener
                    }

                    continuation.resume(
                        Result.success(
                            Location(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    )
                }
                .addOnFailureListener {
                    continuation.resume(
                        Result.failure(it)
                    )
                }
        }

    @Suppress("DEPRECATION")
    override suspend fun getAddress(
        latitude: Double,
        longitude: Double
    ): Result<String> {

        return try {

            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    suspendCancellableCoroutine<List<Address>> { cont ->

                        geocoder.getFromLocation(
                            latitude,
                            longitude,
                            1
                        ) { result ->
                            cont.resume(result)
                        }
                    }

                } else {

                    geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1
                    ) ?: emptyList()
                }

            val address = addresses.firstOrNull()?.getAddressLine(0).orEmpty()

            Result.success(address)

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message ?: "No se pudo obtener la dirección."
                )
            )
        }
    }
}