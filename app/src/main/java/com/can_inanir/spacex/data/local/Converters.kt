package com.can_inanir.spacex.data.local

import androidx.room.TypeConverter
import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.model.Measurement
import com.can_inanir.spacex.domain.model.PayloadWeight
import com.can_inanir.spacex.domain.model.Weight
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromMeasurement(measurement: Measurement): String {
        return Gson().toJson(measurement)
    }

    @TypeConverter
    fun toMeasurement(value: String): Measurement {
        return Gson().fromJson(value, Measurement::class.java)
    }

    @TypeConverter
    fun fromWeight(weight: Weight): String {
        return Gson().toJson(weight)
    }

    @TypeConverter
    fun toWeight(value: String): Weight {
        return Gson().fromJson(value, Weight::class.java)
    }

    @TypeConverter
    fun fromPayloadWeightList(list: List<PayloadWeight>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toPayloadWeightList(value: String): List<PayloadWeight> {
        val listType = object : TypeToken<List<PayloadWeight>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromLinks(links: Launch.Links): String {
        return Gson().toJson(links)
    }

    @TypeConverter
    fun toLinks(value: String): Launch.Links {
        return Gson().fromJson(value, Launch.Links::class.java)
    }

    @TypeConverter
    fun fromPatches(patches: Launch.Patches?): String {
        return Gson().toJson(patches)
    }

    @TypeConverter
    fun toPatches(value: String): Launch.Patches? {
        return Gson().fromJson(value, Launch.Patches::class.java)
    }

    @TypeConverter
    fun fromLaunchpadImages(images: Launchpad.LaunchpadImages): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toLaunchpadImages(value: String): Launchpad.LaunchpadImages {
        return Gson().fromJson(value, Launchpad.LaunchpadImages::class.java)
    }
}
