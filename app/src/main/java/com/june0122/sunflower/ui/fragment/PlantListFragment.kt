package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.api.GithubService
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.Users
import com.june0122.sunflower.ui.activity.MainActivity
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.ui.adapter.STATUS_LOADING
import com.june0122.sunflower.utils.PlantClickListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val DIALOG_PLANT = "DialogPlant"

class PlantListFragment : Fragment() {
    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRecyclerView: RecyclerView // View
    private lateinit var plantListAdapter: PlantListAdapter // View

    private var itemCount = 0 // View.not() 화면을 그려주기 위한 도구 > ViewModel 포함한 데이터 일 수 있죠.
    private var currentPage = 1  // View.not() 화면을 그려주기 위한 도구 > ViewModel 포함한 데이터 일 수 있죠. or Model
    private val perPage = 20 // View.not() 화면을 그려주기 위한 도구 > ViewModel 포함한 데이터 일 수 있죠. or Model
    private var lastPage = 0 // View.not() 화면을 그려주기 위한 도구 > ViewModel 포함한 데이터 일 수 있죠. or Model

    // 이렇게 하시면 안됩니다.
    // val viewModel: PlantListViewModel by viewModels() // 이거 안만들어져요.
    // 참고 문서 https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
//    private val viewModel = PlantListViewModel(plantListAdapter)
    private val viewModel: PlantListViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlantListViewModel(plantListAdapter) as T
                }
            }
        }
    )

//    override fun success() {
//        // 성공 했는지를 알 수 있어요.
//    }
//
//    override fun fail() {
//        // 실패 했는지
//    }
//
//    override fun loadMoreSuccess(items: List<String>) {
////        plantListAdapter.items.addAll(items)
//    }

//    private val presenter = PlantListPresenter(this)
//
//    private val viewModel = PlantViewModel()
//
//    init {
//        presenter.load()
////        viewModel.success.observe(viewLifecycleOwner) {
////
////        }
//        viewModel.fail.observe(viewLifecycleOwner) {
//
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root
        // SpanCount는 ViewModel, Model
        val layoutManager = GridLayoutManager(context, 2) // View
        layoutManager.spanSizeLookup = object : SpanSizeLookup() { // View
            override fun getSpanSize(position: Int): Int {
                return if (position == itemCount) layoutManager.spanCount else 1
            }
        }

        plantRecyclerView = binding.rvPlantList
        plantRecyclerView.layoutManager = layoutManager
        plantRecyclerView.addItemDecoration(PlantListItemDecoration(2, 60, true))

        plantListAdapter = PlantListAdapter(viewModel)
        viewModel.showDetail.observe(viewLifecycleOwner) {
            val plantDetailFragment = PlantDetailFragment.newInstance(it)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, plantDetailFragment)
                .addToBackStack(null)
                .commit()
        }

        /**
         * 시나리오
         *
         * Adapter click 일어남 - 1
         * click event는? 어디에서 일어날까요? ViewModel - 1
         * ViewModel 아이템 정보를 가져오고, - 2
         * 3번을 하기 위한 과정
         *  - ViewModel에서는 통지할 무언가? Callback event. LiveData
         *  - View 옵저빙을 한다.
         *     - 이벤트가 오면 commit()
         */
        plantListAdapter = PlantListAdapter(object : PlantClickListener { // ClickListener = ViewModel? - 1
            override fun onPlantClick(position: Int) {
                val plantData = plantListAdapter.items[position] // ViewModel - 2
//                val plantDetailFragment = PlantDetailFragment.newInstance(plantData) // View - 3
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.container, plantDetailFragment)
//                    .addToBackStack(null)
//                    .commit()
            }

            /**
             * 시나리오
             *
             * 롱클릭은 어디서 일어나나요? View(Adapter.ViewHolder)
             * 이 이벤트는 어디서 받나요? ViewModel에서 받고
             * 뷰모델에는 어디로 통지해줘야 하는데 그게 어딜까요? -
             * view가 ViewModel에 구독하고, 동작하도록 한다.
             * - LiveData
             */
            override fun onPlantLongClick(position: Int) {
                val plantData = plantListAdapter.items[position]
                PlantDialogFragment.newInstance(plantData).apply {
                    show(this@PlantListFragment.parentFragmentManager, DIALOG_PLANT)
                }
            }
        })

        /**
         * 직접 시나리오를 작성해보세요.
         */
        plantRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() { // < ---- 액션의 등록 및 시작
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 마지막 아이템이 완전히 보일 때 해당 포지션을 반환

                // <--- Data . view에 대한 data
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                // 분리
                // 조건에 대한 부분을 나눌 수 있다.

                if (canLoaded(binding.rvPlantList.canScrollVertically(1).not(), lastVisibleItemPosition, itemCount)) {
//                if (!binding.rvPlantList.canScrollVertically(1) && lastVisibleItemPosition == itemCount) {
                    recyclerView.post {
                        plantListAdapter.deleteProgress()
                    }
                    currentPage++
                    // 모델
                    if (currentPage <= lastPage) getUserList()
                }
            }
        })

        plantRecyclerView.adapter = plantListAdapter

        return view
    }

    private fun canLoaded(canScrollVertically: Boolean, lastVisibleItemPosition: Int, totalCount: Int): Boolean =
        canScrollVertically && lastVisibleItemPosition == totalCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserList()

        binding.fabAddPlant.setOnClickListener {
            val items = plantListAdapter.items
            // 데이터를 직접 입력해서 아이템을 추가하는 식으로 변경 예정
            items.add(
                Plant(
                    imageUrl = "https://avatars.githubusercontent.com/u/39554623?v=4",
                    name = "june0122",
                    description = "Junior Android Developer"
                )
            )
            plantListAdapter.notifyItemInserted(items.size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserList() {
        val userListCall: Call<Users> = GithubService.create().getUserList(query = "june01", perPage, currentPage)

        userListCall.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    response.body()?.let { users -> updateUserList(users) }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "API rate limit exceeded.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e("PlantList", "XXX - ${t.localizedMessage}")
            }
        })
    }

    private fun updateUserList(users: Users) {
        val plantListItems = plantListAdapter.items
        val totalCount = users.total_count
        lastPage = (totalCount / perPage) + 1

        users.items.forEach {
            plantListItems.add(
                Plant(
                    imageUrl = it.avatarUrl,
                    name = it.login,
                    description = ""
                )
            )
        }

        plantListAdapter.notifyItemRangeInserted(itemCount, users.items.size)
        itemCount += users.items.size

        if (currentPage < lastPage) {
            plantListItems.add(Plant("", "", STATUS_LOADING)) // progressbar 보여주기 위한 아이템 1개 추가
            plantListAdapter.notifyItemInserted(itemCount + 1)
        }
    }
}