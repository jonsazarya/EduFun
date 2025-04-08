    package com.example.edufun

    import android.content.Intent
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.appcompat.widget.SearchView
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.activityViewModels
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.Observer
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.edufun.adapter.CategoryAdapter
    import com.example.edufun.database.CategoryDatabaseHelper
    import com.example.edufun.database.ChapterDatabaseHelper
    import com.example.edufun.database.QuizDatabaseHelper
    import com.example.edufun.database.QuizDetailDatabaseHelper
    import com.example.edufun.databinding.FragmentHomeBinding
    import com.example.edufun.model.Category
    import com.example.edufun.model.User
    import com.example.edufun.view.ViewModelFactory
    import com.example.edufun.view.lesson.LessonDetailActivity
    import com.example.edufun.view.main.MainViewModel
    import java.util.Locale

    class HomeFragment : Fragment(), CategoryAdapter.OnCategoryClickListener {

        private lateinit var binding: FragmentHomeBinding
        private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper
        private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
        private lateinit var quizDatabaseHelper: QuizDatabaseHelper
        private lateinit var quizDetailDatabaseHelper: QuizDetailDatabaseHelper
        private val mainViewModel: MainViewModel by viewModels {
            ViewModelFactory.getInstance(requireContext())
        }
        private lateinit var categoryAdapter: CategoryAdapter
        private var categories: List<Category> = emptyList()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentHomeBinding.inflate(inflater, container, false)

            categoryDatabaseHelper = CategoryDatabaseHelper(requireContext())
            chapterDatabaseHelper = ChapterDatabaseHelper(requireContext())
            quizDatabaseHelper = QuizDatabaseHelper(requireContext())
            quizDetailDatabaseHelper = QuizDetailDatabaseHelper(requireContext())

            mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                updateUI(user)
            }

            addSampleData()
            setupRecyclerView()
            setupSearchView()

            return binding.root
        }

        private fun addSampleData() {
            val mathCategoryId = categoryDatabaseHelper.addCategory("Matematika", "Matematika itu menyenangkan", R.drawable.math)
            val scienceCategoryId = categoryDatabaseHelper.addCategory("IPA", "IPA itu menyenangkan", R.drawable.science)
            val englishCategoryId = categoryDatabaseHelper.addCategory("Bahasa Inggris", "Bahasa Inggris itu menyenangkan", R.drawable.english)

            chapterDatabaseHelper.addChapter("Bab 1", "Bilangan Bulat", mathCategoryId, "math_1")
            chapterDatabaseHelper.addChapter("Bab 2", "Operasi Bilangan Bulat (Perpangkatan)", mathCategoryId, "math_2")
            chapterDatabaseHelper.addChapter("Bab 3", "Bilangan Pecahan", mathCategoryId, "math_3")

            chapterDatabaseHelper.addChapter("Bab 1", "Hakikat Ilmu Sains & Metode Ilmiah", scienceCategoryId, "science_1")
            chapterDatabaseHelper.addChapter("Bab 2", "Zat dan Perubahannya", scienceCategoryId, "science_2")
            chapterDatabaseHelper.addChapter("Bab 3", "Suhu Kalor Pemuaian", scienceCategoryId, "science_3")

            chapterDatabaseHelper.addChapter("Bab 1", "Greeting, Thank You, and Sorry", englishCategoryId, "english_1")
            chapterDatabaseHelper.addChapter("Bab 2", "Identities, Hobbies, Family Members", englishCategoryId, "english_2")
            chapterDatabaseHelper.addChapter("Bab 3", "Time, Day, Month, Date in English", englishCategoryId, "english_3")

            val mathQuizId1 = quizDatabaseHelper.addQuiz("Kuis 1", "Bilangan Bulat", mathCategoryId)
            val mathQuizId2 = quizDatabaseHelper.addQuiz("Kuis 2", "Operasi Bilangan Bulat (Perpangkatan", mathCategoryId)
            val mathQuizId3 = quizDatabaseHelper.addQuiz("Kuis 3", "Bilangan Pecahan", mathCategoryId)

            val scienceQuiz1 = quizDatabaseHelper.addQuiz("Kuis 1", "Hakikat Ilmu Sains & Metode Ilmiah", scienceCategoryId)
            val scienceQuiz2 = quizDatabaseHelper.addQuiz("Kuis 2", "Zat dan Perubahannya", scienceCategoryId)
            val scienceQuiz3 = quizDatabaseHelper.addQuiz("Kuis 3", "Suhu Kalor Pemuaian", scienceCategoryId)

            val englishQuiz1 = quizDatabaseHelper.addQuiz("Kuis 1", "Greeting, Thank You, and Sorry", englishCategoryId)
            val englishQuiz2 = quizDatabaseHelper.addQuiz("Kuis 2", "Identities, Hobbies, Family Members", englishCategoryId)
            val englishQuiz3 = quizDatabaseHelper.addQuiz("Kuis 3", "Time, Day, Month, Date in English", englishCategoryId)

            quizDetailDatabaseHelper.addQuizDetail(mathQuizId1, "Hasil dari -15 + 20 adalah ...", "-5", "5","35", "-35", "5")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId1, "Hasil dari 12 - (-8) adalah ...", "4", "-4","20", "-20", "20")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId1, "Hasil dari -7 x 6 adalah ...", "42", "-42","13", "-13", "-42")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId1, "Hasil dari -48/(-6) adalah ...", "8", "-8","6", "-6", "8")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId1, "Sifat komutatif perkalian menunjukkan bahwa untuk setiap bilangan bulat a dan b berlaku ...", "a + b = b + a", "a x b = b x a","a - b = b - a", "a / b = b / a", "a x b = b x a")

            quizDetailDatabaseHelper.addQuizDetail(mathQuizId3, "Hasil dari 2/5 + 1/5 adalah ...", "3/5", "1/5","2/10", "1/2", "3/5")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId3, "Hasil dari 3/4 - 1/2 adalah ...", "1/4", "1/2","3/8", "5/8", "1/4")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId3, "Hasil dari 2/3 x 3/5 adalah ...", "6/15", "1/5","2/5", "1/3", "2/5")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId3, "Hasil dari 4/5 ÷ 2/3 adalah ...", "12/10", "6/5","8/15", "2/3", "6/5")
            quizDetailDatabaseHelper.addQuizDetail(mathQuizId3, "Pecahan 8/12 dapat disederhanakan menjadi ...", "2/3", "3/4","4/6", "1/2", "2/3")

            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz1, "Ilmu sains adalah suatu cara untuk ...", "Memprediksi masa depan", "Mengumpulkan dan menganalisis data untuk memahami fenomena alam", "Mengembangkan teknologi tanpa dasar penelitian", "Menyebarkan informasi tanpa bukti", "Mengumpulkan dan menganalisis data untuk memahami fenomena alam")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz1, "Salah satu karakteristik ilmu pengetahuan adalah ...", "Bersifat absolut dan tidak dapat berubah", "Didasarkan pada pengamatan dan eksperimen", "Hanya berlaku untuk fenomena yang tidak dapat dijelaskan", "Mengandalkan opini pribadi", "Didasarkan pada pengamatan dan eksperimen")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz1, "Langkah pertama dalam metode ilmiah adalah ...", "Mengumpulkan data", "Membuat hipotesis", "Mengajukan pertanyaan", "Melakukan eksperimen", "Mengajukan pertanyaan")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz1, "Hipotesis dalam penelitian ilmiah adalah ...", "Kesimpulan akhir dari penelitian", "Pernyataan yang dapat diuji dan dibuktikan", "Data yang dikumpulkan selama eksperimen", "Teori yang sudah diterima secara umum", "Pernyataan yang dapat diuji dan dibuktikan")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz1, "Observasi dalam metode ilmiah dilakukan untuk ...", "Mengabaikan data yang tidak relevan", "Mengumpulkan informasi awal sebelum eksperimen", "Menyimpulkan hasil tanpa bukti", "Mengembangkan teori baru tanpa pengujian", "Mengumpulkan informasi awal sebelum eksperimen")

            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz2, "Zat dapat didefinisikan sebagai ...", "Segala sesuatu yang memiliki massa dan volume", "Hanya benda padat yang dapat dilihat", "Hanya cairan yang dapat mengalir", "Energi yang tidak memiliki massa", "Segala sesuatu yang memiliki massa dan volume")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz2, "Sifat fisika dari suatu zat adalah ...", "Sifat yang berubah ketika zat mengalami reaksi kimia", "Sifat yang dapat diamati tanpa mengubah komposisi zat", "Sifat yang hanya dapat diukur dengan alat khusus", "Sifat yang tidak dapat diukur", " Sifat yang dapat diamati tanpa mengubah komposisi zat")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz2, "Contoh perubahan fisika adalah ...", "Pembakaran kayu", "Pembekuan air menjadi es", "Reaksi asam dan basa", "Fermentasi gula menjadi alkohol", " Pembekuan air menjadi es")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz2, "Perubahan kimia ditandai dengan ...", "Perubahan bentuk fisik zat", "Terjadinya reaksi yang menghasilkan zat baru", "Perubahan suhu tanpa reaksi", "Perubahan warna tanpa reaksi", "Terjadinya reaksi yang menghasilkan zat baru")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz2, "Zat yang memiliki bentuk tetap dan volume tetap adalah ...", "Gas", "Cair", "Padat", "Plasma", "Padat")

            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz3, "Suhu adalah ukuran dari ...", "Jumlah kalor yang terkandung dalam suatu benda", "Energi kinetik rata-rata partikel dalam suatu benda", "Massa jenis suatu benda", "Volume suatu benda", "Energi kinetik rata-rata partikel dalam suatu benda")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz3, "Kalor adalah ...", "Suhu suatu benda", "Energi yang berpindah dari benda bersuhu tinggi ke benda bersuhu rendah", "Massa jenis suatu benda", "Volume suatu benda", "Energi yang berpindah dari benda bersuhu tinggi ke benda bersuhu rendah")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz3, "Jika suatu zat menerima kalor, maka ...", "Suhu zat tersebut pasti turun", "Suhu zat tersebut pasti naik", "Suhu zat tersebut bisa naik atau tetap, tergantung pada jenis zat", "Suhu zat tersebut tidak akan berubah", "Suhu zat tersebut bisa naik atau tetap, tergantung pada jenis zat")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz3, "Pemuaian terjadi karena ...", "Penurunan suhu yang menyebabkan partikel bergerak lebih lambat", "Peningkatan suhu yang menyebabkan partikel bergerak lebih cepat dan menjauh", "Penambahan massa zat", "Pengurangan volume zat", "Peningkatan suhu yang menyebabkan partikel bergerak lebih cepat dan menjauh")
            quizDetailDatabaseHelper.addQuizDetail(scienceQuiz3, "Contoh pemanfaatan pemuaian dalam kehidupan sehari-hari adalah ...", "Penggunaan termometer bimetal", "Pembuatan es krim", "Penggunaan kompor listrik", "Pembuatan pupuk", "Penggunaan termometer bimetal")

            quizDetailDatabaseHelper.addQuizDetail(englishQuiz1, "What is the most appropriate greeting to use when you meet your teacher in the morning?", " Good night", "Good afternoon", "Good morning", "Goodbye", "Good morning")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz1, "Your friend helps you carry your books. What should you say to show your gratitude?", "I'm sorry", "Thank you", "Excuse me", "Hello", "Thank you")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz1, "You accidentally step on someone's foot. What should you say?", "You're Welcome", "Congratulations", "I'm sorry", "Goodluck", "I'm sorry")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz1, "Someone says \"Thank you\" to you. What is an appropriate response?", "I'm sorry", "Goodbye", "You're welcome", "Hello", "You're welcome")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz1, "Which situation is most appropriate to use the word \"sorry\"?", "When you receive a gift", "When you want to ask someone to repeat what they said", "When you meet someone for the first time", "When you are saying goodbye", "When you want to ask someone to repeat what they said")

            quizDetailDatabaseHelper.addQuizDetail(englishQuiz2, "What is usually included in someone's identity?", "Their favorite color", "Their name, age, and nationality", "Their shoe size", "Their lucky number", "Their name, age, and nationality")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz2, "Which of the following is an example of a hobby?", "Going to school", "Eating dinner", "Playing the guitar", "Sleeping", "Playing the guitar")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz2, "Who is the son of your aunt or uncle?", "Your cousin", "Your nephew", "Your brother", "Your grandfather", "Your cousin")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz2, "Which sentence best describes a hobby?", "It's something you have to do every day.", "It's something you enjoy doing in your free time.", "It's something you get paid to do.", "It's something you do because you are forced to.", "It's something you enjoy doing in your free time.")
            quizDetailDatabaseHelper.addQuizDetail(englishQuiz2, "Your mother's father is your ...", "Uncle", "Grandfather", "Brother", "Cousin", "Grandfather")
        }

        private fun setupRecyclerView() {
            binding.progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                categories = categoryDatabaseHelper.getAllCategories()
                categoryAdapter = CategoryAdapter(categories, this)
                binding.rvCategory.layoutManager = LinearLayoutManager(requireContext())
                binding.rvCategory.adapter = categoryAdapter

                binding.progressBar.visibility = View.GONE
            }, 1000)
        }

        private fun setupSearchView() {
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterCategories(newText)
                    return true
                }

            })
        }

        private fun filterCategories(query: String?) {
            if (query != null) {
                val filteredList = ArrayList<Category>()
                val lowerCaseQuery = query.toLowerCase(Locale.ROOT)
                for (i in categories) {
                    if(i.name.toLowerCase(Locale.ROOT).contains(lowerCaseQuery)){
                        filteredList.add(i)
                    }
                }

                if (filteredList.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                } else {
                    categoryAdapter.updateList(filteredList)
                }
            }
        }

        private fun updateUI(user: User) {
            binding.tvUsername.text = user.email.ifEmpty { "Hai! User" }
        }

        override fun onCategoryClick(category: Category) {
            val intent = Intent(requireContext(), LessonDetailActivity::class.java)
            intent.putExtra("lesson_title", category.name)
            intent.putExtra("lesson_desc", category.desc)
            intent.putExtra("image_res_id", category.imageResId)
            intent.putExtra("category_id", category.id)
            startActivity(intent)
        }
    }


