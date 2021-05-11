package com.accion.photo.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    public MutableLiveData<String> getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

    private MutableLiveData<String> searchValue = new MutableLiveData<>();
}
