package oc.moneylog.server.adapter.`in`.web.home

import io.swagger.v3.oas.annotations.Operation
import oc.moneylog.server.application.home.HomeSummaryUseCase
import oc.moneylog.server.dto.HomeSummaryResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/home")
class HomeController(
    private val homeSummaryUseCase: HomeSummaryUseCase,
) {
    @Operation(summary = "홈 요약 조회")
    @GetMapping("/summary")
    fun homeSummary(): HomeSummaryResponse = homeSummaryUseCase.get()
}
