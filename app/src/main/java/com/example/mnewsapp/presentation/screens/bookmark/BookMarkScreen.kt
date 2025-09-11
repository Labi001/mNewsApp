package com.example.mnewsapp.presentation.screens.bookmark

import SnackBartEvent
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mnewsapp.R
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.maper.toArticleDto
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.presentation.components.DropdownMenu
import com.example.mnewsapp.presentation.components.SecondShimmerEffect
import com.example.mnewsapp.presentation.navigation.Routes
import com.example.mnewsapp.ui.theme.boldbodyFontFamily
import com.example.mnewsapp.ui.theme.displayFontFamily
import com.example.mnewsapp.ui.theme.playfairFont
import com.example.mnewsapp.ui.theme.secondTitleColor
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BookMarkScreen(
                   navController: NavController,
                   snackbarHostState: SnackbarHostState,
                   animatedVisibilityScope: AnimatedVisibilityScope,
                   sharedViewModel: SharedViewModel,
                   event: Flow<SnackBartEvent>,
                   viewModel: BookMarkViewModel,
                   windowSize: WindowWidthSizeClass,
                   innerPadding: PaddingValues
)
{

    LaunchedEffect(key1 = Unit) {

        event.collect { event ->

            when(event){
                is SnackBartEvent.ShowToast -> {

                    snackbarHostState.showSnackbar(message = event.message, duration = SnackbarDuration.Short)

                }
            }

        }


    }

    val uiState = viewModel.uiState.collectAsState()

   // val isLandscapeLike = windowSize == WindowWidthSizeClass.Medium || windowSize == WindowWidthSizeClass.Expanded


    val loading = uiState.value is BookmarkScreenUIEvents.Loading
    val error = (uiState.value as? BookmarkScreenUIEvents.Error)?.message
    val allFavorites = (uiState.value as? BookmarkScreenUIEvents.Success)?.allFavorites

    val openDialog = remember {
        mutableStateOf(false)
    }



    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {

        BookmarkTopAppBar(
            isOpen = openDialog.value,
            onThreeDotsClick = {


                openDialog.value = true
            },
            onMenuItemClick = {

                viewModel.deleteAllArticles()
                openDialog.value = false
            },
            onDismissClick = {
                openDialog.value = false
            }
        )


        BookmarkContent(
            isLoading = loading,
            error = error,
            allBookMarks = allFavorites?: emptyList(),
            navController = navController,
            sharedViewModel = sharedViewModel,
            animatedVisibilityScope = animatedVisibilityScope,
            viewModel,
            windowSize = windowSize,
            innerPadding = innerPadding


        )






    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkTopAppBar(
    modifier: Modifier = Modifier,
    onThreeDotsClick:() -> Unit,
    onDismissClick:() -> Unit,
    onMenuItemClick:() -> Unit,
    isOpen: Boolean,
) {

    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {

            Text(
                text = "My Bookmarks",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = playfairFont,
                    color = secondTitleColor
                )
            )

        },
        actions = {

          DropdownMenu(
              isOpen = isOpen,
              onDismissClick = onDismissClick,
              onMenuItemClick = onMenuItemClick,
              onThreeDotsClick = onThreeDotsClick
          )


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )


}


@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BookmarkContent(
    isLoading: Boolean = false,
    error: String?,
    allBookMarks: List<ArticleEntity>,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: BookMarkViewModel,
    windowSize: WindowWidthSizeClass,
    innerPadding: PaddingValues
) {

     val isLandscape = windowSize == WindowWidthSizeClass.Medium || windowSize == WindowWidthSizeClass.Expanded

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount = (screenWidthDp / 300.dp).toInt().coerceAtLeast(1)

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


    if (allBookMarks.isEmpty()){

        Box (modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {

            Column(modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {

                Icon(painter = painterResource(id = R.drawable.out_of_stock),
                    contentDescription = "Empty Image",
                    Modifier.size(200.dp),
                    tint = MaterialTheme.colorScheme.onSecondary)

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "There is no Article Saved !",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = boldbodyFontFamily,
                    color = MaterialTheme.colorScheme.primary)



            }


        }


    }else {



        LazyVerticalGrid(
            modifier =  if(isLandscape) Modifier.fillMaxSize()
                .navigationBarsPadding()  else
                    Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(
                start = 2.dp,
                top = 5.dp,
                end = 2.dp,
                bottom = 5.dp
            ),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            val isSingleItem = allBookMarks.size == 1
            var shape: RoundedCornerShape

            itemsIndexed(items = allBookMarks) { index, article ->

                if(isLandscape){

                    val rowCount = (allBookMarks.size + columnCount - 1) / columnCount
                    val rowIndex = index / columnCount
                    val columnIndex = index % columnCount

                    val isFirstRow = rowIndex == 0
                    val isLastRow = rowIndex == rowCount - 1
                    val isFirstColumn = columnIndex == 0
                    val isLastColumn = columnIndex == columnCount - 1 || index == allBookMarks.lastIndex

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
                        index == allBookMarks.lastIndex -> RoundedCornerShape(
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


                BookMarkRow(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(300),
                        fadeOutSpec = tween(300),
                        placementSpec = tween(100)
                    ),
                    article = article.toArticleDto(),
                    onPagerClick = { article ->

                        sharedViewModel.addArticle(article)


                        if (now - lastClickTime > 700) {
                            lastClickTime = now
                            navController.navigate(Routes.DetailScreen)

                        }


                    },
                    shape,
                    animatedVisibilityScope = animatedVisibilityScope,
                    viewModel = viewModel,
                    onSourceClick = { sourceQuery,sourceName ->

                        if (now - lastClickTime > 700) {
                            lastClickTime = now
                            navController.navigate(Routes.SecondScreen(query = sourceQuery, isSportCategory = false,isSource = true, sourceName = sourceName))

                        }

                    },



                )


            }


        }


    }

}



@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.BookMarkRow(
    modifier: Modifier = Modifier,
    article: ArticleDto,
    onPagerClick: (ArticleDto) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSourceClick: (String,String) ->  Unit = { _, _ -> },
    viewModel: BookMarkViewModel,

    ) {



    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(article.urlToImage)
        .crossfade(true)
        .error(R.drawable.ic_noimage)
        .fallback(R.drawable.ic_noimage)
        .build()


            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        onPagerClick(article)
                    }
                    .padding(horizontal = 10.dp, vertical = 1.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = shape)
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
                        .padding(vertical = 10.dp)
                        .padding(end = 6.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                )
                {

                    Spacer(modifier = Modifier.height(15.dp))

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

                    Spacer(modifier = Modifier.height(5.dp))

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

                        IconButton(
                            onClick = {
                            viewModel.deleteArticle(url = article.url?:"")
                        })
                        {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_del),
                                contentDescription = "Delete Icon",
                                tint = MaterialTheme.colorScheme.primary)

                        }


                    }


                }


            }







}



