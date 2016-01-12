package io.dwak.holohackernews.app.network

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.dwak.holohackernews.app.HackerNewsApplication
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.model.json.StoryJson
import retrofit2.mock.BehaviorDelegate
import rx.Observable
import javax.inject.Inject

class MockService(private val delegate : BehaviorDelegate<HackerNewsService>)
: HackerNewsService {
    private val stories = hashMapOf<Long, StoryJson>()
    lateinit var assetManager : AssetManager @Inject set
    val moshi : Moshi

    init {
        HackerNewsApplication.instance.appComponent.inject(this)
        moshi = Moshi.Builder().build()
    }

    override fun getAskHnStories() : Observable<MutableList<StoryJson>> {
        throw UnsupportedOperationException()
    }

    override fun getBestStories() : Observable<MutableList<StoryJson>> {
        throw UnsupportedOperationException()
    }

    override fun getItemDetails(itemId : Long) : Observable<StoryDetailJson> {
        throw UnsupportedOperationException()
    }

    override fun getNewestStories() : Observable<MutableList<StoryJson>> {
        throw UnsupportedOperationException()
    }

    override fun getNewShowHnStories() : Observable<MutableList<StoryJson>> {
        throw UnsupportedOperationException()
    }

    override fun getShowHnStories() : Observable<MutableList<StoryJson>> {
        throw UnsupportedOperationException()
    }

    override fun getTopStories() : Observable<MutableList<StoryJson>> {
        val topStories = loadJsonFromAsset("top.json")
        val response = moshi.adapter<List<StoryJson>>(Types.newParameterizedType(List::class.java, StoryJson::class.java))
                .fromJson(topStories)
        return delegate.returningResponse(response).getTopStories()
    }

    override fun getTopStoriesPageTwo() : Observable<MutableList<StoryJson>> {
        val topStories = loadJsonFromAsset("top2.json")
        val response = moshi.adapter<List<StoryJson>>(Types.newParameterizedType(List::class.java, StoryJson::class.java))
                .fromJson(topStories)
        return delegate.returningResponse(response).getTopStories()
    }

    private fun loadJsonFromAsset(filename : String) : String {
        val jsonFile = assetManager.open(filename)
        val size = jsonFile.available()
        val buffer = ByteArray(size)
        jsonFile.read(buffer)
        jsonFile.close()
        return String(buffer, "UTF-8")
    }
}