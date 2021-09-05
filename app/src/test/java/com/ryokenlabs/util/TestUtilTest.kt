package com.ryokenlabs.util


import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.util.TestUtil.checkCurrency
import org.junit.Test

class TestUtilTest {

    /***
     *
     * class to setup quick tests
     *
     * */

    @Test
    fun `empty currencies`() {
        val result = checkCurrency("")
        assertThat(result).isFalse()
    }
}