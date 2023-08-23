package auth.example.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Document(collection = "users")
class User {
    @Id
    var id: String? = null

    var name = ""

    @Indexed(unique = true)
    var email = ""

    var password = ""
        @JsonIgnore
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }

    fun register(name: String, email: String, password: String) {
        if (name.isEmpty())
            throw IllegalArgumentException("O Nome não pode ser vazio")
        if (email.isEmpty())
            throw IllegalArgumentException("O email não pode ser vazio")
        if (password.isEmpty())
            throw IllegalArgumentException("A senha não pode ser vazia")

    }
}


