package com.threeblades.kuro.ui

object KuroLines {
    private val onTap = listOf(
        "What.",
        "Oh good — you're alive. Astonishing.",
        "Yes. Hi. Get on with whatever you're meant to be doing.",
        "I was napping. You woke me. Hope it was important.",
        "Still here. Still old. Still waiting for you to do the thing.",
        "Mm. Tapping me. Astounding.",
        "You tapped me. Now what.",
        "Hello. I am already unimpressed.",
        "Right. What needs doing.",
        "Honestly. The amount of tapping.",
    )

    private val onAck = listOf(
        "Good. Off your plate.",
        "Finally. Don't make me say it again.",
        "Mm. About time. Move along.",
        "Logged. Try not to need rescuing again.",
        "Good. I'll trouble you later, no doubt.",
    )

    const val IDLE_BUBBLE = "Tap me. Or don't. See if I care."

    fun randomTap(): String = onTap.random()
    fun randomAck(): String = onAck.random()
}
