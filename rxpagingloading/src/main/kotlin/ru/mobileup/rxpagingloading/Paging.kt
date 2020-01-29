package ru.mobileup.rxpagingloading

import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 *
 * This interface describes [input][actions] and [output][state] of a state machine.
 * It is used for implementing data loading and paging.
 *
 * @see PagingImpl
 */
interface Paging<T> {

    /**
     * UI-events or intentions to change a state of data.
     */
    enum class Action { REFRESH, FORCE_REFRESH, LOAD_NEXT_PAGE }

    /**
     * The observer of changing [state][State].
     */
    val state: Observable<State<T>>

    /**
     * The observer of changing [state][State].
     */
    val actions: Consumer<Action>

    /**
     * LCE and paging state.
     *
     * @property[content] loaded data.
     * @property[loading] indicates that the first page is loading or updating.
     * @property[error] an error occurred when loading or updating data (the first page loading).
     * @property[pageLoading] indicates that the next page is loading.
     * @property[pagingError] an error occurred when loading the next page.
     * @property[lastPage] the last page has been loaded.
     */
    data class State<T>(
        val content: List<T>? = null,
        val loading: Boolean = false,
        val error: Throwable? = null,
        val pageLoading: Boolean = false,
        val pagingError: Throwable? = null,
        val lastPage: Page<T>? = null
    ) {

        /**
         * Indicates that the end of the list has been reached and that it is no longer necessary to request the next page.
         */
        val isEndReached: Boolean get() = lastPage?.isEndReached ?: false
    }

    /**
     * Describes the page data.
     * Here you can define custom logic to determine that the end of the list is reached.
     */
    interface Page<T> {
        val items: List<T>
        val lastItem: T? get() = items.lastOrNull()
        val isEndReached: Boolean
    }
}