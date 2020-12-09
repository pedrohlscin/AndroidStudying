package com.pedroso.beelog.viewmodel

import androidx.lifecycle.*
import com.pedroso.beelog.database.data.Location
import com.pedroso.beelog.database.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    val allLocations: LiveData<List<Location>> = repository.allLocations.asLiveData()

    fun insert(location : Location) = viewModelScope.launch {
        repository.insert(location)
    }
}

class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}