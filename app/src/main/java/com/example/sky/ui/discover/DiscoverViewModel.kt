package com.example.sky.ui.discover

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoverViewModel : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query

    private fun searchQueryChanged(query: String) {
        _query.value = query
    }

    inner class DiscoverSearchObserver(private var query: String) :
        BaseObservable() {

        var search: String
            @Bindable get() = query
            set(value) {
                query = value
                notifyPropertyChanged(BR.discoverSearch)
                searchQueryChanged(value)
            }
    }
}

