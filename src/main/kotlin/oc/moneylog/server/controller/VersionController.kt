package oc.moneylog.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class VersionController {
    @GetMapping("/version")
    fun version(): Map<String, String> = mapOf(
        "service" to "money-log-server",
        "version" to "v1-alpha",
        "note" to "includes auth skeleton",
    )
}
