package com.example.mnewsapp.presentation.screens.second

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mnewsapp.R
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.domain.utils.SportItems
import com.example.mnewsapp.domain.utils.TestTags
import com.example.mnewsapp.presentation.components.SecondShimmerEffect
import com.example.mnewsapp.presentation.navigation.Routes
import com.example.mnewsapp.presentation.screens.home.PublishedTodayRow
import com.example.mnewsapp.ui.theme.playfairFont
import com.example.mnewsapp.ui.theme.secondTitleColor


@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.SecondScreen(
    navController: NavController,
    viewModel: SecondScreenViewModel = hiltViewModel(),
    query: String,
    isSportCategory: Boolean = false,
    isSource: Boolean = false,
    sourceName: String,
    sharedViewModel: SharedViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    animatedVisibilityScope: AnimatedVisibilityScope,
    windowSize: WindowWidthSizeClass,

    )
{


    LaunchedEffect(Unit) {
        scrollBehavior.state.heightOffset = 0f

    }

    val uiState = viewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val error = remember {
        mutableStateOf<String?>(null)
    }

    val recentNews = remember {
        mutableStateOf<NewsObjectDto?>(null)
    }


    when(uiState.value){

        is SecondScreenViewModel.SecondScreenUIEvents.Error -> {

            val errorMsg = (uiState.value as SecondScreenViewModel.SecondScreenUIEvents.Error).message

            loading.value = false
            error.value = errorMsg

        }
        SecondScreenViewModel.SecondScreenUIEvents.Loading -> {

            loading.value = true
            error.value = null

            if(isSource){

                viewModel.getRecentNewsBySource(sourceName = query.lowercase())
            }else{

                viewModel.getRecentNewsByCategory(category = query.lowercase())
            }




        }
        is SecondScreenViewModel.SecondScreenUIEvents.Success -> {

            val data = (uiState.value as SecondScreenViewModel.SecondScreenUIEvents.Success)

            recentNews.value = data.recentNews
            loading.value = false
            error.value = null


        }
    }




    Column (modifier = Modifier.fillMaxSize()
        .testTag(TestTags.Second),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {

        var lastClickTime by remember { mutableLongStateOf(0L) }
        val now = System.currentTimeMillis()

      SecondTopAppBar(
          onBackIconClick = {

              if (now - lastClickTime > 700) {
                  lastClickTime = now

                  navController.navigateUp()

              }

          },
          title = sourceName.ifEmpty { query },
          scrollBehavior = scrollBehavior,
          isSource = isSource
      )


            SecondContent(
                isLoading = loading.value,
                error = error.value,
                recentObject = recentNews.value,
                navController = navController,
                sharedViewModel = sharedViewModel,
                scrollBehavior = scrollBehavior,
                isSport = isSportCategory,
                onChipsClick = { icon,title ->

                    if (now - lastClickTime > 700) {
                        lastClickTime = now

                        navController.navigate(Routes.SportScreen(title = title,icon = icon))

                    }



                },
                animatedVisibilityScope = animatedVisibilityScope,
                windowSize = windowSize

            )



    }



}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondTopAppBar(
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit,
    title: String,
    isSource: Boolean,
    scrollBehavior: TopAppBarScrollBehavior
) {



    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = playfairFont,
                    color = if(isSource) MaterialTheme.colorScheme.onSecondary else secondTitleColor
                )
            )

        },
        navigationIcon = {

            IconButton(onClick = {
                onBackIconClick()
            })
            {

                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    modifier = Modifier.height(25.dp)
                        .width(28.dp),
                    contentDescription = "Menu Icon",
                    tint = secondTitleColor)

            }


        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,

        )
    )


}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.SecondContent(
    isLoading: Boolean = false,
    isSport: Boolean,
    error: String?,
    recentObject: NewsObjectDto?,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    onChipsClick:(icon:Int,title: String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    windowSize: WindowWidthSizeClass,
) {


    val sportItemList = listOf(

        SportItems(icon = R.drawable.football,"Soccer"),
        SportItems(icon = R.drawable.rugby_ball,"Rugby"),
        SportItems(icon = R.drawable.tennis_ball,"Tennis"),
        SportItems(icon = R.drawable.basketball,"Basketball"),
        SportItems(icon = R.drawable.cheese,"Chess"),
        SportItems(icon = R.drawable.boxing_glove,"Boxing"),
        SportItems(icon = R.drawable.martial_arts,"Martial Arts"),
        SportItems(icon = R.drawable.ball,"Cricket"),
        SportItems(icon = R.drawable.golf_ball_with_dents,"Golf"),
        SportItems(icon = R.drawable.helmet,"Motorsports"),
        SportItems(icon = R.drawable.hockey_puck,"Hockey"),

        )

    if(isLoading){
        SecondShimmerEffect()

    }

    error?.let {

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {

            Text(text = it,
                style = MaterialTheme.typography.bodyMedium)


        }


    }

    if(recentObject != null){

            AnimatedVisibility(visible = isSport)
            {

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {


                    items( items = sportItemList){ item->

                        SuggestionChip(
                            modifier = Modifier.height(40.dp),
                            onClick = {

                                onChipsClick(item.icon,item.title)

                            },

                            icon = {

                                Icon(painter = painterResource(id = item.icon),
                                    contentDescription = "Sports Icon",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary)

                            },
                            label = {

                                Text(text = item.title,
                                    color = MaterialTheme.colorScheme.onBackground)


                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onBackground,
                                iconContentColor = MaterialTheme.colorScheme.onBackground,
                            )
                        )


                    }


                }

                Spacer(modifier = Modifier.height(10.dp))



            }

        var lastClickTime by remember { mutableLongStateOf(0L) }
        val now = System.currentTimeMillis()

            ShowNews(
                recentObject = recentObject,
                onPagerClick = { mArticle ->

                    sharedViewModel.addArticle(mArticle)


                    if (now - lastClickTime > 700) {
                        lastClickTime = now
                        navController.navigate(Routes.DetailScreen)

                    }

                },
                scrollBehavior = scrollBehavior,
                animatedVisibilityScope = animatedVisibilityScope,
                windowSize = windowSize
            )









    }



}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.ShowNews(
             recentObject: NewsObjectDto,
             onPagerClick: (ArticleDto) -> Unit,
             scrollBehavior: TopAppBarScrollBehavior,
             animatedVisibilityScope: AnimatedVisibilityScope,
             windowSize: WindowWidthSizeClass,)
{

    val isLandscape = windowSize == WindowWidthSizeClass.Medium || windowSize == WindowWidthSizeClass.Expanded

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount = (screenWidthDp / 300.dp).toInt().coerceAtLeast(1)


    LazyVerticalGrid(
        modifier = if(isLandscape) Modifier.fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
        else Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {


        itemsIndexed(items = recentObject.articles){ index, article ->

            val isSingleItem = recentObject.articles.size == 1
            var shape: RoundedCornerShape


            if(isLandscape){

                val rowCount = (recentObject.articles.size + columnCount - 1) / columnCount
                val rowIndex = index / columnCount
                val columnIndex = index % columnCount

                val isFirstRow = rowIndex == 0
                val isLastRow = rowIndex == rowCount - 1
                val isFirstColumn = columnIndex == 0
                val isLastColumn = columnIndex == columnCount - 1 || index == recentObject.articles.lastIndex

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
                    index == recentObject.articles.lastIndex -> RoundedCornerShape(
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp,
                        topStart = 4.dp,
                        topEnd = 4.dp
                    ) // Last item
                    else -> RoundedCornerShape(4.dp) // Middle items


                }
            }



            PublishedTodayRow(article = article,
                onPagerClick = onPagerClick,
                shape,
                animatedVisibilityScope = animatedVisibilityScope)

        }



    }



}