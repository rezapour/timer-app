package me.rezapour.timer_list.mapper

import me.rezapour.domain.model.Routine
import me.rezapour.timer_list.model.TimerItem
import me.rezapour.ui.mapper.Mapper

class TimerItemMapper : Mapper<Routine, TimerItem> {
    override fun mapDomainToUIModel(domain: Routine): TimerItem {
        return TimerItem(
            id = domain.id,
            name = domain.name,
            workSeconds = domain.workSeconds,
            restSeconds = domain.restSeconds,
            rounds = domain.rounds,
        )
    }

    override fun mapDomainToUIModel(domains: List<Routine>): List<TimerItem> {
        return domains.map { mapDomainToUIModel(it) }
    }

    override fun mapUIModelToDomain(item: TimerItem): Routine {
        return Routine(
            id = item.id,
            name = item.name,
            workSeconds = item.workSeconds,
            restSeconds = item.restSeconds,
            rounds = item.rounds,
        )
    }

    override fun mapUIModelToDomain(items: List<TimerItem>): List<Routine> {
        return items.map { mapUIModelToDomain(it) }
    }
}