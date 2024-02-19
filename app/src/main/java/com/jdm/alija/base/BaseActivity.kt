package com.jdm.alija.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jdm.alija.presentation.util.slideLeft
import com.jdm.alija.presentation.util.slideRight

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    @get:LayoutRes
    abstract val layoutResId: Int
    lateinit var binding: T
        private set



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        initState()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }


    abstract fun initView()
    abstract fun subscribe()
    abstract fun initEvent()
    abstract fun initData()


    open fun initState() {
        initView()
        initEvent()
        subscribe()
        initData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        slideLeft()
    }

    override fun finish() {
        super.finish()
        slideLeft()
    }
    fun goToActivity(intent: Intent) {
        startActivity(intent)
        slideRight()
    }
    protected fun isPermissionAllow(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
    protected fun isPermissionAllow(permission: Array<String>): Boolean {
        return permission.filter {

            var result = ContextCompat.checkSelfPermission(this, it)
            result == PackageManager.PERMISSION_DENIED }.isEmpty()
    }
    protected fun exitApp() {
        this.moveTaskToBack(true)
        this.finish()
        System.exit(0)
    }

}
