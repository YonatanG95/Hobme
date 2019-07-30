/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package AppUtils;

import android.content.Context;

import AppViewModel.CustomViewModelFactory;
import DataSources.AppRepository;

/**
 * Provides all fragments (views) with an instance of a viewModel factory, including repository and executors
 */
public class InjectorUtils {

    /**
     * Gets an instance of the application repository (local & remote data access)
     * @param context
     * @param executors
     * @return - repository instance
     */
    public static AppRepository provideRepository(Context context, AppExecutors executors) {
        return AppRepository.getInstance(context, executors);
    }

    /**
     * Gets an instance of executors class (background jobs)
     * @return - executors instance
     */
    public static AppExecutors provideExecutors(){
        return AppExecutors.getInstance();
    }

    /**
     * Provides a viewModel factory with a repository and executors
     * @param context
     * @return - viewModelFactory class instance
     */
    public static CustomViewModelFactory provideViewModelFactory(Context context) {
        AppExecutors executors = provideExecutors();
        AppRepository repository = provideRepository(context.getApplicationContext(), executors);
        return new CustomViewModelFactory(repository);
    }

}