package it.vergeit.gtrclassic.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import it.vergeit.gtrclassic.GtrClassic
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    var greatWidget: GreatWidget? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getIntExtra(REQUEST_CODE, 0) != GREATWIDGET_CODE) {
            Log.e(TAG, "AlarmReceiver unknown request code!")
        } else {
            updateGreatWidget(context)
        }
    }

    private fun updateGreatWidget(context: Context) {
        val now = Calendar.getInstance()
        val hours = now[Calendar.HOUR_OF_DAY]
        val minutes = now[Calendar.MINUTE]
        val seconds = now[Calendar.SECOND]
        aquireWakeLock(context)
        Log.i(TAG, String.format("AlarmReceiver onReceive %02d:%02d:%02d ", now[Calendar.HOUR_OF_DAY], now[Calendar.MINUTE], now[Calendar.SECOND]))
        greatWidget = GtrClassic.getGreatWidget()
        val greatWidget = greatWidget
        //greatWidget.onDataUpdate(DataType.TIME, new Time(seconds, minutes, hours, -1));
        greatWidget?.scheduleUpdate() ?: Log.e(TAG, "AlarmReceiver error getting widget instance!")
    }

    private fun aquireWakeLock(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        pm?.newWakeLock(6, "GreatFit:GreatWidget")?.acquire(1000)
                ?: Log.e(TAG, "AlarmReceiver null PowerManager!")
    }

    companion object {
        const val GREATWIDGET_CODE = 1234
        const val REQUEST_CODE = "code"
        private const val TAG = "VergeIT-LOG"
    }
}