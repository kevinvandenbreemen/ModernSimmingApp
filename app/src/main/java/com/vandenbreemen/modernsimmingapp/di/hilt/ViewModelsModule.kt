package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import com.vandenbreemen.modernsimmingapp.viewmodels.AddGroupViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.OnboardingViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.OverviewViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.PostListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ViewModelsModule {

    @Provides
    fun providesOnboardingViewModel(simContentProviderInteractor: SimContentProviderInteractor, addGroupViewModel: AddGroupViewModel): OnboardingViewModel {
        return OnboardingViewModel(simContentProviderInteractor, addGroupViewModel)
    }

    @Provides
    fun providesAddGroupViewModel(postsDatabase: PostsDatabase): AddGroupViewModel {
        return AddGroupViewModel(postsDatabase)
    }

    @Provides
    fun providesOverviewViewModel(simContentProviderInteractor: SimContentProviderInteractor): OverviewViewModel {
        return OverviewViewModel(simContentProviderInteractor)
    }

    @Provides
    fun providesPostListViewModel(simContentProviderInteractor: SimContentProviderInteractor): PostListViewModel {
        return PostListViewModel(simContentProviderInteractor)
    }

}