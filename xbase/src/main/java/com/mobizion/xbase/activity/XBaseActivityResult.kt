package com.mobizion.xbase.activity

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract


/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 12/12/2022
 */

class XBaseActivityResult<Input, Result> private constructor(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<Input, Result>,
    private var onActivityResult: OnActivityResult<Result>?
) {
    /**
     * Callback interface
     */
    fun interface OnActivityResult<O> {
        /**
         * Called after receiving a result from the target activity
         */
        fun onActivityResult(result: O)
    }

    private val launcher: ActivityResultLauncher<Input>

    init {
        launcher = caller.registerForActivityResult(contract) { result: Result ->
            callOnActivityResult(
                result
            )
        }
    }

    fun setOnActivityResult(onActivityResult: OnActivityResult<Result>?) {
        this.onActivityResult = onActivityResult
    }
    /**
     * Launch activity, same as [ActivityResultLauncher.launch] except that it allows a callback
     * executed after receiving a result from the target activity.
     */
    /**
     * Same as [.launch] with last parameter set to `null`.
     */
    fun launch(
        input: Input,
        onActivityResult: OnActivityResult<Result>
    ) {
        this.onActivityResult = onActivityResult
        launcher.launch(input)
    }

    private fun callOnActivityResult(result: Result) {
        onActivityResult?.onActivityResult(result)
    }

    companion object {

        /**
         * Register activity result using a [ActivityResultContract] and an in-place activity result callback like
         * the default approach. You can still customise callback using [.launch].
         */
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller,
            contract: ActivityResultContract<Input, Result>,
            onActivityResult: OnActivityResult<Result>?
        ): XBaseActivityResult<Input, Result> {
            return XBaseActivityResult(caller, contract, onActivityResult)
        }

        /**
         * Same as [.registerForActivityResult] except
         * the last argument is set to `null`.
         */
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller,
            contract: ActivityResultContract<Input, Result>
        ): XBaseActivityResult<Input, Result> {
            return registerForActivityResult(caller, contract, null)
        }
    }
}