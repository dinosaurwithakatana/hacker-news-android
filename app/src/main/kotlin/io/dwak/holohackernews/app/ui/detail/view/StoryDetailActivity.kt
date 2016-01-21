package io.dwak.holohackernews.app.ui.detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.extension.getLong
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.extension.withArgs

class StoryDetailActivity : AppCompatActivity(){

    companion object {
        val ITEM_ID = "ITEM_ID"

        fun newIntent(context : Context, itemId : Long) : Intent {
            return Intent(context, StoryDetailActivity::class.java).withArgs {
                putExtra(ITEM_ID, itemId)
            }
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)
        navigateTo(StoryDetailFragment.newInstance(getLong(ITEM_ID)), addToBackStack = false)
    }
}