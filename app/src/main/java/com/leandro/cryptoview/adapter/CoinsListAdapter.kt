package com.leandro.cryptoview.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.leandro.cryptoview.R
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.util.IMAGE_URL
import com.leandro.cryptoview.util.formatterToDecimal
import com.leandro.cryptoview.util.getProgressDrawable
import com.leandro.cryptoview.util.loadImage
import com.leandro.cryptoview.view.ListFragmentDirections
import kotlinx.android.synthetic.main.item_coin.view.*
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

class CoinsListAdapter(val coinList: ArrayList<Coin>) :
    RecyclerView.Adapter<CoinsListAdapter.CoinViewHolder>() {

    fun updateCoinList(coins: List<Coin>) {
        coinList.clear()
        coinList.addAll(coins)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_coin, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.view.imv_coinIcon.loadImage(IMAGE_URL + coinList[position].id
                + ".png", getProgressDrawable(holder.view.imv_coinIcon.context))
        holder.view.txt_coinName.text = coinList[position].name
        holder.view.txt_coinSymbol.text = coinList[position].symbol

        holder.view.txt_value1h.text = coinList[position].percent_change_1h?.let {
            formatterToDecimal(it,
                2).toString()
        }
        formatterStyle(holder.view.txt_value1h,
            holder.view.txt_percentSymbol,
            holder.view.imv_situation)


        holder.view.txt_priceBRL.text =
            coinList[position].price?.let { formatterToDecimal(it, 2) }.toString()

        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            action.cryptocurrencyId = coinList[position].coin_id
            Navigation.findNavController(it)
                .navigate(action)
        }
    }


    fun formatterStyle(txt_valor: TextView, txt_percentSymbol: TextView, imv: ImageView) {
        try {
            if (txt_valor.text.toString().contains("-")) {
                txt_valor.setTextColor(Color.parseColor("#FF0000"))
                txt_percentSymbol.setTextColor(Color.parseColor("#FF0000"))
                imv.setImageResource(R.drawable.ic_trending_down_red_24)
            } else {
                txt_valor.setTextColor(Color.parseColor("#32CD32"))
                txt_percentSymbol.setTextColor(Color.parseColor("#32CD32"))
                imv.setImageResource(R.drawable.ic_trending_up_green_24)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount() = coinList.size

    class CoinViewHolder(var view: View) : RecyclerView.ViewHolder(view)

}