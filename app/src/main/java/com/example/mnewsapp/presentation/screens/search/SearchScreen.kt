package com.example.mnewsapp.presentation.screens.search

import SearchEvent
import SearchState
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mnewsapp.R
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.presentation.components.MSearchBar
import com.example.mnewsapp.presentation.components.SecondShimmerEffect
import com.example.mnewsapp.presentation.navigation.Routes
import com.example.mnewsapp.presentation.screens.home.PublishedTodayRow
import com.example.mnewsapp.ui.theme.boldbodyFontFamily
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.SearchScreen(
    navController: NavController,
    onEvent: (SearchEvent) -> Unit,
    state: SearchState,
    scrollBehavior: TopAppBarScrollBehavior,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedViewModel: SharedViewModel,
    windowSize: WindowWidthSizeClass
) {

    val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(true) {

        delay(200)
        focusRequester.requestFocus()

    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        MSearchBar(
            hint = "Search anything...",
            query = state.query,
            onValueChange = { newQuery ->

                onEvent(
                    SearchEvent.OnQueryChange(
                        query = newQuery,
                        from = yesterday,
                        to = yesterday
                    )
                )
            },

            onClearClick = {

                onEvent(SearchEvent.OnQueryChange(query = "", from = "", to = ""))
            },
            keyboardActions = KeyboardActions(

                onSearch = {

                    if (state.query.isEmpty()) {

                        return@KeyboardActions
                    } else {

                        keyboardController?.hide()
                        onEvent(SearchEvent.OnSearch)
                        focusManager.clearFocus()

                    }


                }


            ),
            scrollBehavior = scrollBehavior

        )


        SearchContent(
            state = state,
            scrollBehavior = scrollBehavior,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedViewModel = sharedViewModel,
            navController = navController,
            windowSize = windowSize

        )


    }


}

@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchContent(
    state: SearchState,
    scrollBehavior: TopAppBarScrollBehavior,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    windowSize: WindowWidthSizeClass
) {

    val isLandscape = windowSize == WindowWidthSizeClass.Medium || windowSize == WindowWidthSizeClass.Expanded

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount = (screenWidthDp / 300.dp).toInt().coerceAtLeast(1)

    if (state.isSearching) {


        SecondShimmerEffect()


    } else if (state.searchedArticles.isNullOrEmpty() && !state.hasSearched) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                Icon(
                    painter = painterResource(id = R.drawable.out_of_stock),
                    contentDescription = "Empty Image",
                    Modifier.size(200.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Search any carater or news !",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = boldbodyFontFamily,
                    color = MaterialTheme.colorScheme.primary
                )


            }


        }


    } else if (state.searchedArticles?.isNotEmpty()==true) {

        LazyVerticalGrid(
            modifier = if(isLandscape) Modifier.fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
            else Modifier.fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(
                start = 2.dp,
                top = 10.dp,
                end = 2.dp,
                bottom = 56.dp
            ),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            val isSingleItem = state.searchedArticles.size == 1
            var shape: RoundedCornerShape

            itemsIndexed(items = state.searchedArticles) { index, article ->

                if(isLandscape){

                    val rowCount = (state.searchedArticles.size + columnCount - 1) / columnCount
                    val rowIndex = index / columnCount
                    val columnIndex = index % columnCount

                    val isFirstRow = rowIndex == 0
                    val isLastRow = rowIndex == rowCount - 1
                    val isFirstColumn = columnIndex == 0
                    val isLastColumn = columnIndex == columnCount - 1 || index == state.searchedArticles.lastIndex

                    shape = RoundedCornerShape(
                        topStart = if (isFirstRow && isFirstColumn) 15.dp else 4.dp,
                        topEnd = if (isFirstRow && isLastColumn) 15.dp else 4.dp,
                        bottomStart = if (isLastRow && isFirstColumn) 15.dp else 4.dp,
                        bottomEnd = if (isLastRow && isLastColumn) 15.dp else 4.dp,
                    )



                }else {

                    shape = when {
                        isSingleItem -> RoundedCornerShape(15.dp) // Only one item in the list
                        index == 0 -> RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 15.dp,
                            bottomStart = 4.dp,
                            bottomEnd = 4.dp
                        ) // First item
                        index == state.searchedArticles.lastIndex -> RoundedCornerShape(
                            bottomStart = 15.dp,
                            bottomEnd = 15.dp,
                            topStart = 4.dp,
                            topEnd = 4.dp
                        ) // Last item
                        else -> RoundedCornerShape(4.dp) // Middle items


                    }


                }




                var lastClickTime by remember { mutableLongStateOf(0L) }
                val now = System.currentTimeMillis()

                PublishedTodayRow(article = article,
                    onPagerClick = { mArticle ->

                        sharedViewModel.addArticle(mArticle)

                        if (now - lastClickTime > 700) {
                            lastClickTime = now
                            navController.navigate(Routes.DetailScreen)

                        }

                    },
                    shape = shape,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onSourceClick = { sourceQuery,sourceName ->

                        if (now - lastClickTime > 700) {
                            lastClickTime = now
                            navController.navigate(Routes.SecondScreen(query = sourceQuery, isSportCategory = false,isSource = true, sourceName = sourceName))

                        }

                    })


            }


        }


    }else if(state.hasSearched && state.searchedArticles.isNullOrEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                Icon(
                    painter = painterResource(id = R.drawable.out_of_stock),
                    contentDescription = "Empty Image",
                    Modifier.size(200.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "There is no clue for this !",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = boldbodyFontFamily,
                    color = MaterialTheme.colorScheme.primary
                )


            }


        }

    }


}