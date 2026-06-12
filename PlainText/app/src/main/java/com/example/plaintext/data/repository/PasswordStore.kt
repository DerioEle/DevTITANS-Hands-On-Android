package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow

interface PasswordDBStore {
    fun getList(): Flow<List<Password>>
    suspend fun add(password: Password): Long
    suspend fun update(password: Password)
    fun get(id: Int): Password?
    suspend fun save(passwordInfo: PasswordInfo)
    suspend fun isEmpty(): Flow<Boolean>
}

class FakePasswordDBStore : PasswordDBStore {
    private val passwordsFlow = MutableStateFlow(
        listOf(
            Password(
                id = 1,
                name = "Twitter",
                login = "dev",
                password = "••••••",
                notes = "conta dev"
            ),
            Password(
                id = 2,
                name = "Facebook",
                login = "devtitans",
                password = "••••••",
                notes = null
            ),
            Password(
                id = 3,
                name = "Moodle",
                login = "dev.com",
                password = "••••••",
                notes = "notes"
            )
        )
    )

    private var nextId = 4

    override fun getList(): Flow<List<Password>> = passwordsFlow

    override suspend fun add(password: Password): Long {
        val new = password.copy(id = nextId++)
        passwordsFlow.value = passwordsFlow.value + new
        return new.id.toLong()
    }

    override suspend fun update(password: Password) {
        val current = passwordsFlow.value.toMutableList()
        val idx = current.indexOfFirst { it.id == password.id }
        if (idx >= 0) current[idx] = password
        passwordsFlow.value = current
    }

    override fun get(id: Int): Password? = passwordsFlow.value.find { it.id == id }

    override suspend fun save(passwordInfo: PasswordInfo) {
        val idToUse = if (passwordInfo.id == 0) nextId++ else passwordInfo.id
        val p = Password(
            id = idToUse,
            name = passwordInfo.name,
            login = passwordInfo.login,
            password = passwordInfo.password,
            notes = passwordInfo.notes
        )
        val current = passwordsFlow.value.toMutableList()
        val idx = current.indexOfFirst { it.id == p.id }
        if (idx >= 0) current[idx] = p else current.add(p)
        passwordsFlow.value = current
    }

    override suspend fun isEmpty(): Flow<Boolean> = passwordsFlow.map { it.isEmpty() }
}

class LocalPasswordDBStore(
    private val passwordDao : PasswordDao
): PasswordDBStore {
    override fun getList(): Flow<List<Password>> {
        return passwordDao.getAll()
    }

    override suspend fun add(password: Password): Long {
        return passwordDao.insert(password)
    }

    override suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    override fun get(id: Int): Password? {
        return kotlinx.coroutines.runBlocking {
            passwordDao.getById(id).firstOrNull()
        }
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        passwordDao.insert(
            Password(
                id = passwordInfo.id,
                name = passwordInfo.name,
                login = passwordInfo.login,
                password = passwordInfo.password,
                notes = passwordInfo.notes
            )
        )
    }

    override suspend fun isEmpty(): Flow<Boolean> {
        return kotlinx.coroutines.flow.flow {
            emit(passwordDao.count() == 0)
        }
    }
}