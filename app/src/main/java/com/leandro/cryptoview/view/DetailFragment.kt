package com.leandro.cryptoview.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.leandro.cryptoview.R
import com.leandro.cryptoview.model.AppDatabase
import com.leandro.cryptoview.model.repository.CoinRepository
import com.leandro.cryptoview.utils.IMAGE_URL
import com.leandro.cryptoview.utils.formatterToDecimal
import com.leandro.cryptoview.utils.getProgressDrawable
import com.leandro.cryptoview.utils.loadImage
import com.leandro.cryptoview.viewmodel.DetailViewModel
import com.leandro.cryptoview.viewmodel.DetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var coinId=0
    lateinit var db: AppDatabase
    val entries = ArrayList<BarEntry>()

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

        configBarData()
        configBarChartStyle()
        observeViewModel()
    }
    private fun observeViewModel(){
        viewModel.coinLiveData.observe(viewLifecycleOwner, Observer { coin->
            coin?.let {

                entries.add(BarEntry(1f, coin.percent_change_1h.toString().toFloat()))
                entries.add(BarEntry(2f, coin.percent_change_24h.toString().toFloat()))
                entries.add(BarEntry(3f, coin.percent_change_7d.toString().toFloat()))
                configBarData()

                txt_d_coinSymbol.text = coin.symbol
                txt_d_coinName.text = coin.name
                txt_d_priceBRL.text =
                    coin.price?.let { it1 -> formatterToDecimal(it1, 2).toString() }
                txt_valueVolume24h.text =
                    coin.volume_24h?.let { it1 -> formatterToDecimal(it1, 2).toString() }
                txt_valueMarketCap.text =
                    coin.market_cap?.let { it1 -> formatterToDecimal(it1, 2).toString() }
                txt_valueCirculatingSupply.text =
                    coin.circulating_supply?.let { it1 -> formatterToDecimal(it1, 2).toString() }
                context?.let { imv_d_coin.loadImage(IMAGE_URL+coin.id+".png", getProgressDrawable(it)) }
            }
        })

    }
    private fun configBarData(){
        val barDataSet = BarDataSet(setChartData(), "data")
        barDataSet.setColor(getResources().getColor(R.color.colorBarChart))
        barDataSet.valueTextSize = 16f
        barDataSet.valueTextColor = getResources().getColor(R.color.colorWhite)

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }

    private fun setChartData(): ArrayList<BarEntry>? {
        return entries
    }

    private fun configBarChartStyle(){

        val xAxisLabel: ArrayList<String> = ArrayList()
        xAxisLabel.add("")
        xAxisLabel.add("1 Hora [%]")
        xAxisLabel.add("24 Horas [%]")
        xAxisLabel.add("7 Dias [%]")

        barChart.description.isEnabled = false
        barChart.getLegend().setEnabled(false)
        barChart.animate()
        barChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(xAxisLabel))
        barChart.getXAxis().textSize = 11f
        barChart.getXAxis().textColor= getResources().getColor(R.color.colorAccent)

        barChart.getXAxis().setAxisLineColor(getResources().getColor(R.color.colorAccent))
        barChart.getXAxis().setTextColor(getResources().getColor(R.color.textColor))
        barChart.getXAxis().gridColor = getResources().getColor(R.color.colorLineChart)

        barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.textColor))
        barChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.colorLineChart))
        barChart.getAxisLeft().textSize = 14f
        barChart.getAxisLeft().gridColor = getResources().getColor(R.color.textColor)

        barChart.getAxisRight().setAxisLineColor(getResources().getColor(R.color.colorLineChart))
        barChart.getAxisRight().setTextColor(getResources().getColor(R.color.textColor))
        barChart.getAxisRight().textSize = 14f
        barChart.getAxisRight().gridColor = getResources().getColor(R.color.colorLineChart)
    }



}