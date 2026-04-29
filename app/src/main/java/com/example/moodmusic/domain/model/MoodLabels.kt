package com.example.moodmusic.domain.model

val moodLabels = listOf(
    EmotionLabel("Alegría", "😄", 270f, 0.75f, MoodType.HAPPY, "happy upbeat pop"),
    EmotionLabel("Inspiración", "✨", 292.5f, 0.75f, MoodType.HAPPY, "inspiring cinematic music"),
    EmotionLabel("Ilusión", "🌟", 315f, 0.75f, MoodType.HAPPY, "dreamy pop music"),
    EmotionLabel("Motivación", "⚡", 337.5f, 0.75f, MoodType.ENERGY, "motivational pop music"),

    EmotionLabel("Energía", "🔥", 0f, 0.75f, MoodType.ENERGY, "energetic dance music"),
    EmotionLabel("Equilibrio", "🧘", 22.5f, 0.75f, MoodType.RELAX, "balanced calm music"),
    EmotionLabel("Relajo", "🌿", 45f, 0.75f, MoodType.RELAX, "relax chill music"),
    EmotionLabel("Tranquilidad", "😌", 67.5f, 0.75f, MoodType.RELAX, "peaceful piano music"),

    EmotionLabel("Comodidad", "🫧", 90f, 0.75f, MoodType.RELAX, "comfort lofi music"),
    EmotionLabel("Aburrimiento", "😐", 112.5f, 0.75f, MoodType.SAD, "bored chill music"),
    EmotionLabel("Soledad", "🌧️", 135f, 0.75f, MoodType.SAD, "lonely acoustic songs"),
    EmotionLabel("Tristeza", "😢", 157.5f, 0.75f, MoodType.SAD, "sad acoustic music"),

    EmotionLabel("Cansancio", "😴", 180f, 0.75f, MoodType.SAD, "sleep calm ambient music"),
    EmotionLabel("Ansiedad", "😟", 202.5f, 0.75f, MoodType.ANGRY, "anxiety calm music"),
    EmotionLabel("Enfado", "😠", 225f, 0.75f, MoodType.ANGRY, "angry rock music"),
    EmotionLabel("Frustración", "😤", 247.5f, 0.75f, MoodType.ANGRY, "frustration rock music")
)