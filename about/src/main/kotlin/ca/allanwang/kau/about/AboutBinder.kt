/*
 * Copyright 2018 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.allanwang.kau.about

import android.content.Context
import ca.allanwang.kau.utils.startActivity
import ca.allanwang.kau.utils.withSceneTransitionAnimation

/**
 * Created by Allan Wang on 2017-07-22.
 */

/**
 * About activity launcher
 */
inline fun <reified T : AboutActivityBase> Context.kauLaunchAbout() =
    startActivity<T>(
        bundleBuilder = {
            withSceneTransitionAnimation(this@kauLaunchAbout)
        }
    )
