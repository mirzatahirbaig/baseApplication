package com.xdevelopers.xfilemanager

import com.mobizion.xbase.activity.XBaseActivity
import com.xdevelopers.xfilemanager.databinding.ActivityXfileManagerBinding
import com.xdevelopers.xfilemanager.viewmodels.FileManagerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class XFileManagerActivity : XBaseActivity<ActivityXfileManagerBinding>(ActivityXfileManagerBinding::inflate) {

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