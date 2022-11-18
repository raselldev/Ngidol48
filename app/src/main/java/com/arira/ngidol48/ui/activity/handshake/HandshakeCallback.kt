package com.arira.ngidol48.ui.activity.handshake

import com.arira.ngidol48.model.ParentHandshake

interface HandshakeCallback {
    fun handshakeShow(data: ParentHandshake)
}