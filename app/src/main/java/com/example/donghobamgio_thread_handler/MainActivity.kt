package com.example.donghobamgio_thread_handler

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var runnable: Runnable? = null
    var startTime: Long = 0
    var systemTime: Long = 0
    var pauseTime: Long = 0
    var textSize: Float = 1F;
    lateinit var handler: Handler
    var checkStart: Boolean = false
    var checkTextSizeToSmall : Boolean = false
    var i: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(Looper.getMainLooper()){
            when(it.what) {
                UPDATE_UI -> {
                    txt_time.text = it.obj.toString()
                    txt_time.textSize = textSize
                    if (!checkTextSizeToSmall && textSize <= 90) {
                        textSize ++
                        if (textSize == 90f){
                            checkTextSizeToSmall = true
                        }
                    } else{
                        textSize --;
                        if (textSize == 1f){
                            checkTextSizeToSmall = false
                        }
                    }
                }
                else -> {
                }
            }
            return@Handler true
        }


        btn_start.setOnClickListener({
            if (checkStart){
                return@setOnClickListener
            }
            checkStart = true
            startTime = SystemClock.uptimeMillis();
            runnableTime().start()
        })
        btn_stop.setOnClickListener({
            if (!checkStart){
                return@setOnClickListener
            }
            pauseTime = 0;
            checkStart = false
            runnableTime().interrupt()
        })
        btn_pause.setOnClickListener({
            if (!checkStart){
                return@setOnClickListener
            }
            checkStart = false
            pauseTime += systemTime
            runnableTime().interrupt()
        })
    }

    fun runnableTime() : Thread {
        val thread = Thread {

                    while (checkStart) {
                        systemTime = SystemClock.uptimeMillis() - startTime;
                        var updateTime = pauseTime + systemTime;
                        var secs = updateTime / 1000;
                        var mins = secs / 60;
                        secs = secs % 60;
                        var milliseconds = updateTime % 1000

                        Log.e("!121", "" + txt_time.text)

                        var mess: Message = Message()
                        mess.what = UPDATE_UI
                        mess.obj =
                            "" + mins + ":" + String.format("%02d", secs) + ":" + String.format(
                                "%03d",
                                milliseconds
                            )
                        Thread.sleep(100)
                        Log.e("!1211111", Thread.currentThread().name)
                        handler.sendMessage(mess)
                    }
      }
        return thread
    }


    companion object{
        const val UPDATE_UI = 1
    }
}
