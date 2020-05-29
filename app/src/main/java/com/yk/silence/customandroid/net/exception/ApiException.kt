package com.yk.silence.customandroid.net.exception

import java.lang.RuntimeException

/**
 * 异常
 */
class ApiException(var code: Int, override var message: String) : RuntimeException()