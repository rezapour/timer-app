package me.rezapour.timer_list.mapper

import me.rezapour.domain.model.Timer
import me.rezapour.timer_list.model.TimerItem
import me.rezapour.ui.mapper.Mapper

class TimerItemMapper : Mapper<Timer, TimerItem> {
    override fun mapDomainToUIModel(domain: Timer): TimerItem {
        return TimerItem(
            id = domain.id,
            name = domain.name,
            workSeconds = domain.workSeconds,
            restSeconds = domain.restSeconds,
            rounds = domain.rounds,
        )
    }

    override fun mapDomainToUIModel(domains: List<Timer>): List<TimerItem> {
        return domains.map { mapDomainToUIModel(it) }
    }

    override fun mapUIModelToDomain(item: TimerItem): Timer {
        return Timer(
            id = item.id,
            name = item.name,
            workSeconds = item.workSeconds,
            restSeconds = item.restSeconds,
            rounds = item.rounds,
        )
    }

    override fun mapUIModelToDomain(items: List<TimerItem>): List<Timer> {
        return items.map { mapUIModelToDomain(it) }
    }
}