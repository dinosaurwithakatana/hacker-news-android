package io.dwak.holohackernews.app.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.network.HackerNewsService;
import com.google.gson.*;
import io.dwak.holohackernews.app.network.ReadabilityService;
import io.dwak.holohackernews.app.network.models.ReadabilityArticle;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.lang.reflect.Type;

/**
 * Created by vishnu on 5/3/14.
 */
public class BaseFragment extends Fragment {
    protected View mContainer;
    protected ProgressBar mProgressBar;
    protected HackerNewsService mHackerNewsService;
    protected ReadabilityService mReadabilityService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Long.class, new LongTypeAdapter());
        Gson gson = gsonBuilder.create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://fathomless-island-9288.herokuapp.com/")
                .build();

        mHackerNewsService = restAdapter.create(HackerNewsService.class);

        if (!HoloHackerNewsApplication.isTRAVIS()) {
            RestAdapter readabilityRestAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://readability.com/api/content/v1/")
                    .build();

            mReadabilityService = readabilityRestAdapter.create(ReadabilityService.class);
        }
    }

    protected void showProgress(boolean showProgress){
        mContainer.setVisibility(showProgress ? View.INVISIBLE: View.VISIBLE);
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.INVISIBLE);
    }

    class LongTypeAdapter implements JsonDeserializer<Long>{

        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if("".equals(json.getAsString())){
                return 0l;
            }
            else {
                return json.getAsLong();
            }
        }
    }
}
