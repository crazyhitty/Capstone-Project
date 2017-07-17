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

package com.crazyhitty.chdev.ks.predator.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.ui.activities.UserProfileActivity;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/16/17 10:10 PM
 * Description: Unavailable
 */

public class CommentUserPreviewDialog extends AlertDialog {
    private static final String TAG = "CommentUserPreviewDialog";

    @BindView(R.id.image_view_user)
    SimpleDraweeView imgViewUser;
    @BindView(R.id.text_view_user_name_alternate)
    TextView txtUsernameAlternate;
    @BindView(R.id.text_view_user_name)
    TextView txtUsername;
    @BindView(R.id.text_view_user_headline)
    TextView txtUserHeadline;
    @BindView(R.id.text_view_comment_body)
    TextView txtCommentBody;
    @BindView(R.id.text_view_comment_extra_details)
    TextView txtCommentExtraDetails;
    @BindView(R.id.scroll_view_comment_content)
    ScrollView scrollViewCommentContent;
    @BindView(R.id.relative_layout_comment_content)
    RelativeLayout relativeLayoutCommentContent;
    @BindView(R.id.view_divider)
    View viewDivider;

    public static void show(Context context, Comment comment) {
        CommentUserPreviewDialog commentUserPreviewDialog = new CommentUserPreviewDialog(context,
                comment);
        commentUserPreviewDialog.show();
    }

    protected CommentUserPreviewDialog(@NonNull Context context, Comment comment) {
        super(context);
        initializeViews(context, comment);
    }

    protected CommentUserPreviewDialog(@NonNull Context context, int themeResId, Comment comment) {
        super(context, themeResId);
        initializeViews(context, comment);
    }

    protected CommentUserPreviewDialog(@NonNull Context context,
                                       boolean cancelable,
                                       @Nullable OnCancelListener cancelListener,
                                       Comment comment) {
        super(context, cancelable, cancelListener);
        initializeViews(context, comment);
    }

    private void initializeViews(Context context, final Comment comment) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_comment_user_preview, null);
        setView(view);
        ButterKnife.bind(this, view);

        setButton(BUTTON_POSITIVE,
                context.getText(R.string.dialog_comment_user_preview_positive_button),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserProfileActivity.startActivity(getContext(), comment.getUserId());
                    }
                });

        setButton(BUTTON_NEGATIVE,
                context.getText(R.string.dialog_comment_user_preview_negative_button),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                });

        setButton(BUTTON_NEUTRAL,
                context.getText(R.string.dialog_comment_user_preview_neutral_button),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Not yet implemented!", Toast.LENGTH_SHORT).show();
                        //shareComment(comment);
                    }
                });

        setContent(comment);
    }

    private void setContent(Comment comment) {
        txtUsernameAlternate.setText(getContext().getString(R.string.dialog_comment_user_preview_username_alternative,
                comment.getUsernameAlternative()));
        txtUsername.setText(comment.getUsername());
        txtUserHeadline.setText(comment.getUserHeadline());
        txtCommentBody.setText(Html.fromHtml(comment.getBody()));
        txtCommentBody.setMovementMethod(LinkMovementMethod.getInstance());

        // Set uer image.
        String userImageUrl = comment.getUserImageThumbnailUrl();
        userImageUrl = ImageUtils.getCustomCommentUserImageThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(getContext(), 44),
                ScreenUtils.dpToPxInt(getContext(), 44));
        imgViewUser.setImageURI(userImageUrl);

        // Hide headline text if headline is empty.
        txtUserHeadline.setVisibility(TextUtils.isEmpty(comment.getUserHeadline()) ?
                View.GONE : View.VISIBLE);

        // Set extra details.
        String extraDetails = String.format("%s \u2022 %s",
                getContext().getResources().getQuantityString(R.plurals.item_post_details_comment_votes,
                        comment.getVotes(),
                        comment.getVotes()),
                getContext().getString(getStringResourceIdForTimeUnit(comment.getTimeUnit()),
                        comment.getTimeAgo()));
        txtCommentExtraDetails.setText(extraDetails);

        ViewTreeObserver observer = scrollViewCommentContent.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onGlobalLayout() {
                int viewHeight = scrollViewCommentContent.getMeasuredHeight();
                int contentHeight = relativeLayoutCommentContent.getHeight();
                Logger.d(TAG, "viewHeight: " + viewHeight + " ; contentHeight: " + contentHeight);
                if (viewHeight  - contentHeight < 0) {
                    // Scrollable.
                    Logger.d(TAG, "View is scrollable.");
                    viewDivider.setVisibility(View.VISIBLE);
                    relativeLayoutCommentContent.setPadding(0,
                            0,
                            0,
                            ScreenUtils.dpToPxInt(getContext(), 8));
                } else {
                    Logger.d(TAG, "View is not scrollable.");
                    viewDivider.setVisibility(View.GONE);
                    relativeLayoutCommentContent.setPadding(0,
                            0,
                            0,
                            0);
                }
            }
        });
    }

    private int getStringResourceIdForTimeUnit(Comment.TIME_UNIT timeUnit) {
        switch (timeUnit){
            case SECOND_AGO:
                return R.string.item_post_details_comment_second_ago;
            case SECOND_AGO_PLURAL:
                return R.string.item_post_details_comment_second_ago_plural;
            case MINUTE_AGO:
                return R.string.item_post_details_comment_minute_ago;
            case MINUTE_AGO_PLURAL:
                return R.string.item_post_details_comment_minute_ago_plural;
            case HOUR_AGO:
                return R.string.item_post_details_comment_hour_ago;
            case HOUR_AGO_PLURAL:
                return R.string.item_post_details_comment_hour_ago_plural;
            case DAY_AGO:
                return R.string.item_post_details_comment_day_ago;
            case DAY_AGO_PLURAL:
                return R.string.item_post_details_comment_day_ago_plural;
            default:
                throw new IllegalArgumentException("Provided TIME_UNIT is not valid.");
        }
    }

    private void shareComment(Comment comment) {
        /*String body = getContext().getString(R.string.dialog_comment_user_preview_share_content,
                comment.getUsernameAlternative(),
                "",
                "");*/

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(Intent.createChooser(sharingIntent,
                getContext().getString(R.string.share_using)));
    }
}