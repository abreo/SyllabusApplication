package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.scope.PerUser;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/12.
 */

@PerUser
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public abstract class UserComponent {

    private static UserComponent INSTANCE;

    public static UserComponent buildInstance(AppComponent appComponent) {
        INSTANCE = DaggerUserComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule())
                .build();
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }
}