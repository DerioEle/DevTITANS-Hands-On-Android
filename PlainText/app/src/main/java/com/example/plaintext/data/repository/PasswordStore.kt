package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordDBStore @Inject constructor(
    private val passwordDao: PasswordDao
) {

    fun passwords(): Flow<List<Password>> =
        passwordDao.getAll()

    fun password(id: Int): Flow<Password?> =
        passwordDao.getById(id)

    suspend fun insert(password: Password) =
        passwordDao.insert(password)

    suspend fun update(password: Password) =
        passwordDao.update(password)

    suspend fun delete(password: Password) =
        passwordDao.delete(password)

    suspend fun count(): Int =
        passwordDao.count()

    suspend fun deleteAll() =
        passwordDao.deleteAll()
}