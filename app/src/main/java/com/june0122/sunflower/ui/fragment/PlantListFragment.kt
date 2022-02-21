package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.api.GithubService
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.Users
import com.june0122.sunflower.ui.adapter.PlantListAdapter
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root

        plantRecyclerView = binding.rvPlantList
        plantRecyclerView.layoutManager = GridLayoutManager(context, 2)
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
                    imageUrl = "https://www.thespruce.com/thmb/-W1mZNWR5tVwvp4reWuEoe0ZEyc=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/GettyImages-902133540-7e603f4ab7aa4aadb097c61d2627da24.jpg",
                    name = "Quinoa",
                    description = "Quinoa (Chenopodium quinoa) is a flowering plant in the Amaranth family that is grown as a crop primarily for its edible seeds. It has been grown for human consumption for thousands of years, originating from mountainous regions of South America. Quinoa cultivation has now grown to over 70 countries around the world. This ancient superfood is packed with vitamins and minerals and has a nice mild taste. The seeds are often cooked like rice, or ground into a flour that can be used as a gluten-free alternative in cooking and baking."
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
        val userListCall: Call<Users> = GithubService.create().getUserList(query = "june", perPage = 20, page = 1)

        userListCall.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    response.body()?.let { users -> updateUserList(users) }
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
        users.items.forEach {
            plantListItems.add(
                Plant(
                    imageUrl = it.avatarUrl,
                    name = it.login,
                    description = ""
                )
            )
        }
        plantListAdapter.notifyItemRangeInserted(0, users.items.size)
    }
}