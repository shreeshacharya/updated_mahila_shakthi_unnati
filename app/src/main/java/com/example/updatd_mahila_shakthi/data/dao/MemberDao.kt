package com.example.updatd_mahila_shakthi.data.dao

import androidx.room.*
import com.example.updatd_mahila_shakthi.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert
    suspend fun insert(member: Member): Long

    @Update
    suspend fun update(member: Member)

    @Delete
    suspend fun delete(member: Member)

    @Query("SELECT * FROM members WHERE isActive = 1 ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE memberId = :id")
    suspend fun getMemberById(id: Long): Member?

    @Query("SELECT * FROM members WHERE (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%') AND isActive = 1")
    fun searchMembers(query: String): Flow<List<Member>>

    @Query("SELECT COUNT(*) FROM members WHERE isActive = 1")
    fun getTotalMemberCount(): Flow<Int>

    @Query("SELECT * FROM members WHERE isActive = 1 ORDER BY name ASC")
    suspend fun getAllMembersList(): List<Member>
}
