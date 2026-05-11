package com.example.updatd_mahila_shakthi.data.repository

import com.example.updatd_mahila_shakthi.data.dao.UserDao
import com.example.updatd_mahila_shakthi.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: User): Long = userDao.insert(user)
    suspend fun login(email: String, passwordHash: String): User? = userDao.login(email, passwordHash)
    suspend fun getByEmail(email: String): User? = userDao.getByEmail(email)
    suspend fun emailExists(email: String): Boolean = userDao.emailExists(email)
    suspend fun update(user: User) = userDao.update(user)
}
