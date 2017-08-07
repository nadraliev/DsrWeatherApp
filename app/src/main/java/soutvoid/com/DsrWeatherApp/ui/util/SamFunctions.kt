package soutvoid.com.DsrWeatherApp.ui.util

import android.animation.Animator
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.Animation
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import soutvoid.com.DsrWeatherApp.app.log.Logger

/**
 * функции, позволяющие реализовать одну необходимую функцию, не имплементируя весь интерфейс
 */


/**
 * листенер получения нового положения через геолокацию
 */
fun LocationListener(listener: (location: Location) -> Unit): LocationListener = object : LocationListener {
    override fun onLocationChanged(p0: Location?) {
        p0?.let(listener)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}

/**
 * callback, вызываемый при свайпе элемента RecyclerView
 * @param [swipeDirections] направления свайпа
 */
fun SimpleItemSwipeCallback(swipeDirections: Int, callback: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit)
        : ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, swipeDirections) {
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        //ignored
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        viewHolder?.let { callback(it, direction) }
    }
}

/**
 * листенер, вызываемый PlaceAutocompleteFragment при выборе места пользователем
 */
fun PlaceSelectionListener(listener: (place: Place) -> Unit): PlaceSelectionListener =
        object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place?) {
                p0?.let { listener(it) }
            }

            override fun onError(p0: Status?) {
                p0?.statusMessage?.let { Logger.d(it) }
            }
        }

/**
 * выполнить код, если оба аргумента не null
 */
inline fun <A, B, R> ifNotNull(a: A?, b: B?, code: (A,B) -> R) {
    if (a != null && b != null)
        code(a,b)
}

fun SnackbarDismissedListener(listener: (transientBottomBar: Snackbar?, event: Int) -> Unit)
        : BaseTransientBottomBar.BaseCallback<Snackbar> =
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                listener(transientBottomBar, event)
            }
        }


fun AnimationEndedListener(listener: (animator: Animator?) -> Unit)
    :Animator.AnimatorListener = object : Animator.AnimatorListener {

    override fun onAnimationRepeat(p0: Animator?) {}

    override fun onAnimationEnd(p0: Animator?) { listener(p0) }

    override fun onAnimationCancel(p0: Animator?) {}

    override fun onAnimationStart(p0: Animator?) {}
}

fun DrawerClosedListener(listener: (drawerView: View?) -> Unit)
    :DrawerLayout.DrawerListener = object : DrawerLayout.DrawerListener {
    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
    }

    override fun onDrawerClosed(drawerView: View?) {
        listener(drawerView)
    }

    override fun onDrawerOpened(drawerView: View?) {
    }
}

fun ViewDetachedListener(listener: (View) -> Unit): View.OnAttachStateChangeListener =
        object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View?) {
                p0?.let { listener(it) }
            }

            override fun onViewAttachedToWindow(p0: View?) {}
        }