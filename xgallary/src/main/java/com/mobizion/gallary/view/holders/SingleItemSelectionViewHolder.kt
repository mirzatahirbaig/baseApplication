package com.mobizion.gallary.view.holders


//class SingleItemSelectionViewHolder(private val binding: LayoutGalleryItemBinding, val onMediaSelectedListener: OnItemSelectedListener<GalleryMedia>):
//    BaseViewHolder<LayoutGalleryItemBinding, GalleryMedia>(binding) {
//    override fun bind(item: GalleryMedia, position: Int) {
//        binding.cbSelect.visible(false)
//        binding.txtVideoDuration.visible(false)
//        Glide.with(binding.imgImage)
//            .load(item.contentUri)
//            .thumbnail(0.33f)
//            .centerCrop()
//            .into(binding.imgImage)
//        binding.imgImage.setOnClickListener {
//            onMediaSelectedListener.onItemSelected(item,position)
//        }
//    }
//}