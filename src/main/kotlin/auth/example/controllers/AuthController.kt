package auth.example.controllers

import auth.example.dtos.LoginDTO
import auth.example.dtos.Message
import auth.example.dtos.RegisterDTO
import auth.example.models.User
import auth.example.services.UserService
import io.jsonwebtoken.Jwt
import org.springframework.http.ResponseEntity
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("api")
class AuthController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<Any> {
        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        val existingUser = userService.findByEmail(body.email)

        if (existingUser != null)
            ResponseEntity.badRequest().body(Message("E-mail already in use."))

        val savedUser = userService.save(user)

        return ResponseEntity.ok(savedUser)
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found! :( "))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("bad password! :( "))
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