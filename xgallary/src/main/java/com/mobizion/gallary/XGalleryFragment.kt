package com.mobizion.gallary

import com.mobizion.gallary.databinding.FragmentGalleryBinding
import com.mobizion.xbase.fragment.XBaseFragment

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
class XGalleryFragment:XBaseFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {

    override fun onViewCreated() {

    }

    override fun enableBackPress() = false
}