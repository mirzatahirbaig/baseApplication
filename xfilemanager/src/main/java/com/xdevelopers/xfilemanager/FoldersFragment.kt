package com.xdevelopers.xfilemanager

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mobizion.base.fragment.BaseFragment
import com.xdevelopers.xfilemanager.adapters.ProjectsAdapter
import com.xdevelopers.xfilemanager.databinding.FragmentFoldersBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.xdevelopers.xfilemanager.viewmodels.FileManagerViewModel

class FoldersFragment: BaseFragment<FragmentFoldersBinding>(FragmentFoldersBinding::inflate) {

    private lateinit var projectsAdapter: ProjectsAdapter
    private val fileMangeViewModel: FileManagerViewModel by sharedViewModel()
    private val args: FoldersFragmentArgs by navArgs()
    override fun onViewCreated() {
        projectsAdapter = ProjectsAdapter{ media, position ->
            Toast.makeText(requireContext(), media.title, Toast.LENGTH_SHORT).show()
            findNavController().navigate(FoldersFragmentDirections.actionFoldersFragmentSelf(media.title))
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