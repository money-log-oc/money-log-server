package oc.moneylog.server.application.home

import oc.moneylog.server.dto.HomeSummaryResponse

interface HomeSummaryUseCase {
    fun get(): HomeSummaryResponse
}
