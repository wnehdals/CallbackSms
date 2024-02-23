package com.jdm.alija.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    @get:LayoutRes
    abstract val layoutResId: Int

    protected var backPressedTime: Long = 0
    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    open var onBackPressedCallback: OnBackPressedCallback? = null

    private lateinit var requestPermission: ActivityResultLauncher<Array<String>>




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPress()
        initState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun initView()
    abstract fun initEvent()
    abstract fun subscribe()
    abstract fun initData()

    open fun initState() {
        initView()
        initEvent()
        subscribe()
        initData()
    }

    fun setOnBackPress() {
        if (onBackPressedCallback == null) {
            return
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)
    }
    fun removeBackPress() {
        if (onBackPressedCallback == null) {
            return
        }
        onBackPressedCallback?.remove()
    }

    override fun onDetach() {
        onBackPressedCallback?.remove()
        onBackPressedCallback = null
        super.onDetach()
    }
    protected fun exitApp() {
        requireActivity().finish()
    }

}
