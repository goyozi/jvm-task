package com.interview.jvmtask

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.time.Clock
import java.time.Duration
import javax.annotation.PostConstruct

@Configuration
class Config {

    @PostConstruct
    fun createOutputDir() {
        File(outputDir()).mkdirs()
    }

    @Bean
    fun clock() = Clock.systemDefaultZone()

    @Bean
    fun timeout() = Duration.ofDays(1)

    @Bean
    fun outputDir() = "output/"
}