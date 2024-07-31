package auth.example.repository

import auth.example.models.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Int> {
    fun findByEmail(email: String): User?

    fun getById(id: Int): User
}
