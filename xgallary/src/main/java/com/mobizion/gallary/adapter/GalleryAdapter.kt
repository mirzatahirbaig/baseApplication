package com.mobizion.gallary.adapter




//class GalleryAdapter(
//    private val isChat: Boolean,
//    private val onMediaSelectedListener: OnItemSelectedListener<GalleryMedia>,
//    val checkedChangedListener: CheckedChangedListener
//) : BaseListAdapter<GalleryMedia, MediaDiffUtil>(MediaDiffUtil()) {
//
//    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int) =
//        if (!isChat) {
//            SingleItemSelectionViewHolder(
//                LayoutGalleryItemBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                ),
//                onMediaSelectedListener
//            )
//        } else {
//            MultiItemSelectionViewHolder(
//                LayoutGalleryItemBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                ),
//                checkedChangedListener
//            )
//        }
//
//    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
//        if (!isChat) {
//            (holder as SingleItemSelectionViewHolder).bind(getItem(position), position)
//        } else {
//            (holder as MultiItemSelectionViewHolder).bind(getItem(position), position)
//        }
//    }
//
//    override fun getItemViewType(position: Int) = position
//}