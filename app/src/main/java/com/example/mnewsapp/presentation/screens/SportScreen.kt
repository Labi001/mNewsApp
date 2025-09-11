package com.example.mnewsapp.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mnewsapp.R
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.domain.utils.formatDate
import com.example.mnewsapp.presentation.components.SportShimmerEffect
import com.example.mnewsapp.presentation.navigation.Routes
import com.example.mnewsapp.presentation.screens.home.HomeScreenUIEvents
import com.example.mnewsapp.presentation.screens.home.HomeViewModel
import com.example.mnewsapp.presentation.screens.home.PublishedTodayRow
import com.example.mnewsapp.ui.theme.boldbodyFontFamily
import com.example.mnewsapp.ui.theme.customTitleColor
import com.example.mnewsapp.ui.theme.displayFontFamily
import com.example.mnewsapp.ui.theme.playfairFont
import com.example.mnewsapp.ui.theme.secondTitleColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.SportScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    icon:Int,
    title: String,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    query: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    windowSize: WindowWidthSizeClass,
){

    val uiState = viewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val error = remember {
        mutableStateOf<String?>(null)
    }

    val recentObject = remember {
        mutableStateOf<NewsObjectDto?>(null)
    }

    val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)

    val fromMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE)


    when(uiState.value){

        is HomeScreenUIEvents.Error -> {

            val errorMsg = (uiState.value as HomeScreenUIEvents.Error).message

            loading.value = false
            error.value = errorMsg

        }

        HomeScreenUIEvents.Loading -> {

            loading.value = true
            error.value = null
            viewModel.getPopularNews(query = query, from = yesterday, fromM = fromMonth, to = yesterday)

        }
        is HomeScreenUIEvents.Success -> {

            val data = (uiState.value as HomeScreenUIEvents.Success)


            recentObject.value = data.recentNews
            loading.value = false
            error.value = null


        }
    }

    var lastClickTime by remember { mutableLongStateOf(0L) }
    val now = System.currentTimeMillis()

    Column (modifier = Modifier.fillMaxSize()
        .navigationBarsPadding()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {

            SportTopAppBar(
                icon = icon,
                title = title,
                onBackClick = {
                    if (now - lastClickTime > 700) {
                        lastClickTime = now

                        navController.navigateUp()

                    }
                }
            )





            Content(
                isLoading = loading.value,
                error = error.value,
                recentObject = recentObject.value,
                navController = navController,
                sharedViewModel = sharedViewModel,
                animatedVisibilityScope = animatedVisibilityScope,
                windowSize = windowSize
            )
        



    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.Content(
    isLoading: Boolean = false,
    error: String? = null,
    recentObject: NewsObjectDto?,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    windowSize: WindowWidthSizeClass,
) {



    if(isLoading){

        SportShimmerEffect()


    }

    error?.let {

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {

            Text(text = it,
                style = MaterialTheme.typography.bodyMedium)


        }


    }

    if(recentObject != null ){

        var lastClickTime by remember { mutableLongStateOf(0L) }
        val now = System.currentTimeMillis()

        ShowSportNews(
            recentObject = recentObject,
            onPagerClick = { mArticle ->

                sharedViewModel.addArticle(mArticle)


                if (now - lastClickTime > 700) {
                    lastClickTime = now
                    navController.navigate(Routes.DetailScreen)

                }

            },
            animatedVisibilityScope = animatedVisibilityScope,
            sharedViewModel = sharedViewModel,
            navController = navController,
            onSourceClick = { sourceQuery,sourceName ->

                if (now - lastClickTime > 700) {
                    lastClickTime = now
                    navController.navigate(Routes.SecondScreen(query = sourceQuery, isSportCategory = false,isSource = true, sourceName = sourceName))

                }



            },
            windowSize = windowSize)



    }



}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.ShowSportNews(
    recentObject: NewsObjectDto,
    onPagerClick: (ArticleDto) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    onSourceClick: (String,String) -> Unit,
    windowSize: WindowWidthSizeClass,
    )
{

    val isLandscape = windowSize == WindowWidthSizeClass.Medium || windowSize == WindowWidthSizeClass.Expanded

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount = (screenWidthDp / 300.dp).toInt().coerceAtLeast(1)

    val pageState = rememberPagerState (pageCount = {5}, initialPage = 0)
    var currentPage by remember { mutableIntStateOf(0) }

    // Auto Slide
    LaunchedEffect(Unit) {
        while(true) {
            delay(4000)
            val nextPage = (pageState.currentPage + 1) % pageState.pageCount
            pageState.animateScrollToPage(nextPage)
        }
    }

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.settledPage }.collectLatest { pageIndex ->
            currentPage = pageIndex
        }
    }


    LaunchedEffect(key1 = currentPage) {
        pageState.animateScrollToPage(page = currentPage)
    }

    Spacer(modifier = Modifier.height(5.dp))

    HorizontalPager(state = pageState,
        beyondViewportPageCount = 2)
    { pageIndex ->

        val imageUrl = recentObject.articles[pageIndex].urlToImage

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.ic_noimage)
            .fallback(R.drawable.ic_noimage)
            .build()

        Box(
            modifier = Modifier
                .height(242.dp)
                .fillMaxWidth()
                .sharedElement(
                    sharedContentState = rememberSharedContentState(
                        key = "$imageUrl"
                    ),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(horizontal = 15.dp)
                .clickable{



                    sharedViewModel.addArticle(recentObject.articles[pageIndex])

                    navController.navigate(Routes.DetailScreen)



                    // onPagerClick(recentObject.articles[pageIndex])

                }
                .background(Color.Transparent)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.BottomStart

        )
        {

            AsyncImage(model = imageRequest,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .width(90.dp)
                    .height(35.dp)
                    .offset(x = 10.dp, y = (-195).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            )
            {

                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = recentObject.articles[pageIndex].author?:"No Author",
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = displayFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )


            }




            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp)
                    .height(87.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            {


                Text(
                    text = recentObject.articles[pageIndex].title?:"No Title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 5.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    fontFamily = boldbodyFontFamily,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 5.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    Text(
                        text = recentObject.articles[pageIndex].source?.name?:"No Source",
                        modifier = Modifier.clickable{
                           onSourceClick(recentObject.articles[pageIndex].source?.id?:"",recentObject.articles[pageIndex].source?.name?:"")
                        },
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        lineHeight = 12.sp,
                        fontFamily = displayFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        text = formatDate(recentObject.articles[pageIndex].publishedAt?:"") ,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        lineHeight = 12.sp,
                        fontFamily = displayFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )


                }


            }


        }



    }
    
    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
        Modifier
            .fillMaxWidth()
            .height(15.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(5) { index ->
            val color =
                if (currentPage == index) MaterialTheme.colorScheme.primary else Color.Transparent
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .border(
                        width = 1.dp,
                        color = customTitleColor,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)

            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Text(
        text = "Recent News ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp),
        textAlign = TextAlign.Start,
        maxLines = 1,
        fontFamily = boldbodyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(25.dp))


    LazyVerticalGrid(
        modifier = if(isLandscape) Modifier.fillMaxWidth()
            .navigationBarsPadding()
        else Modifier.fillMaxWidth(),
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(vertical = 1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        itemsIndexed(items = recentObject.articles.drop(5)){ index, article ->

            val isSingleItem = recentObject.articles.size == 1

            val droppedArticles = recentObject.articles.drop(5)

            var shape: RoundedCornerShape

            if(isLandscape){

                val rowCount = (droppedArticles.size + columnCount - 1) / columnCount
                val rowIndex = index / columnCount
                val columnIndex = index % columnCount

                val isFirstRow = rowIndex == 0
                val isLastRow = rowIndex == rowCount - 1
                val isFirstColumn = columnIndex == 0
                val isLastColumn = columnIndex == columnCount - 1 || index == droppedArticles.lastIndex

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
                    index == droppedArticles.lastIndex -> RoundedCornerShape(
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp,
                        topStart = 4.dp,
                        topEnd = 4.dp
                    ) // Last item
                    else -> RoundedCornerShape(4.dp) // Middle items


                }


            }




            PublishedTodayRow(
                article = article,
                onPagerClick = onPagerClick,
                shape,
                animatedVisibilityScope = animatedVisibilityScope,
                onSourceClick = onSourceClick
            )

        }



    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportTopAppBar(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onBackClick: () -> Unit,
    ) {

    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start)
            {

                IconButton(onClick = {
                    onBackClick()
                })
                {

                    Icon(painter = painterResource(id = icon),
                        modifier = Modifier.size(25.dp),
                        contentDescription = "Menu Icon",
                        tint = MaterialTheme.colorScheme.primary)

                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = playfairFont,
                        color = secondTitleColor
                    )
                )



            }



        },
        navigationIcon = {

            IconButton(onClick = {
                onBackClick()
            })
            {

                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    tint = secondTitleColor)

            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )


}