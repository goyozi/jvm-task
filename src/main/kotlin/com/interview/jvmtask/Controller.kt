package com.interview.jvmtask

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileOutputStream
import java.time.Clock
import java.time.Duration
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@RestController
class Controller(val clock: Clock,
                 val timeout: Duration,
                 val outputDir: String) {

    private val writeLock = ReentrantLock()

    private var lastRollover = clock.instant()
    private var file = FileOutputStream(currentFile())

    @PostMapping("/users")
    fun save(user: User) {
        writeLock.withLock {
            if (Duration.between(lastRollover, clock.instant()) > timeout) {
                rollover()
            }

            val proto = UserProtos.User.newBuilder()
                    .setId(user.id)
                    .setName(user.name)
                    .build()

            proto.writeDelimitedTo(file)
        }
    }

    private fun rollover() {
        file.close()
        lastRollover = clock.instant()
        file = FileOutputStream(currentFile())
    }

    private fun currentFile() = "$outputDir/users-${lastRollover.epochSecond}.bin"
}

data class User(var id: Int = -1,
                var name: String = "")