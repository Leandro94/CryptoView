package com.leandro.cryptoview.view.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.leandro.cryptoview.R
import com.leandro.cryptoview.databinding.ItemCoinBinding
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.utils.IMAGE_URL
import com.leandro.cryptoview.utils.getProgressDrawable
import com.leandro.cryptoview.utils.loadImage
import com.leandro.cryptoview.view.ListFragmentDirections
import kotlinx.android.synthetic.main.item_coin.view.*
import java.lang.Exception

class CoinsListAdapter(val coinList: ArrayList<Coin>) :
    RecyclerView.Adapter<CoinsListAdapter.CoinViewHolder>(), CoinClickListener {

    fun updateCoinList(coins: List<Coin>) {
        coinList.clear()
        coinList.addAll(coins)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view =  DataBindingUtil.inflate<ItemCoinBinding>(inflater, R.layout.item_coin, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        formatterStyle(coinList[position].percent_change_24h.toString(),holder.view.txtValue24h,
            holder.view.txtPercentSymbol24h,
            holder.view.imvSituation24h)

        formatterStyle(coinList[position].percent_change_1h.toString(),holder.view.txtValue1h,
            holder.view.txtPercentSymbol,
            holder.view.imvSituation)

        holder.view.imvCoinIcon.loadImage(IMAGE_URL + coinList[position].id + ".png",
            getProgressDrawable(holder.view.imvCoinIcon.context))

        holder.view.coin = coinList[position]
        holder.view.listener =  this
    }
    fun formatterStyle(string: String, txt_valor: TextView, txt_percentSymbol: TextView, imv: ImageView) {
        try {
            if (string.contains("-")) {
                txt_valor.setTextColor(Color.parseColor("#FF0000"))
                txt_percentSymbol.setTextColor(Color.parseColor("#FF0000"))
                imv.setImageResource(R.drawable.ic_trending_down_red_24)

            }
            else if(string.toDouble()==0.00){
                txt_valor.setTextColor(Color.parseColor("#C1C1C1"))
                txt_percentSymbol.setTextColor(Color.parseColor("#C1C1C1"))
                imv.setImageResource(R.drawable.ic_trending_flat_grey_24)
            }
            else {
                txt_valor.setTextColor(Color.parseColor("#32CD32"))
                txt_percentSymbol.setTextColor(Color.parseColor("#32CD32"))
                imv.setImageResource(R.drawable.ic_trending_up_green_24)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount() = coinList.size

    class CoinViewHolder(var view: ItemCoinBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCoinClicked(v: View) {
        val id = v.coinId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.cryptocurrencyId = id
        Navigation.findNavController(v)
            .navigate(action)
    }

}