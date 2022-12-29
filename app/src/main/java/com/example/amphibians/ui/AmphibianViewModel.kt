
package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApiService
import kotlinx.coroutines.launch

enum class AmphibianApiStatus { LOADING, ERROR, DONE }

class AmphibianViewModel : ViewModel() {

    // MutableLiveData internal yang menyimpan status permintaan terbaru
    private val _status = MutableLiveData<AmphibianApiStatus>()

    // LiveData eksternal yang tidak dapat diubah untuk status permintaan
    val status: LiveData<AmphibianApiStatus> = _status

    // Secara internal, kami menggunakan MutableLiveData, karena kami akan memperbarui Daftar MarsPhoto
    // dengan nilai baru
    private val _amphibians = MutableLiveData<List<Amphibian>>()

    // Antarmuka LiveData eksternal ke properti tidak dapat diubah, jadi hanya kelas ini yang dapat memodifikasi
    val amphibians: LiveData<List<Amphibian>> = _amphibians

    // Secara internal, kami menggunakan MutableLiveData, karena kami akan memperbarui Daftar MarsPhoto
    // dengan nilai baru
    private val _amphibian = MutableLiveData<Amphibian>()

    // Antarmuka LiveData eksternal ke properti tidak dapat diubah, jadi hanya kelas ini yang dapat memodifikasi
    val amphibian: LiveData<Amphibian> = _amphibian

    init {
        getAmphibians()
    }

    private fun getAmphibians() {
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try {
                _amphibians.value = AmphibianApiService.AmphibianApi.retrofitService.getAmphibians()
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AmphibianApiStatus.ERROR
                _amphibians.value = listOf()
            }
        }
    }

    fun onAmphibianClicked(amphibian: Amphibian) {
        _amphibian.value = amphibian
    }
}
