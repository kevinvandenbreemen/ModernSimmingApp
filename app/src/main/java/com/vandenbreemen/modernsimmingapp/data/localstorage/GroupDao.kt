package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupDao {

    @Insert
    fun store(group: Group)

    @Query("select * from `group` where name=:name")
    fun findGroupByName(name: String): Group?

    @Query("select * from `group`")
    fun list(): List<Group>

}
