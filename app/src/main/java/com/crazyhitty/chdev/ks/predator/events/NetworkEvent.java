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

package com.crazyhitty.chdev.ks.predator.events;

import android.net.ConnectivityManager;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/10/2017 5:26 PM
 * Description: Event used for managing internet connection changes.
 */

public class NetworkEvent {
    private boolean connected;
    private TYPE type;

    public NetworkEvent() {
    }

    public NetworkEvent(boolean connected, TYPE type) {
        this.connected = connected;
        this.type = type;
    }

    public NetworkEvent(boolean connected, int networkType) {
        this.connected = connected;
        setType(networkType);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(int networkType) {
        switch (networkType) {
            case ConnectivityManager.TYPE_MOBILE:
                type = TYPE.MOBILE;
                break;
            case ConnectivityManager.TYPE_WIFI:
                type = TYPE.WIFI;
                break;
            default:
                type = TYPE.OTHER;
                break;
        }
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        WIFI,
        MOBILE,
        OTHER
    }
}
