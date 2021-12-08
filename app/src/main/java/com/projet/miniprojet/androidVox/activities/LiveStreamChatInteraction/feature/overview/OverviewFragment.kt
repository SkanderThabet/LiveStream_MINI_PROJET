package com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.feature.overview

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.databinding.FragmentOverviewBinding
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.feature.event.detail.EventDetailsActivity
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.AppConfig

/**
 * Fragment that shows information about the conference: title, description,
 * statistics, partners, schedule, etc.
 */
class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentOverviewBinding.inflate(inflater, container, false)
            .apply { _binding = this }
            .root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleSectionInclude.eventSubtitleTextView.text = Html.fromHtml(
            getString(R.string.overview_subtitle),
            Html.FROM_HTML_MODE_COMPACT
        )
        binding.event1Include.event1CardView.setOnClickListener {
            startActivity(
                EventDetailsActivity.createIntent(
                    requireContext(),
                    AppConfig.LIVESTREAM_ESG_DATA,
                    requireContext().getString(R.string.overview_hypercube_title)
                )
            )
        }
        binding.event2Include.event2CardView.setOnClickListener {
            startActivity(
                EventDetailsActivity.createIntent(
                    requireContext(),
                    AppConfig.LIVESTREAM_DATA_STRATEGY,
                    requireContext().getString(R.string.overview_layers_title)
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
