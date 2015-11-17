package io.dwak.holohackernews.app.dagger.module;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.client.OkClient;

@Module
public class OkClientModule {
    List<Interceptor> mInterceptorList;

    public OkClientModule() {
    }

    public OkClientModule(List<Interceptor> interceptorList) {
        mInterceptorList = interceptorList;
    }

    @Provides
    @Named("okclient")
    OkClient providesOkClient(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(true);
        okHttpClient.setFollowSslRedirects(true);

        if(mInterceptorList != null){
            okHttpClient.networkInterceptors().addAll(mInterceptorList);
        }

        okHttpClient.networkInterceptors().add(new StethoInterceptor());

        return new OkClient(okHttpClient);
    }
}
