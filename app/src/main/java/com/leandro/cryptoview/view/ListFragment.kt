package com.leandro.cryptoview.view

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.leandro.cryptoview.R
import com.leandro.cryptoview.view.adapters.CoinsListAdapter
import com.leandro.cryptoview.model.AppDatabase
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.model.repository.CoinRepository
import com.leandro.cryptoview.viewmodel.ListViewModel
import com.leandro.cryptoview.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val coinsListAdapter = CoinsListAdapter(arrayListOf())
    lateinit var db: AppDatabase

    var listAllItems = arrayListOf<Coin>()
    val listSearch = arrayListOf<Coin>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
        val menuItem = menu.findItem(R.id.nav_search)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = "Nome da criptomoeda?"
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    try {
                        if (newText!!.isNotEmpty()) {
                            val search = newText.toLowerCase(Locale.getDefault())
                            listSearch.clear()

                            listAllItems.forEach {
                                if (it.name?.toLowerCase(Locale.getDefault())!!.contains(search)) {
                                    listSearch.add(it)
                                    coinsListAdapter.updateCoinList(listSearch)
                                }
                            }
                        } else {
                            coinsListAdapter.updateCoinList(listAllItems)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(view.context)
        val dao = db.coinDao()
        val repository = CoinRepository(dao)
        val viewModelFactory = ListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
        viewModel.refresh()

        coinsListRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinsListAdapter
        }

        refreshLayout.setOnRefreshListener {
            refreshList()
        }
        observeViewModel()
    }

    fun refreshList() {
        coinsListRecycler.visibility = View.GONE
        progressLoadList.visibility = View.VISIBLE

        viewModel.refresh()
        refreshLayout.isRefreshing = false
    }

    fun observeViewModel() {
        viewModel.coins.observe(viewLifecycleOwner, Observer { coins ->
            coins?.let {
                coinsListRecycler.visibility = View.VISIBLE
                coinsListAdapter.updateCoinList(coins)
                listAllItems.clear()
                listAllItems.addAll(coins)
            }
        })
        viewModel.coinsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                if(it){
                    Toast.makeText(activity, "Não foi possível conectar ao serviço. Dados da última conexão serão utilizados caso existam.", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(activity, "Dados atualizados com sucesso.", Toast.LENGTH_SHORT).show()
                }
            }

        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progressLoadList.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    coinsListRecycler.visibility = View.GONE
                }
            }
        })

    }

}