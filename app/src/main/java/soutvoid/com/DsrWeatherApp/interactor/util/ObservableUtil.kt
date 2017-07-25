package soutvoid.com.DsrWeatherApp.interactor.util

import rx.Observable
import rx.Scheduler
import rx.functions.FuncN
import rx.functions.Functions
import java.util.*

/**
 * содержит недостающие методы для Observable
 */
object ObservableUtil {

    /**
     * работает также как {@see Observable#combineLatestDelayError}, но еще распараллеливает запросы
     */
    fun <T, R> combineLatestDelayError(scheduler: Scheduler,
                                       sources: List<Observable<out T>>,
                                       combineFunction: FuncN<out R>): Observable<R> {
        var sourcesNew = sources
        sourcesNew = toParallel(sourcesNew, scheduler)
        return Observable.combineLatestDelayError(sourcesNew, combineFunction)
    }

    fun <T1, T2, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, combineFunction: Function2<T1, T2, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>,
                                                combineFunction: Function3<T1, T2, T3, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>,
                                                    combineFunction: Function4<T1, T2,  T3,  T4, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>,
                                                        combineFunction: Function5< T1,  T2,  T3,  T4,  T5, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>,
                                                            combineFunction: Function6< T1,  T2,  T3,  T4,  T5,  T6, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6), Functions.fromFunc(combineFunction))
    }


    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>,
                                                                combineFunction: Function7< T1,  T2,  T3,  T4,  T5,  T6,  T7, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>, o8: Observable<out T8>,
                                                                    combineFunction: Function8< T1,  T2,  T3,  T4,  T5,  T6,  T7,  T8, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>, o8: Observable<out T8>,
                                                                        o9: Observable<out T9>,
                                                                        combineFunction: Function9< T1,  T2,  T3,  T4,  T5,  T6,  T7,  T8,  T9, R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), Functions.fromFunc(combineFunction))
    }

    private fun <T> toParallel(sources: List<Observable<out T>>, scheduler: Scheduler): List<Observable<out T>> {
        return sources
                .map({ source -> source.subscribeOn(scheduler) })
                .toList()
    }
}
