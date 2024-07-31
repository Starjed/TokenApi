package auth.example.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Document(collection = "users")
class User(
    private val name: String,
    @Indexed(unique = true)
    private var email: String,
    @JsonIgnore private var password: String // Senha criptografada
) {
    @Id
    var id: String? = null

    // Propriedade para definir a senha com criptografia
    var pw: String
        get() = password
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            password = passwordEncoder.encode(value)
        }

    // Método para comparar a senha em texto puro com a criptografada
    fun comparePassword(rawPassword: String): Boolean {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.matches(rawPassword, pw)
    }
}


