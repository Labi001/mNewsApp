package com.example.mnewsapp.presentation.navigation

import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mnewsapp.R
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.presentation.screens.onboard.OnboardingScreen
import com.example.mnewsapp.presentation.screens.bookmark.BookMarkScreen
import com.example.mnewsapp.presentation.screens.detail.DetailScreen
import com.example.mnewsapp.presentation.screens.detail.DetailViewModel
import com.example.mnewsapp.presentation.screens.search.SearchScreen
import com.example.mnewsapp.presentation.screens.SportScreen
import com.example.mnewsapp.presentation.screens.bookmark.BookMarkViewModel
import com.example.mnewsapp.presentation.screens.home.HomeScreen
import com.example.mnewsapp.presentation.screens.onboard.OnBoardViewModel
import com.example.mnewsapp.presentation.screens.search.SearchViewModel
import com.example.mnewsapp.presentation.screens.second.SecondScreen
import com.example.mnewsapp.ui.theme.customTitleColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
)
@Composable
fun NavigationSetup(
    windowSize: WindowWidthSizeClass,
){

    val navController = rememberNavController()

    val sharedViewModel: SharedViewModel = hiltViewModel()

    val onBoardViewModel: OnBoardViewModel = hiltViewModel()

    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    val isTouchExplorationEnabled = remember {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        am.isEnabled && am.isTouchExplorationEnabled
    }

    val shouldShowBottomBar = remember {
        mutableStateOf(false)
    }

    val items = listOf(
        Routes.HomeScreen,
        Routes.BookmarksScreen,
        Routes.SearchScreen,
    )

    val snackbarHostState = remember { SnackbarHostState() }


    val isFirstTime by onBoardViewModel.isFirstTime.collectAsState()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    LaunchedEffect(currentRoute) {

        shouldShowBottomBar.value = items.any { route ->
            currentRoute?.startsWith(route::class.qualifiedName ?: "") == true

        }
    }




    Scaffold(modifier = Modifier.fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        snackbarHost = {

            SnackbarHost(hostState = snackbarHostState)
            {

                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background,
                )

            }



        },
        bottomBar = {


                if(shouldShowBottomBar.value){

                    BottomNavigationBar(
                        navController,
                        scrollBehavior,
                        isTouchExplorationEnabled,
                        )

                }



        },
        containerColor = MaterialTheme.colorScheme.background
        )
    { innerPadding ->

        SharedTransitionLayout {

            NavHost(
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                navController = navController,
                startDestination = if(isFirstTime == true) Routes.OnboardingScreen else Routes.HomeScreen,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }

            )
            {


                composable<Routes.HomeScreen> {

                    shouldShowBottomBar.value = true
                    HomeScreen(navController = navController,
                        sharedViewModel = sharedViewModel,
                        animatedVisibilityScope = this,
                        drawerState = drawerState)


                }

                composable<Routes.BookmarksScreen> {

                    shouldShowBottomBar.value = true

                    val viewModel: BookMarkViewModel = hiltViewModel()

                    BookMarkScreen(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        animatedVisibilityScope = this,
                        sharedViewModel = sharedViewModel,
                        event = viewModel.event,
                        viewModel = viewModel,
                        windowSize = windowSize,
                        innerPadding = innerPadding

                    )


                }

                composable<Routes.SearchScreen> {
                    shouldShowBottomBar.value = true

                    val viewModel: SearchViewModel = hiltViewModel()

                    val state by viewModel.state.collectAsStateWithLifecycle()

                    SearchScreen(
                        navController = navController,
                        onEvent = viewModel::onEvent,
                        state = state,
                        scrollBehavior = topScrollBehavior,
                        animatedVisibilityScope = this,
                        sharedViewModel = sharedViewModel,
                        windowSize = windowSize)

                }


                composable<Routes.DetailScreen>  {
                    shouldShowBottomBar.value = false

                    val viewModel: DetailViewModel = hiltViewModel()


                    DetailScreen(
                        navController = navController,
                        sharedViewModel,
                        animatedVisibilityScope = this,
                        viewModel = viewModel,
                        event = viewModel.event,
                        snackbarHostState = snackbarHostState,
                    )


                }

                composable<Routes.SecondScreen> {

                    shouldShowBottomBar.value = false

                    val secondArg = it.toRoute<Routes.SecondScreen>()

                    SecondScreen(
                        navController = navController,
                        query = secondArg.query,
                        sharedViewModel = sharedViewModel,
                        scrollBehavior = topScrollBehavior,
                        isSportCategory = secondArg.isSportCategory,
                        isSource = secondArg.isSource,
                        sourceName = secondArg.sourceName,
                        animatedVisibilityScope = this,
                        windowSize = windowSize
                    )


                }

                composable<Routes.SportScreen>  {

                    shouldShowBottomBar.value = false

                    val sportArg = it.toRoute<Routes.SportScreen>()

                    SportScreen(
                        icon = sportArg.icon,
                        title = sportArg.title,
                        query = sportArg.title,
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        animatedVisibilityScope = this,
                        windowSize = windowSize

                    )

                }


                composable<Routes.OnboardingScreen>  {

                    shouldShowBottomBar.value = false

                    OnboardingScreen(navController = navController,
                        viewModel = onBoardViewModel)

                }

            }


        }



    }




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    scrollBehavior: BottomAppBarScrollBehavior,
    isTouchExplorationEnabled: Boolean,

) {

    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Search,
        BottomNavItems.BookMarks,
    )


    BottomAppBar(modifier = Modifier
        .fillMaxWidth()
        .drawWithContent {
            drawContent()
            drawLine(
                color = customTitleColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2f

            )

        },
        containerColor = MaterialTheme.colorScheme.background,
        scrollBehavior = if (!isTouchExplorationEnabled) scrollBehavior else null,
       )
    {



        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


        items.forEach{ item ->

            val isSelected = currentRoute?.substringBefore("?") == item.route::class.qualifiedName

            NavigationBarItem(
                modifier = Modifier.height(56.dp)
                    .testTag(item.title),
                selected = isSelected,
                onClick = {

                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { startRout->

                            popUpTo(startRout) {

                                saveState = true
                            }

                        }

                        launchSingleTop = true
                        restoreState = true

                    }

                },
                icon = {

                    Icon(painter = painterResource(id = if (isSelected) item.iconFilled else item.iconUnfilled),
                        contentDescription = "Bottom Icons",
                        modifier = Modifier.size(26.dp),
                        tint = if(isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline)

                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )


        }




    }




}

sealed class BottomNavItems(val route:Any,val title:String,val iconUnfilled:Int,val iconFilled:Int){

    data object Home: BottomNavItems(Routes.HomeScreen,"Home", R.drawable.ic_home,R.drawable.ic_home_fill)
    data object Search: BottomNavItems( Routes.SearchScreen ,"Search", R.drawable.ic_search,R.drawable.ic_search)
    data object BookMarks: BottomNavItems(Routes.BookmarksScreen,"Bookmarks", R.drawable.ic_bookmarks,R.drawable.ic_bookmarks_filled)

}