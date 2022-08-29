package com.mobizion.xbase.fragment.extensions

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

/**
 * Extension to get Dimension resource
 * @param id is resource id
 * @return Dimension
 */

fun Fragment.getDimension(@DimenRes id: Int): Float = requireActivity().resources.getDimension(id)

/**
 * Extension to navigate from one fragment to another
 * @param navDirections navigationDirection define in navigation graph
 */

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

/**
 * Extension to get fragment manager
 * @return current fragment manager
 */

fun Fragment.getManager() = requireActivity().supportFragmentManager