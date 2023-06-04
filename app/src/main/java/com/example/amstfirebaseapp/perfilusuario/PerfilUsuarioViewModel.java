package com.example.amstfirebaseapp.perfilusuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PerfilUsuarioViewModel extends ViewModel {

    public class UserUiState {
        private final String uniqueUserId;
        private final String userEmail;
        private final String userRealName;
        private final String userPhoto;

        public UserUiState(String uniqueUserId, String userEmail, String userRealName, String userPhoto) {
            this.uniqueUserId = uniqueUserId;
            this.userEmail = userEmail;
            this.userRealName = userRealName;
            this.userPhoto = userPhoto;
        }
    }

    private final MutableLiveData<UserUiState> userUiState =
            new MutableLiveData<>(new UserUiState(null, null, null, null));

    public LiveData<UserUiState> getUiState() {
        return userUiState;
    }


}
