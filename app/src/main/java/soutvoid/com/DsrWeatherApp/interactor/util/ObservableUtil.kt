package soutvoid.com.DsrWeatherApp.interactor.util

import java.util.Arrays

import rx.Observable
import rx.Scheduler
import rx.functions.Func2
import rx.functions.Func3
import rx.functions.Func4
import rx.functions.Func5
import rx.functions.Func6
import rx.functions.Func7
import rx.functions.Func8
import rx.functions.Func9
import rx.functions.FuncN
import rx.functions.Functions

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

    fun <T1, T2, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, combineFunction: Func2<in T1, in T2, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>,
                                                combineFunction: Func3<in T1, in T2, in T3, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>,
                                                    combineFunction: Func4<in T1, in T2, in T3, in T4, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>,
                                                        combineFunction: Func5<in T1, in T2, in T3, in T4, in T5, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>,
                                                            combineFunction: Func6<in T1, in T2, in T3, in T4, in T5, in T6, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6), Functions.fromFunc(combineFunction))
    }


    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>,
                                                                combineFunction: Func7<in T1, in T2, in T3, in T4, in T5, in T6, in T7, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>, o8: Observable<out T8>,
                                                                    combineFunction: Func8<in T1, in T2, in T3, in T4, in T5, in T6, in T7, in T8, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), Functions.fromFunc(combineFunction))
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineLatestDelayError(scheduler: Scheduler, o1: Observable<out T1>, o2: Observable<out T2>, o3: Observable<out T3>, o4: Observable<out T4>, o5: Observable<out T5>, o6: Observable<out T6>, o7: Observable<out T7>, o8: Observable<out T8>,
                                                                        o9: Observable<out T9>,
                                                                        combineFunction: Func9<in T1, in T2, in T3, in T4, in T5, in T6, in T7, in T8, in T9, out R>): Observable<R> {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), Functions.fromFunc(combineFunction))
    }

    private fun <T> toParallel(sources: List<Observable<out T>>, scheduler: Scheduler): List<Observable<out T>> {
        return sources
                .map({ source -> source.subscribeOn(scheduler) })
                .toList()
    }
}
