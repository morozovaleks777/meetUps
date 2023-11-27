package com.morozov.meetups.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dwkidsandroid.presentation.navigation.SharedViewModel
import com.morozov.meetups.presentation.home_screen.HomeScreen
import com.morozov.meetups.presentation.login.LoginScreen
import com.morozov.meetups.presentation.mapScreen.MapScreen
import com.morozov.meetups.presentation.navigation.NavigationUtils.getExtrasViewModel
import com.morozov.meetups.presentation.splash.SplashScreen


private const val ANIMATION_SPEED = 900
private const val EPISODE_NAME = "episodeName"
const val VIDEO_ID = "videoId"
private const val SHOW_ID = "showId"

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalAnimationApi::class,)
@ExperimentalComposeUiApi
@Composable
fun AppNavigation(
    navController: NavHostController,
) {

    NavHost(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        navController = navController,
        startDestination = AppScreens.HomeScreen.name,
    ) {
        composableAnimated(
            route = AppScreens.SplashScreen.name,
        ) {
            SplashScreen(navController = navController)
        }
        composableAnimated(AppScreens.HomeScreen.name) {
            val transition = rememberUpdatedState(newValue = transition)
            HomeScreen(
                navController = navController,
                homeViewModel = hiltViewModel(),
                sharedViewModel = navController.getExtrasViewModel(navEntry = it),
                transition = transition.value
            )
        }
        composableAnimated(
            route = AppScreens.LoginScreen.name,
        ) {
            LoginScreen(navController = navController)
        }
        composableAnimated(
            route = AppScreens.MapScreen.name,
        ) {
            MapScreen(navController = navController)
        }
//        composableAnimated(
//            route = AppScreens.DetailScreen.name,
//        ) { backStackEntry ->
//            DetailScreen(
//                navController = navController,
//                detailViewModel = hiltViewModel(),
//                sharedViewModel = navController.getExtrasViewModel(navEntry = backStackEntry),
//                isWideScreen = isWideScreen,
//            )
//        }
//        dialog(
//            route = AppScreens.DetailScreenTablet.name,
//        ) { backStackEntry ->
//            DetailScreenTablet(
//                navController = navController,
//                detailViewModel = hiltViewModel(),
//                sharedViewModel = navController.getExtrasViewModel(navEntry = backStackEntry),
//                isWideScreen = isWideScreen,
//            )
//        }
//        composableAnimated(
//            route = AppScreens.VideoPlayerScreen.name,
//        ) { backStackEntry ->
//            val sharedViewModel = navController.getExtrasViewModel(navEntry = backStackEntry)
//            val video = sharedViewModel.getSharedData<Episode>(NavigationData.SELECTED_EPISODE)
//            val seasons =
//                sharedViewModel.getSharedData<List<Season>>(NavigationData.SELECTED_SHOW_EPISODES)
//            val isWatchedEpisode = sharedViewModel.getSharedData<Boolean>(NavigationData.IS_WATCHED_EPISODE)
//
//            if (video != null && seasons != null) {
//                VideoPlayerScreen(
//                    navController = navController,
//                    videoViewModel = hiltViewModel(),
//                    currentEpisode = video,
//                    currentSeasons = seasons,
//                    isWatchedEpisode = isWatchedEpisode ?: false
//                )
//            } else {
//                Timber.e("Missing data for player!")
//                navController.popBackStack()
//            }
//        }
//        composableAnimated(
//            route = AppScreens.AccountScreen.name
//        ) {
//            AccountScreen(
//                navController = navController,
//                accountViewModel = hiltViewModel(),
//                isWideScreen = isWideScreen
//            )
//        }
//        composableAnimated(
//            route = AppScreens.BillingDetailsScreen.name
//        ) {
//            BillingDetailsScreen(
//                navController = navController,
//                billingDetailsViewModel = hiltViewModel(),
//                isWideScreen = isWideScreen
//            )
//        }
//        composableAnimated(
//            route = AppScreens.HelpScreen.name
//        ) {
//            HelpScreen(
//                navController = navController,
//                helpViewModel = hiltViewModel(),
//                isWideScreen = isWideScreen
//            )
//        }
//        composableAnimated(
//            route = AppScreens.LegalScreen.name
//        ) {
//            LegalScreen(
//                navController = navController,
//                legalViewModel = hiltViewModel(),
//                isWideScreen = isWideScreen
//            )
//        }
//        composableAnimated(
//            route = "${AppScreens.ContentLockScreen.name}/?${NavigationArguments.ARG_AUTH_STATE}={${NavigationArguments.ARG_AUTH_STATE}}",
//            enterTransition = Transitions.FADE_IN,
//            exitTransition = Transitions.FADE_OUT,
//            arguments = listOf(navArgument(NavigationArguments.ARG_AUTH_STATE) {
//                type = NavType.EnumType(AuthMainStates::class.java)
//                defaultValue = AuthMainStates.Initial
//            })
//        ) {
//            ContentLockScreen(
//                navController = navController,
//                viewModel = hiltViewModel(),
//                isWideScreen = isWideScreen,
//                subscriptionManager = subscriptionManager,
//            )
      //  }
    }
}

private fun NavGraphBuilder.composableAnimated(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = Transitions.SLIDE_IN,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = Transitions.SLIDE_OUT,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        content = content
    )
}

object Transitions {
    val SLIDE_IN: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(ANIMATION_SPEED)
        )
    }
    val SLIDE_OUT: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(ANIMATION_SPEED)
        )
    }
    val FADE_IN: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        fadeIn(
            animationSpec = tween(ANIMATION_SPEED)
        )
    }
    val FADE_OUT: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        fadeOut(
            animationSpec = tween(ANIMATION_SPEED)
        )
    }
}

object NavigationUtils {

    fun NavController.navigate(screens: AppScreens, vararg arguments: Any) {
        var basicRoute = screens.name


        basicRoute += when (arguments.isEmpty()) {
            true -> when (screens) {
                AppScreens.ContentLockScreen -> "/"
                else -> ""
            }

            false -> when (screens) {
                AppScreens.ContentLockScreen -> "/?${NavigationArguments.ARG_AUTH_STATE}=${arguments[0]}"
                else -> arguments.joinToString(separator = "/")
            }
        }

        navigate(route = basicRoute)
    }

    @Composable
    fun NavController.getExtrasViewModel(navEntry: NavBackStackEntry): SharedViewModel {
        val parentEntry =
            remember(navEntry) { getBackStackEntry(AppScreens.HomeScreen.name) }

        return hiltViewModel(parentEntry)
    }

//    @Composable
//    fun <T> NavController.saveExtras(key: String, value: T) {
//        val parentEntry =
//            remember(currentBackStackEntry) { getBackStackEntry(AppScreens.HomeScreenNew.name) }
//        val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)
//
//        sharedViewModel.shareData(key, value)
//    }
}

object NavigationData {
    const val SELECTED_SHOW = "selected_show_"
    const val SELECTED_EPISODE = "selected_episode_"
    const val SELECTED_SHOW_EPISODES = "selected_show_episodes"
    const val IS_WATCHED_EPISODE = "is_watched_episode"
}

object NavigationArguments {
    const val ARG_AUTH_STATE = "arg_auth_state_"
}
