package com.fit2081.ian_34423680.nutritrackpro_app.utils

object PersonaData {
    val personaDescriptions = mapOf(
        "Health Devotee" to "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas.",
        "Mindful Eater" to "I’m health-conscious and being healthy and eating healthy is important to me. I make conscious lifestyle decisions based on what I believe healthy means.",
        "Wellness Striver" to "I aspire to be healthy (but struggle sometimes). I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes.",
        "Balance Seeker" to "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I get inspired by social media like restaurants, recipes, and eating tips.",
        "Health Procrastinator" to "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics, but other things in life take priority.",
        "Food Carefree" to "I’m not bothered about healthy eating. I don’t really see the point and I don’t care what I eat."
    )

    fun getImageResourceName(persona: String): String {
        return persona.lowercase().replace(" ", "_")
    }
}