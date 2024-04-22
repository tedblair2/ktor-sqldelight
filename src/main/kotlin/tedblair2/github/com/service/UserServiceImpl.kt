package tedblair2.github.com.service

import kotlinx.datetime.LocalDate
import tedblair2.github.com.db.Database
import tedblair2.github.com.model.User

class UserServiceImpl(
    private val db:Database
) : UserService {

    private val query=db.usersQueries

    override suspend fun getUsers(): List<User> {
        return query.getUsers(mapper =::mapUser).executeAsList()
    }

    override suspend fun insertUser(user: User) {
        query.transaction {
            query.insertUser(name = user.name, address = user.address, age = user.age, date_of_birth = user.dateOfBirth)
        }
    }

    override suspend fun getUser(id: Int): User? {
        return query.getUser(id,::mapUser).executeAsOneOrNull()
    }

    private fun mapUser(
        id: Int,
        name:String,
        address:String,
        age:Int,
        dateOfBirth:LocalDate
    ):User{
        return User(id,name,address,age,dateOfBirth)
    }
}