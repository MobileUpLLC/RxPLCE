package ru.mobileup.rxpagingloading.sample.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.visibility
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.android.synthetic.main.footer_paging.view.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_error_view.*
import kotlinx.android.synthetic.main.layout_progress_view.*
import me.dmdev.rxpm.base.PmActivity
import me.dmdev.rxpm.bindTo
import ru.mobileup.rxpagingloading.sample.R

class PagingActivity : PmActivity<PagingPm>() {

    private val itemsRepository = ItemsRepository()

    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)

        val footer =
            LayoutInflater.from(this)
                .inflate(R.layout.footer_paging, swipeRefreshLayout, false)

        itemsAdapter = ItemsAdapter(footer)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = itemsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(
                EndlessRecyclerViewScrollListener(
                    presentationModel.nextPageAction.consumer::accept
                )
            )
        }

        setupSettings()
    }

    override fun providePresentationModel(): PagingPm {
        return PagingPm(itemsRepository)
    }

    override fun onBindPresentationModel(pm: PagingPm) {

        pm.content bindTo { itemsAdapter.submitList(it) }

        pm.scrollToTop bindTo { recyclerView?.smoothScrollToPosition(0) }

        pm.pageIsLoading bindTo itemsAdapter.footerView.progressBar.visibility()
        pm.pageErrorVisible bindTo itemsAdapter.footerView.pageLoadingErrorText.visibility()
        pm.pageErrorVisible bindTo itemsAdapter.footerView.retryButton.visibility()

        pm.isLoading bindTo progressBar.visibility()
        pm.isRefreshing bindTo swipeRefreshLayout::setRefreshing
        pm.refreshEnabled bindTo swipeRefreshLayout::setEnabled

        pm.contentViewVisible bindTo recyclerView.visibility()
        pm.emptyViewVisible bindTo emptyView.visibility()
        pm.errorViewVisible bindTo errorView.visibility()

        pm.showError bindTo {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        swipeRefreshLayout.refreshes() bindTo pm.refreshAction
        retryButton.clicks() bindTo pm.retryAction
        itemsAdapter.footerView.retryButton.clicks() bindTo pm.retryNextPageAction
    }

    private fun setupSettings() {

        val spinner = toolbar.menu.findItem(R.id.spinner).actionView as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.repository_settings, android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> itemsRepository.mode = ItemsRepository.Mode.NORMAL
                    1 -> itemsRepository.mode = ItemsRepository.Mode.ERROR
                    2 -> itemsRepository.mode = ItemsRepository.Mode.EMPTY_DATA
                    3 -> itemsRepository.mode = ItemsRepository.Mode.RANDOM_ERROR
                }
            }

        }
    }
}