package soutvoid.com.DsrWeatherApp.ui.base.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import soutvoid.com.DsrWeatherApp.util.SdkUtil

abstract class TranslucentStatusActivityView : BaseActivityView() {

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        if (SdkUtil.supportsKitkat())
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}