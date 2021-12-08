package com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.feature.event.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.databinding.FragmentEventsBinding
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.feature.event.detail.EventDetailsActivity
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.AppConfig

/**
 * Fragment with a list of conference events.
 */
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEventsBinding.inflate(inflater, container, false)
            .apply { _binding = this }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
}
