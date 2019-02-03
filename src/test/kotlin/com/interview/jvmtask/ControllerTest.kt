package com.interview.jvmtask

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.time.Clock
import java.time.Duration
import java.time.Instant

class ControllerTest {
    private val initialTime = Instant.now()

    private val clock = mock<Clock> { on { instant() } doReturn initialTime }
    private val timeout = Duration.ofMinutes(1)
    private val outputDir = "testOutput/"

    lateinit var controller: Controller

    @Before
    fun setup() {
        File(outputDir).mkdirs()
        controller = Controller(clock, timeout, outputDir)
    }

    @Test
    fun writingUsersToFile() {
        controller.save(User(123, "John Doe"))

        val user = savedUser(initialTime)
        assertEquals(123, user.id)
        assertEquals("John Doe", user.name)
    }

    @Test
    fun noRolloverBeforeTimeout() {
        val newTime = initialTime.plusSeconds(60)
        whenever(clock.instant()).doReturn(newTime)

        controller.save(User(234, "Jane Doe"))

        val user = savedUser(initialTime)
        assertEquals(234, user.id)
        assertEquals("Jane Doe", user.name)
    }

    @Test
    fun rolloverAfterTimeout() {
        val newTime = initialTime.plusSeconds(61)
        whenever(clock.instant()).doReturn(newTime)

        controller.save(User(345, "Max Doe"))

        val user = savedUser(newTime)
        assertEquals(345, user.id)
        assertEquals("Max Doe", user.name)
    }

    private fun savedUser(rolloverTime: Instant): UserProtos.User {
        val output = FileInputStream("$outputDir/users-${rolloverTime.epochSecond}.bin")
        return UserProtos.User.parseDelimitedFrom(output)
    }
}