package ru.mobileup.rxpagingloading.sample.loading

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.visibility
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_error_view.*
import kotlinx.android.synthetic.main.layout_progress_view.*
import me.dmdev.rxpm.base.PmActivity
import me.dmdev.rxpm.bindTo
import ru.mobileup.rxpagingloading.sample.R

class LoadingActivity : PmActivity<LoadingPm>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    override fun providePresentationModel(): LoadingPm {
        return LoadingPm(DataRepository())
    }

    override fun onBindPresentationModel(pm: LoadingPm) {

        pm.content bindTo { contentView.text = it.text }

        pm.isLoading bindTo progressBar.visibility()

        pm.contentViewVisible bindTo contentView.visibility()
        pm.emptyViewVisible bindTo emptyView.visibility()
        pm.errorViewVisible bindTo errorView.visibility()

        retryButton.clicks() bindTo pm.retryAction
        forceRefreshButton.clicks() bindTo pm.forceRefreshAction
    }
}