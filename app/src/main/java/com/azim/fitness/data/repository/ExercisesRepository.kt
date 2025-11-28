package com.azim.fitness.data.repository

import com.azim.fitness.R
import com.azim.fitness.db.dao.ExercisesDao
import com.azim.fitness.db.entity.Exercise
import com.azim.fitness.preferences.PreferencesHelper
import com.azim.fitness.ui.goals.Goal

class ExercisesRepository(
    private val preferencesHelper: PreferencesHelper,
    private val exercisesDao: ExercisesDao
) {

    fun getLocalExercises() = exercisesDao.getLocalExercises()

    suspend fun addExercises(exercises: List<Exercise>) {
        val count = exercisesDao.countRecords()
        if (count == 0) {
            exercisesDao.addExercises(exercises)
        }
    }

    suspend fun updateExercise(id: Int, completed: Boolean) =
        exercisesDao.updateExercise(id, completed)

    fun getExercises() =
        when (preferencesHelper.goal) {
        Goal.LOOSE_WEIGHT -> looseWeightExercises
        Goal.GAIN_MUSCLE -> gainMuscleExercises
        Goal.MAINTAIN_FORM -> maintainFormExercises
    }

    private val looseWeightExercises = listOf(
        Exercise(
            id = 1,
            name = "Приседания",
            technique = "Ноги на ширине плеч\n" +
                    "Спина ровная\n" +
                    "Опускаться до параллели бедер с полом",
            techniqueImg = R.drawable.squats,
            sets = 3,
            reps = "15–20 раз"
        ),

        Exercise(
            id = 2,
            name = "Выпады",
            technique = "Длинный шаг вперед\n" +
                    "Колено задней ноги опускается почти до пола\n" +
                    "Корпус держать прямо",
            techniqueImg = R.drawable.lunges,
            sets = 3,
            reps = "по 12 раз на каждую ногу"
        ),

        Exercise(
            id = 3,
            name = "Планка",
            technique = "Локти под плечами\n" +
                    "Тело прямое\n" +
                    "Не прогибаться в пояснице",
            techniqueImg = R.drawable.plank,
            sets = 3,
            reps = "30–45 секунд"
        ),

        Exercise(
            id = 4,
            name = "Берпи",
            technique = "Присед → упор лёжа → обратно → прыжок\n" +
                    "Корпус держать ровно\n" +
                    "На приземлении смягчать колени",
            techniqueImg = R.drawable.burpee,
            techniqueVideo = "https://youtu.be/50-0F_gQHFI?si=5aVngMbYbu6HaEmY",
            sets = 3,
            reps = "10–15 раз"
        ),

        Exercise(
            id = 5,
            name = "Jumping Jacks",
            technique = "Прыжок с разведением рук и ног\n" +
                    "Держать темп ровным\n" +
                    "Колени слегка мягкие",
            techniqueImg = R.drawable.jumping_jack,
            sets = 3,
            reps = "40–50 повторений или 2–3 минуты"
        ),

        Exercise(
            id = 6,
            name = "Бег",
            techniqueVideo = "https://youtu.be/XtbwtiQaLLw?si=xz4ptvdYUCWrfh21",
            sets = 0,
            reps = "5 км"
        )
    )

    private val gainMuscleExercises = listOf(
        Exercise(
            id = 7,
            name = "Отжимания",
            technique = "Ладони на ширине плеч\n" +
                    "Корпус прямой\n" +
                    "Опускаться до касания груди полом",
            sets = 3,
            reps = "12–20 раз"
        ),

        Exercise(
            id = 8,
            name = "Отжимания узким хватом (на трицепс)",
            technique = "Ладони под плечами или чуть уже\n" +
                    "Локти вдоль корпуса\n" +
                    "Корпус прямой",
            sets = 3,
            reps = "10–15 раз"
        ),

        Exercise(
            id = 9,
            name = "Подтягивания",
            technique = "Полный вис на прямых руках\n" +
                    "Подтягивать грудь к перекладине\n" +
                    "Опускаться до полного разгибания локтей",
            sets = 4,
            reps = "6–12 раз"
        ),

        Exercise(
            id = 10,
            name = "«Супермен» (спина)",
            technique = "Одновременный подъём рук и ног\n" +
                    "Держать паузу 1–2 секунды\n" +
                    "Не прогибаться чрезмерно в пояснице",
            sets = 3,
            reps = "12–15 раз"
        ),

        Exercise(
            id = 11,
            name = "Болгарские приседания",
            technique = "Одна нога на опоре сзади\n" +
                    "Корпус слегка наклонён вперёд\n" +
                    "Опускаться до параллели бедра",
            sets = 3,
            reps = "10–12 раз на каждую ногу"
        ),

        Exercise(
            id = 12,
            name = "Планка с подъёмом ног",
            technique = "Локти под плечами\n" +
                    "Корпус прямой\n" +
                    "Поочередно поднимать ноги, сохраняя равновесие",
            sets = 3,
            reps = "12–15 подъёмов на каждую ногу"
        )
    )

    private val maintainFormExercises = listOf(
        Exercise(
            id = 13,
            name = "Лёгкие отжимания",
            technique = "Руки чуть шире плеч\n" +
                    "Корпус ровный\n" +
                    "Опускаться медленно, подниматься плавно",
            sets = 2,
            reps = "10–15 раз"
        ),

        Exercise(
            id = 14,
            name = "Приседания с собственным весом",
            technique = "Ноги на ширине плеч\n" +
                    "Спина ровная\n" +
                    "Движение плавное, без рывков",
            sets = 2,
            reps = "12–15 раз"
        ),

        Exercise(
            id = 15,
            name = "Планка классическая",
            technique = "Локти под плечами\n" +
                    "Тело прямое\n" +
                    "Дыхание ровное",
            techniqueImg = R.drawable.plank,
            sets = 2,
            reps = "20–30 секунд"
        ),

        Exercise(
            id = 16,
            name = "Ягодичный мост",
            technique = "Стопы на ширине таза\n" +
                    "Поднимать таз до прямой линии корпуса\n" +
                    "Сжимать ягодицы в верхней точке",
            sets = 2,
            reps = "15–20 раз"
        ),

        Exercise(
            id = 17,
            name = "Подъём колен к груди стоя",
            technique = "Корпус ровный\n" +
                    "Поднимать колени на уровень пояса\n" +
                    "Темп средний",
            sets = 2,
            reps = "20 раз на каждую ногу"
        ),

        Exercise(
            id = 18,
            name = "Скручивания лёгкие",
            technique = "Поясница прижата к полу\n" +
                    "Поднимать плечи, не тянуть шею\n" +
                    "Движение контролируемое",
            sets = 2,
            reps = "12–15 раз"
        )
    )
}