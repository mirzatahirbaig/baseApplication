package com.mobizion.xbase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mobizion.xbase.view.model.PermissionViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
abstract class XBaseFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    Fragment()  {

    lateinit var binding: B
    lateinit var mInflater: LayoutInflater
    val permissionViewModel:PermissionViewModel by sharedViewModel()


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

    /**
     * Override the activity Back Pressed function with it's own Back Pressed
     */

    protected open fun registerBackPress() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(enableBackPress()) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    open fun backPressed(){
        findNavController().popBackStack()
    }

    abstract fun onViewCreated()

    abstract fun enableBackPress(): Boolean

    /**
     * Extension to observe live data
     */

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, onChanged)
    }
}