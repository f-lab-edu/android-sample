package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.api.GithubService
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.Users
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
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantListAdapter: PlantListAdapter

    private var itemCount = 0
    private var currentPage = 1
    private val perPage = 20
    private var lastPage = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == itemCount) layoutManager.spanCount else 1
            }
        }

        plantRecyclerView = binding.rvPlantList
        plantRecyclerView.layoutManager = layoutManager
        plantRecyclerView.addItemDecoration(PlantListItemDecoration(2, 60, true))
        plantListAdapter = PlantListAdapter(object : PlantClickListener {
            override fun onPlantClick(position: Int) {
                val plantData = plantListAdapter.items[position]
                val plantDetailFragment = PlantDetailFragment.newInstance(plantData)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, plantDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onPlantLongClick(position: Int) {
                val plantData = plantListAdapter.items[position]
                PlantDialogFragment.newInstance(plantData).apply {
                    show(this@PlantListFragment.parentFragmentManager, DIALOG_PLANT)
                }
            }
        })

        plantRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 마지막 아이템이 완전히 보일 때 해당 포지션을 반환
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (!binding.rvPlantList.canScrollVertically(1) && lastVisibleItemPosition == itemCount) {
                    recyclerView.post {
                        plantListAdapter.deleteProgress()
                    }
                    currentPage++
                    if (currentPage <= lastPage) getUserList()
                }
            }
        })

        plantRecyclerView.adapter = plantListAdapter

        return view
    }

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