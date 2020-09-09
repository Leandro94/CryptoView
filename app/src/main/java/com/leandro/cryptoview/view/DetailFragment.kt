package com.leandro.cryptoview.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leandro.cryptoview.R
import com.leandro.cryptoview.service.AppDatabase
import com.leandro.cryptoview.model.repository.CoinRepository
import com.leandro.cryptoview.util.IMAGE_URL
import com.leandro.cryptoview.util.getProgressDrawable
import com.leandro.cryptoview.util.loadImage
import com.leandro.cryptoview.viewmodel.DetailViewModel
import com.leandro.cryptoview.viewmodel.DetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var coinId=0
    lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            coinId = DetailFragmentArgs.fromBundle(it).cryptocurrencyId
        }

        db = AppDatabase.getInstance(view.context)
        val dao = db.coinDao()
        val repository = CoinRepository(dao)
        val viewModelFactory = DetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        viewModel.getOne(coinId)

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.coinLiveData.observe(viewLifecycleOwner, Observer { coin->
            coin?.let {
                txt_d_name.text = coin.name
                context?.let { imv_d_coin.loadImage(IMAGE_URL+coin.id+".png", getProgressDrawable(it)) }
            }
        })
    }

}