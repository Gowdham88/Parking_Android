package com.polsec.pyrky.viewmodel;

import android.arch.lifecycle.ViewModel;

/**
 * Created by thulirsoft on 7/18/18.
 */

public class ProfileViewModel extends ViewModel {
   /* //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Hero>> heroList;

    //we will call this method to get the data
    public LiveData<List<Hero>> getHeroes() {
        //if the list is null
        if (heroList == null) {
            heroList = new MutableLiveData<List<Hero>>();
            //we will load it asynchronously from server in this method
            loadHeroes();
        }

        //finally we will return the list
        return heroList;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadHeroes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Hero>> call = api.getHeroes();


        call.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hero>> call, @NonNull Response<List<Hero>> response) {

                //finally we are setting the list to our MutableLiveData
                heroList.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Hero>> call, @NonNull Throwable t) {

            }
        });
    }
*/
}
