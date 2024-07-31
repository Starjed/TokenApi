package auth.example.controllers

import auth.example.dtos.LoginDTO
import auth.example.dtos.Message
import auth.example.dtos.RegisterDTO
import auth.example.models.User
import auth.example.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("api")
class AuthController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<Any> {
        val existingUser = userService.findByEmail(body.email)

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Message("E-mail already in use."))
        } else {

            val newUser = User(body.name, body.email, body.password)

            userService.save(newUser)

            return ResponseEntity.ok(newUser)
        }
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.notFound().build()

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message("Bad password! :("))
        }
        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.HS256, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        val responseBody = mapOf(
            "token" to jwt,
            "user" to user
        )

        return ResponseEntity.ok(responseBody)
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Logout!"))
    }
}
