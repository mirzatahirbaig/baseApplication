package com.xdevelopers.xfilemanager

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mobizion.xbase.fragment.XBaseFragment
import com.xdevelopers.xfilemanager.adapters.ProjectsAdapter
import com.xdevelopers.xfilemanager.databinding.FragmentFoldersBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.xdevelopers.xfilemanager.viewmodels.FileManagerViewModel

class FoldersFragment: XBaseFragment<FragmentFoldersBinding>(FragmentFoldersBinding::inflate) {

    private lateinit var projectsAdapter: ProjectsAdapter
    private val fileMangeViewModel: FileManagerViewModel by sharedViewModel()
    private val args: FoldersFragmentArgs by navArgs()
    override fun onViewCreated() {
        projectsAdapter = ProjectsAdapter{ media, _ ->
           if (!media.isFile){
               findNavController().navigate(FoldersFragmentDirections.actionFoldersFragmentSelf(media.title))
           }else{
               val intent = Intent()
               intent.data = media.filePath
               requireActivity().setResult(Activity.RESULT_OK, intent)
               requireActivity().finish()
           }
        }
        binding.projectRecyclerView.layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
        binding.projectRecyclerView.adapter = projectsAdapter

        (context as XFileManagerActivity).selectDirectory = args.folderName
            fileMangeViewModel.getAllProjects(args.folderName)

        fileMangeViewModel.projects.observe {
            projectsAdapter.submitList(it)
        }
    }

    override fun enableBackPress(): Boolean {
        return false
    }

}