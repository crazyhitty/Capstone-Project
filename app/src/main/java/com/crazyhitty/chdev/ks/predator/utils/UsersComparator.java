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

import com.crazyhitty.chdev.ks.predator.models.User;

import java.util.Comparator;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/14/2017 8:44 PM
 * Description: Unavailable
 */

public class UsersComparator implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        if (user1.getType() == User.TYPE.HUNTER ||
                user1.getType() == User.TYPE.BOTH &&
                        user2.getType() == User.TYPE.MAKER) {
            return -1;
        } else if (user2.getType() == User.TYPE.HUNTER ||
                user2.getType() == User.TYPE.BOTH &&
                        user1.getType() == User.TYPE.MAKER) {
            return 1;
        } else if (user1.getType() == User.TYPE.HUNTER ||
                user1.getType() == User.TYPE.MAKER ||
                user1.getType() == User.TYPE.BOTH &&
                        user2.getType() == User.TYPE.UPVOTER) {
            return -1;
        } else if (user1.getType() == User.TYPE.UPVOTER &&
                user2.getType() == User.TYPE.HUNTER ||
                user2.getType() == User.TYPE.MAKER ||
                user2.getType() == User.TYPE.BOTH) {
            return 1;
        }
        return 0;
    }
}
