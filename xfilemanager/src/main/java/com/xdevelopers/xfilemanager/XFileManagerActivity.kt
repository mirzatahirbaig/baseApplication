package com.xdevelopers.xfilemanager

import com.mobizion.base.activity.BaseActivity
import com.xdevelopers.xfilemanager.databinding.ActivityXfileManagerBinding
import com.xdevelopers.xfilemanager.viewmodels.FileManagerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class XFileManagerActivity : BaseActivity<ActivityXfileManagerBinding>(ActivityXfileManagerBinding::inflate) {

    private val fileMangeViewModel: FileManagerViewModel by viewModel()
    var selectDirectory: String = ""

    override fun initViews() {
       binding.doneTextView.setOnClickListener {
           finish()
       }
        binding.addNewProject.setOnClickListener {
            fileMangeViewModel.createNewProject(selectDirectory)
        }
    }
    override fun shouldHideKeyboard(): Boolean {
        return false
    }

}