package com.frogobox.kickstart.mvvm.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frogobox.kickstart.core.BaseFragment
import com.frogobox.kickstart.databinding.FragmentFavoriteBinding
import com.frogobox.kickstart.mvvm.watch.WatchActivity
import com.frogobox.kickstart.util.Constant
import com.frogobox.recycler.core.FrogoRecyclerNotifyListener
import com.frogobox.recycler.core.IFrogoBindingAdapter
import com.frogobox.recycler.ext.injectorBinding
import com.frogobox.ui.databinding.FrogoRvListType2Binding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoFragment : BaseFragment<FragmentFavoriteBinding>() {

    private val viewModel: VideoViewModel by viewModel()

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun setupViewModel() {
        viewModel.apply {
            simpleVideoList.observe(viewLifecycleOwner) {
                setupRv(it)
            }
        }
    }

    override fun onViewCreatedExt(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedExt(view, savedInstanceState)
        viewModel.getSimpleVideoList()
    }

    private fun setupRv(data: List<Video>) {
        binding.rvVideo
            .injectorBinding<Video, FrogoRvListType2Binding>()
            .addData(data)
            .addCallback(object : IFrogoBindingAdapter<Video, FrogoRvListType2Binding> {
                override fun onItemClicked(
                    binding: FrogoRvListType2Binding,
                    data: Video,
                    position: Int,
                    notifyListener: FrogoRecyclerNotifyListener<Video>,
                ) {
                    startActivity(Intent(requireContext(), WatchActivity::class.java).apply {
                        putExtra(Constant.Extra.EXTRA_VIDEO_URL, data.url)
                        putExtra(Constant.Extra.EXTRA_VIDEO_TITLE, data.title)
                    })
                }

                override fun onItemLongClicked(
                    binding: FrogoRvListType2Binding,
                    data: Video,
                    position: Int,
                    notifyListener: FrogoRecyclerNotifyListener<Video>,
                ) {

                }

                override fun setViewBinding(parent: ViewGroup): FrogoRvListType2Binding {
                    return FrogoRvListType2Binding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun setupInitComponent(
                    binding: FrogoRvListType2Binding,
                    data: Video,
                    position: Int,
                    notifyListener: FrogoRecyclerNotifyListener<Video>,
                ) {
                    binding.apply {
                        frogoRvListType2TvTitle.text = data.title
                        frogoRvListType2TvSubtitle.text = data.url
                    }
                }
            })
            .createLayoutLinearVertical(false)
            .build()
    }

}