import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.opeqesample.R
import com.example.opeqesample.activities.HomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers


// test in androidTest sample


@RunWith(AndroidJUnit4ClassRunner::class)
class SampleHomeScreenUiTest {

  /* @get:Rule
   var rule:ActivityScenarioRule<HomeActivity> =ActivityScenarioRule<HomeActivity>(
           HomeActivity::class.java)
*/
   @get:Rule
   var activityRule = ActivityTestRule(HomeActivity::class.java)

   @Before
   fun setup(){

       //val scenario = rule.scenario

   }
    @Before
    fun after(){
        Thread.sleep(10000)

    }


   @Test
   @Throws(InterruptedException::class)
   fun testVisibilityRecyclerView() {
      Thread.sleep(1000)
     onView(ViewMatchers.withId(R.id.home_recycler))
              .inRoot(
                      RootMatchers.withDecorView(
                              Matchers.`is`(
                                      activityRule.activity.window.decorView
                              )
                      )
              )
              .check(matches(isDisplayed()))
   }






}