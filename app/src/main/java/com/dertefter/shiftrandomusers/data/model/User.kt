package com.dertefter.shiftrandomusers.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@Parcelize
data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val registered: Registered,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
    val nat: String
): Parcelable

@Parcelize
data class Name(val title: String, val first: String, val last: String): Parcelable

@Parcelize
data class Location(val city: String, val country: String, val street: Street, val coordinates: Coordinates): Parcelable

@Parcelize
data class Street(val number: Int, val name: String): Parcelable

@Parcelize
data class Coordinates(val latitude: String, val longitude: String): Parcelable

@Parcelize
data class Login(val uuid: String, val username: String): Parcelable

@Parcelize
data class Dob(val date: String, val age: Int) : Parcelable {
    fun getLocalDate(): LocalDate {
        return Instant.parse(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

@Parcelize
data class Registered(val date: String, val age: Int): Parcelable {
    fun getLocalDate(): LocalDate {
        return Instant.parse(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

@Parcelize
data class Id(val name: String, val value: String?): Parcelable

@Parcelize
data class Picture(val large: String, val medium: String, val thumbnail: String): Parcelable