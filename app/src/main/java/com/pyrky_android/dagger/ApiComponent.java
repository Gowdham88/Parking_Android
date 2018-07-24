package com.pyrky_android.dagger;

import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.fragment.HomeFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by thulirsoft on 7/16/18.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {

    void inject(HomeActivity activity);
    void inject(HomeFragment homeFragment);

}
