package soutvoid.com.DsrWeatherApp.ui.util

import io.realm.RealmList
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.domain.triggers.area.Area
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.SavedCondition
import soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod.Time
import soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod.TimeExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod.TimePeriod
import soutvoid.com.DsrWeatherApp.interactor.triggers.data.NewTriggerRequest
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime

object TriggersUtils {

    fun createRequest(savedTrigger: SavedTrigger): NewTriggerRequest {
        return NewTriggerRequest(
                createTimePeriod(savedTrigger.notificationTimes),
                createConditions(savedTrigger.conditions),
                listOf(Area(listOf(savedTrigger.location.longitude, savedTrigger.location.latitude))))
    }

    private fun createTimePeriod(notificationsTimes: List<NotificationTime>): TimePeriod {
        val start = Time(
                TimeExpression.after,
                0
        )
        val end = Time(
                TimeExpression.after,
                5 * 24 * 60 * 60 * 1000
        )
        return TimePeriod(start, end)
    }

    private fun createConditions(savedConditions: RealmList<SavedCondition>): List<Condition> {
        return savedConditions.map { Condition(
                ConditionName.valueOf(it.name),
                ConditionExpression.valueOf(it.expression),
                it.amount
        ) }
    }

}