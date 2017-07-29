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

package com.crazyhitty.chdev.ks.predator.utils;

import com.crazyhitty.chdev.ks.predator.models.Comment;

import java.util.concurrent.TimeUnit;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/16/17 5:14 PM
 * Description: Unavailable
 */

public class CommentTimeCalculator {
    private static CommentTimeCalculator sCommentTimeCalculator;

    public static CommentTimeCalculator getInstance() {
        if (sCommentTimeCalculator == null) {
            sCommentTimeCalculator = new CommentTimeCalculator();
        }
        return sCommentTimeCalculator;
    }

    private CommentTimeCalculator() {

    }

    public CommentTime getTimeAgo(long createdAtMillis) {
        long timeDifferenceInMillis = System.currentTimeMillis() - createdAtMillis;
        if (timeDifferenceInMillis <= TimeUnit.SECONDS.toMillis(1)) {
            return new CommentTime(1, Comment.TIME_UNIT.SECOND_AGO);
        } else if (timeDifferenceInMillis < TimeUnit.MINUTES.toMillis(1)) {
            return new CommentTime(TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMillis),
                    Comment.TIME_UNIT.SECOND_AGO_PLURAL);
        } else if (timeDifferenceInMillis < TimeUnit.MINUTES.toMillis(2)) {
            return new CommentTime(TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis),
                    Comment.TIME_UNIT.MINUTE_AGO);
        } else if (timeDifferenceInMillis < TimeUnit.HOURS.toMillis(1)) {
            return new CommentTime(TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis),
                    Comment.TIME_UNIT.MINUTE_AGO_PLURAL);
        } else if (timeDifferenceInMillis < TimeUnit.HOURS.toMillis(2)) {
            return new CommentTime(TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis),
                    Comment.TIME_UNIT.HOUR_AGO);
        } else if (timeDifferenceInMillis < TimeUnit.DAYS.toMillis(1)) {
            return new CommentTime(TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis),
                    Comment.TIME_UNIT.HOUR_AGO_PLURAL);
        } else if (timeDifferenceInMillis < TimeUnit.DAYS.toMillis(2    )) {
            return new CommentTime(TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis),
                    Comment.TIME_UNIT.DAY_AGO);
        } else {
            return new CommentTime(TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis),
                    Comment.TIME_UNIT.DAY_AGO_PLURAL);
        }
    }

    public class CommentTime {
        private long timeAgo;
        private Comment.TIME_UNIT timeUnit;

        public CommentTime() {
        }

        public CommentTime(long timeAgo, Comment.TIME_UNIT timeUnit) {
            this.timeAgo = timeAgo;
            this.timeUnit = timeUnit;
        }

        public long getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(long timeAgo) {
            this.timeAgo = timeAgo;
        }

        public Comment.TIME_UNIT getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(Comment.TIME_UNIT timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
}
