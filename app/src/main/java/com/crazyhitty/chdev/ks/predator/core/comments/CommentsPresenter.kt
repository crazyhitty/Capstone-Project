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

package com.crazyhitty.chdev.ks.predator.core.comments

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper
import com.crazyhitty.chdev.ks.predator.models.Comment
import com.crazyhitty.chdev.ks.predator.utils.CommentTimeCalculator
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils
import com.crazyhitty.chdev.ks.predator.utils.Logger
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import java.util.*

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     11/12/17 1:35 PM
 * Description: Unavailable
 */
class CommentsPresenter(private var view: CommentsContract.View) : CommentsContract.Presenter {
    companion object {
        private const val TAG = "CommentsPresenter"
        private const val PER_PAGE = 50
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var page: Int = 0

    override fun subscribe() {

    }

    override fun unSubscribe() {
        view.hideLoading()
        compositeDisposable.clear()
    }

    override fun fetchOfflineComments(postId: Int) {
        val commentsSingle = fetchCommentsFromDatabase(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        compositeDisposable.add(commentsSingle.subscribe(
                {
                    if (it.isEmpty())
                        view.commentsUnavailable()
                    else
                        view.showComments(it)
                },
                {
                    Logger.e(TAG, it)
                    view.commentsUnavailable()
                }
        ))
    }

    override fun fetchOnlineComments(token: String, postId: Int) {
        page = 0

        val commentsSingle = ProductHuntRestApi.getApi()
                .getPostComments(CoreUtils.getAuthToken(token), postId, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .flatMap { postCommentsData -> saveComments(postId, postCommentsData) }
                .observeOn(Schedulers.io())
                .flatMap { _ -> fetchCommentsFromDatabase(postId) }
                .observeOn(AndroidSchedulers.mainThread())

        compositeDisposable.add(commentsSingle.subscribe(
                {
                    if (it.isEmpty())
                        view.commentsUnavailable()
                    else
                        view.showComments(it)
                },
                {
                    Logger.e(TAG, it)
                    view.commentsUnavailable()
                }
        ))
    }

    override fun loadMoreOnlineComments(token: String, postId: Int) {
        page ++

        val commentsSingle = ProductHuntRestApi.getApi()
                .getPostComments(CoreUtils.getAuthToken(token), postId, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .flatMap { postCommentsData -> saveComments(postId, postCommentsData) }
                .observeOn(Schedulers.io())
                .flatMap { _ -> fetchCommentsFromDatabase(postId) }
                .observeOn(AndroidSchedulers.mainThread())

        compositeDisposable.add(commentsSingle.subscribe(
                {
                    if (it.isEmpty())
                        view.commentsUnavailable()
                    else
                        view.showComments(it)
                },
                {
                    Logger.e(TAG, it)
                    page --
                    view.commentsUnavailable()
                }
        ))
    }

    private fun saveComments(postId: Int, postCommentsData: PostCommentsData):
            Single<PostCommentsData> {
        return Single.create{ emitter ->
            // First remove existing comments for this post.
            PredatorDatabase.getInstance()
                    .deleteCommentsForPost(postId)
            addCommentsToDatabase(postCommentsData.comments)
            emitter.onSuccess(postCommentsData)
        }
    }

    private fun addCommentsToDatabase(comments: List<PostCommentsData.Comments>) {
        comments.forEach {
            PredatorDatabase.getInstance()
                    .insertComment(PredatorDbValuesHelper.getContentValuesForComments(it))
            addCommentsToDatabase(it.childComments)
        }
    }

    private fun fetchCommentsFromDatabase(postId: Int): Single<List<Comment>> {
        return Single.create{
            val comments = PredatorDatabase.getInstance()
                    .getCommentsForPost(postId,
                            0,
                            ArrayList<Comment>(),
                            0,
                            CommentTimeCalculator.getInstance())
            it.onSuccess(comments)
        }
    }
}