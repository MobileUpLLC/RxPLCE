package ru.mobileup.rxpagingloading.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_launch.*
import ru.mobileup.rxpagingloading.sample.loading.LoadingActivity
import ru.mobileup.rxpagingloading.sample.paging.PagingActivity
import ru.mobileup.rxpagingloading.sample.refreshing.RefreshingActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        pagingSample.setOnClickListener {
            launchActivity(PagingActivity::class.java)
        }

        loadingSample.setOnClickListener {
            launchActivity(LoadingActivity::class.java)
        }

        refreshingSample.setOnClickListener {
            launchActivity(RefreshingActivity::class.java)
        }
    }

    private fun launchActivity(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}