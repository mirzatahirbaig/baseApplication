/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.mobizion.base.view.model.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    Fragment() {

    lateinit var binding: B
    lateinit var mInflater: LayoutInflater

    val permissionsViewModel by sharedViewModel<PermissionsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBackPress()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mInflater = inflater
        binding = bindingFactory(inflater, container, false)
        return binding.root
    }

    protected open fun registerBackPress() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(enableBackPress() /* enabled by default */) {
                override fun handleOnBackPressed() {
                    backPressed(binding.root)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    open fun backPressed(view: View){

    }

    abstract fun onViewCreated()

    abstract fun enableBackPress(): Boolean

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, onChanged)
    }
}