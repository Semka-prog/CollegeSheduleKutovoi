package com.example.collegeshedulekutovoi.data.api

import com.example.collegeshedulekutovoi.data.dto.ScheduleByDateDto
import com.example.collegeshedulekutovoi.data.dto.GroupDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {

    @GET("api/schedule/group/{groupName}")
    suspend fun getSchedule(
        @Path("groupName") groupName: String,
        @Query("start") startDate: String,
        @Query("end") endDate: String
    ): List<ScheduleByDateDto>

    @GET("api/groups")
    suspend fun getAllGroups(): List<GroupDto>
}
