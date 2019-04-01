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

import AppModel.AppRepository;
import AppViewModel.CustomViewModelFactory;


public class InjectorUtils {

    public static AppRepository provideRepository(Context context) {
        return AppRepository.getInstance(context);
    }

    public static AppExecutors provideExecutors(){
        return AppExecutors.getInstance();
    }

    public static CustomViewModelFactory provideViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        AppExecutors executors = provideExecutors();
        return new CustomViewModelFactory(repository, executors);
    }

}