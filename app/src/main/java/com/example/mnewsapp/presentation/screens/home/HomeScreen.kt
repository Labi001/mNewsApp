package com.example.mnewsapp.presentation.screens.home


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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.example.mnewsapp.domain.utils.DrawerItems
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.domain.utils.TestTags
import com.example.mnewsapp.domain.utils.formatDate
import com.example.mnewsapp.presentation.components.HomeShimmerEffect
import com.example.mnewsapp.presentation.navigation.Routes
import com.example.mnewsapp.ui.theme.boldbodyFontFamily
import com.example.mnewsapp.ui.theme.customTitleColor
import com.example.mnewsapp.ui.theme.displayFontFamily
import com.example.mnewsapp.ui.theme.playfairFont
import com.example.mnewsapp.ui.theme.secondTitleColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    drawerState: DrawerState
) {

    val uiState = viewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val error = remember {
        mutableStateOf<String?>(null)
    }

    val serverresponse = remember {
        mutableStateOf<NewsObjectDto?>(null)
    }

    val recentNews = remember {
        mutableStateOf<NewsObjectDto?>(null)
    }

    val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)

    val fromMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE)


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val scope = rememberCoroutineScope()


    val drawerItemList = listOf(

        DrawerItems(
            icon = R.drawable.ic_science,
            title = "Science",
            badgeCount = 0,
            hasBadge = false
        ),

        DrawerItems(icon = R.drawable.ic_sport,
            title = "Sport",
            badgeCount = 0,
            hasBadge = false),

        DrawerItems(
            icon = R.drawable.ic_bussiness,
            title = "Business",
            badgeCount = 0,
            hasBadge = false
        ),

        DrawerItems(
            icon = R.drawable.ic_health,
            title = "Health",
            badgeCount = 0,
            hasBadge = false
        ),
        DrawerItems(
            icon = R.drawable.ic_entertaiment,
            title = "Entertainment",
            badgeCount = 0,
            hasBadge = false
        ),

        DrawerItems(icon = R.drawable.cpu,
            title = "Technology",
            badgeCount = 0,
            hasBadge = false)


    )

    var selectedItem by remember {

        mutableStateOf(drawerItemList[0])
    }

    when (uiState.value) {

        is HomeScreenUIEvents.Error -> {

            val errorMsg = (uiState.value as HomeScreenUIEvents.Error).message

            loading.value = false
            error.value = errorMsg

        }

        HomeScreenUIEvents.Loading -> {

            loading.value = true
            error.value = null
            viewModel.getPopularNews(
                query = "general",
                from = yesterday,
                fromM = fromMonth,
                to = yesterday
            )

        }

        is HomeScreenUIEvents.Success -> {

            val data = (uiState.value as HomeScreenUIEvents.Success)

            serverresponse.value = data.newsResponse
            recentNews.value = data.recentNews
            loading.value = false
            error.value = null


        }
    }

    ModalNavigationDrawer(
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier
                    .width(screenWidth * 0.7f)
                    .testTag(TestTags.DrawerContainer),
                drawerShape = RoundedCornerShape(8.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background
            )
            {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Color.Transparent),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                )
                {


                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    )
                    {


                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {

                            Icon(
                                painter = painterResource(id = R.drawable.news_logo),
                                modifier = Modifier.size(90.dp),
                                contentDescription = "App Icon",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )


                            Text(
                                text = "Best App For News!",
                                modifier = Modifier.padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            )


                        }

                        HorizontalDivider(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )


                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )

                    Text(
                        text = "News App",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 18.sp
                        ),
                        fontWeight = FontWeight.Bold
                    )


                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )


                    drawerItemList.forEach { item ->


                        NavigationDrawerItem(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            selected = item == selectedItem,
                            onClick = {

                                selectedItem = item
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate(
                                        Routes.SecondScreen(
                                            query = item.title,
                                            isSportCategory = item.title == "Sport",
                                            isSource = false
                                        )
                                    ) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }


                            },
                            label = {

                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )

                            },
                            icon = {


                                Icon(
                                    painter = painterResource(id = item.icon),
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = item.title
                                )

                            },
                            shape = RoundedCornerShape(25.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.Transparent,
                                unselectedContainerColor = Color.Transparent,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = secondTitleColor,
                                unselectedTextColor = secondTitleColor,
                            )

                        )


                    }


                }


            }


        },
        drawerState = drawerState
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            var lastClickTime by remember { mutableLongStateOf(0L) }

            HomeTopAppBar(
                onMenuClick = {

                    val now = System.currentTimeMillis()
                    if (now - lastClickTime > 700) {
                        lastClickTime = now

                        scope.launch {
                            drawerState.open()
                        }

                    }

                }
            )

            HomeContent(
                isLoading = loading.value,
                error = error.value,
                newsObject = serverresponse.value,
                recentObject = recentNews.value,
                navController = navController,
                sharedViewModel,
                animatedVisibilityScope = animatedVisibilityScope,

            )


        }


    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.HomeContent(
    isLoading: Boolean = false,
    error: String? = null,
    newsObject: NewsObjectDto?,
    recentObject: NewsObjectDto?,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    var lastClickTime by remember { mutableLongStateOf(0L) }

    if (isLoading) {

        HomeShimmerEffect()


    }

    error?.let {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {

            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )


        }


    }

    if (newsObject != null && recentObject != null) {
        val now = System.currentTimeMillis()

        ShowNews(
            newsObject,
            recentObject = recentObject,
            onPagerClick = { mArticle ->

                sharedViewModel.addArticle(mArticle)

                if (now - lastClickTime > 700) {
                    lastClickTime = now
                    navController.navigate(Routes.DetailScreen)

                }


            },
            onSourceClick = { sourceQuery,sourceName ->


                if (now - lastClickTime > 700) {

                    lastClickTime = now
                    navController.navigate(Routes.SecondScreen(query = sourceQuery, isSportCategory = false,isSource = true, sourceName = sourceName))

                }


            },
            animatedVisibilityScope = animatedVisibilityScope,
        )


    }


}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.ShowNews(
    newsObject: NewsObjectDto,
    recentObject: NewsObjectDto,
    onPagerClick: (ArticleDto) -> Unit,
    onSourceClick: (String,String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {


    val pageState = rememberPagerState(pageCount = { 5 }, initialPage = 0)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
                contentPadding = PaddingValues(
                bottom = 56.dp
                ),
    )
    {


        item {

            HorizontalPager(
                state = pageState,
                beyondViewportPageCount = 2
            )
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
                        .padding(horizontal = 15.dp)
                        .clickable {
                            onPagerClick(recentObject.articles[pageIndex])
                        }
                        .background(Color.Transparent)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.BottomStart

                )
                {

                    AsyncImage(
                        model = imageRequest,
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    key = "$imageUrl"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            text = recentObject.articles[pageIndex].author ?: "No Author",
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
                            text = recentObject.articles[pageIndex].title ?: "No Title",
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
                                    modifier = Modifier.clickable{

                                        onSourceClick(recentObject.articles[pageIndex].source?.id?:"",
                                            recentObject.articles[pageIndex].source?.name?:"")

                                    },
                                    text = recentObject.articles[pageIndex].source?.name
                                        ?: "No Source",
                                    textAlign = TextAlign.Start,
                                    maxLines = 2,
                                    lineHeight = 12.sp,
                                    fontFamily = displayFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSecondary)


                            Text(
                                text = formatDate(
                                    recentObject.articles[pageIndex].publishedAt ?: ""
                                ),
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
                        if (pageState.currentPage == index) MaterialTheme.colorScheme.primary else Color.Transparent
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

        }

        item {


            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Top News",
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

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(242.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            )
            {

                items(items = newsObject.articles) { article ->

                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .crossfade(true)
                        .error(R.drawable.ic_noimage)
                        .fallback(R.drawable.ic_noimage)
                        .build()

                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .padding(horizontal = 10.dp)
                            .clickable {
                                onPagerClick(article)
                            }
                            .background(Color.Transparent)
                            .fillMaxHeight()
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.BottomStart

                    )
                    {

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .matchParentSize()
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(
                                        key = "${article.urlToImage}"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope

                                )
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                text = article.author ?: "No Author",
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
                                .width(159.dp)
                                .offset(y = (-10).dp)
                                .padding(horizontal = 10.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                        {

                            Text(
                                text = article.title ?: "No Title!",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 10.dp,
                                        end = 10.dp,
                                        top = 10.dp
                                    ),
                                textAlign = TextAlign.Start,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 15.sp,
                                fontFamily = boldbodyFontFamily,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 5.dp)
                                    .padding(horizontal = 10.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            )
                            {

                                Text(
                                    modifier = Modifier.clickable{
                                        onSourceClick(article.source?.id?:"",article.source?.name?:"")
                                    },
                                    text = article.source?.name ?: "",
                                    textAlign = TextAlign.Start,
                                    maxLines = 2,
                                    lineHeight = 12.sp,
                                    fontFamily = displayFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    text = formatDate(article.publishedAt ?: ""),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
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


            }

        }

        item {

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Published Today",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                textAlign = TextAlign.Start,
                maxLines = 1,
                fontFamily = boldbodyFontFamily,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(25.dp))



            recentObject.articles.drop(5).forEachIndexed { index, article ->

                val isSingleItem = recentObject.articles.size == 1

                val droppedArticles = recentObject.articles.drop(5)

                val shape = when {

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

                PublishedTodayRow(
                    article = article,
                    onPagerClick = onPagerClick,
                    shape = shape,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onSourceClick = onSourceClick

                )

            }

//            LazyColumn(modifier = Modifier.fillMaxWidth()
//                .height(300.dp),
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(10.dp))
//            {
//
//                items(items = recentObject.articles){ article ->
//
//                    PublishedTodayRow(article = article)
//
//                }
//
//
//            }


        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {

            Text(
                text = "News App",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = playfairFont,
                    color = secondTitleColor
                )
            )

        },
        navigationIcon = {

            IconButton(onClick = {
                onMenuClick()
            },
                modifier = Modifier.testTag(TestTags.DrawerBtn))
            {

                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    modifier = Modifier
                        .height(25.dp)
                        .width(28.dp),
                    contentDescription = "Menu Icon",
                    tint = secondTitleColor
                )

            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )


}

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.PublishedTodayRow(
    article: ArticleDto,
    onPagerClick: (ArticleDto) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSourceClick: (String,String) ->  Unit = { _, _ -> },

    ) {

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(article.urlToImage)
        .crossfade(true)
        .error(R.drawable.ic_noimage)
        .fallback(R.drawable.ic_noimage)
        .build()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onPagerClick(article)
            }
            .padding(horizontal = 10.dp, vertical = 1.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape)
            .clip(shape = shape),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = "${article.urlToImage}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }




        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 6.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        )
        {



            Text(
                text = article.title ?: "No Title",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = boldbodyFontFamily,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Text(
                    modifier = Modifier.weight(0.7f)
                        .clickable{
                            onSourceClick(article.source?.id?:"",article.source?.name?:"")
                        },
                    text = article.source?.name ?: "No Source",
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = displayFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Text(
                    modifier = Modifier.weight(0.3f),
                    text = formatDate(article.publishedAt ?: ""),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = displayFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )


            }


        }


    }


}

//@Preview(showBackground = true)
//@Composable
//fun PublishedTodayRowPreview() {
//    MNewsAppTheme {
//        PublishedTodayRow()
//    }
//}