package echo.com.surveys.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import echo.com.surveys.R
import echo.com.surveys.adapter.SurveyFragmentPagerAdapter
import echo.com.surveys.model.Auth
import echo.com.surveys.model.AuthRequest
import echo.com.surveys.model.Survey
import echo.com.surveys.model.SurveyRequest
import echo.com.surveys.rest.ApiUtils
import echo.com.surveys.util.Constants
import echo.com.surveys.util.DialogUtils
import echo.com.surveys.util.SharedPrefUtility
import kotlinx.android.synthetic.main.app_bar_survey.*
import kotlinx.android.synthetic.main.content_survey.*
import kotlinx.android.synthetic.main.layout_survey_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SurveyActivity : BaseFragmentActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapter: SurveyFragmentPagerAdapter
    var surveys: ArrayList<Survey> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_survey_activity)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        initAdapter()
        loadSurveys()
    }

    private fun initAdapter() {
        adapter = SurveyFragmentPagerAdapter(getSupportFragmentManager(),surveys)
        viewPager.adapter = adapter
    }


    fun getAccessToken(){
        val authRequest = AuthRequest()
        ApiUtils.getAPIService(this).getToken(authRequest).enqueue(object: Callback<Auth>{
            override fun onFailure(call: Call<Auth>, t: Throwable) {
                DialogUtils.showToast(SurveyActivity.this, getString(R.string.general_error))            }

            override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                if(response.body() != null){
                    SharedPrefUtility.getInstance(SurveyActivity.this).updateAuth(response.body())
                    loadSurveys()
                }
            }

        })

    }

    fun loadSurveys(){
        surveys.clear()
        val token = SharedPrefUtility.getInstance(SurveyActivity.this).auth.accessToken;
        ApiUtils.getAPIService(this).getSurveys(SurveyRequest(token)).enqueue(object: Callback<List<Survey>> {
            override fun onFailure(call: Call<List<Survey>>, t: Throwable) {
                DialogUtils.showToast(SurveyActivity.this, getString(R.string.general_error))
            }

            override fun onResponse(call: Call<List<Survey>>, response: Response<List<Survey>>) {
                if(response.body() != null){
                    SharedPrefUtility.getInstance(SurveyActivity.this).updateAuth(response.body())
                    loadSurveys()
                }
            }
        })
        updateViewPager()
    }

    fun updateViewPager(){
        adapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.survey, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_refresh -> {
                loadSurveys()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}