package tedblair2.github.com.service

import tedblair2.github.com.model.User

interface UserService {
    suspend fun getUsers():List<User>
    suspend fun insertUser(user: User)
    suspend fun getUser(id:Int):User?
}