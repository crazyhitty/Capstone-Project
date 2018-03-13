/*
 * MIT License
 *
 * Copyright (c) 2016 Kartik Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.crazyhitty.chdev.ks.predator.core.posts

import android.R
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount
import com.crazyhitty.chdev.ks.predator.data.Constants
import com.crazyhitty.chdev.ks.predator.helper.AccountHelper
import com.crazyhitty.chdev.ks.predator.helper.SharedPreferencesHelper
import com.crazyhitty.chdev.ks.predator.models.Post
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/4/17 11:59 PM
 * Description: Unavailable
 */
class PostsPresenter(private val private val accountHelper: AccountHelper) : PostsContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var day: Calendar = Calendar.getInstance()

    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun fetch() {
        accountHelper.getAuthToken(sharedPreferencesHelper.getAuthTokenType())
                .flatMap { getPosts(it, day) }
                .
    }

    override fun fetchNextDay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun redirectToPostDetailsScreen(postId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getPosts(authToken: String, day: Calendar): Single<List<Post>> {
        val dayString = SimpleDateFormat(Constants.DateFormat.PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT,
                Locale.getDefault()).format(day.time)
        ProductHuntRestApi.getApi()
                .getPostsCategoryWise(CoreUtils.getAuthToken(authToken),
                        Constants.Posts.CATEGORY_ALL,
                        dayString)
                .flatMap {  }
        return Single.create {
            Prea
        }
    }

    private fun convert
}