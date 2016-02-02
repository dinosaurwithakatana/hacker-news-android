package io.dwak.holohackernews.app.ui.detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.extension.getLong
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.extension.with

class StoryDetailActivity : AppCompatActivity(){

    companion object {
        val ITEM_ID = "ITEM_ID"

        fun newIntent(context : Context, itemId : Long) : Intent {
            return Intent(context, StoryDetailActivity::class.java).with {
                putExtra(ITEM_ID, itemId)
            }
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        Slidr.attach(this, SlidrConfig.Builder().edge(true)
                .position(SlidrPosition.LEFT)
                .build())
        navigateTo(StoryDetailFragment.newInstance(getLong(ITEM_ID)), addToBackStack = false)
    }

    override fun onOptionsItemSelected(item : MenuItem?) : Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}