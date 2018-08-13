package com.polsec.pyrky.dagger;

import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.fragment.HomeFragment;

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
