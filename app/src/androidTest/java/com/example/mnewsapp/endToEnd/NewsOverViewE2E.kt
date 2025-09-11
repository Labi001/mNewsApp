package com.example.mnewsapp.endToEnd

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.example.mnewsapp.domain.utils.TestTags
import com.example.mnewsapp.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@HiltAndroidTest
class NewsOverViewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navHostController: NavHostController


  //  private lateinit var repositoryFake: NewsRepositoryFake


    @SuppressLint("ViewModelConstructorInComposable")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class,
        ExperimentalMaterial3Api::class
    )
    @Before
    fun setUp() {

        hiltRule.inject()

//        composeRule.setContent {
//
//            MNewsAppTheme {
//
//                navHostController = rememberNavController()
//
//              //  repositoryFake = NewsRepositoryFake()
//
//
//                val snackbarHostState = remember { SnackbarHostState() }
//                val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//                val context = LocalContext.current
//                val activity = context as Activity
//                val windowSizeClass = calculateWindowSizeClass(activity = activity)
//                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//
//             //   NavigationSetup(windowSize = windowSizeClass.widthSizeClass)
//
//
//
//
//                Scaffold (modifier = Modifier.fillMaxSize(),
//                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
//                    containerColor = MaterialTheme.colorScheme.background,
//                    contentColor = MaterialTheme.colorScheme.onBackground,
//                    snackbarHost = {
//
//                        SnackbarHost(hostState = snackbarHostState){
//
//                            Snackbar(
//                                snackbarData = it,
//                                containerColor = MaterialTheme.colorScheme.primary,
//                                contentColor = MaterialTheme.colorScheme.background,
//                            )
//
//                        }
//
//                    },
//                    content = { innerPadding ->
//
//                        val onBoardViewModel: OnBoardViewModel = hiltViewModel()
//                        val sharedViewModel: SharedViewModel = hiltViewModel()
//                        val bookMarkViewModel: BookMarkViewModel = hiltViewModel()
//                        val searchViewModel: SearchViewModel = hiltViewModel()
//                        val detailViewModel: DetailViewModel = hiltViewModel()
//
//                        SharedTransitionLayout {
//
//                            NavHost(
//                                modifier = Modifier
//                                    .padding(bottom = innerPadding.calculateBottomPadding()),
//                                navController = navHostController,
//                                startDestination = Routes.HomeScreen,
//                                enterTransition = { fadeIn(animationSpec = tween(300)) },
//                                exitTransition = { fadeOut(animationSpec = tween(300)) },
//                                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
//                                popExitTransition = { fadeOut(animationSpec = tween(300)) }
//                            )
//                            {
//
//
//                                composable<Routes.HomeScreen> {
//
//                                    HomeScreen(
//                                        navController = navHostController,
//                                        sharedViewModel = sharedViewModel,
//                                        animatedVisibilityScope = this,
//                                        drawerState = drawerState
//                                    )
//
//
//                                }
//
//                                composable<Routes.BookmarksScreen> {
//
//                                    BookMarkScreen(
//                                        navController = navHostController,
//                                        snackbarHostState = snackbarHostState,
//                                        animatedVisibilityScope = this,
//                                        sharedViewModel = sharedViewModel,
//                                        event = bookMarkViewModel.event,
//                                        viewModel = bookMarkViewModel,
//                                        windowSize = windowSizeClass.widthSizeClass,
//                                        innerPadding = innerPadding
//                                    )
//
//
//                                }
//
//                                composable<Routes.SearchScreen> {
//                                   // shouldShowBottomBar.value = true
//
//
//                                    val state by searchViewModel.state.collectAsStateWithLifecycle()
//
//                                    SearchScreen(
//                                        navController = navHostController,
//                                        onEvent = searchViewModel::onEvent,
//                                        state = state,
//                                        scrollBehavior = topScrollBehavior,
//                                        animatedVisibilityScope = this,
//                                        sharedViewModel = sharedViewModel,
//                                        windowSize = windowSizeClass.widthSizeClass)
//
//                                }
//
//                                composable<Routes.DetailScreen>  {
//                                  //  shouldShowBottomBar.value = false
//
//                                    DetailScreen(
//                                        navController = navHostController,
//                                        sharedViewModel,
//                                        animatedVisibilityScope = this,
//                                        viewModel = detailViewModel,
//                                        event = detailViewModel.event,
//                                        snackbarHostState = snackbarHostState,
//                                    )
//
//
//                                }
//
//                                composable<Routes.SecondScreen> {
//
//                                   // shouldShowBottomBar.value = false
//
//                                    val secondArg = it.toRoute<Routes.SecondScreen>()
//
//                                    SecondScreen(
//                                        navController = navHostController,
//                                        query = secondArg.query,
//                                        sharedViewModel = sharedViewModel,
//                                        scrollBehavior = topScrollBehavior,
//                                        isSportCategory = secondArg.isSportCategory,
//                                        isSource = secondArg.isSource,
//                                        sourceName = secondArg.sourceName,
//                                        animatedVisibilityScope = this,
//                                        windowSize = windowSizeClass.widthSizeClass
//                                    )
//
//
//                                }
//
//                                composable<Routes.SportScreen>  {
//
//                                  //  shouldShowBottomBar.value = false
//
//                                    val sportArg = it.toRoute<Routes.SportScreen>()
//
//                                    SportScreen(
//                                        icon = sportArg.icon,
//                                        title = sportArg.title,
//                                        query = sportArg.title,
//                                        navController = navHostController,
//                                        sharedViewModel = sharedViewModel,
//                                        animatedVisibilityScope = this,
//                                        windowSize = windowSizeClass.widthSizeClass
//
//                                    )
//
//                                }
//
//                                composable<Routes.OnboardingScreen>  {
//
//                                   // shouldShowBottomBar.value = false
//
//                                    OnboardingScreen(
//                                        navController = navHostController,
//                                        viewModel = onBoardViewModel
//                                    )
//
//                                }
//
//
//                            }
//
//
//                        }
//
//                    })
//
//
//            }
//
//
//
//        }


    }



    @Test
    fun navigateToEachBottomTab_andVerifyScreenContent() {

        // Tap on Search tab
        composeRule.onNodeWithTag("Search").performClick()

        // Assert Search screen is displayed
        composeRule.onNodeWithText("Search").assertIsDisplayed()

        // Tap on Bookmarks
        composeRule.onNodeWithTag("Bookmarks").performClick()
        composeRule.onNodeWithText("Bookmarks").assertIsDisplayed()

        // Back to Home
        composeRule.onNodeWithTag("Home").performClick()
        composeRule.onNodeWithText("Home").assertIsDisplayed()
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun app_can_navigate_with_drawer() {

        composeRule.onNodeWithTag(TestTags.DrawerBtn).performClick()

         composeRule.waitForIdle()

        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Science").performClick()
        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
       // composeRule.waitForIdle()
       // composeRule.onNodeWithTag(TestTags.Second).assertIsDisplayed()

//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = false, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()
//
//
//
//
//        composeRule.onNodeWithTag(TestTags.DrawerBtn,useUnmergedTree = true).performClick()
//
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Sport").performClick()
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
//       // composeRule.waitForIdle()
//
//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = true, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()
//
//
//        composeRule.onNodeWithTag(TestTags.DrawerBtn,useUnmergedTree = true).performClick()
//
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Business").performClick()
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
////        composeRule.waitForIdle()
////        composeRule.onNodeWithTag(TestTags.Second).assertIsDisplayed()
//
//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = false, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()
//
//
//
//
//        composeRule.onNodeWithTag(TestTags.DrawerBtn,useUnmergedTree = true).performClick()
//
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Health").performClick()
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
////        composeRule.waitForIdle()
////        composeRule.onNodeWithTag(TestTags.Second).assertIsDisplayed()
//
//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = false, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()
//
//
//
//        composeRule.onNodeWithTag(TestTags.DrawerBtn,useUnmergedTree = true).performClick()
//
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Entertainment").performClick()
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
////        composeRule.waitForIdle()
////        composeRule.onNodeWithTag(TestTags.Second).assertIsDisplayed()
//
//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = false, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()
//
//        composeRule.onNodeWithTag(TestTags.DrawerBtn,useUnmergedTree = true).performClick()
//
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Technology").performClick()
//        composeRule.onNodeWithTag(TestTags.DrawerContainer).assertDoesNotExist()
////        composeRule.waitForIdle()
////        composeRule.onNodeWithTag(TestTags.Second).assertIsDisplayed()
//
//        assertThat(
//            navHostController
//                .currentDestination
//                ?.route
//                ?.startsWith(Routes.SecondScreen(query = "sport", isSportCategory = false, isSource = true, sourceName = "science")::class.qualifiedName?:"")
//        ).isTrue()






    }





}