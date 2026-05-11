package com.example.updatd_mahila_shakthi.data.repository

import com.example.updatd_mahila_shakthi.data.dao.MemberDao
import com.example.updatd_mahila_shakthi.data.model.Member
import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {
    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()
    suspend fun getMemberById(id: Long): Member? = memberDao.getMemberById(id)
    fun searchMembers(query: String): Flow<List<Member>> = memberDao.searchMembers(query)
    fun getTotalMemberCount(): Flow<Int> = memberDao.getTotalMemberCount()
    suspend fun insert(member: Member): Long = memberDao.insert(member)
    suspend fun update(member: Member) = memberDao.update(member)
    suspend fun delete(member: Member) = memberDao.delete(member)
    suspend fun getAllMembersList(): List<Member> = memberDao.getAllMembersList()
}
