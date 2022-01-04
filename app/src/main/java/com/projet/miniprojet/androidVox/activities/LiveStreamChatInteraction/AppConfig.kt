package com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction

import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.UserCredentials
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User

object AppConfig {
    const val API_KEY = "7de6n9dsyzjx"

    /**
     * Available livestream channels.
     */
    const val LIVESTREAM_ESG_DATA = "livestream:esg_data_7c56f238-aed1-40f1-83b6-d1172a26973f"
    const val LIVESTREAM_DATA_STRATEGY =
        "livestream:data_strategy_c80d2d33-88a9-4bff-9ce4-0a13c3272238"


    val availableUsers: List<UserCredentials> = listOf(
        UserCredentials(
            name = "Skander Thabet",
            image = "https://lh3.googleusercontent.com/fife/AAWUweUhX40OYOIPBez2bjYEzUkzPMSu4DafwDdZOvrbv0WnN4CoHSRdhfmTILoci8QFgccB_6InDI4egmSLcs_y_p0bzRg9HjJbHggtbms9MaSeBEkcdbgS427Q-NjhHTUS6eSN0FIsm-7MV2_mXbKq4bGVlA32YHgS4VwOJa0G_bO5a0cTcD6Fxy96ZQUUduemtqvmVZ9UkpDqxdQdOjvrVLXPIyH0RYHk5iK5rqPEvGaIRQF5r6G284VH31R_OwfutzQcOm_ayQan5kODsmOi-iXb4TpGe4qKw_Cz--UgJxkrBBUOG9_D538vZXRMbhnDP2K01XEGCLJriPfP8rmrrMYbDVRXiN2VA1T0dbdFitMtrJNdGC0eJdF7Kb3Bj5zNc9E4m0VzFRfdvC1-bBRWngECZzMh2TgrTlYAQ8th8HKsq8FUMaOXP7VFgLn55kbfxUmycP6hKLlLxvoQ2r-hySZ1wmb54sGfCScx4dqSVm_GyYnJsF8Yx9IF2CM8_q7Iwq-9KdDSe9YBET0F8NmFnUekI0gDaRnO4GszBzWNDGqPBWgjtRmYeLrRvVFN53HrHIeZm6fTH677hqI8Vw0ykdSN08QehDRJAFetdGJnIqWsVS54xq4pKkbL1YzjxhNhQrQlOeH_rfWxIiG2vVFx6rOvC8mzq4frTJ6pBqAt5hWgU7UroKpcDrmgIIo4mVyvaqiKAoMTj2qVg5Cb_20GzzFZJQK0_PYW4vOB4muVEtw5Dxc=b\"",
            id = "1f37e58d-d8b0-476a-a4f2-f8611e0d85d9",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMWYzN2U1OGQtZDhiMC00NzZhLWE0ZjItZjg2MTFlMGQ4NWQ5In0.aom5u_9KymJkz_S-R6AAZmSKAV-k-rCLO6-3sjplfWs"
        )

    )
}
