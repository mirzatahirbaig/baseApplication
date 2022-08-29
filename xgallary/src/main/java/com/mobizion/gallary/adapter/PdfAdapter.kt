package com.mobizion.gallary.adapter

//class PdfAdapter(
//    private val onMediaSelectedListener: OnItemSelectedListener<PdfFile>,
//) : BaseListAdapter<PdfFile, PdfDiffUtil>(PdfDiffUtil()) {
//
//    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int) = PdfViewHolder(
//        LayoutPdfItemBinding.inflate(LayoutInflater.from(parent.context),parent,false),
//        onMediaSelectedListener
//    )
//
//    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as PdfViewHolder).bind(getItem(position),position)
//    }
//
//    override fun getItemViewType(position: Int) = position
//}