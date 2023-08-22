package auth.example.repository

import auth.example.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Int> {
    fun findByEmail(email: String): User?

    fun getById(id: Int): User
}