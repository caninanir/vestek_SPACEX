package com.can_inanir.spacex.data.local

import androidx.room.TypeConverter
import com.can_inanir.spacex.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // List<String> converters
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    // Measurement converters
    @TypeConverter
    fun fromMeasurement(measurement: Measurement): String {
        return Gson().toJson(measurement)
    }

    @TypeConverter
    fun toMeasurement(value: String): Measurement {
        return Gson().fromJson(value, Measurement::class.java)
    }

    // Weight converters
    @TypeConverter
    fun fromWeight(weight: Weight): String {
        return Gson().toJson(weight)
    }

    @TypeConverter
    fun toWeight(value: String): Weight {
        return Gson().fromJson(value, Weight::class.java)
    }

    // PayloadWeight converters
    @TypeConverter
    fun fromPayloadWeight(payloadWeight: PayloadWeight): String {
        return Gson().toJson(payloadWeight)
    }

    @TypeConverter
    fun toPayloadWeight(value: String): PayloadWeight {
        return Gson().fromJson(value, PayloadWeight::class.java)
    }

    // List<PayloadWeight> converters
    @TypeConverter
    fun fromPayloadWeightList(list: List<PayloadWeight>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toPayloadWeightList(value: String): List<PayloadWeight> {
        val listType = object : TypeToken<List<PayloadWeight>>() {}.type
        return Gson().fromJson(value, listType)
    }

    // Launch.Links converters
    @TypeConverter
    fun fromLinks(links: Launch.Links): String {
        return Gson().toJson(links)
    }

    @TypeConverter
    fun toLinks(value: String): Launch.Links {
        return Gson().fromJson(value, Launch.Links::class.java)
    }

    // Launch.Patches converters
    @TypeConverter
    fun fromPatches(patches: Launch.Patches?): String {
        return Gson().toJson(patches)
    }

    @TypeConverter
    fun toPatches(value: String): Launch.Patches? {
        return Gson().fromJson(value, Launch.Patches::class.java)
    }

    // Launchpad.LaunchpadImages converters
    @TypeConverter
    fun fromLaunchpadImages(images: Launchpad.LaunchpadImages): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toLaunchpadImages(value: String): Launchpad.LaunchpadImages {
        return Gson().fromJson(value, Launchpad.LaunchpadImages::class.java)
    }
}