package com.example.mnewsapp.presentation.screens.detail

import SnackBartEvent
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mnewsapp.R
import com.example.mnewsapp.domain.utils.SharedViewModel
import com.example.mnewsapp.domain.utils.formatDate
import com.example.mnewsapp.ui.theme.boldbodyFontFamily
import com.example.mnewsapp.ui.theme.displayFontFamily
import com.example.mnewsapp.ui.theme.playfairFont
import com.example.mnewsapp.ui.theme.secondTitleColor
import com.example.mnewsapp.ui.theme.thirdTitleColor
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedTransitionScope.DetailScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DetailViewModel,
    event: Flow<SnackBartEvent>,
    snackbarHostState: SnackbarHostState,

    )
{

    var lastClickTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(key1 = Unit) {

        event.collect { event ->

            when(event){
                is SnackBartEvent.ShowToast -> {

                    snackbarHostState.showSnackbar(message = event.message, duration = SnackbarDuration.Short)

                }
            }

        }


    }

    val article = sharedViewModel.article

    val articleUrl = remember(article) { article?.url }

    val allArticleUrl by viewModel.allArticlesUrls.collectAsStateWithLifecycle()

    val isArticleAdded = remember(allArticleUrl, articleUrl) {
        articleUrl != null && allArticleUrl.contains(articleUrl)
    }

        // val isArticleAdded = allArticleUrl.contains(article?.url)


    LazyColumn  (modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {


        item {

            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(article?.urlToImage)
                .crossfade(true)
                .error(R.drawable.ic_noimage)
                .fallback(R.drawable.ic_noimage)
                .build()

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter)
            {

                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Detail Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.wrapContentWidth()
                        .height(310.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "${article?.urlToImage}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )


                Box(
                    modifier = Modifier.height(112.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(

                            brush = Brush.linearGradient(
                                colors = listOf(
                                    colorResource(R.color.black1),
                                    colorResource(R.color.black2)

                                ),
                                start = Offset(0f, 0f),
                                end = Offset(0f, Float.POSITIVE_INFINITY)
                            )

                        )
                )


                DetailTopAppBar(
                    onBackIconClick = {

                        val now = System.currentTimeMillis()
                        if (now - lastClickTime > 700) {
                            lastClickTime = now
                            navController.navigateUp()
                        }

                    }
                )


            }



        }

        item {

            Column(
                modifier = Modifier.fillMaxSize()
                    .offset(y = (-10).dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(MaterialTheme.colorScheme.background),
            )
            {


                Column(modifier = Modifier.fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(15.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = article?.title ?:"No Title",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontFamily = boldbodyFontFamily,
                        fontWeight = FontWeight.Bold
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
                            text = article?.source?.name ?:"No Source",
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            lineHeight = 12.sp,
                            fontFamily = displayFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = formatDate(article?.publishedAt ?:""),
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            lineHeight = 12.sp,
                            fontFamily = displayFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = thirdTitleColor
                        )


                    }


                    Spacer(modifier = Modifier.height(1.dp))


                    val cleanDescription = HtmlCompat.fromHtml(article?.content ?:"No Content",
                        HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


                    Text(text = cleanDescription,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        maxLines = 15,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        fontFamily = displayFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = secondTitleColor,
                        fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(180.dp))


                    Box(modifier = Modifier.fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 85.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center

                    )
                    {

                        val context = LocalContext.current


                        Row(modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround)
                        {

                            Icon(painter = painterResource(id = R.drawable.chrome),
                                contentDescription = "Like Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                                    .clickable{

                                        Intent(Intent.ACTION_VIEW).also {

                                            if(article != null){

                                                it.data = article.url?.toUri()

                                                if(it.resolveActivity(context.packageManager) != null){

                                                    context.startActivity(it)
                                                }
                                            }


                                        }


                                    })


                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Share Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable{


                                    Intent(Intent.ACTION_SEND).also {

                                        if(article !=null){
                                            it.putExtra(Intent.EXTRA_TEXT,article.url)
                                            it.type = "text/plain"

                                            if(it.resolveActivity(context.packageManager) != null){

                                                context.startActivity(it)
                                            }
                                        }


                                    }

                                }
                            )

                            Icon(
                                modifier = Modifier.clickable{
                                    article?.let { viewModel.toggleBookMarkStatus(it) }
                                }
                                    .size(26.dp),
                                painter = painterResource(id = if(isArticleAdded) R.drawable.ic_bookmarks_filled else R.drawable.ic_bookmarks ),
                                contentDescription = "BookMarks Icon",
                                tint = MaterialTheme.colorScheme.primary,
                            )




                        }




                    }



                }





            }


        }





    }







}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    modifier: Modifier = Modifier,
    onBackIconClick:() -> Unit
) {

    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {

            Text(
                text = "Detail Screen",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = playfairFont,
                    color = Color.White
                )
            )

        },
        navigationIcon = {

            IconButton(onClick = {onBackIconClick()}) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Icon Back",
                    tint = Color.White)


            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )


}




