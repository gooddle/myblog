package com.teamsparta.myblog.infra.aop

import java.io.Serial

class NotFoundException : RuntimeException {

    @Serial
    private val serialVersionUID: Long = -4937536120992271478L

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)
}